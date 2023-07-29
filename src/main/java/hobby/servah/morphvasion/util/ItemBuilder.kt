package hobby.servah.morphvasion.util

import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta

class ItemBuilder(material: Material) {

    companion object{
        fun setDisplayName(i: ItemStack, name: String): ItemStack {
            val meta = i.itemMeta
            meta.displayName(Utils.convert(name))
            i.itemMeta = meta
            return i
        }

        fun addLineToLore(lore: MutableList<Component>, newLine: Component): List<Component> {
            lore.add(newLine)
            return lore
        }
    }

    private val stack: ItemStack
    private val meta: ItemMeta

    init {
        stack = ItemStack(material)
        meta = stack.itemMeta
    }

    fun setName(name: String): ItemBuilder {
        meta.displayName(Utils.convert(name))
        return this
    }

    fun setLore(vararg lines: String): ItemBuilder {
        val newLore = mutableListOf<Component>()
        for(l in lines) {
            newLore.add(Utils.convert(l))
        }
        meta.lore(newLore)
        return this
    }

    fun setAmount(amount: Int): ItemBuilder {
        stack.amount = amount
        return this
    }

    fun addEnchant(enchant: Enchantment, level: Int): ItemBuilder {
        stack.addUnsafeEnchantment(enchant, level)
        return this
    }

    fun build(): ItemStack {
        stack.itemMeta = meta
        return stack
    }

}