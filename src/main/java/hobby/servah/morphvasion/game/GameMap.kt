package hobby.servah.morphvasion.game

import hobby.servah.morphvasion.MorphVasion
import hobby.servah.morphvasion.util.ItemBuilder
import hobby.servah.morphvasion.util.Utils
import net.kyori.adventure.text.Component
import org.bukkit.*
import org.bukkit.inventory.ItemStack
import org.bukkit.util.ChatPaginator
import java.io.File

class GameMap(icon: Material, private val folder: String, val displayName: Component,
              description: String, private val configPath: String, private val plugin: MorphVasion) {

    var activeWorldFolder: File? = null
    private var bukkitWorld: World? = null
    var playerSpawn: Location? = null
    val display: ItemStack = ItemStack(icon)

    init {
        val meta = display.itemMeta
        meta.displayName(displayName)
        for(line in ChatPaginator.wordWrap(description, 48)) {
            if(!meta.hasLore()) meta.lore(mutableListOf(Utils.convert(line)))
            else meta.lore(ItemBuilder.addLineToLore(meta.lore()!!, Utils.convert(line)))
        }
        display.itemMeta = meta
    }

    fun load(): Boolean {
        if(bukkitWorld != null) {
            Utils.console("<red><bold>Error:</bold> Trying to load a world that was " +
                    "<bold>already</bold> loaded!")
            return false
        }

        activeWorldFolder = File(
            "${Bukkit.getWorldContainer().path}/${folder}_active_${System.currentTimeMillis()}"
        )

        File("${Bukkit.getWorldContainer().path}/maps/$folder")
            .copyRecursively(activeWorldFolder!!, true)

        bukkitWorld = Bukkit.createWorld(WorldCreator(activeWorldFolder!!.name))
        if(bukkitWorld == null) {
            Utils.console("<red><bold>Error:<bold> Could not load world " +
                    "<bold>$folder</bold>. Make sure its folder exists!")
            return false
        }

        bukkitWorld!!.isAutoSave = false

        playerSpawn = Utils.readLocation("maps.$configPath.playerSpawn", plugin)
        playerSpawn?.world = bukkitWorld

        return true
    }

    fun unload() {
        if(bukkitWorld == null || activeWorldFolder == null) {
            Utils.console("<red><bold>Error:</bold> Trying to unload a world that was <bold>not</bold> " +
                    "loaded in the first place!")
            return
        }

        Bukkit.unloadWorld(bukkitWorld!!, false)
        bukkitWorld = null
        activeWorldFolder!!.deleteRecursively()
        activeWorldFolder = null
    }

    fun getWorld(): World? {
        if(bukkitWorld == null) {
            Utils.console("<red><bold>Error:</bold> Trying to reference a world that is <bold>not</bold> " +
                    "loaded!")
            return null
        }
        return bukkitWorld
    }

}