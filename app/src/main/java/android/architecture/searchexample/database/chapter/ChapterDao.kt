package android.architecture.searchexample.database.chapter

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ChapterDao {
    @Insert
    fun insert(chapter: Array<Chapter>)
    @Query("SELECT * FROM MindOrksDb  WHERE chapterName LIKE :query")
    fun getChapterName(query: String): LiveData<List<Chapter>>
}
