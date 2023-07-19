package hobby.servah.morphvasion.util

import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta

class ItemBuilder(material: Material) {

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

    fun setLore(vararg lines: Component): ItemBuilder {
        meta.lore(lines.toList())
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