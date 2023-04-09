package hobby.servah.morphvasion

import hobby.servah.morphvasion.phase.PhaseManager
import org.bukkit.plugin.java.JavaPlugin

class MorphVasion : JavaPlugin() {
    override fun onEnable() {
        // Plugin startup logic
        val phaseManager = PhaseManager(this)
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }
}
