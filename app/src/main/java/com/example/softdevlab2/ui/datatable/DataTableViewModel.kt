package com.example.softdevlab2.ui.datatable

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DataTableViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is DataTable Fragment"
    }
    val text: LiveData<String> = _text
}