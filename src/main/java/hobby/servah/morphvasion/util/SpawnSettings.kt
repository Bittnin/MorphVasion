package hobby.servah.morphvasion.util

import hobby.servah.morphvasion.MorphVasion
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File

class SpawnSettings(main: MorphVasion) {

    var file = File(main.dataFolder, "spawning.yml")
    var config = YamlConfiguration()

    init {
        //load
        if(!file.exists()) main.saveResource("spawning.yml", false)

        try {
            config.load(file)
        } catch (e: Exception) {
            Utils.console("<red><bold>Error:</bold> Could not load the <yellow>spawning.yml<red> config file. " +
                    "Mob Spawning may be broken.")
            e.printStackTrace()
        }
    }

    private fun save() {
        try {
            config.save(file)
        } catch(e: Exception) {
            Utils.convert("<red><bold>Error:</bold> Could not save to the <yellow>spawning.yml<red> " +
                    "config file. Mob Spawning may be broken.")
            e.printStackTrace()
        }
    }

    fun set(path: String, value: Any) {
        config.set(path, value)
        save()
    }

    fun getInt(path: String): Int {
        return config.getInt(path)
    }

    fun get(path: String): Any? {
        return config.get(path)
    }

}