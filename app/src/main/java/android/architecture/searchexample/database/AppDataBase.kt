package android.architecture.searchexample.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [SearchHistory::class],version =1,exportSchema = false)
abstract class AppDataBase : RoomDatabase(){
    abstract fun searchHistoryDao() : SearchHistoryDao

    companion object{
        private var instance : AppDataBase? = null

        fun getInstance(context : Context) : AppDataBase{
            if(instance==null) {
                synchronized(AppDataBase::class){
                    instance = Room.databaseBuilder(context.applicationContext,
                        AppDataBase::class.java,
                        "search.db")
                        .fallbackToDestructiveMigration()
                        .build()
                }
            }
            return instance!!
        }

        fun destroyInstance(){
            instance =null
        }
    }
}