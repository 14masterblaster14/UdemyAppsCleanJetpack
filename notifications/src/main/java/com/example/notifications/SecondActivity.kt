package com.example.notifications

import android.app.NotificationManager
import android.app.RemoteInput
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.NotificationCompat
import androidx.databinding.DataBindingUtil
import com.example.notifications.databinding.ActivityMainBinding
import com.example.notifications.databinding.ActivitySecondBinding

class SecondActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySecondBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_second)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_second)
        receiveInput()
    }

    private fun receiveInput() {
        val KEY_REPLY = "key_reply"
        val intent = this.intent
        val remoteInput = RemoteInput.getResultsFromIntent(intent)
        if (remoteInput != null) {
            val inputString = remoteInput.getCharSequence(KEY_REPLY).toString()
            binding.resultTextView.text = inputString

            val channelID = "com.anushka.notificationdemo.channel1"
            val notificationId = 45

            val repliedNotification = NotificationCompat.Builder(this, channelID)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentText("Your reply received")
                .build()
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.notify(notificationId, repliedNotification)

        }
    }
}