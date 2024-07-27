package com.example.simplebibleapp.logDb

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.simplebibleapp.dataClasses.Selection

@Entity(tableName = "history_table")
class HistoryRecord (
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "selection") val selection: Selection
)

