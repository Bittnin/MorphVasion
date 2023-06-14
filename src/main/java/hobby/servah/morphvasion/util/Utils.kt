package hobby.servah.morphvasion.util

import hobby.servah.morphvasion.MorphVasion
import net.kyori.adventure.text.Component
import org.bukkit.ChatColor

class Utils {

    companion object{

        fun configString(path: String, plugin: MorphVasion): Component {
            return Component.text(ChatColor.translateAlternateColorCodes('&',
                plugin.config.getString(path).toString()))
        }

    }

}