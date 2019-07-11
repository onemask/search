package android.architecture.searchexample

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.SearchRecentSuggestions
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import timber.log.Timber

fun Context.newSearchActivity(): Intent{
    val intent = Intent(this,SearchActivity::class.java)
    return intent
}

class SearchActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        handleIntent()
        Timber.d("onCreate")

    }

    private fun handleIntent() {
        Timber.d("handleIntent")
        if(Intent.ACTION_SEARCH == intent.action){
            intent.getStringExtra(SearchManager.QUERY)?.also {query->

            SearchRecentSuggestions(this,MySuggestionProvider.AUTHORITY,MySuggestionProvider.MODE).saveRecentQuery(query,null)
            /*//검색 실행하는 로직
            domySearch(query)*/
            }
        }
    }

    override fun onNewIntent(intent : Intent){
        Timber.d("onNextIntent")
        setIntent(intent)
        handleIntent()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        Timber.d("onCreateOptionsMenu")
        // Inflate the options menu from XML
        val inflater = menuInflater
        inflater.inflate(R.menu.search_menu, menu)



        // Get the SearchView and set the searchable configuration
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        (menu.findItem(R.id.action_search).actionView as SearchView).apply {
            // Assumes current activity is the searchable activity
            setSearchableInfo(searchManager.getSearchableInfo(componentName))
            setIconifiedByDefault(false) // Do not iconify the widget; expand it by default
        }

        return true
    }
    fun domySearch(query : String){

    }

    //검색 기능이 실행되었을 경우
    override fun onSearchRequested(): Boolean {

        return super.onSearchRequested()
    }

    //검색 History 삭제
    fun clearSearchHistory(){
        SearchRecentSuggestions(this,MySuggestionProvider.AUTHORITY,MySuggestionProvider.MODE).clearHistory()
    }
}
