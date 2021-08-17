package com.example.coroutine

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.coroutine.userapp.MVVMActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main


/**  Refer Jetpack Coroutine and Coroutine1scopes for better coroutines examples.. */

/**
THREAD 1 |---> job_1 (coroutine_1)
|---> job_2 (coroutine_2)
|---> job_3 (coroutine_3)
|---> job_4 (coroutine_3)
|       :
|---> job_n (coroutine_n)

Coroutine works in scope i.e. we need to group the coroutines so that we can handle them in a better way and avoid leaks.

Types of Coroutine Scopes:
|---> GlobalScope : It lives till the application is running and rarely used in android.
|---> viewModelScope : Its a coroutine scope tied to a ViewModel so that when ViewModel destroyed then it will cancel all the coroutines/Job running within this scope.
|---> lifecycleScope : Its a coroutine scope tied to the Activity/Fragment lifecycle so that when corresponding activity/ fragment gets destroyed then it will cancel all the coroutines/Job running within this scope.

Dispatchers (Context)
|---> Main : For main thread / UI thread i.e. while interaction with UI
|---> Default : For heavy computational work processing i.e. intensive CPU task sorting large data, parsing large json
|---> IO : For background thread i.e. I/O operations, DB, network, file operations
|---> Unconfined : runs in current thread but if it is suspended and resumed it will run on suspending function's thread.
Use it with at most care, and mostly use for testing
Note : Best practice to start coroutines on main thread and switch to other thread

Jobs : We can run/tagged multiple jobs in a particular Coroutine scope.

Coroutine Builders:
|--->   launch      :   It's basic builder and doesn't block current thread. It needs scope.
                        It returns instance of job,which can be used as a ref to the coroutine.
                        We use this for coroutines that doesn't have any result as the return value.
|--->   async       :   It launches new coroutine w/o blocking current thread.
                        It returns instance of Deferred<T> and need to invoke await() to get the value.
                        It needs parent coroutine. It doesn't block parent coroutine. But if "await" is called then it blocks.
                        We use this for coroutines that does have any result as the return value.
#   The main difference between launch() and async() is that the first one doesn’t allow to obtain a value returned by its coroutine block, while the second one allows the block to return a value that can be used outside the coroutine.
|--->   Produce :   It's for coroutines which produce a stream of elements and returns an instance of ReceiveChannel Block current thread to run coroutines. So don't use it.
|--->   runBlocking : Block current thread to run this coroutines and returns a result of type T. So don't use it. Mostly used for testing.


Coroutine Methods:
|--->   withContext

Suspended Functions:    A suspending function doesn't block a thread but only suspend the coroutine itself.
                        The thread is returned to the pool while the coroutine is waiting and when waiting is done
                        the coroutine resumes on the free thread in the pool.
                        We can call/invoke suspending function from either a coroutine block or
                        from another suspending function only.
e.g.                withContext
                    withTimeout
                    withTimeoutOrnull
                    join
                    delay
                    await
                    supervisorScope
                    coroutineScope

# We can convert the callbacks to coroutine

# Structured Concurrency :  It is set of language features and best practises introduced for kotlin coroutines to avoid leaks and manage them productively.

# CoroutineScope : It's an interface.

# coroutineScope : Its a suspend function which will create child scope and gives the guarantee to complete all the task of child scope before returning the value
 **/


/**
 * Android Specific Scopes:
 *
 * viewModelScope : Its a coroutine scope tied to a ViewModel so that when ViewModel destroyed then it will cancel
 *                  all the coroutines/Job running within this scope.
 *
 * lifecycleScope : Its a coroutine scope tied to the Activity/Fragment lifecycle so that when corresponding activity/ fragment
 *                  gets destroyed then it will cancel all the coroutines/Job running within this scope.
 *
 */

class MainActivity : AppCompatActivity() {

    private var count = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Normal Coroutines with different threads
        sample1()

        // Carrying out the caluculation on IO thred and display it on Main thread
        btnCount.setOnClickListener {
            tvCount.text = count++.toString()
        }

        btnDownloadUserData.setOnClickListener {

            CoroutineScope(Dispatchers.IO).launch {
                downloadUserData()
            }
        }

        // Sequential Execution

        CoroutineScope(IO).launch {
            Log.i("MasterBlaster", "Calculation Started ...")

            val stock1 = getStock1()
            val stock2 = getStock2()
            val total = stock1 + stock2

            Log.i("MasterBlaster", "Total is -> $total")
        }

        // Parallel Execution

        CoroutineScope(IO).launch {
            Log.i("MasterBlaster", "Calculation Started ...")

            val stock1 = async { getStock1() }
            val stock2 = async { getStock2() }

            val total = stock1.await() + stock2.await()

            Log.i("MasterBlaster", "Total is -> $total")
        }

        // or

        CoroutineScope(Main).launch {
            Log.i("MasterBlaster", "Calculation Started ...")

            val stock1 = async(IO) { getStock1() }
            val stock2 = async(IO) { getStock2() }

            val total = stock1.await() + stock2.await()
            Toast.makeText(applicationContext, "Total is -> $total", Toast.LENGTH_SHORT).show()
            //Log.i("MasterBlaster","Total is -> $total" )
        }


        // Structured Concurrency
        // coroutineScope : A suspending function allow us to create child coroutine within given coroutine scope
        //                  and guaranties the job complete and return the output
        // CoroutineScope : An Interface we normally used

        btnStructuredConcurrency.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {

                totalSCCount.text = UserDataManager().getTotalUserCount().toString()
            }
        }

        btnGotoMvvmActivity.setOnClickListener {
            this.startActivity(Intent(this, MVVMActivity::class.java))
        }
    }

    private fun sample1() {

        CoroutineScope(Dispatchers.IO).launch {
            Log.i("MyTag", " Hello from ${Thread.currentThread().name}")
        }
        // O/p : Hello from DefaultDispatcher-worker-1

        CoroutineScope(Dispatchers.Main).launch {
            Log.i("MyTag", " Hello from ${Thread.currentThread().name}")
        }
        // O/p : Hello from main
    }

    private suspend fun downloadUserData() {

        withContext(Dispatchers.Main) {

            for (i in 1..200000) {

                tvUserMessage.text = "Downloading user $i in ${Thread.currentThread().name}"
                delay(1000)
            }

        }
    }

    private suspend fun getStock1(): Int {
        delay(10000)
        Log.i("MyTag", " Stock 1 returned...")
        return 55000
    }

    private suspend fun getStock2(): Int {
        delay(8000)
        Log.i("MyTag", " Stock 2 returned...")
        return 35000
    }


}

