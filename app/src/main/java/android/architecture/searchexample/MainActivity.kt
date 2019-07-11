package android.architecture.searchexample

import android.annotation.SuppressLint
import android.architecture.searchexample.database.AppDataBase
import android.architecture.searchexample.database.SearchHistoryDao
import android.database.Cursor
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.cursoradapter.widget.SimpleCursorAdapter
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    var searchHistoryDao: SearchHistoryDao? = null
    var searchView : SearchView? = null
    var adapter : SimpleCursorAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Timber.plant(Timber.DebugTree())
        setupToolbar()
        setupButton()

    }

    private fun setupButton() {
        button.setOnClickListener {
            startActivity(newSearchActivity())
        }
    }

    private fun setupToolbar() {
        toolbar.apply {
            title="Example"
            inflateMenu(R.menu.search_menu)
        }

        //NPE 나는지 확인.
        searchHistoryDao.let {
            AppDataBase.getInstance(this.applicationContext).searchHistoryDao()
        }

        toolbar.menu?.let {
            val searchItem = it.findItem(R.id.action_search)
            searchItem?.let {
                //메뉴의 확장과 축소 이벤트 관리.
                it.setOnActionExpandListener(object : MenuItem.OnActionExpandListener{
                    override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
                        layout_search_result.visibility = View.VISIBLE
                        return true
                    }

                    override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                        layout_search_result.visibility = View.GONE
                        return true
                    }
                })

                searchView = it.actionView as SearchView

                searchView?.maxWidth = (Integer.MAX_VALUE)
                searchView?.queryHint ="검색어를 입력해주세요"

                //검색, 변경,이벤트 관리
                searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        Log.d("searchView", "onQueryTextSubmit $query")

                        query?.let {
                            Observable.just(it).observeOn(Schedulers.computation())
                                .map {
                                    val now = System.currentTimeMillis().toString()
                                    searchHistoryDao
                                }
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe({
                                    Log.d("search","Search is success")
                                },{
                                    Log.d("search ", " Search is failed")
                                })
                        }

                        return false
                    }

                    @SuppressLint("CheckResult")
                    override fun onQueryTextChange(newText: String?): Boolean {
                        Log.d("searchView", "onQueryTextChange $newText")
                            Observable.just(newText).observeOn(Schedulers.computation())
                                .map {
                                    searchHistoryDao?.getSearchHistoryCursor()
                                }
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe({
                                    it?.let {
                                        handlerResults(it)
                                        Log.d("search ", " onQueryTextChange success")

                                    }
                                },{
                                    Log.d("search ", " onQueryTextChange is failed")
                                })

                        return false

                    }
                })


                searchView?.setOnSuggestionListener(object : SearchView.OnSuggestionListener{
                    override fun onSuggestionSelect(position: Int): Boolean {
                        return true
                    }

                    override fun onSuggestionClick(position: Int): Boolean {
                        Log.d("search ", " onSuggestionClick ")
                        val cursor = adapter?.getItem(position) as Cursor
                        val txt = cursor.getString(cursor.getColumnIndex("name"))
                        searchView?.setQuery(txt,true)
                        return true
                    }
                })
            }
        }




    }

    private fun handlerResults(cursor : Cursor){
        adapter = SimpleCursorAdapter(this, android.R.layout.simple_list_item_1, cursor, arrayOf("name"), intArrayOf(android.R.id.text1), androidx.cursoradapter.widget.CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER)
        searchView?.suggestionsAdapter = adapter

    }
}
