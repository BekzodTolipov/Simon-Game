package edu.umsl.simonsaysapp

import androidx.lifecycle.ViewModel

class GameModel: ViewModel() {
    lateinit var player: Players
    private set

    fun addNewPlayer(name: String, level: Int){
        player = Players(name, level, 0)
    }

    fun getPlayerInfo(): Players? {
        return player
    }

    fun setPlayerScore(score: Int) {
        player.score = score
    }
}