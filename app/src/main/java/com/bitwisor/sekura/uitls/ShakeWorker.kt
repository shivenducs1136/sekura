package com.bitwisor.sekura.uitls

import android.content.Context
import android.content.Context.SENSOR_SERVICE
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.ContextCompat.startActivity
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.bitwisor.sekura.EmergencyActivity
import com.bitwisor.sekura.MainActivity
import com.bitwisor.sekura.R
import java.util.*
import kotlin.math.sqrt


class ShakeWorker (appContext: Context, workerParams: WorkerParameters):
    Worker(appContext, workerParams) {
    var context = appContext
    var sensorManager: SensorManager? = null
    var acceleration = 0f
    var currentAcceleration = 0f
    var lastAcceleration = 0f
    private lateinit var mediaPlayer:MediaPlayer
    override fun doWork(): Result {

        // Do the work here--in this case, upload the images.
        isShaking(applicationContext)
        // Indicate whether the work finished successfully with the Result
        return Result.success()
    }

    private fun isShaking(context: Context):Boolean {

        var isShake = false
        sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager

        Objects.requireNonNull(sensorManager)!!
            .registerListener(sensorListener, sensorManager!!
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL)

        acceleration = 10f
        currentAcceleration = SensorManager.GRAVITY_EARTH
        lastAcceleration = SensorManager.GRAVITY_EARTH
        return isShake
    }
    private val sensorListener: SensorEventListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent) {

            // Fetching x,y,z values
            val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]
            lastAcceleration = currentAcceleration

            // Getting current accelerations
            // with the help of fetched x,y,z values
            currentAcceleration = sqrt((x * x + y * y + z * z).toDouble()).toFloat()
            val delta: Float = currentAcceleration - lastAcceleration
            acceleration = acceleration * 0.9f + delta

            // Display a Toast message if
            // acceleration value is over 12
            if(acceleration > 60){
                triggerAlarm()
                triggerCall()
            }
        }
        override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}
    }

    private fun triggerCall() {
        val i= Intent(context,EmergencyActivity::class.java)
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET or Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT)
        i.addFlags(Intent.FLAG_GRANT_PREFIX_URI_PERMISSION or Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED)
        context.startActivity(i)
    }

    private fun triggerAlarm() {
        mediaPlayer = MediaPlayer.create(applicationContext, R.raw.siren)
        mediaPlayer.start()

        val v= (applicationContext.getSystemService(Context.VIBRATOR_SERVICE)as Vibrator)

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            v.vibrate(VibrationEffect.createOneShot(1000, VibrationEffect.DEFAULT_AMPLITUDE))

        }
        else{
            v.vibrate(1000)
        }
    }




}
