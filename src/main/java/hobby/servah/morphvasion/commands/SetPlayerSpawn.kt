package hobby.servah.morphvasion.commands

import hobby.servah.morphvasion.MorphVasion
import hobby.servah.morphvasion.util.Utils
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class SetPlayerSpawn(private val plugin: MorphVasion): CommandExecutor {

    override fun onCommand(sender: CommandSender, cmd: Command, label: String, args: Array<out String>?): Boolean {
        if(sender !is Player) {
            Utils.chat("<red>This command can only be executed by players!", sender)
            return false
        }

        if(!sender.hasPermission("morphvasion.setPlayerSpawn")) {
            Utils.chat("<red>You do not have the permission to execute this command!", sender)
            return false
        }

        Utils.saveLocation("maps.${plugin.activeMap}.playerSpawn", sender.location, plugin)
        plugin.saveConfig()
        Utils.chat("<green>The location was successfully saved!", sender)
        return true
    }

}