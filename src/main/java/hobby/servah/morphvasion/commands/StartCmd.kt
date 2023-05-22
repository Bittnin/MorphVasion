package hobby.servah.morphvasion.commands

import hobby.servah.morphvasion.MorphVasion
import hobby.servah.morphvasion.lobby.LobbyPhase
import hobby.servah.morphvasion.manager.PhaseManager
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

class StartCmd(private val plugin: MorphVasion, private val pm: PhaseManager = plugin.getPhaseManager()): CommandExecutor {

    override fun onCommand(sender: CommandSender, cmd: Command, label: String, args: Array<out String>?): Boolean {
        if(pm.getCurrentPhase() !is LobbyPhase) {
            sender.sendMessage(Component.text("You can only start the game when it is in the Lobby phase!").color(NamedTextColor.RED))
            return false
        }

        //This should execute a soft start, meaning not transferring into the game phase directly
        //but rather displaying a short countdown to the player
        //--> there is no direct feedback
        pm.changePhase(pm.getCurrentPhase().getNextPhase())

        return true
    }

}