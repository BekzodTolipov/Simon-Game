package edu.umsl.simonsaysapp.persistence.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import edu.umsl.simonsaysapp.persistence.entities.PlayerEntitiy

@Dao
interface PlayerDao {
    @Insert
    suspend fun addPlayer(player: PlayerEntitiy)

    @Query("SELECT * FROM players ORDER BY score DESC")
    suspend fun fetchPlayers(): List<PlayerEntitiy>
}