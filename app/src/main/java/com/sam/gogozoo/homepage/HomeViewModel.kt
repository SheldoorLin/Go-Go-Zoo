package com.sam.gogozoo.homepage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.sam.gogozoo.ZooApplication
import com.sam.gogozoo.data.source.PublisherRepository
import com.sam.gogozoo.network.LoadApiStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

class HomeViewModel(private val repository: PublisherRepository) : ViewModel() {
    // status: The internal MutableLiveData that stores the status of the most recent request
    private val _status = MutableLiveData<LoadApiStatus>()

    val status: LiveData<LoadApiStatus>
        get() = _status

    // error: The internal MutableLiveData that stores the error of the most recent request
    private val _error = MutableLiveData<String>()

    val error: LiveData<String>
        get() = _error

    // status for the loading icon of swl
    private val _refreshStatus = MutableLiveData<Boolean>()

    val refreshStatus: LiveData<Boolean>
        get() = _refreshStatus

    // Create a Coroutine scope using a job to be able to cancel when needed
    private var viewModelJob = Job()

    // the Coroutine runs using the Main (UI) dispatcher
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)



    /**
     * When the [ViewModel] is finished, we cancel our coroutine [viewModelJob], which tells the
     * Retrofit service to stop.
     */
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    /**
     * Call getArticlesResult() on init so we can display status immediately.
     */
    init {

    }

    val callback1 = OnMapReadyCallback { googleMap ->
        val location1 = LatLng(24.9931338, 121.5907654)
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(10f), 2000, null)
        googleMap.addMarker(MarkerOptions().position(location1).title("國王企鵝"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location1, 17f))
    }
    val callback2 = OnMapReadyCallback { googleMap ->
        val location2 = LatLng(24.9951066, 121.5856424)
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(10f), 2000, null)
        googleMap.addMarker(MarkerOptions().position(location2).title("非洲野驢"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location2, 17f))
    }

//    fun getArticlesResult() {
//
//        coroutineScope.launch {
//
//            _status.value = LoadApiStatus.LOADING
//
//            val result = repository.getArticles()
//
//            _articles.value = when (result) {
//                is Result.Success -> {
//                    _error.value = null
//                    _status.value = LoadApiStatus.DONE
//                    result.data
//                }
//                is Result.Fail -> {
//                    _error.value = result.error
//                    _status.value = LoadApiStatus.ERROR
//                    null
//                }
//                is Result.Error -> {
//                    _error.value = result.exception.toString()
//                    _status.value = LoadApiStatus.ERROR
//                    null
//                }
//                else -> {
//                    _error.value = PublisherApplication.instance.getString(R.string.you_know_nothing)
//                    _status.value = LoadApiStatus.ERROR
//                    null
//                }
//            }
//            _refreshStatus.value = false
//        }
//    }

    fun refresh() {

        if (ZooApplication.INSTANCE.isLiveDataDesign()) {
            _status.value = LoadApiStatus.DONE
            _refreshStatus.value = false

        } else {
            if (status.value != LoadApiStatus.LOADING) {

            }
        }
    }
}
