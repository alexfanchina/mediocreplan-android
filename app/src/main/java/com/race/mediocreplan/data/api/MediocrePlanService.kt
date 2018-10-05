package com.race.mediocreplan.data.api

import com.race.mediocreplan.data.model.Task
import com.race.mediocreplan.data.model.TaskUpdatePopularityResponse
import retrofit2.http.GET
import retrofit2.Call
import retrofit2.http.Query

interface MediocrePlanService {

    @GET("/users/update_task")
    fun getAllTasks(): Call<List<Task>>

    @GET("/users/update_task_popularity")
    fun updateTaskPopularity(@Query(value = "id", encoded = true) taskId: Int): Call<TaskUpdatePopularityResponse>

    companion object {
        const val ENDPOINT = "http://192.158.228.68:3000"
    }
}