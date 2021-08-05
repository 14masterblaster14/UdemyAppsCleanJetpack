package com.example.workmanager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.work.*
import com.example.workmanager.databinding.ActivityMainBinding
import java.util.concurrent.TimeUnit

/**
Work Manager used for ->
Chaining Tasks
Status Updates
Constraints
Ensures Minimum resource usage
Supports Different Versions
Supports Asynchronous Tasks
Supports Periodic Tasks

Work manager is not for tasks that needs to be run in a background thread but donn't need to survive process death

Type of Work Request :  1) Periodic Work Request
2) One Time Work request

Steps to schedule a task with Work Manager :
1) Create Worker class i.e. subclass of Worker class
2) Create a Work request
3) Enqueue the request
4) Get the status updates (i.e. WorkInfo)
Work States : BLOCKED, ENQUEUED, RUNNING and SUCCEEDED

Constraints : The conditions in which u want to execute ur work

Chaining :
We can set inputs and outputs data of a worker class
We can sequentially and parallel chain different task

Periodic Work Request : Minimum period length = 15 min

 */

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    companion object {
        const val KEY_COUNT_VALUE = "key_count"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.button.setOnClickListener {
            //setOneTimeSimpleWorkRequest()
            //setOneTimeWorkRequest() // With inputData, outputData, constraints and State update
            //setChainingRequest()
            setPeriodicWorkRequest() // Will execute after given interval
        }
    }

    private fun setOneTimeSimpleWorkRequest() {
        val uploadRequest = OneTimeWorkRequest.Builder(UploadWorker::class.java)
            .build()

        WorkManager.getInstance(applicationContext)
            .enqueue(uploadRequest)
    }

    private fun setOneTimeWorkRequest() {

        val workManager = WorkManager.getInstance(applicationContext)

        // Passsing Input data
        val data: Data = Data.Builder()
            .putInt(KEY_COUNT_VALUE, 125)
            .build()

        val constraints = Constraints.Builder()
            .setRequiresCharging(true)
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val uploadRequest = OneTimeWorkRequest.Builder(UploadWorker::class.java)
            .setConstraints(constraints) // setting constraints
            .setInputData(data) // Passing data
            .build()

        workManager.enqueue(uploadRequest)

        workManager.getWorkInfoByIdLiveData(uploadRequest.id)
            .observe(this, Observer {
                binding.textView.text = it.state.name
                if (it.state.isFinished) {
                    val data = it.outputData
                    val message = data.getString(UploadWorker.KEY_WORKER)
                    Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
                }
            })


    }

    private fun setChainingRequest() {

        val workManager = WorkManager.getInstance(applicationContext)

        // Passsing Input data
        val data: Data = Data.Builder()
            .putInt(KEY_COUNT_VALUE, 125)
            .build()

        val constraints = Constraints.Builder()
            .setRequiresCharging(true)
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val uploadRequest = OneTimeWorkRequest.Builder(UploadWorker::class.java)
            .setConstraints(constraints) // setting constraints
            .setInputData(data) // Passing data
            .build()

        val filteringRequest = OneTimeWorkRequest.Builder(FilteringWorker::class.java)
            .build()

        val compressingRequest = OneTimeWorkRequest.Builder(CompressingWorker::class.java)
            .build()

        val downloadingWorker = OneTimeWorkRequest.Builder(DownloadingWorker::class.java)
            .build()

        // Chaining Sequential workers execution
        workManager
            .beginWith(filteringRequest)
            .then(compressingRequest)
            .then(uploadRequest)
            .enqueue()

        // Chaining Parallel workers execution
        val paralleWorks = mutableListOf<OneTimeWorkRequest>()

        paralleWorks.add(downloadingWorker)
        paralleWorks.add(filteringRequest)
        workManager
            .beginWith(paralleWorks)
            .then(compressingRequest)
            .then(uploadRequest)
            .enqueue()

        workManager.getWorkInfoByIdLiveData(uploadRequest.id)
            .observe(this, Observer {
                binding.textView.text = it.state.name
                if (it.state.isFinished) {
                    val data = it.outputData
                    val message = data.getString(UploadWorker.KEY_WORKER)
                    Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
                }
            })
    }

    private fun setPeriodicWorkRequest() {
        val periodicWorkRequest = PeriodicWorkRequest
            .Builder(DownloadingWorker::class.java, 16, TimeUnit.MINUTES)
            .build()
        WorkManager.getInstance(applicationContext).enqueue(periodicWorkRequest)
    }
}