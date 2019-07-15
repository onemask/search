package android.architecture.searchexample.database.addData

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "SearchHistory")
data class SearchHistory (
    @PrimaryKey(autoGenerate = true)var id : Int,
    @ColumnInfo(name = "name")var name : String,
    @ColumnInfo(name ="updateTime" ) var updateTime : String
)

