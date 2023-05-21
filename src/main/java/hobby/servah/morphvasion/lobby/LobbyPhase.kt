package hobby.servah.morphvasion.lobby

import hobby.servah.morphvasion.MorphVasion
import hobby.servah.morphvasion.manager.Phase
import org.bukkit.Bukkit
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.FoodLevelChangeEvent
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryMoveItemEvent
import org.bukkit.event.player.PlayerAttemptPickupItemEvent
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.event.player.PlayerJoinEvent

class LobbyPhase(plugin : MorphVasion) : Phase(plugin){

    private val itemHandler: LobbyItem = LobbyItem()

    override fun enable() {
        for (p in Bukkit.getOnlinePlayers()) setupPlayer(p)
    }

    override fun disable() {
        //TODO
    }

    override fun getNextPhase() {

    }

    private fun setupPlayer(p: Player) {
        //make sure to reset the attributes (walk speed maybe) if a player left during a main game phase
        p.foodLevel = 20
        p.getAttribute(Attribute.GENERIC_MAX_HEALTH)?.baseValue = 20.0
        p.health = 20.0

        //give the player all the necessary items
        p.inventory.setItem(0, itemHandler.mapVote)
        p.inventory.setItem(4, itemHandler.teamSelect)
        if(p.isOp || p.hasPermission("morphvasion.start")) {
            p.inventory.setItem(8, itemHandler.adminStart)
        }
    }

    @EventHandler
    fun onJoin(e: PlayerJoinEvent){
        setupPlayer(e.player)
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
        if(e.cursor === null) return
        e.isCancelled = true
        itemHandler.onItemClick(e)
    }

}