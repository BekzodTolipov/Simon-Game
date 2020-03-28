package edu.umsl.simonsaysapp.persistence.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "players")
data class PlayerEntitiy(
    @ColumnInfo val name: String,
    @ColumnInfo val score: Int
) {
    @PrimaryKey(autoGenerate = true) var id: Int = 0
}