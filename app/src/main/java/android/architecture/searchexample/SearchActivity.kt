package android.architecture.searchexample

import android.annotation.SuppressLint
import android.app.SearchManager
import android.architecture.searchexample.adapter.RecentSearchAdapter
import android.architecture.searchexample.adapter.SearchAdapter
import android.architecture.searchexample.database.addData.AppDataBase
import android.architecture.searchexample.database.addData.SearchHistory
import android.architecture.searchexample.database.chapter.Chapter
import android.architecture.searchexample.database.chapter.ChapterDatabase
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.SearchRecentSuggestions
import android.view.Menu
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import io.reactivex.Completable
import io.reactivex.CompletableObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Action
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_search.*
import timber.log.Timber

fun Context.newSearchActivity(): Intent{
    val intent = Intent(this,SearchActivity::class.java)
    return intent
}

class SearchActivity : AppCompatActivity() {

    lateinit var recentSearchAdapter: RecentSearchAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        Timber.plant(Timber.DebugTree())
        setupRecnentSeach()
        handleIntent()
    }

    private fun setupRecnentSeach() {
        recycler_searchHistory.layoutManager = LinearLayoutManager(applicationContext)
        recentSearchAdapter = RecentSearchAdapter(
            listener = { type, searchHistory ->
                when (type) {
                    0 -> this.onSearchRequested()
                }
            })
        recycler_searchHistory.adapter = recentSearchAdapter
        recycler_searchHistory.visibility = View.GONE

    }

    private fun handleIntent() {
        Timber.d("handleIntent")
        if (Intent.ACTION_SEARCH == intent.action) {
            Timber.d("handleIntent true")

            intent.getStringExtra(SearchManager.QUERY)?.also { query ->
                SearchRecentSuggestions(this, MySuggestionProvider.AUTHORITY, MySuggestionProvider.MODE)
                    .saveRecentQuery(query, null)
                Timber.d("query $query")
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        Timber.d("onNextIntent")
        setIntent(intent)
        handleIntent()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        Timber.d("onCreateOptionsMenu")
        // Inflate the options menu from XML
        val inflater = menuInflater
        inflater.inflate(R.menu.search_menu, menu)

        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView
        searchView.isSubmitButtonEnabled = true
        searchView.queryHint = "Input SearchView"
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            //검색 click
            override fun onQueryTextSubmit(query: String?): Boolean {
                Timber.d("onQueryTextSubmit")
                updateSearch(query!!)
                getNameFromDb(query)
                return true
            }

            //query가 바뀔때 불리운다. -> 추천 검색어
            override fun onQueryTextChange(newText: String?): Boolean {
                Timber.d("onQueryTextChange")
                getNameFromDb(newText)
                return true
            }
        })

        return super.onCreateOptionsMenu(menu)
    }

    override fun onSearchRequested(): Boolean {
        return super.onSearchRequested()
    }

    private fun getNameFromDb(searchText: String?) {
        val searchTextQuery = "%$searchText%"
        val chapterDatabase = ChapterDatabase.getDatabase(this)
        chapterDatabase?.chapterDao()?.getChapterName(searchTextQuery)
            ?.observe(this, object : Observer<List<Chapter>> {
                override fun onChanged(chapter: List<Chapter>?) {
                    if (chapter == null) {
                        return
                    }
                    val adapter = SearchAdapter(
                        this@SearchActivity,
                        R.layout.item_serach,
                        chapter
                    )
                    lvSearchResult.adapter = adapter
                }
            })
    }

    @SuppressLint("CheckResult")
    fun getSearchHistoryByName(searchString: String) {
        val appDataBase = AppDataBase.getInstance(this)
        val searchHistory = SearchHistory(0,searchString,System.currentTimeMillis().toString())

        appDataBase.searchHistoryDao().getSearchHistoryByName(searchString)
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    Timber.d("SearchFragment searchHistoryDao getSearchHistoryByName onSuccess : $it")
                    searchHistory.id = it.id
                    updateSearch(searchString)
                },
                {
                    Timber.d("SearchFragment searchHistoryDao getSearchHistoryByName Error cause : ${it.cause}")
                    Timber.d("SearchFragment searchHistoryDao getSearchHistoryByName Error message: ${it.message}")
                },
                {
                    Timber.d("SearchFragment searchHistoryDao getSearchHistoryByName onComplete:")
                }
            )
    }


    fun domySearch(query : String){

    }

    fun updateSearch(searchString: String) {
        val appDataBase = AppDataBase.getInstance(this)
        val searchHistory = SearchHistory(0,searchString,System.currentTimeMillis().toString())
        Completable.fromAction(Action {
            appDataBase.searchHistoryDao().update(searchHistory)
            }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : CompletableObserver {
                    override fun onComplete() {
                        Timber.d("searchHistoryDao updateSearch onComplete")
                    }

                    override fun onSubscribe(d: Disposable) {
                        Timber.d("searchHistoryDao updateSearch onSubscribe $d")
                    }

                    override fun onError(e: Throwable) {
                        Timber.d("searchHistoryDao updateSearch onError $e")
                    }
                })
        }




    //검색 History 삭제
    fun clearSearchHistory(){
        SearchRecentSuggestions(this,MySuggestionProvider.AUTHORITY,MySuggestionProvider.MODE).clearHistory()
    }
}
