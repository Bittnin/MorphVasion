package hobby.servah.morphvasion

import hobby.servah.morphvasion.commands.StartCmd
import hobby.servah.morphvasion.manager.PhaseManager
import org.bukkit.plugin.java.JavaPlugin

class MorphVasion : JavaPlugin() {

    private lateinit var phaseManager: PhaseManager

    override fun onEnable() {
        phaseManager = PhaseManager(this)

        registerCommands()
    }

    override fun onDisable() {
    }

    private fun registerCommands() {
        getCommand("start")?.setExecutor(StartCmd(this))
    }

    fun getPhaseManager(): PhaseManager { return phaseManager }
}
