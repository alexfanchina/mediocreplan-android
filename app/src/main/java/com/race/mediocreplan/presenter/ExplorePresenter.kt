package com.race.mediocreplan.presenter

import android.util.Log
import com.race.mediocreplan.data.RetrofitClientInstance
import com.race.mediocreplan.data.api.MediocrePlanService
import com.race.mediocreplan.data.model.Task
import com.race.mediocreplan.ui.IExploreView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ExplorePresenter(val iView: IExploreView?) : IExplorePresenter {

    companion object {
        const val TAG = "ExplorePresenter"
    }

    override fun getAllTasks() {
        val mediocrePlanService =
                RetrofitClientInstance.getRetrofitInstance()
                        .create(MediocrePlanService::class.java)
        val call = mediocrePlanService.getAllTasks()
        call.enqueue(object : Callback<List<Task>> {
            override fun onFailure(call: Call<List<Task>>?, t: Throwable?) {
                Log.e(TAG, "error" + t?.message)
            }

            override fun onResponse(call: Call<List<Task>>?, response: Response<List<Task>>?) {
                Log.d(TAG, response?.raw().toString())
                iView?.onTasksLoad(response?.body())
            }
        })
    }

    override fun updateTaskPopularity() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}