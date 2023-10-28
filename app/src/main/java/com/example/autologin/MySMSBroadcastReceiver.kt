package com.example.autologin;

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.Status

/**
 * BroadcastReceiver to wait for SMS messages. This can be registered either
 * in the AndroidManifest or at runtime.  Should filter Intents on
 * SmsRetriever.SMS_RETRIEVED_ACTION.
 */
class MySMSBroadcastReceiver : BroadcastReceiver() {
    private var otpReceiveListener: OTPReceiveListener? = null
     var smsBroadcastReceiverListener: SmsBroadcastReceiverListener? = null

    fun init(smsBroadcastReceiverListener: SmsBroadcastReceiverListener?) {
        this.smsBroadcastReceiverListener = smsBroadcastReceiverListener
    }

    fun init(otpReceiveListener: OTPReceiveListener?) {
        this.otpReceiveListener = otpReceiveListener
    }
    private var listener : SmsListener? = null
    fun init(smsListener: SmsListener){
        this.listener = smsListener
    }

    override fun onReceive(context: Context, intent: Intent) {
        Log.d("jhoom", "OnReceive Called")
        if (SmsRetriever.SMS_RETRIEVED_ACTION == intent.action) {
            val extras = intent.extras
            Log.d("jhoom", "extras ${extras.toString()}")
            if (extras != null) {
                val status = extras.get(SMS_RETRIEVE_STATUS) as Status?
                Log.d("jhoom", "status ${status.toString()}")
                if (status != null)
                    when (status.statusCode) {
                    CommonStatusCodes.SUCCESS -> {
                        // Get SMS message contents
                        val message = extras.getString(SMS_RETRIEVED_MESSAGE)
                        Log.d("jhoom", "msg ${message.toString()}")
                        val otp = extras.getString(SmsRetriever.EXTRA_SMS_MESSAGE)
                        Log.d("jhoom", otp.toString())
                        message?.let { listener?.onSmsReceived(it) }

                    }

                    CommonStatusCodes.TIMEOUT -> if (otpReceiveListener != null) otpReceiveListener!!.onOTPTimeOut()
                }
            }
        }
    }

    interface SmsListener {
        fun onSmsReceived(message: String)
        fun onSmsTimeOut()
    }

    interface OTPReceiveListener {
        fun onOTPReceived(otp: String?)
        fun onOTPTimeOut()
    }

    interface SmsBroadcastReceiverListener {
        fun onSuccess(intent: Intent?)
        fun onFailure()
    }

    companion object {
        private const val SMS_RETRIEVED_ACTION = "com.google.android.gms.auth.api.phone.SMS_RETRIEVED"
        private const val SMS_RETRIEVE_STATUS = "com.google.android.gms.auth.api.phone.EXTRA_STATUS"
        private const val SMS_RETRIEVED_MESSAGE = "com.google.android.gms.auth.api.phone.EXTRA_SMS_MESSAGE"
    }
}