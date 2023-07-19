package hobby.servah.morphvasion.game

import hobby.servah.morphvasion.MorphVasion
import hobby.servah.morphvasion.lobby.GameMap
import hobby.servah.morphvasion.manager.Phase
import hobby.servah.morphvasion.util.Utils
import org.bukkit.Bukkit
import org.bukkit.World

class GamePhase(plugin: MorphVasion): Phase(plugin) {

    private lateinit var gameMap: GameMap
    private lateinit var world: World

    override fun enable() {
        gameMap = plugin.getMaps()[plugin.activeMap]!!
        gameMap.load()
        world = gameMap.getWorld()!!
        Bukkit.getOnlinePlayers().forEach { Utils.move(world, it) }
    }

    override fun disable() {
        TODO("Not yet implemented")
    }

    override fun getNextPhase(): Phase {
        TODO("Not yet implemented")
    }
}