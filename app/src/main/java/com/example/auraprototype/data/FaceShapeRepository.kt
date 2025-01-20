package com.example.auraprototype.data

import android.content.Context
import android.content.res.AssetFileDescriptor
import android.graphics.Bitmap
import android.graphics.Bitmap.createScaledBitmap
import android.graphics.Color
import android.os.FileUtils


import org.tensorflow.lite.DataType
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.TensorFlowLite.init
import org.tensorflow.lite.support.common.FileUtil
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.FileInputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel


class FaceShapeRepository ( private val context : Context) {

    fun classifyFaceShape(bitmap: Bitmap) : String{
        val tfLiteOptions = Interpreter.Options() // can be configured to use GPU
        val interpreter = Interpreter(FileUtil.loadMappedFile(context, "model.tflite"), tfLiteOptions)
        // Resize and preprocess the bitmap
        val resizedBitmap = Bitmap.createScaledBitmap(bitmap, 224, 224, true)
        val inputBuffer = preprocessBitmap(resizedBitmap)

        // Get the input tensor shape and set it
//        val inputDetails = interpreter.getInputDetails()
//        val inputIndex = inputDetails[0].index

        // Run inference
//        val outputDetails = interpreter.getOutputDetails()
        val outputBuffer = TensorBuffer.createFixedSize(intArrayOf(1, 5), DataType.FLOAT32)  // Assuming 5 classes
        interpreter.run(inputBuffer, outputBuffer.buffer)

        // Process the output
        val outputArray = outputBuffer.floatArray
        val maxIndex = outputArray.indices.maxByOrNull { outputArray[it] } ?: -1

        // Print all the probabilities and class index
        outputArray.forEachIndexed { index, probability ->
            println("Class $index: $probability")
        }

        // Map the class index to a label
        val classLabels = listOf("Heart", "Oblong", "Oval", "Round", "Square")  // Update with your actual class labels
        val predictedLabel = if (maxIndex != -1) classLabels[maxIndex] else "Unknown"

        // Clean up resources
        interpreter.close()

        return predictedLabel
    }

    // Preprocess bitmap to fit model input format
    private fun preprocessBitmap(bitmap: Bitmap): ByteBuffer {
        val byteBuffer = ByteBuffer.allocateDirect(4 * 224 * 224 * 3) // 224x224 image with 3 channels
        byteBuffer.order(ByteOrder.nativeOrder())

        val intValues = IntArray(224 * 224)

        // Convert the Bitmap to a byte buffer
        bitmap.getPixels(intValues, 0, 224, 0, 0, 224, 224)
        var pixel = 0

        // Normalize pixel values to the range [0, 1]
        for (i in intValues.indices) {
            val value = intValues[i]
            byteBuffer.putFloat(((value shr 16 and 0xFF) / 255.0f))  // Red
            byteBuffer.putFloat(((value shr 8 and 0xFF) / 255.0f))   // Green
            byteBuffer.putFloat(((value and 0xFF) / 255.0f))        // Blue
        }

        return byteBuffer
    }



}