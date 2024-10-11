package com.example.feedback2.userInterface

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.feedback2.data.Novel

class NovelAdapter (private var novels: List<Novel>) : BaseAdapter(){

    override fun getCount(): Int {
        return novels.size
    }

    override fun getItem(position: Int): Novel {
        return novels[position]
    }

    override fun getItemId(position: Int) : Long {
        return novels[position].id.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = convertView ?: LayoutInflater.from(parent?.context)               //Creamos una vista para comprobar si es nulo y si lo es, la inflamos
          .inflate(android.R.layout.simple_list_item_2, parent, false)        //Segun lo explicado en clase.

        val titleTextView = view.findViewById<TextView>(android.R.id.text1)
        val authorTextView = view.findViewById<TextView>(android.R.id.text2)

        val novel = getItem(position)

        titleTextView.text = novel.title
        authorTextView.text = novel.author

        titleTextView.text = "${novel.title}"

        return view
    }

    fun setNovels(novels: List<Novel>){
        this.novels = novels
        notifyDataSetChanged()
    }

}