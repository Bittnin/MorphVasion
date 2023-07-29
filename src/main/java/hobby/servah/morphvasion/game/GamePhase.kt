package hobby.servah.morphvasion.game

import hobby.servah.morphvasion.MorphVasion
import hobby.servah.morphvasion.manager.Phase
import hobby.servah.morphvasion.util.Utils
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.World
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerJoinEvent
import java.util.*

class GamePhase(plugin: MorphVasion): Phase(plugin) {

    private lateinit var gameMap: GameMap
    private lateinit var world: World

    val isMob = HashMap<UUID, Boolean>()

    override fun enable() {
        gameMap = plugin.getMaps()[plugin.activeMap]!!
        gameMap.load()
        world = gameMap.getWorld()!!
        plugin.currentWorld = world
        for(p in Bukkit.getOnlinePlayers()) {
            Utils.move(world, p)
            if(gameMap.playerSpawn != null) p.teleport(gameMap.playerSpawn!!)
            if(isMob[p.uniqueId] == null) {
                makeSpectator(p)
                continue
            }
            if(isMob[p.uniqueId] != true) continue
            // put the mobs into fake spectator mode so that they can look around and select a character
            val newLoc = p.location
            newLoc.y += 50
            p.teleport(newLoc)
            p.isInvisible = true
        }
    }

    override fun disable() {
        TODO("Not yet implemented")
    }

    override fun getNextPhase(): Phase {
        TODO("Not yet implemented")
    }

    private fun makeSpectator(p: Player) {
        // e.g.: a player joined while the game phase was already running -> make them a spectator
        //TODO: more functionality like fake spectator mode, etc.
        p.gameMode = GameMode.SPECTATOR
    }

    @EventHandler
    fun onJoin(e: PlayerJoinEvent) {
        val p = e.player
        if(isMob[p.uniqueId] == null) {
            makeSpectator(p)
            Utils.chat("<red>You joined a running match and have therefore been put into " +
                    "<bold>spectator</bold> mode!", p)
        }
    }
}