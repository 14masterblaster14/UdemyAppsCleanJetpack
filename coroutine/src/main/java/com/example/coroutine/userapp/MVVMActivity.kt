package com.example.coroutine.userapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.coroutine.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MVVMActivity : AppCompatActivity() {

    private lateinit var mvvvmActivityViewModel: MvvmActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_m_v_v_m)

        mvvvmActivityViewModel = ViewModelProvider(this).get(MvvmActivityViewModel::class.java)

        // mvvvmActivityViewModel.getUsers()
        mvvvmActivityViewModel.users.observe(this, Observer { myUsers ->
            myUsers.forEach {
                Log.i("MyTag", "name is ${it.name}")
            }

        })

        /**

        lifecycleScope.launch(Dispatchers.IO) {
        Log.d("MasterBlaster","Current thread is :-> ${Thread.currentThread().name}")
        }

        lifecycleScope.launch(Dispatchers.Main) {
        Log.d("MasterBlaster","Current thread is :-> ${Thread.currentThread().name}")
        }

        lifecycleScope.launchWhenCreated {

        }

        lifecycleScope.launchWhenStarted {

        }

        lifecycleScope.launchWhenResumed {

        }

         **/
    }

}