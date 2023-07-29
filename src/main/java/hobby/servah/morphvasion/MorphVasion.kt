package hobby.servah.morphvasion

import hobby.servah.morphvasion.commands.SetPlayerSpawn
import hobby.servah.morphvasion.commands.StartCmd
import hobby.servah.morphvasion.game.GameMap
import hobby.servah.morphvasion.manager.PhaseManager
import hobby.servah.morphvasion.util.Utils
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.World
import org.bukkit.plugin.java.JavaPlugin

class MorphVasion : JavaPlugin() {

    private lateinit var phaseManager: PhaseManager
    private lateinit var startCmd: StartCmd
    private lateinit var setPlayerSpawn: SetPlayerSpawn

    private val maps = HashMap<String, GameMap>()

    var activeMap = "Lobby"
    private lateinit var lobbyMap: World
    lateinit var currentWorld: World

    override fun onEnable() {
        saveDefaultConfig()

        Utils.prefix = config.getString("prefix").toString()

        lobbyMap = Bukkit.getServer().worlds[0]
        currentWorld = lobbyMap

        indexMaps()

        phaseManager = PhaseManager(this)

        registerCommands()

        Utils.console("<bold><rainbow>The MorphVasion Plugin has been successfully enabled!")
    }

    override fun onDisable() {
        saveConfig()

        maps.values.forEach { it.unload() }

        Utils.console("<bold><rainbow>Byeeeeeeeeeeee!")
    }

    private fun registerCommands() {
        startCmd = StartCmd(this)
        getCommand("start")?.setExecutor(startCmd)
        setPlayerSpawn = SetPlayerSpawn(this)
        getCommand("setplayerspawn")?.setExecutor(setPlayerSpawn)
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
            val icon = Material.getMaterial(config.getString("maps.$k.icon").toString())
            val folder = config.getString("maps.$k.folder")
            val displayName = Utils.configString("maps.$k.name", this)
            val description = config.getString("maps.$k.description")
            if(icon == null || folder == null || description == null) {
                Utils.console("<red><bold>Error:</bold> The config.yml entry of the map <bold>$k</bold> " +
                        "contains errors and needs to be fixed!")
                return
            }
            maps[k] = GameMap(icon, folder, displayName, description, k, this)
        }
    }

    fun getPhaseManager(): PhaseManager { return phaseManager }
    fun getStartCmd(): StartCmd { return startCmd }
    fun getMaps(): HashMap<String, GameMap> { return maps }
}
