package hobby.servah.morphvasion.game

import java.util.*

class Gamer { // basically like player but contains more info

    companion object {

        private val gamers = HashMap<UUID, Gamer>()

        fun getGamer(u: UUID): Gamer {
            if(gamers[u] == null) gamers[u] = Gamer()

            return gamers[u]!!
        }

    }

    var isInMobSelection = false

}