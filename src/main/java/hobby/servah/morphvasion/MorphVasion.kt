package hobby.servah.morphvasion

import hobby.servah.morphvasion.manager.PhaseManager
import org.bukkit.plugin.java.JavaPlugin

class MorphVasion : JavaPlugin() {
    override fun onEnable() {
        val phaseManager = PhaseManager(this)
    }

    override fun onDisable() {
    }
}
