package android.architecture.searchexample.database.chapter

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "MindOrksDb")
data class Chapter(
    @PrimaryKey
    @ColumnInfo(name = "chapterName") val chapterName: String
) {
    companion object {
        //data 넣기.
        fun populateData(): Array<Chapter> {
            return arrayOf<Chapter>(
                Chapter("MindOrks"),
                Chapter("GetMeAnApp"),
                Chapter("BestContentApp"),
                Chapter("Hackerspace")
            )
        }
    }
}