package hobby.servah.morphvasion.lobby

import hobby.servah.morphvasion.util.Utils
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.World
import org.bukkit.WorldCreator
import org.bukkit.inventory.ItemStack
import org.bukkit.util.ChatPaginator
import java.io.File

class GameMap(private val icon: Material, private val folder: String, val displayName: Component,
              private val description: String) {

    private var activeWorldFolder: File? = null
    private var bukkitWorld: World? = null
    val display: ItemStack = ItemStack(icon)

    init {
        val meta = display.itemMeta
        meta.displayName(displayName)
        ChatPaginator.wordWrap(description, 24).forEach { meta.lore()?.add(Utils.convert(it)) }
        display.itemMeta = meta
    }

    fun load(): Boolean {
        if(bukkitWorld != null) {
            Utils.console("<red><bold>Error:</bold> Trying to load a world that was " +
                    "<bold>already</bold> loaded!")
            return false
        }

        activeWorldFolder = File(
            Bukkit.getWorldContainer().parent + "/maps",
            folder + "_active_" + System.currentTimeMillis()
        )

        File(Bukkit.getWorldContainer().parent + "/maps", folder)
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