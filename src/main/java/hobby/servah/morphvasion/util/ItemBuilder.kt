package hobby.servah.morphvasion.util

import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta

class ItemBuilder(material: Material) {

    private var stack: ItemStack? = null
    private var meta: ItemMeta? = null

    init {
        stack = ItemStack(material)
        meta = stack!!.itemMeta
    }

    fun setName(name: String): ItemBuilder {
        meta!!.displayName(Component.text(name))
        return this
    }

    fun setLore(vararg lines: String): ItemBuilder {
        val finalLore = mutableListOf<Component>()
        lines.forEach { l ->
            finalLore.add(Component.text(l))
        }
        meta!!.lore(finalLore)
        return this
    }

    fun setAmount(amount: Int): ItemBuilder {
        stack!!.amount = amount
        return this
    }

    fun build(): ItemStack {
        stack!!.itemMeta = meta
        return stack!!
    }

}