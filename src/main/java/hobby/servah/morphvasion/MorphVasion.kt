package hobby.servah.morphvasion

import hobby.servah.morphvasion.commands.StartCmd
import hobby.servah.morphvasion.lobby.GameMap
import hobby.servah.morphvasion.manager.PhaseManager
import hobby.servah.morphvasion.util.Utils
import org.bukkit.Material
import org.bukkit.plugin.java.JavaPlugin

class MorphVasion : JavaPlugin() {

    private lateinit var phaseManager: PhaseManager
    private lateinit var startCmd: StartCmd

    private var maps = HashMap<String, GameMap>()

    override fun onEnable() {
        saveDefaultConfig()

        Utils.prefix = config.getString("prefix").toString()

        indexMaps()

        phaseManager = PhaseManager(this)

        registerCommands()
    }

    override fun onDisable() {

    }

    private fun registerCommands() {
        startCmd = StartCmd(this)
        getCommand("start")?.setExecutor(startCmd)
    }

    private fun indexMaps() {
        if(config.getConfigurationSection("maps") == null) {
            Utils.console("<red><bold>Error:</bold> The <bold>maps</bold> section of the config.yml " +
                    "seems to be missing <bold>entirely</bold>. You can <bold>delete</bold> the " +
                    "config.yml if you face issues like this!")
            return
        }
        for(k in config.getConfigurationSection("maps")!!.getKeys(false)) {
            if(k == "default") continue
            val icon = Material.getMaterial(config.getString("$k.icon").toString())
            val folder = config.getString("$k.folder")
            val displayName = Utils.configString("$k.name", this)
            val description = config.getString("$k.description")
            if(icon == null || folder == null || description == null) {
                Utils.console("<red><bold>Error:</bold> The config.yml entry of the map <bold>$k</bold> " +
                        "contains errors and needs to be fixed!")
                return
            }
            maps[k] = GameMap(icon, folder, displayName, description)
        }
    }

    fun getPhaseManager(): PhaseManager { return phaseManager }
    fun getStartCmd(): StartCmd { return startCmd }
    fun getMaps(): HashMap<String, GameMap> { return maps }
}
