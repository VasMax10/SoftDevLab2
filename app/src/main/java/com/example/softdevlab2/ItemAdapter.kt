package com.example.softdevlab2

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.softdevlab2.ui.datatable.DataTableFragment
import kotlinx.android.synthetic.main.fragment_datatable.view.tvName
import kotlinx.android.synthetic.main.item_row.view.*

class ItemAdapter(val datatableFragment: DataTableFragment, val context: Context, val items: ArrayList<InterbrandRating>) :
        RecyclerView.Adapter<ItemAdapter.ViewHolder>() {


    /**
     * Inflates the item views which is designed in the XML layout file
     *
     * create a new
     * {@link ViewHolder} and initializes some private fields to be used by RecyclerView.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
                LayoutInflater.from(context).inflate(
                        R.layout.item_row,
                        parent,
                        false
                )
        )
    }

    /**
     * Binds each item in the ArrayList to a view
     *
     * Called when RecyclerView needs a new {@link ViewHolder} of the given type to represent
     * an item.
     *
     * This new ViewHolder should be constructed with a new View that can represent the items
     * of the given type. You can either create a new View manually or inflate it from an XML
     * layout file.
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = items.get(position)

        holder.tvName.text = item.name
        holder.tvRank.text = item.rank.toString()
        holder.tvCost.text = item.cost.toString()
//        holder.tvCity.text = item.city
//        holder.tvChange.text = item.change.toString()

        // Updating the background color according to the odd/even positions in list.
        if (position % 2 == 0) {
            holder.llMain.setBackgroundColor(
                    ContextCompat.getColor(
                            context,
                            R.color.colorLightGray
                    )
            )
        } else {
            holder.llMain.setBackgroundColor(ContextCompat.getColor(context, R.color.white))
        }

        holder.ivMap.setOnClickListener { view ->
            datatableFragment.showMap(item)
        }

        holder.ivInfo.setOnClickListener { view ->
            datatableFragment.showRecordDialog(item)
        }

        holder.ivEdit.setOnClickListener { view ->
            datatableFragment.updateRecordDialog(item)
        }
        holder.ivDelete.setOnClickListener { view ->
                datatableFragment.deleteRecordAlertDialog(item)
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
        val llMain = view.llMain
        val tvName = view.tvName
        val tvRank = view.tvRank
        val tvCost = view.tvCost
//        val tvCity = view.tvCity
//        val tvChange = view.tvChange
        val ivMap = view.ivMap
        val ivInfo = view.ivInfo
        val ivEdit = view.ivEdit
        val ivDelete = view.ivDelete
    }
}