package com.sam.gogozoo.facilityDialog

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sam.gogozoo.data.FacilityItem
import com.sam.gogozoo.data.MockData
import com.sam.gogozoo.data.source.ZooRepository

class FacilityDialogViewModel(private val repository: ZooRepository, private val facilityItem: FacilityItem?) : ViewModel() {

    private val _leave = MutableLiveData<Boolean>()

    val leave: LiveData<Boolean>
        get() = _leave

    fun leave() {
        _leave.value = true
    }

    fun onLeaveCompleted() {
        _leave.value = null
    }

    val listFac = MutableLiveData<FacilityItem>()

    val selectItem = MutableLiveData<String>()

    init {
        forImage()
    }

    fun forImage(){
        val list = mutableListOf<String>()
        MockData.localFacility.forEach {
            val name = it.name
            list.add(name)
        }
        MockData.listFotFacImage = list
        Log.d("sam","listFotFacImage=${MockData.listFotFacImage}")
    }

}