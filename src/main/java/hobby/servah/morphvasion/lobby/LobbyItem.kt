package hobby.servah.morphvasion.lobby

import hobby.servah.morphvasion.MorphVasion
import hobby.servah.morphvasion.util.ItemBuilder
import hobby.servah.morphvasion.util.Utils
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.command.defaults.HelpCommand
import org.bukkit.enchantments.Enchantment
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemFlag
import java.util.*
import kotlin.math.ceil

// This class has all the items that are used during the LobbyPhase centralized
class LobbyItem(private val plugin: MorphVasion) {

    val mapVote = ItemBuilder(Material.MAP).setName("<yellow><bold>Map Voting").build()
    val adminStart = ItemBuilder(Material.REDSTONE).setName("<red><bold>Start").build()
    val teamSelect = ItemBuilder(Material.WHITE_WOOL).setName("<rainbow>Click to select Team")
        .setLore("<yellow><bold>Right click</bold> this item",
                "<yellow>in your hand to select your team!").build()

    private val maps = plugin.getMaps()
    private val voteInv: Inventory
    val votes = HashMap<UUID, String>()

    val mobWish = HashMap<UUID, Boolean>() // players who want to play as mobs

    init {
        val size = (ceil(maps.size.toDouble() / 9.0) * 9).toInt()
        voteInv = Bukkit.createInventory(null, size, Utils.convert("<yellow><bold>" +
                "Map Vote"))
        for(m in maps.values) voteInv.addItem(m.display)
    }

    fun itemRightClick(e: PlayerInteractEvent) {
        val p = e.player

        if(e.item!!.itemMeta?.displayName() == adminStart.itemMeta.displayName()) {
            plugin.getStartCmd().onCommand(p, HelpCommand(), "start", null)
        }

        val id = p.uniqueId

        if(e.item!!.lore() == teamSelect.lore()) { // Cycle through the different team options
            mobWish[id] = mobWish[id] == false
            if(mobWish[id]!!) { // Player has selected the mob team
                e.item!!.type = Material.ZOMBIE_HEAD
                ItemBuilder.setDisplayName(e.item!!, "<yellow>Selected Team: <green><bold>MOBS")
            } else {
                e.item!!.type = Material.DIAMOND_CHESTPLATE
                ItemBuilder.setDisplayName(e.item!!, "<yellow>Selected Team: <blue><bold>HUMANS")
            }
        }

        if(e.item!!.itemMeta?.displayName() == mapVote.itemMeta.displayName()) {
            if(votes[id] != null) { // this is for marking the map the player voted for
                val clone = Bukkit.createInventory(null, voteInv.size, Utils.convert("<yellow><bold>" +
                        "Map Vote"))
                clone.contents = voteInv.contents
                for(i in clone.contents) {
                    if(i == null) continue
                    if(i.displayName() != maps[votes[id]]?.display?.displayName()) continue
                    i.addUnsafeEnchantment(Enchantment.LUCK, 1) //TODO: maybe highlight more
                    val meta = i.itemMeta
                    meta.addItemFlags(ItemFlag.HIDE_ENCHANTS)
                    i.setItemMeta(meta)
                }
                p.openInventory(clone)
                return
            }
            p.openInventory(voteInv)
        }

    }

    fun onInvClick(e: InventoryClickEvent) {
        for(s in maps.keys) {
            if(e.currentItem?.itemMeta?.displayName() != maps[s]?.displayName) continue
            votes[e.whoClicked.uniqueId] = s
        }
    }

}