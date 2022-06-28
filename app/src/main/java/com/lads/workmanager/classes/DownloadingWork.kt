package com.lads.workmanager

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.work.Worker
import androidx.work.WorkerParameters
import java.text.SimpleDateFormat
import java.util.*

class DownloadingWork(context: Context, params: WorkerParameters) : Worker(context, params) {
    override fun doWork(): Result {
        return try {
            for (i in 0..2000) {
                Log.i(TAG, "Downloading:$i ")
            }
            val time = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
            val currentDate: String = time.format(Date())
            Toast.makeText(applicationContext, "$currentDate", Toast.LENGTH_LONG).show()
            Log.i(TAG, "Completed interval $currentDate ")
            return Result.success()
        } catch (e: Exception) {
            Result.failure()
        }

    }


}