package com.example.softdevlab2.ui.datatable

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.softdevlab2.*
import com.example.softdevlab2.ui.datatable.DataTableViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.dialog_create.*
import kotlinx.android.synthetic.main.dialog_create.tvCrCancel
import kotlinx.android.synthetic.main.dialog_create.tvCreate
import kotlinx.android.synthetic.main.dialog_details.*
import kotlinx.android.synthetic.main.dialog_update.*
import kotlinx.android.synthetic.main.dialog_update.etUpdateChange
import kotlinx.android.synthetic.main.dialog_update.etUpdateCity
import kotlinx.android.synthetic.main.dialog_update.etUpdateCost
import kotlinx.android.synthetic.main.dialog_update.etUpdateName
import kotlinx.android.synthetic.main.dialog_update.etUpdateRank
import kotlinx.android.synthetic.main.fragment_datatable.*


class DataTableFragment : Fragment() {

    private lateinit var dataTableViewModel: DataTableViewModel

//    private var fragment1Binding: DataTableFragmentBinding? = null

    private lateinit var comm : Communicator

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        dataTableViewModel =
                ViewModelProvider(this).get(DataTableViewModel::class.java)

        val root = inflater.inflate(R.layout.fragment_datatable, container, false)

        comm = requireActivity() as Communicator


        return root
//        val binding = DataTableBinding.inflate(inflater, container, false)
//        fragment1Binding = binding
//
//        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupListofDataIntoRecyclerView()

        val databaseHandler: DatabaseHandler = DatabaseHandler(requireContext())
//        val res = databaseHandler.viewBrandsChangeMore50()
        val res = databaseHandler.viewBrandsValueMore20()
        Toast.makeText(context, res.toString(), Toast.LENGTH_LONG).show()


