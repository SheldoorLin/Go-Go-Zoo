package com.sam.gogozoo.data

import android.os.Parcelable
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.parcel.Parcelize

@Parcelize
data class NavInfo(
    val title: String = "",
    val latLng: String = ""
):Parcelable