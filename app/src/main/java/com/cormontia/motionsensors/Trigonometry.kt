package com.cormontia.motionsensors

import kotlin.math.atan2
import kotlin.math.sqrt

class Quaternion(val w: Double, val x: Double, val y: Double, val z: Double)

class EulerAngles {
    var roll: Double = 0.0
    var pitch: Double = 0.0
    var yaw: Double = 0.0
}

class Trigonometry {
    companion object {

        // Taken from Wikipedia and translated to Kotlin:
        // https://en.wikipedia.org/wiki/Conversion_between_quaternions_and_Euler_angles

        // This implementation assumes normalized quaternion
        // converts to Euler angles in 3-2-1 sequence
        fun toEulerAngles(q: Quaternion): EulerAngles {
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

        fun radiansToDegrees(radians: Double) = 180.0 * radians / Math.PI
    }
}