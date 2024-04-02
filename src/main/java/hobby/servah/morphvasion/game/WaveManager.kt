package hobby.servah.morphvasion.game

import hobby.servah.morphvasion.MorphVasion
import hobby.servah.morphvasion.util.SpawnSettings

class WaveManager(plugin: MorphVasion) {

    private var wave: Int = 0
    private val unlocked = mutableSetOf(0, 1) // unlocked by default
    private val spawnCfg = SpawnSettings(plugin)

    private fun calculateMobs(): List<Int> {
        // based on: wave, the unlocked mobs, and spawning.yml multipliers
        // integer is the amount to be spawned, list index represents the id
        val ids = mutableListOf("zombie", "creeper", "spider", "skeleton", "builder", "witch", "ghast")
        val counts = mutableListOf<Int>()

        spawnCfg.config.getConfigurationSection("mobs")?.getKeys(false)?.forEachIndexed { i, key ->
            val default = spawnCfg.getInt("mobs.$key")

            var multiplier = spawnCfg.config.getDouble("waves.$wave.general")
            if(multiplier == 0.0) multiplier = 1.0

            var specificMultiplier = spawnCfg.config.getDouble("waves.$wave.${ids[i]}")
            if(specificMultiplier == 0.0) specificMultiplier = 1.0

            if(unlocked.contains(i)) counts.add(i, (default*wave*multiplier*specificMultiplier).toInt())
        }

        return counts
    }

}