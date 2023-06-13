package hobby.servah.morphvasion.lobby

import hobby.servah.morphvasion.MorphVasion
import hobby.servah.morphvasion.util.ItemBuilder
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.command.defaults.HelpCommand
import org.bukkit.event.inventory.InventoryClickEvent

// This class has all the items that are used during the LobbyPhase centralized
class LobbyItem(val plugin: MorphVasion) {

    val mapVote = ItemBuilder(Material.MAP).setName(
        Component.text("Map Voting").color(NamedTextColor.YELLOW)
            .decoration(TextDecoration.BOLD, true)
    ).build()
    val adminStart = ItemBuilder(Material.REDSTONE).setName(
        Component.text("Start").color(NamedTextColor.RED)
            .decoration(TextDecoration.BOLD, true)
    ).build()
    val teamSelect = ItemBuilder(Material.WHITE_WOOL).setName(
        Component.text("Click to select Team").color(NamedTextColor.WHITE)
    ).build()

    fun onItemClick(e: InventoryClickEvent) {
        if(!e.isRightClick) return

        if(e.currentItem?.itemMeta?.displayName() == adminStart.itemMeta.displayName()) {
            plugin.getStartCmd().onCommand(e.whoClicked, HelpCommand(), "start", null)
        }
    }

}