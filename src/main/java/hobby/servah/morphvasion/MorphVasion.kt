package hobby.servah.morphvasion

import hobby.servah.morphvasion.commands.StartCmd
import hobby.servah.morphvasion.manager.PhaseManager
import org.bukkit.plugin.java.JavaPlugin

class MorphVasion : JavaPlugin() {

    private lateinit var phaseManager: PhaseManager
    private lateinit var startCmd: StartCmd

    override fun onEnable() {
        saveDefaultConfig()

        phaseManager = PhaseManager(this)

        registerCommands()
    }

    override fun onDisable() {
    }

    private fun registerCommands() {
        startCmd = StartCmd(this)
        getCommand("start")?.setExecutor(startCmd)
    }

    fun getPhaseManager(): PhaseManager { return phaseManager }
    fun getStartCmd(): StartCmd { return startCmd }
}
