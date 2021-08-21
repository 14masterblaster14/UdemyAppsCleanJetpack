package com.example.viewbinding

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.viewbinding.databinding.ActivityMainBinding

/**
 *  Databinding :   We can bind views views with data and vice versa
 *
 *  ViewBinding :   We can bind only views with data but can't bind data with views.
 *                  It's easy and superior in performance (due to faster compile time as annotation processor) than Databinding.
 *                  Requires no xml changes.
 *                  There is no binding expressions, two way binding and binding adapters.
 *
 * Note: There is nothing Viewbinding can do that databinding can't do.
 */

class MainActivity : AppCompatActivity() {

    private lateinit var activityMainBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)

        activityMainBinding.submitButton.setOnClickListener {
            displayGreeting()
        }
    }

    private fun displayGreeting() {
        activityMainBinding.apply {
            greetingTextView.text = "Hello! " + nameEditText.text
        }

    }
}