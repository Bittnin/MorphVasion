package hobby.servah.morphvasion.game

import hobby.servah.morphvasion.util.Utils
import net.kyori.adventure.text.Component
import org.bukkit.*
import org.bukkit.inventory.ItemStack
import org.bukkit.util.ChatPaginator
import java.io.File

class GameMap(icon: Material, private val folder: String, val displayName: Component,
    description: String, val playerSpawn: Location?) {

    private var activeWorldFolder: File? = null
    private var bukkitWorld: World? = null
    val display: ItemStack = ItemStack(icon)

    init {
        val meta = display.itemMeta
        meta.displayName(displayName)
        for(word in ChatPaginator.wordWrap(description, 24)) {
            if(!meta.hasLore()) meta.lore(mutableListOf(Utils.convert(word)))
            else meta.lore()!!.add(Utils.convert(word))
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
            "${Bukkit.getWorldContainer().path}/maps",
            "${folder}_active_${System.currentTimeMillis()}"
        )

        File("${Bukkit.getWorldContainer().path}/maps/$folder")
            .copyRecursively(activeWorldFolder!!, true)

        bukkitWorld = Bukkit.createWorld(WorldCreator(activeWorldFolder!!.name))
        if(bukkitWorld == null) {
            Utils.console("<red><bold>Error:<bold> Could not load world " +
                    "<bold>$displayName</bold>. Make sure its folder exists!")
            return false
        }

        bukkitWorld!!.isAutoSave = false
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