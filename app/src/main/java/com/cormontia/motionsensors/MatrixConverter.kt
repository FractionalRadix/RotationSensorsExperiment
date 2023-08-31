package com.cormontia.motionsensors

class MatrixConverter {
    companion object {
        /**
         * Given an array of float values, representing a 4x4 matrix in column-major order,
         * return a 3*3 matrix that gives the transformation projected (orthonormal) on the YZ (?) plane.
         */
        fun openGLMatrixToGraphicsMatrix1(matrix: FloatArray): FloatArray /* android.graphics.Matrix */ {
            val newValues = FloatArray(9)

            newValues[0] = matrix[0]; newValues[1] = matrix[4]; newValues[2] = 0f //matrix[12]
            newValues[3] = matrix[1]; newValues[4] = matrix[5]; newValues[5] = 0f //matrix[13]
            newValues[6] = 0f; newValues[7] = 0f; newValues[8] = 1f

            return newValues
        }

        /**
         * Given an array of float values, representing a 4x4 matrix in column-major order,
         * return a 3*3 matrix that gives the transformation projected (orthonormal) on the YZ (?) plane.
         */
        fun openGLMatrixToGraphicsMatrix2(matrix: FloatArray): FloatArray /* android.graphics.Matrix */ {
            val newValues = FloatArray(9)

            newValues[0] = matrix[0]; newValues[1] = matrix[8]; newValues[2] = 0f //matrix[12]
            newValues[3] = matrix[1]; newValues[4] = matrix[9]; newValues[5] = 0f //matrix[13]
            newValues[6] = 0f; newValues[7] = 0f; newValues[8] = 1f

            return newValues
        }

        /**
         * Given an array of float values, representing a 4x4 matrix in column-major order,
         * return a 3*3 matrix that gives the transformation projected (orthonormal) on the YZ (?) plane.
         */
        fun openGLMatrixToGraphicsMatrix3(matrix: FloatArray): FloatArray /* android.graphics.Matrix */ {
            val newValues = FloatArray(9)

            newValues[0] = matrix[4]; newValues[1] = matrix[8]; newValues[2] = 0f //matrix[12]
            newValues[3] = matrix[5]; newValues[4] = matrix[9]; newValues[5] = 0f //matrix[13]
            newValues[6] = 0f; newValues[7] = 0f; newValues[8] = 1f

            return newValues
        }
    }
}