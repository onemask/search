package android.architecture.searchexample.database.addData

import android.database.Cursor
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
import io.reactivex.Flowable
import io.reactivex.Maybe

@Dao
interface SearchHistoryDao {
    @Query("SELECT * FROM searchhistory ORDER BY updateTime DESC LIMIT 10")
    fun getSearchHistory() : Flowable<List<SearchHistory>>

    @Query("SELECT * FROM searchhistory ORDER BY updateTime DESC LIMIT 10")
    fun getSearchHistoryCursor() : Cursor

    @Query("SELECT * FROM searchhistory WHERE name =:nameString")
    fun getSearchHistoryByName(nameString: String) : Maybe<SearchHistory>

    @Update
    fun update(searchHistory: SearchHistory)
}