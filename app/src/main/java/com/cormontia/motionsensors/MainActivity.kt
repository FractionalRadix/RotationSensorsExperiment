package com.cormontia.motionsensors

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView

class MainActivity : AppCompatActivity(), SensorEventListener {
    private lateinit var sensorManager: SensorManager
    //TODO?~ Does it need to be nullable? Copied this line from the docs. (https://developer.android.com/guide/topics/sensors/sensors_motion#sensors-motion-rotate)
    private var sensor: Sensor? = null

    private lateinit var tvXValue: TextView
    private lateinit var tvYValue: TextView
    private lateinit var tvZValue: TextView
    private lateinit var tvHalfCosTheta: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)

        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)

        tvXValue = findViewById(R.id.rotationXValue)
        tvYValue = findViewById(R.id.rotationYValue)
        tvZValue = findViewById(R.id.rotationZValue)
        tvHalfCosTheta = findViewById(R.id.rotationCosineHalfTheta)
    }

    override fun onPause() {
        super.onPause()
        // Sensors are a drain on the battery. Unregister them when they're not in use.
        sensorManager.unregisterListener(this)
    }

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onDestroy() {
        super.onDestroy()
        //TODO?- Wouldn't the listener be gone automatically if the Activity is Destroyed?
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(p0: SensorEvent?) {
        if (p0 != null) {
            tvXValue.text = p0.values[0].toString()
            tvYValue.text = p0.values[1].toString()
            tvZValue.text = p0.values[2].toString()
            tvHalfCosTheta.text = p0.values[3].toString()
        } else {
            Log.i("Sensing!", "sensor event is null...")
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        //TODO("Not yet implemented")
    }
}