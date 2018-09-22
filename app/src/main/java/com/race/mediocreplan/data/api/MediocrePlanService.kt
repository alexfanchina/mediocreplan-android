package com.race.mediocreplan.data.api

import com.race.mediocreplan.data.model.Task
import com.race.mediocreplan.data.model.TaskUpdatePopularityResponse
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.Call

interface MediocrePlanService {

    @GET("/users/update_task")
    fun getAllTasks(): Call<List<Task>>

    @GET("/users/update_task_popularity?id={id}")
    fun updateTaskPopularity(@Path("id") taskId: Int): Call<TaskUpdatePopularityResponse>

    companion object {
        const val ENDPOINT = "http://192.158.228.68:3000"
    }
}