package hobby.servah.morphvasion.game

import hobby.servah.morphvasion.MorphVasion
import hobby.servah.morphvasion.game.morphs.Morphs
import hobby.servah.morphvasion.util.SpawnSettings

class WaveManager(main: MorphVasion) {

    private var wave: Int = 0
    private val unlocked = mutableSetOf(Morphs.ZOMBIE, Morphs.CREEPER) // unlocked by default
    private val spawnCfg = SpawnSettings(main)

    private fun calculateMobs(): List<Int> {
        // based on: wave, the unlocked mobs, and TODO: waves.yml multipliers
        // integer is the amount to be spawned, list index represents the id
        val mobs = mutableListOf<Int>()

        for (i in 0 until Morphs.values().size) {
            //TODO
        }

        return mobs
    }

}