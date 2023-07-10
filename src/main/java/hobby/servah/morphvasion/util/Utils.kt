package hobby.servah.morphvasion.util

import hobby.servah.morphvasion.MorphVasion
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.Bukkit
import org.bukkit.entity.Player

class Utils {

    companion object{

        var prefix = ""

        fun convert(message: String): Component {
            return MiniMessage.miniMessage().deserialize(message)
        }

        fun chat(message: String, p: Player){
            p.sendMessage(MiniMessage.miniMessage().deserialize(prefix + message))
        }

        fun console(message: String) {
            Bukkit.getConsoleSender().sendMessage(MiniMessage.miniMessage()
                .deserialize(prefix + message))
        }

        fun configString(path: String, plugin: MorphVasion): Component {
            return Component.text(plugin.config.getString(path).toString())
        }

    }

}