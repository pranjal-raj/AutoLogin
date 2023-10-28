package com.example.autologin

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.autologin.databinding.ActivityMainBinding
import com.google.android.gms.auth.api.phone.SmsRetriever
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


lateinit var binding : ActivityMainBinding
lateinit var mySMSBroadcastReceiver : MySMSBroadcastReceiver

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        startSMSRetrieverClient();
        mySMSBroadcastReceiver = MySMSBroadcastReceiver()
//        val receiver = mySMSBroadcastReceiver.init(object : MySMSBroadcastReceiver.SmsListener{
//            override fun onSmsReceived(message: String) {
//               Log.d("jhoom" , "$message")
//            }
//
//            override fun onSmsTimeOut() {
//                Log.d("jhoom" , "Time out ")
//            }
//        })
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_main)
        registerBroadcastReceiver()
        findViewById<Button>(R.id.button).setOnClickListener {

            val phone = findViewById<EditText>(R.id.phone).text.toString()
            api_controller.retrofit.create(apiService::class.java).loginRequest(LoginRequest("farmer","8299606580")).enqueue(object : Callback<String>{
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    when(response.code())
                    {
                        201->
                        {
                            Toast.makeText(this@MainActivity, "it.toString()", Toast.LENGTH_SHORT).show()

                            mySMSBroadcastReceiver.init(object : MySMSBroadcastReceiver.SmsListener{
                                override fun onSmsReceived(message: String) {
                                    Log.d("jhoom", message)
                                }

                                override fun onSmsTimeOut() {
                                    Log.d("jhoom", "Timed Out ")
                                }
                            })
                        }


                        400->
                        {
                            Toast.makeText(this@MainActivity, it.toString(), Toast.LENGTH_SHORT).show()
                        }

                        401->
                        {
                            Toast.makeText(this@MainActivity, it.toString(), Toast.LENGTH_SHORT).show()
                        }

                    }
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    Toast.makeText(this@MainActivity, t.message, Toast.LENGTH_SHORT).show()
                }
            })
        }
    }



    private fun startSMSRetrieverClient() {
        val client = SmsRetriever.getClient(this)
        //We can add sender phone number or leave it blank
        // I'm adding null here
        client.startSmsRetriever().addOnSuccessListener {
            Log.d("jhoom", "On Success")
            Toast.makeText(applicationContext, "On Success", Toast.LENGTH_LONG).show()
        }.addOnFailureListener {
            Log.d("jhoom", "On OnFailure")
            Toast.makeText(applicationContext, "On OnFailure", Toast.LENGTH_LONG).show()
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        if (mySMSBroadcastReceiver != null) this@MainActivity.unregisterReceiver(mySMSBroadcastReceiver)
    }



    private fun registerBroadcastReceiver() {
        val smsBroadcastReceiver = MySMSBroadcastReceiver()
        smsBroadcastReceiver.init(smsListener = object : MySMSBroadcastReceiver.SmsListener{
            override fun onSmsReceived(message: String) {
                Log.d("jhoom", message)
            }

            override fun onSmsTimeOut() {
                Log.d("jhoom", "dinsimcivdmdv")
            }
        })
        val intentFilter = IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION)
        this@MainActivity.registerReceiver(smsBroadcastReceiver, intentFilter, SmsRetriever.SEND_PERMISSION,null, Context.RECEIVER_EXPORTED, )
        Log.d("jhoom" , "Regsitered")
    }

    override fun onStart() {
        super.onStart()
        registerBroadcastReceiver()
    }


}