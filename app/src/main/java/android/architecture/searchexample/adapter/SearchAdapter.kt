package android.architecture.searchexample.adapter

import android.architecture.searchexample.R
import android.architecture.searchexample.database.chapter.Chapter
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class SearchAdapter(contextt: Context, val layout: Int, val chapter: List<Chapter>) : ArrayAdapter<Chapter>(contextt, layout, chapter) {

    override fun getCount(): Int = chapter.size

    override fun getItem(position: Int): Chapter?  = chapter[position]

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var retView: View
        var vi = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        retView = if (convertView == null) {
            vi.inflate(layout, null)
        } else {
            convertView
        }
        var chapterItem = getItem(position)
        val chapterName = retView.findViewById(R.id.text_search) as TextView
        chapterName.text = chapterItem!!.chapterName
        return retView
    }
}