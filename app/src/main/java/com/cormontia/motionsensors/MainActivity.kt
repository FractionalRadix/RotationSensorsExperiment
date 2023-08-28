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

        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_UI) // WAS: SENSOR_DELAY_NORMAL

        tvXValue = findViewById(R.id.rotationXValue)
        tvYValue = findViewById(R.id.rotationYValue)
        tvZValue = findViewById(R.id.rotationZValue)
        tvHalfCosTheta = findViewById(R.id.rotationCosineHalfTheta)
    }

    override fun onPause() {
        super.onPause()

        // Unregistering sensors, as suggested here in Android's official developer documentation:
        // https://developer.android.com/guide/topics/sensors/sensors_overview
        // "It's also important to note that this example uses the onResume() and onPause() callback methods to register and unregister the sensor event listener.
        // As a best practice you should always disable sensors you don't need, especially when your activity is paused.
        // Failing to do so can drain the battery in just a few hours because some sensors have substantial power requirements and can use up battery power quickly."
        //
        // HOWEVER...
        // According to AndroidAuthority, though: "you shouldn’t unregister your listeners in onPause(),
        // as in Android 7.0 and higher applications can run in split-screen and picture-in-picture mode,
        // where they’re in a paused state, but remain visible onscreen."
        // https://www.androidauthority.com/master-android-sensors-946024/
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

    override fun onSensorChanged(event: SensorEvent?) {
        if (event != null) {
            tvXValue.text = event.values[0].toString()
            tvYValue.text = event.values[1].toString()
            tvZValue.text = event.values[2].toString()
            tvHalfCosTheta.text = event.values[3].toString()

            // Could go for 3x3 - we're going to overwrite the Z-axis values in that matrix, right?
            var matrix = FloatArray(16)

            SensorManager.getRotationMatrixFromVector(matrix, event.values)
        } else {
            Log.i("Sensing!", "sensor event is null...")
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        //TODO("Not yet implemented")
    }
}