        fab.setOnClickListener{ view ->
            createRecordDialog()
        }

    }

    fun showMap(brand : InterbrandRating){
        comm.passDataCom(brand.name, brand.city)
    }

    /**
     * Function is used to get the Items List which is added in the database table.
     */
    private fun getItemsList(): ArrayList<InterbrandRating> {
        //creating the instance of DatabaseHandler class
        val databaseHandler: DatabaseHandler = DatabaseHandler(requireContext())
        //calling the viewEmployee method of DatabaseHandler class to read the records
        val brandList: ArrayList<InterbrandRating> = databaseHandler.viewBrands()

        return brandList
    }

    /**
     * Function is used to show the list on UI of inserted data.
     */
    private fun setupListofDataIntoRecyclerView() {

        if (getItemsList().size > 0) {

            rvItemsList.visibility = View.VISIBLE
            tvNoRecordsAvailable.visibility = View.GONE

            // Set the LayoutManager that this RecyclerView will use.
            rvItemsList.layoutManager = LinearLayoutManager(requireContext())
            // Adapter class is initialized and list is passed in the param.
            val itemAdapter = ItemAdapter(this, requireContext(), getItemsList())
            // adapter instance is set to the recyclerview to inflate the items.
            rvItemsList.adapter = itemAdapter
        } else {

            rvItemsList.visibility = View.GONE
            tvNoRecordsAvailable.visibility = View.VISIBLE
        }
    }

    /**
     * Method is used to show the custom update dialog.
     */
    fun createRecordDialog() {
        val createDialog = Dialog(requireContext(), R.style.Theme_Dialog)
        createDialog.setCancelable(false)
        /*Set the screen content from a layout resource.
         The resource will be inflated, adding all top-level views to the screen.*/
        createDialog.setContentView(R.layout.dialog_create)

        createDialog.tvCreate.setOnClickListener(View.OnClickListener {

            val name = createDialog.etCreateName.text.toString()
            val rank = createDialog.etCreateRank.text.toString()
            val cost = createDialog.etCreateCost.text.toString()
            val city = createDialog.etCreateCity.text.toString()
            val change = createDialog.etCreateChange.text.toString()

            val databaseHandler: DatabaseHandler = DatabaseHandler(requireContext())

            if (!name.isEmpty() && !rank.isEmpty() && !cost.isEmpty() && !city.isEmpty()
                    &&!change.isEmpty()) {
                val status =
                        databaseHandler.addBrand(InterbrandRating(0, name, rank.toInt(), cost.toDouble(), city, change.toInt()))
                if (status > -1) {
                    Toast.makeText(context, "Record Created.", Toast.LENGTH_LONG).show()

                    setupListofDataIntoRecyclerView()

                    createDialog.dismiss() // Dialog will be dismissed
                }
            } else {
                Toast.makeText(
                        context,
                        "Fields cannot be blank",
                        Toast.LENGTH_LONG
                ).show()
            }
        })
        createDialog.tvCrCancel.setOnClickListener(View.OnClickListener {
            createDialog.dismiss()
        })
        //Start the dialog and display it on screen.
        createDialog.show()
    }

    /**
     * Method is used to show the custom update dialog.
     */
    fun updateRecordDialog(brand: InterbrandRating) {
        val updateDialog = Dialog(requireContext(), R.style.Theme_Dialog)
        updateDialog.setCancelable(false)
        /*Set the screen content from a layout resource.
         The resource will be inflated, adding all top-level views to the screen.*/
        updateDialog.setContentView(R.layout.dialog_update)

        updateDialog.etUpdateName.setText(brand.name)
        updateDialog.etUpdateRank.setText(brand.rank.toString())
        updateDialog.etUpdateCost.setText(brand.cost.toString())
        updateDialog.etUpdateCity.setText(brand.city)
        updateDialog.etUpdateChange.setText(brand.change.toString())

        updateDialog.tvUpdate.setOnClickListener(View.OnClickListener {

            val name = updateDialog.etUpdateName.text.toString()
            val rank = updateDialog.etUpdateRank.text.toString()
            val cost = updateDialog.etUpdateCost.text.toString()
            val city = updateDialog.etUpdateCity.text.toString()
            val change = updateDialog.etUpdateChange.text.toString()

            val databaseHandler: DatabaseHandler = DatabaseHandler(requireContext())

            if (!name.isEmpty() && !rank.isEmpty() && !cost.isEmpty() && !city.isEmpty()
                    &&!change.isEmpty()) {
                val status =
                        databaseHandler.updateBrand(InterbrandRating(brand.id,
                                name, rank.toInt(), cost.toDouble(), city, change.toInt()))
                if (status > -1) {
                    Toast.makeText(context, "Record Updated.", Toast.LENGTH_LONG).show()

                    setupListofDataIntoRecyclerView()

                    updateDialog.dismiss() // Dialog will be dismissed
                }
            } else {
                Toast.makeText(
                        context,
                        "Fields cannot be blank",
                        Toast.LENGTH_LONG
                ).show()
            }
        })
        updateDialog.tvUpCancel.setOnClickListener(View.OnClickListener {
            updateDialog.dismiss()
        })
        //Start the dialog and display it on screen.
        updateDialog.show()
    }

    /**
     * Method is used to show the delete alert dialog.
     */
    fun deleteRecordAlertDialog(brand: InterbrandRating) {
        val builder = AlertDialog.Builder(context)
        //set title for alert dialog
        builder.setTitle("Delete Record")
        //set message for alert dialog
        builder.setMessage("Are you sure you wants to delete ${brand.name}.")
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        //performing positive action
        builder.setPositiveButton("Yes") { dialogInterface, which ->

            //creating the instance of DatabaseHandler class
            val databaseHandler: DatabaseHandler = DatabaseHandler(requireContext())
            //calling the deleteEmployee method of DatabaseHandler class to delete record
            val status = databaseHandler.deleteBrand(InterbrandRating(brand.id, "", 0, 0.0,"",0))
            if (status > -1) {
                Toast.makeText(
                        context,
                        "Record deleted successfully.",
                        Toast.LENGTH_LONG
                ).show()

                setupListofDataIntoRecyclerView()
            }

            dialogInterface.dismiss() // Dialog will be dismissed
        }
        //performing negative action
        builder.setNegativeButton("No") { dialogInterface, which ->
            dialogInterface.dismiss() // Dialog will be dismissed
        }
        // Create the AlertDialog
        val alertDialog: AlertDialog = builder.create()
        // Set other dialog properties
        alertDialog.setCancelable(false) // Will not allow user to cancel after clicking on remaining screen area.
        alertDialog.show()  // show the dialog to UI
    }

    fun showRecordDialog(brand: InterbrandRating) {
        val detailsDialog = Dialog(requireContext(), R.style.Theme_Dialog)
        detailsDialog.setCancelable(false)
        /*Set the screen content from a layout resource.
         The resource will be inflated, adding all top-level views to the screen.*/
        detailsDialog.setContentView(R.layout.dialog_details)

        detailsDialog.etShowName.setText(brand.name)
        detailsDialog.etShowRank.setText(brand.rank.toString())
        val showcost = brand.cost.toString() + " B"
        detailsDialog.etShowCost.setText(showcost)
        detailsDialog.etShowCity.setText(brand.city)
        var showchange = brand.change.toString() + "%"
        if (brand.change > 0)
            showchange = "+" + showchange
        detailsDialog.etShowChange.setText(showchange)

        detailsDialog.tvCloseShow.setOnClickListener(View.OnClickListener {
            detailsDialog.dismiss()
        })
        //Start the dialog and display it on screen.
        detailsDialog.show()
    }
}