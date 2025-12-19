package com.example.raudect.model.repository

import com.google.firebase.ml.modeldownloader.CustomModelDownloadConditions
import com.google.firebase.ml.modeldownloader.DownloadType
import com.google.firebase.ml.modeldownloader.FirebaseModelDownloader
import org.tensorflow.lite.Interpreter


class FirebaseMachineLearningRepository {
    private val conditions = CustomModelDownloadConditions.Builder()
        .requireWifi()
        .build()

    private val firebaseModelDownloader: FirebaseModelDownloader = FirebaseModelDownloader.getInstance()

    fun getMachineLearningModel(input: Array<FloatArray>, callback: (Result<Array<FloatArray>>)-> Unit){
        //download model stored in firebase server
        firebaseModelDownloader.getModel("FraudDetector", DownloadType.LOCAL_MODEL, conditions)
            .addOnSuccessListener { model ->
                val modelFile = model.file
                //check null for interpreter
                if (modelFile != null){
                    //prep to make and run interpreter
                    val output = Array(1){ FloatArray(1) }
                    val interpreter = Interpreter(modelFile, Interpreter.Options())

                    //run model
                    interpreter.run(input, output)

                    //pass result over to caller in callback
                    callback(Result.success(output))
                }
                else{
                    throw Exception("Machine Learning Model failed to load")
                }
            }
            .addOnFailureListener {exception ->
                throw exception
            }
    }
}