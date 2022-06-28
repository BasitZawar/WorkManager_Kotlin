package com.lads.workmanager

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import java.text.SimpleDateFormat
import java.util.*

public class UploadWork(context: Context, params: WorkerParameters) : Worker(context, params) {
    override fun doWork(): Result {
        return try {
            val count: Int = inputData.getInt(MainActivity.KEY_COUNT_VALUE, 0)
            for (i in 0 until count) {
                Log.i(TAG, "Uploading:$i ")
            }
            val time = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
            val currentDate: String = time.format(Date())
            val outPutData = Data.Builder()
                .putString(KEY_WORKER, currentDate).build()
            return Result.success(outPutData)
        } catch (e: Exception) {
            Result.failure()
        }
    }

    companion object {
        const val KEY_WORKER = "Key_uploadWork"
    }
}