package com.example.softdevlab2.ui.select

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SelectViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "This is DataTable Fragment"
    }
    val text: LiveData<String> = _text
}