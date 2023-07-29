package hobby.servah.morphvasion.util

import hobby.servah.morphvasion.MorphVasion
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class Utils {

    companion object{

        var prefix = ""

        fun convert(message: String): Component {
            return MiniMessage.miniMessage().deserialize(message)
        }

        fun chat(message: String, c: CommandSender) {
            c.sendMessage(convert(prefix + message))
        }

        fun chat(message: String): Component {
            return convert(prefix + message)
        }

        fun console(message: String) {
            Bukkit.getConsoleSender().sendMessage(convert(prefix + message))
        }

        fun configString(path: String, plugin: MorphVasion): Component {
            return convert(plugin.config.getString(path).toString())
        }

        fun move(world: World, p: Player) {
            p.teleport(Location(world,0.0,0.0, 0.0).toHighestLocation())
        }

        fun saveLocation(path: String, loc: Location, plugin: MorphVasion) {
            val conf = plugin.config
            conf.set("$path.x", loc.x)
            conf.set("$path.y", loc.y)
            conf.set("$path.z", loc.z)
            conf.set("$path.yaw", loc.yaw)
            conf.set("$path.pitch", loc.pitch)
        }

        fun readLocation(path: String, plugin: MorphVasion): Location {
            val conf = plugin.config
            return Location(plugin.currentWorld, conf.getDouble("$path.x"), conf.getDouble("$path.y"),
                conf.getDouble("$path.z"), conf.getDouble("$path.yaw").toFloat(),
                conf.getDouble("$path.pitch").toFloat())
        }

    }

}