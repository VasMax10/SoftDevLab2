package com.example.softdevlab2

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.softdevlab2.ui.datatable.DataTableFragment
import com.example.softdevlab2.ui.select.SelectFragment
import kotlinx.android.synthetic.main.fragment_datatable.view.tvName
import kotlinx.android.synthetic.main.item_row.view.*
import kotlinx.android.synthetic.main.select_item_row.view.*

class SelectItemAdapter(val datatableFragment: SelectFragment, val context: Context, val items: ArrayList<InterbrandRating>) :
        RecyclerView.Adapter<SelectItemAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
                LayoutInflater.from(context).inflate(
                        R.layout.select_item_row,
                        parent,
                        false
                )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = items.get(position)

        holder.selName.text = item.name
        holder.selRank.text = item.rank.toString()
        holder.selCost.text = item.cost.toString()
        holder.selCity.text = item.city
        holder.selChange.text = item.change.toString()

        // Updating the background color according to the odd/even positions in list.
        if (position % 2 == 0) {
            holder.selMain.setBackgroundColor(
                    ContextCompat.getColor(
                            context,
                            R.color.colorLightGray
                    )
            )
        } else {
            holder.selMain.setBackgroundColor(ContextCompat.getColor(context, R.color.white))
        }
    }

    /**
     * Gets the number of items in the list
     */
    override fun getItemCount(): Int {
        return items.size
    }

    /**
     * A ViewHolder describes an item view and metadata about its place within the RecyclerView.
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // Holds the TextView that will add each item to
        val selMain = view.selMain
        val selName = view.selName
        val selRank = view.selRank
        val selCost = view.selCost
        val selCity = view.selCity
        val selChange = view.selChange
    }
}