package com.ilnur.cardsnew.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.ilnur.cardsnew.R
import com.ilnur.cardsnew.database.Subject
import com.ilnur.cardsnew.viewmodel.SharedViewModel

class SubjectAdapter(val context: Context, val data: List<Subject>,
                     val itemSelectedListener: ItemSelectedListener
) :
    RecyclerView.Adapter<SubjectAdapter.SubjViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubjViewHolder {
        return SubjViewHolder(LayoutInflater.from(context).inflate(R.layout.recycler_item_subj, parent, false))
    }

    override fun onBindViewHolder(holder: SubjViewHolder, position: Int) {
        holder.bindItems(data[position])

    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class SubjViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val title: TextView = itemView.findViewById(R.id.title)
        val image: ImageView = itemView.findViewById(R.id.image)

        init {
            itemView.setOnClickListener{
                itemSelectedListener.onItemSelected(data[adapterPosition])
            }
        }
        fun bindItems(item: Subject){
            title.text = item.title
            //image will be later
        }

     }

    interface ItemSelectedListener {
        fun onItemSelected(item:Subject)
    }
}

