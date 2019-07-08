package android.architecture.searchexample.database

import android.database.Cursor
import androidx.room.Dao
import androidx.room.Query
import io.reactivex.Flowable

@Dao
interface SearchHistoryDao {
    @Query("SELECT * FROM searchhistory ORDER BY updateTime DESC LIMIT 10")
    fun getSearchHistory() : Flowable<List<SearchHistory>>

    @Query("SELECT * FROM searchhistory ORDER BY updateTime DESC LIMIT 10")
    fun getSearchHistoryCursor() : Cursor

}