package android.architecture.searchexample.adapter

import android.architecture.searchexample.R
import android.architecture.searchexample.database.addData.SearchHistory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_serach.view.*

class RecentSearchAdapter(
    private val listener: (Int,SearchHistory) -> Unit
) : RecyclerView.Adapter<RecentSearchAdapter.ViewHolder>() {
    private var dataSet: List<SearchHistory> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentSearchAdapter.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view : View = inflater.inflate(R.layout.item_serach,parent,false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: RecentSearchAdapter.ViewHolder, position: Int) {
        holder.bind(position, dataSet[position], listener)
    }

    override fun getItemCount(): Int = dataSet.size

    fun prependData(dataSet: List<SearchHistory>?) {
        this.dataSet = emptyList()
        dataSet?.let {
            this.dataSet = ArrayList(dataSet)
        }
        notifyDataSetChanged()
    }

    fun appendData(dataSet: List<SearchHistory>) {
        val previousDataSetSize = this.dataSet.size
        this.dataSet += dataSet
        notifyItemRangeInserted(previousDataSetSize, dataSet.size)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(position: Int, searchHistory: SearchHistory, listener: (Int,SearchHistory) -> Unit) = with(itemView) {
            this.text_search.text=searchHistory.name
            this.setOnClickListener {
                listener(position,searchHistory)
            }
        }

    }

}



