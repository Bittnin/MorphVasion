package hobby.servah.morphvasion.manager

import hobby.servah.morphvasion.MorphVasion
import hobby.servah.morphvasion.game.GamePhase
import hobby.servah.morphvasion.lobby.LobbyPhase
import hobby.servah.morphvasion.util.Utils
import org.bukkit.Bukkit
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener

abstract class Phase(protected val plugin: MorphVasion) : Listener {
    abstract fun enable()
    abstract fun disable()
    abstract fun getNextPhase(): Phase
}

// All the phases are centralized and their listeners are only registered when they really need to be
class PhaseManager(private val plugin: MorphVasion) {

    private var currentPhase: Phase = LobbyPhase(plugin)

    init {
        Bukkit.getPluginManager().registerEvents(currentPhase, plugin)
        currentPhase.enable()
    }

    fun changePhase(newPhase: Phase) {
        if(currentPhase is LobbyPhase && newPhase is GamePhase) Bukkit.broadcast(Utils.chat("<yellow>The game " +
                "will be <bold>starting</bold> now, please be patient! <gray>(Lag may occur)"))
        currentPhase.disable()
        HandlerList.unregisterAll(currentPhase)

        currentPhase = newPhase
        Bukkit.getPluginManager().registerEvents(currentPhase, plugin)
        currentPhase.enable()
    }

    fun getCurrentPhase(): Phase { return currentPhase }

}