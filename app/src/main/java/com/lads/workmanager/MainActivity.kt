package com.lads.workmanager

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.work.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val button = findViewById<Button>(R.id.btnStart)
        button.setOnClickListener {
//            oneTimeWorkRequest()
            setPeriodicWorkRequest()
        }
    }

    private fun oneTimeWorkRequest() {
        val textView = findViewById<TextView>(R.id.tvResult)

        val workManager: WorkManager = WorkManager.getInstance(applicationContext)

        val data: Data = Data.Builder().putInt(KEY_COUNT_VALUE, 100).build()

        val constraints: Constraints = Constraints.Builder()
            .setRequiresCharging(true)
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        //Create work request...
        val uploadRequest: OneTimeWorkRequest =
            OneTimeWorkRequestBuilder<UploadWork>()
                .setConstraints(constraints)
                .setInputData(data)
                .build()

        val filteringRequest = OneTimeWorkRequest.Builder(FilteringWork::class.java).build()
        val compressingRequest = OneTimeWorkRequest.Builder(CompressingWork::class.java).build()
        val downloadingRequest = OneTimeWorkRequest.Builder(DownloadingWork::class.java).build()
        val parallel: MutableList<OneTimeWorkRequest> = mutableListOf<OneTimeWorkRequest>()
        parallel.add(downloadingRequest)
        parallel.add(filteringRequest)
        workManager
            .beginWith(parallel)
            .then(compressingRequest)
            .then(uploadRequest)
            .enqueue()
        workManager.getWorkInfoByIdLiveData(uploadRequest.id).observe(this, Observer {
            textView.text = it.state.name
            if (it.state.isFinished) {
                val data: Data = it.outputData
                val message: String? = data.getString(UploadWork.KEY_WORKER)
                Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun setPeriodicWorkRequest() {
        val periodicWorkRequest =
            PeriodicWorkRequest.Builder(DownloadingWork::class.java, 1, TimeUnit.MINUTES).build()
        WorkManager.getInstance(applicationContext).enqueue(periodicWorkRequest)
        WorkManager.getInstance().cancelWorkById(periodicWorkRequest.id)
    }

    companion object {
        const val KEY_COUNT_VALUE = "Key_Count"
    }
}