package com.bitwisor.sekura.uitls

import android.content.Context
import android.content.Context.SENSOR_SERVICE
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.media.MediaPlayer
import androidx.core.content.ContextCompat.getSystemService
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.bitwisor.sekura.R
import java.util.*
import kotlin.math.sqrt


class ShakeWorker (appContext: Context, workerParams: WorkerParameters):
    Worker(appContext, workerParams) {
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
            if(acceleration > 12){
                triggerAlarm()
            }
        }
        override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}
    }

    private fun triggerAlarm() {
        mediaPlayer = MediaPlayer.create(applicationContext, R.raw.alarm)
        mediaPlayer.start()
    }

//    override fun onResume() {
//        sensorManager?.registerListener(sensorListener, sensorManager!!.getDefaultSensor(
//            Sensor .TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL
//        )
//        super.onResume()
//    }


//    override fun onPause() {
//        sensorManager!!.unregisterListener(sensorListener)
//        super.onPause()
//    }


}
