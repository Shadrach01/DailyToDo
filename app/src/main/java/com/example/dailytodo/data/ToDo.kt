package com.example.dailytodo.data

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

/**
 * Entity data class that represents a single row in the database
 */

@Entity(tableName = "todos")
@Parcelize
data class ToDo(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val details: String,
    @ColumnInfo(name = "time")
    var timeString: String
): Parcelable {

    var time: LocalTime
        get() {
            return LocalTime.parse(timeString, DateTimeFormatter.ofPattern("hh:mm a"))
        }
        set(value) {
            timeString = value.format(DateTimeFormatter.ofPattern("hh:mm a"))
        }
}

