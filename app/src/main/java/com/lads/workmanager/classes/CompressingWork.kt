package com.lads.workmanager

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters

class CompressingWork(context: Context, params: WorkerParameters) : Worker(context, params) {
    override fun doWork(): Result {
        return try {
            for (i in 0..200) {
                Log.i(TAG, "Compressing:$i ")
            }
            return Result.success()
        } catch (e: Exception) {
            Result.failure()
        }

    }


}