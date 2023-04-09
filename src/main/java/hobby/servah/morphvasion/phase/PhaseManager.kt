package hobby.servah.morphvasion.phase

import hobby.servah.morphvasion.MorphVasion
import org.bukkit.Bukkit
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener

abstract class Phase(val plugin : MorphVasion) : Listener {
    abstract fun disable()
    abstract fun getNextPhase()
}

class PhaseManager(private val plugin: MorphVasion){

    private var currentPhase: Phase = LobbyPhase(plugin)

    init {
        changePhase(currentPhase)
    }

    fun changePhase(newPhase: Phase) {
        currentPhase.disable()
        HandlerList.unregisterAll(currentPhase)

        currentPhase = newPhase
        Bukkit.getPluginManager().registerEvents(currentPhase, plugin)
    }

}