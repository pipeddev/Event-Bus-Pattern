package com.pipe.d.dev.eventbuspatt.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.pipe.d.dev.eventbuspatt.eventBus.EventBus
import com.pipe.d.dev.eventbuspatt.SportEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class SportService: Service() {

    private val job = Job()
    private val scope = CoroutineScope(Dispatchers.IO + job)

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    fun saveResult(result: SportEvent.ResultSuccess) {
        scope.launch {
            val response = if (result.isWarning)
                SportEvent.ResultError(30, "Error al guardar")
            else SportEvent.SaveEvent
            EventBus.instance().publish(response)
        }
    }

     fun setupSubscribers(viewScope: CoroutineScope) {
         viewScope.launch {
            EventBus.instance().subscribe<SportEvent> { event ->
                when(event) {
                    is SportEvent.CloseAdEvent ->
                        Log.i("Project pipe", "Ad was closed Send data to server... ")
                        //binding.btnAd.visibility = View.GONE
                    else -> {}
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }

    // Singleton
    companion object {
        private val _service: SportService by lazy { SportService()}

        fun instance() = _service
    }
}