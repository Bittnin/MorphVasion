package hobby.servah.morphvasion.lobby

import hobby.servah.morphvasion.MorphVasion
import hobby.servah.morphvasion.util.ItemBuilder
import hobby.servah.morphvasion.util.Utils
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.command.defaults.HelpCommand
import org.bukkit.enchantments.Enchantment
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemFlag
import java.util.*
import kotlin.collections.HashMap
import kotlin.math.ceil

// This class has all the items that are used during the LobbyPhase centralized
class LobbyItem(private val plugin: MorphVasion) {

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

    private val maps = plugin.getMaps()
    private val voteInv: Inventory
    private val votes = HashMap<UUID, String>()

    init {
        val size = (ceil(maps.size.toDouble() / 9.0) * 9).toInt()
        voteInv = Bukkit.createInventory(null, size, Utils.convert("<yellow><bold>" +
                "Map Vote"))
        for(m in maps.values) voteInv.addItem(m.display)
    }

    fun onItemClick(e: InventoryClickEvent) {
        if(!e.isRightClick) return

        if(e.currentItem?.itemMeta?.displayName() == adminStart.itemMeta.displayName()) {
            plugin.getStartCmd().onCommand(e.whoClicked, HelpCommand(), "start", null)
        }

        val id = e.whoClicked.uniqueId

        if(e.currentItem?.itemMeta?.displayName() == mapVote.itemMeta.displayName()) {
            if(votes[id] != null) { // this is for marking the map the player voted for
                val clone = Bukkit.createInventory(null, voteInv.size, Utils.convert("<yellow><bold>" +
                        "Map Vote"))
                clone.contents = voteInv.contents
                for(i in clone) {
                    if(i.displayName() != maps[votes[id]]?.displayName) continue
                    i.addEnchantment(Enchantment.LUCK, 0)
                    val meta = i.itemMeta
                    meta.addItemFlags(ItemFlag.HIDE_ENCHANTS)
                    i.setItemMeta(meta)
                }
                e.whoClicked.openInventory(clone)
                return
            }
            e.whoClicked.openInventory(voteInv)
        }

        for(s in maps.keys) {
            if(e.currentItem?.itemMeta?.displayName() != maps[s]?.displayName) continue
            votes[e.whoClicked.uniqueId] = s
        }
    }

}