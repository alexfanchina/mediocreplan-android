package com.race.mediocreplan.data.model

import com.google.gson.annotations.SerializedName

data class TaskUpdatePopularityResponse(
        @SerializedName("id") var _id: Int,
        @SerializedName("status") var status: String)