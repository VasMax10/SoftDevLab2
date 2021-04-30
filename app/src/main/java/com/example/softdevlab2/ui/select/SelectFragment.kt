package com.example.softdevlab2.ui.select

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.softdevlab2.*
import com.example.softdevlab2.ui.datatable.DataTableViewModel
import kotlinx.android.synthetic.main.dialog_create.*
import kotlinx.android.synthetic.main.dialog_details.*
import kotlinx.android.synthetic.main.dialog_update.*
import kotlinx.android.synthetic.main.fragment_datatable.*
import kotlinx.android.synthetic.main.fragment_datatable.rvItemsList
import kotlinx.android.synthetic.main.fragment_select.*

class SelectFragment : Fragment() {
    private lateinit var selectViewModel: SelectViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        selectViewModel =
                ViewModelProvider(this).get(SelectViewModel::class.java)

        val root = inflater.inflate(R.layout.fragment_select, container, false)

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupListofDataIntoRecyclerView()
        setCountChange()
    }

    fun setCountChange() {

        //creating the instance of DatabaseHandler class
        val databaseHandler: DatabaseHandler = DatabaseHandler(requireContext())
        //calling the viewEmployee method of DatabaseHandler class to read the records
        val count = databaseHandler.viewBrandsValueMore20()

        val res = "DataBase contains $count elements that cost more than 20 Billions"

        select_count.text = res
    }

    /**
     * Function is used to get the Items List which is added in the database table.
     */
    private fun getItemsList(): ArrayList<InterbrandRating> {
        //creating the instance of DatabaseHandler class
        val databaseHandler: DatabaseHandler = DatabaseHandler(requireContext())
        //calling the viewEmployee method of DatabaseHandler class to read the records
        val brandList: ArrayList<InterbrandRating> = databaseHandler.viewBrandsChangeMore50()

        return brandList
    }

    /**
     * Function is used to show the list on UI of inserted data.
     */
    private fun setupListofDataIntoRecyclerView() {

        if (getItemsList().size > 0) {

            selItemsList.visibility = View.VISIBLE
            selNoRecordsAvailable.visibility = View.GONE

            // Set the LayoutManager that this RecyclerView will use.
            selItemsList.layoutManager = LinearLayoutManager(requireContext())
            // Adapter class is initialized and list is passed in the param.
            val selectItemAdapter = SelectItemAdapter(this, requireContext(), getItemsList())
            // adapter instance is set to the recyclerview to inflate the items.
            selItemsList.adapter = selectItemAdapter
        } else {

            selItemsList.visibility = View.GONE
            selNoRecordsAvailable.visibility = View.VISIBLE
        }
    }
}