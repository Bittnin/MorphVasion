package hobby.servah.morphvasion.manager

import hobby.servah.morphvasion.MorphVasion
import hobby.servah.morphvasion.lobby.LobbyPhase
import org.bukkit.Bukkit
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener

abstract class Phase(val plugin : MorphVasion) : Listener {
    abstract fun enable()
    abstract fun disable()
    abstract fun getNextPhase()
}

// All the phases are centralized and their listeners are only registered when they really need to be
class PhaseManager(private val plugin: MorphVasion) {

    private var currentPhase: Phase = LobbyPhase(plugin)

    init {
        Bukkit.getPluginManager().registerEvents(currentPhase, plugin)
        currentPhase.enable()
    }

    fun changePhase(newPhase: Phase) {
        currentPhase.disable()
        HandlerList.unregisterAll(currentPhase)

        currentPhase = newPhase
        Bukkit.getPluginManager().registerEvents(currentPhase, plugin)
        currentPhase.enable()
    }

}