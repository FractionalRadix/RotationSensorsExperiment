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
import kotlin.math.acos
import kotlin.math.atan2
import kotlin.math.sqrt

class MainActivity : AppCompatActivity(), SensorEventListener {
    private lateinit var sensorManager: SensorManager
    //TODO?~ Does it need to be nullable? Copied this line from the docs. (https://developer.android.com/guide/topics/sensors/sensors_motion#sensors-motion-rotate)
    private var sensor: Sensor? = null

    private lateinit var tvXValue: TextView
    private lateinit var tvYValue: TextView
    private lateinit var tvZValue: TextView
    private lateinit var tvHalfCosTheta: TextView
    private lateinit var cvCustomView: ArrowDisplay

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

        cvCustomView = findViewById(R.id.arrowDisplay)
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
        // According to AndroidAuthority, though: "you shouldn't unregister your listeners in onPause(),
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

    fun rad2deg(rad: Double): Double = 180.0 * rad / Math.PI

    override fun onSensorChanged(event: SensorEvent?) {
        val displayQuaternion = false // When false, display euler angles instead.
        if (event != null) {

            if (displayQuaternion) {
                tvXValue.text = event.values[0].toString()
                tvYValue.text = event.values[1].toString()
                tvZValue.text = event.values[2].toString()
                tvHalfCosTheta.text = event.values[3].toString()
            }

            // Could go for 3x3 - we're going to overwrite the Z-axis values in that matrix, right?
            val matrix = FloatArray(16)

            //TODO?~ Convert this 4x4 android.opengl.Matrix to a 3x3 android.graphics.Matrix ?
            SensorManager.getRotationMatrixFromVector(matrix, event.values)

            val halfTheta = acos(event.values[3]).toDouble()
            val theta = 2.0 * halfTheta
            val thetaInDegrees = rad2deg(theta)
            val rotatedMatrix = android.graphics.Matrix()

            rotatedMatrix.setTranslate(300f, 300f)
            rotatedMatrix.preRotate(thetaInDegrees.toFloat())

            //if (false) {
            //    val rotatedMatrixArray = FloatArray(9)
            //    rotatedMatrix.getValues(rotatedMatrixArray)
            //    cvCustomView.receiveMatrix(rotatedMatrixArray)
            //} else {
                val rotatedMatrixArray1 = openGLMatrixToGraphicsMatrix1(matrix)
                val rotatedMatrixArray2 = openGLMatrixToGraphicsMatrix2(matrix)
                val rotatedMatrixArray3 = openGLMatrixToGraphicsMatrix3(matrix)
                cvCustomView.receiveMatrices(rotatedMatrixArray1, rotatedMatrixArray2, rotatedMatrixArray3)
            //}

            if (!displayQuaternion) {
                val quaternion = Quaternion(
                    event.values[0].toDouble(),
                    event.values[1].toDouble(),
                    event.values[2].toDouble(),
                    event.values[3].toDouble()
                )
                val eulerAngles = toEulerAngles(quaternion)

                tvXValue.text = eulerAngles.roll.toString()
                tvYValue.text = eulerAngles.pitch.toString()
                tvZValue.text = eulerAngles.yaw.toString()
                tvHalfCosTheta.text = "N/A" //event.values[3].toString()
            }


        } else {
            Log.i("Sensing!", "sensor event is null...")
        }
    }

    /**
     * Given an array of float values, representing a 4x4 matrix in column-major order,
     * return a 3*3 matrix that gives the transformation projected (orthonormal) on the YZ (?) plane.
     */
    private fun openGLMatrixToGraphicsMatrix1(matrix: FloatArray): FloatArray /* android.graphics.Matrix */ {
        val newValues = FloatArray(9)

        newValues[0] = matrix[0] ; newValues[1] = matrix[4]; newValues[2] = 0f //matrix[12]
        newValues[3] = matrix[1] ; newValues[4] = matrix[5]; newValues[5] = 0f //matrix[13]
        newValues[6] =     0f    ; newValues[7] =    0f;     newValues[8] = 1f

        return newValues
    }

    /**
     * Given an array of float values, representing a 4x4 matrix in column-major order,
     * return a 3*3 matrix that gives the transformation projected (orthonormal) on the YZ (?) plane.
     */
    private fun openGLMatrixToGraphicsMatrix2(matrix: FloatArray): FloatArray /* android.graphics.Matrix */ {
        val newValues = FloatArray(9)

        newValues[0] = matrix[0] ; newValues[1] = matrix[8]; newValues[2] = 0f //matrix[12]
        newValues[3] = matrix[1] ; newValues[4] = matrix[9]; newValues[5] = 0f //matrix[13]
        newValues[6] =     0f    ; newValues[7] =    0f;     newValues[8] = 1f

        return newValues
    }

    /**
     * Given an array of float values, representing a 4x4 matrix in column-major order,
     * return a 3*3 matrix that gives the transformation projected (orthonormal) on the YZ (?) plane.
     */
    private fun openGLMatrixToGraphicsMatrix3(matrix: FloatArray): FloatArray /* android.graphics.Matrix */ {
        val newValues = FloatArray(9)

        newValues[0] = matrix[4] ; newValues[1] = matrix[8]; newValues[2] = 0f //matrix[12]
        newValues[3] = matrix[5] ; newValues[4] = matrix[9]; newValues[5] = 0f //matrix[13]
        newValues[6] =     0f    ; newValues[7] =    0f;     newValues[8] = 1f

        return newValues
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        // Not used in this app.
    }

    // Taken from Wikipedia and translated to Kotlin:
    // https://en.wikipedia.org/wiki/Conversion_between_quaternions_and_Euler_angles
    class Quaternion(val w: Double, val x: Double, val y: Double, val z: Double)
        //val w: Double = 0.0
        //val x: Double = 0.0
        //val y: Double = 0.0
        //val z: Double = 0.0


    class EulerAngles {
        var roll: Double = 0.0
        var pitch: Double = 0.0
        var yaw: Double = 0.0
    };

    // This implementation assumes normalized quaternion
    // converts to Euler angles in 3-2-1 sequence
    private fun toEulerAngles(q: Quaternion): EulerAngles {
        val angles = EulerAngles()

        // roll (x-axis rotation)
        val sinr_cosp = 2 * (q.w * q.x + q.y * q.z);
        val cosr_cosp = 1 - 2 * (q.x * q.x + q.y * q.y);
        angles.roll = atan2(sinr_cosp, cosr_cosp);
        angles.roll = radiansToDegrees(angles.roll)

        //TODO!+ Handle the +90 degrees / -90 degrees situation!
        // pitch (y-axis rotation)
        val sinp = sqrt(1 + 2 * (q.w * q.y - q.x * q.z));
        val cosp = sqrt(1 - 2 * (q.w * q.y - q.x * q.z));
        angles.pitch = 2 * atan2(sinp, cosp) - Math.PI / 2;
        angles.pitch = radiansToDegrees(angles.pitch)

        // yaw (z-axis rotation)
        val siny_cosp = 2 * (q.w * q.z + q.x * q.y);
        val cosy_cosp = 1 - 2 * (q.y * q.y + q.z * q.z);
        angles.yaw = atan2(siny_cosp, cosy_cosp);
        angles.yaw = radiansToDegrees(angles.yaw)

        return angles;
    }

    private fun radiansToDegrees(radians: Double) = 180.0 * radians / Math.PI
}