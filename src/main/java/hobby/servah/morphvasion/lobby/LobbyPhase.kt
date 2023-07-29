package hobby.servah.morphvasion.lobby

import hobby.servah.morphvasion.MorphVasion
import hobby.servah.morphvasion.game.GamePhase
import hobby.servah.morphvasion.manager.Phase
import org.bukkit.Bukkit
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.block.Action
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.FoodLevelChangeEvent
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryMoveItemEvent
import org.bukkit.event.player.*
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.scheduler.BukkitTask
import kotlin.math.roundToInt


class LobbyPhase(plugin : MorphVasion) : Phase(plugin) {

    private val itemHandler: LobbyItem = LobbyItem(plugin)
    private val config = plugin.config

    private val nextPhase = GamePhase(plugin)

    //counter
    private val maxLobbyTimer = config.getInt("lobby.maxLobbyTimer")
    private var secondsLeft= -1
    private lateinit var counterTask: BukkitTask

    override fun enable() {
        for (p in Bukkit.getOnlinePlayers()) setupPlayer(p)
        counter()
    }

    override fun disable() {
        counterTask.cancel()
        // choose the map with the most votes/random one and pass it on
        val voteCounts = HashMap<String, Int>()
        for(v in itemHandler.votes.values) {
            if(voteCounts[v] == null) voteCounts[v] = 0
            voteCounts[v] = voteCounts[v]!!.plus(1)
        }
        var highestCount = 0
        var highest: String? = null
        for(m in voteCounts.keys) {
            if(voteCounts[m] == null) voteCounts[m] = 0
            if(voteCounts[m]!! <= highestCount) continue
            highestCount = voteCounts[m]!!
            highest = m
        }

        if(highest != null) plugin.activeMap = highest // stays default if nobody voted
        else plugin.activeMap = plugin.config.getString("maps.default")!!

        //assign everyone to their teams whilst trying to give everyone their preferred team
        var mobs = 0
        var humans = 0
        for(p in Bukkit.getOnlinePlayers()) {
            val id = p.uniqueId
            if(itemHandler.mobWish[id] != null) {
                nextPhase.isMob[id] = itemHandler.mobWish[id]!!
                if(itemHandler.mobWish[id]!!) mobs++
                else humans++
                continue
            }
            // the player has not used the team selection device and can be assigned (mostly) randomly
            nextPhase.isMob[id] = humans != 0
        }
        if(Bukkit.getOnlinePlayers().size < 2) return

        // make sure that none of the teams are empty
        if(mobs == 0) nextPhase.isMob[Bukkit.getOnlinePlayers().random().uniqueId] = true
        else if(humans == 0) nextPhase.isMob[Bukkit.getOnlinePlayers().random().uniqueId] = false
    }

    override fun getNextPhase(): Phase {
        return nextPhase
    }

    private fun counter() {
        calculateSecondsLeft()
        counterTask = Bukkit.getScheduler().runTaskTimer(plugin, Runnable {
            for (p in Bukkit.getOnlinePlayers()) {
                p.level = secondsLeft
                p.exp = secondsLeft.toFloat() / maxLobbyTimer.toFloat()
            }
            if (Bukkit.getOnlinePlayers().size < 2) return@Runnable

            if (secondsLeft == 0) {
                plugin.getPhaseManager().changePhase(this.getNextPhase())
                return@Runnable
            }
            secondsLeft--
        }, 0L, 20L)
    }

    private fun calculateSecondsLeft() {
        val playerCount = Bukkit.getOnlinePlayers().size

        secondsLeft = if (playerCount < 3) maxLobbyTimer
        // it works --> don't touch it
        else (((maxLobbyTimer - (maxLobbyTimer / (plugin.config.getInt("lobby.playerDropOff") + 1)))
                / (playerCount / plugin.config.getInt("lobby.playerDropOff")))
                / 10).toDouble().roundToInt() * 10
    }

    private fun setupPlayer(p: Player) {
        //make sure to reset the attributes (walk speed maybe) if a player left during a main game phase
        p.foodLevel = 20
        p.getAttribute(Attribute.GENERIC_MAX_HEALTH)?.baseValue = 20.0
        p.health = 20.0

        //give the player all the necessary items
        p.inventory.clear()
        p.inventory.setItem(0, itemHandler.mapVote)
        p.inventory.setItem(4, itemHandler.teamSelect)
        if(p.isOp || p.hasPermission("morphvasion.start")) {
            p.inventory.setItem(8, itemHandler.adminStart)
        }
    }

    @EventHandler
    fun onJoin(e: PlayerJoinEvent){
        calculateSecondsLeft()
        setupPlayer(e.player)
    }

    @EventHandler
    fun onLeave(e: PlayerQuitEvent) {
        calculateSecondsLeft()

        itemHandler.votes.remove(e.player.uniqueId)
    }

    @EventHandler
    fun onDamage(e: EntityDamageEvent) {
        e.isCancelled = true
    }

    @EventHandler
    fun onPickUp(e : PlayerAttemptPickupItemEvent) {
        if(e.player.hasPermission("morphvasion.*")) return
        e.item.remove()
        e.isCancelled = true
    }

    @EventHandler
    fun onBlockBreak(e : BlockBreakEvent) {
        if(e.player.hasPermission("morphvasion.*")) return
        e.isCancelled = true
    }

    @EventHandler
    fun onHunger(e: FoodLevelChangeEvent) {
        e.isCancelled = true
    }

    @EventHandler
    fun onBlockPlace(e: BlockPlaceEvent) {
        if(e.player.hasPermission("morphvasion.*")) return
        e.isCancelled = true
    }

    @EventHandler
    fun onItemDrop(e: PlayerDropItemEvent) {
        if(e.player.hasPermission("morphvasion.*")) return
        e.isCancelled = true
    }

    @EventHandler
    fun onItemMoveBetweenInvs(e: InventoryMoveItemEvent) {
        e.isCancelled = true
    }

    @EventHandler
    fun onItemClick(e: InventoryClickEvent) {
        //TODO: maybe improve the way this cancels item movement to completely eliminate it
        if(e.cursor == null) return
        e.isCancelled = true
        itemHandler.onInvClick(e)
    }

    @EventHandler
    fun onPlayerInteract(e: PlayerInteractEvent) {
        if(e.hand == EquipmentSlot.OFF_HAND) return // to avoid calling the event twice when not needed
        if(e.action != Action.RIGHT_CLICK_AIR && e.action != Action.RIGHT_CLICK_BLOCK) return
        if(e.item == null) return
        e.isCancelled = true
        itemHandler.itemRightClick(e)
    }

}