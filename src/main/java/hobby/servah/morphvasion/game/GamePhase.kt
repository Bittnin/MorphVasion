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
import kotlin.collections.HashMap

class GamePhase(plugin: MorphVasion): Phase(plugin) {

    private lateinit var gameMap: GameMap
    private lateinit var world: World

    val isMob = HashMap<UUID, Boolean>()

    override fun enable() {
        gameMap = plugin.getMaps()[plugin.activeMap]!!
        gameMap.load()
        world = gameMap.getWorld()!!
        plugin.currentWorld = world
        Bukkit.getOnlinePlayers().forEach {
            Utils.move(world, it)
            if(isMob[it.uniqueId] == null) makeSpectator(it)
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
        makeSpectator(e.player)
    }
}