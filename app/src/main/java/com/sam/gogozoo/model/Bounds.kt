/*
 * Created by Muhammad Utsman on 31/12/2018
 * Copyright (c) 2018 . All rights reserved.
 * Last modified 12/31/18 11:21 PM
 */

package com.sam.gogozoo.model

import com.google.gson.annotations.SerializedName

data class Bounds(
        @SerializedName("northeast")
        var northeast: Northeast?,
        @SerializedName("southwest")
        var southwest: Southwest?
)