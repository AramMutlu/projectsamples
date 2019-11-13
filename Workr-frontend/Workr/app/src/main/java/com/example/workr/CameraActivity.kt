package com.example.workr

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraX
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureConfig
import androidx.camera.core.Preview
import androidx.camera.core.PreviewConfig
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner

import android.content.pm.PackageManager
import android.graphics.Matrix
import android.os.Bundle
import android.os.Environment
import android.util.Rational
import android.util.Size
import android.view.Surface
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import com.example.workr.Classes.User
import com.google.gson.Gson
import okhttp3.*

import java.io.File
import java.io.IOException
import java.util.*

class CameraActivity : AppCompatActivity(), Callback {

    private val client = OkHttpClient()
    private val JSON = MediaType.parse("application/json; charset=utf-8")

    private val REQUEST_CODE_PERMISSIONS = 101
    private val REQUIRED_PERMISSIONS =
        arrayOf("android.permission.CAMERA", "android.permission.WRITE_EXTERNAL_STORAGE")//
    lateinit var textureView: TextureView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)

        textureView = findViewById(R.id.view_finder)

        //Check is permissions are granted, then start the Camera
        if (allPermissionsGranted()) {
            startCamera() //start camera if permission has been granted by user
        } else {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }
    }

    private fun startCamera() {
        CameraX.unbindAll()

        val aspectRatio = Rational(textureView.width, textureView.height)
        val screen = Size(textureView.width, textureView.height) //size of the screen

        //Set the Camera Configuration
        val pConfig =
            PreviewConfig.Builder()
                .setTargetAspectRatio(aspectRatio)
                .setLensFacing(CameraX.LensFacing.BACK) //CHANGE TO FRONT LATER IN PRODUCTION FOR SELFIES
                .setTargetResolution(screen)
                .build()
        val preview = Preview(pConfig)

        preview.onPreviewOutputUpdateListener =
            Preview.OnPreviewOutputUpdateListener { output ->
                //to update the surface texture we  have to destroy it first then re-add it
                val parent = textureView.parent as ViewGroup
                parent.removeView(textureView)
                parent.addView(textureView, 0)

                textureView.surfaceTexture = output.surfaceTexture
                updateTransform()
            }


        val imageCaptureConfig =
            ImageCaptureConfig.Builder()
                .setCaptureMode(ImageCapture.CaptureMode.MIN_LATENCY)
                .setTargetRotation(windowManager.defaultDisplay.rotation)
                .build()
        val imgCap = ImageCapture(imageCaptureConfig)

        findViewById<ImageButton>(R.id.imgCapture).setOnClickListener {
            val file =
                File(Environment.getExternalStorageDirectory().toString() + "/" + System.currentTimeMillis() + ".png")
            imgCap.takePicture(file, object : ImageCapture.OnImageSavedListener {
                override fun onError(
                    imageCaptureError: ImageCapture.ImageCaptureError,
                    message: String,
                    cause: Throwable?
                ) {
                    val msg = "Pic capture failed : $message"
                    Toast.makeText(baseContext, msg, Toast.LENGTH_LONG).show()
                    cause?.printStackTrace()
                }

                override fun onImageSaved(file: File) {
                    var encodedFile = encoder(file.absolutePath)
                    val msg = "Pic captured at " + file.absolutePath
                    Toast.makeText(baseContext, msg, Toast.LENGTH_LONG).show()
                    apiCall_uploadPic(encodedFile)
                }

            })
        }

        //bind to lifecycle:
        CameraX.bindToLifecycle(this, preview, imgCap)
    }

    //Encode picture to base64
    fun encoder(filePath: String): String {
        val bytes = File(filePath).readBytes()
        val base64 = Base64.getEncoder().encodeToString(bytes)
        return base64
    }

    private fun updateTransform() {
        val mx = Matrix()
        val w = textureView.measuredWidth.toFloat()
        val h = textureView.measuredHeight.toFloat()

        val cX = w / 2f
        val cY = h / 2f

        val rotationDgr: Int
        val rotation = textureView.rotation.toInt()

        when (rotation) {
            Surface.ROTATION_0 -> rotationDgr = 0
            Surface.ROTATION_90 -> rotationDgr = 90
            Surface.ROTATION_180 -> rotationDgr = 180
            Surface.ROTATION_270 -> rotationDgr = 270
            else -> return
        }

        mx.postRotate(rotationDgr.toFloat(), cX, cY)
        textureView.setTransform(mx)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {

        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera()
            } else {
                Toast.makeText(this, "Permissions not granted by the user.", Toast.LENGTH_SHORT)
                    .show()
                finish()
            }
        }
    }

    //Check if Permissions are granted
    private fun allPermissionsGranted(): Boolean {
        for (permission in REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return false
            }
        }
        return true
    }

    //Apicall to upload the picture to the database in base64
    fun apiCall_uploadPic(picture: String) {
        val KEY_AUTHORIZATION = "token"
        val sharedPref =
            getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE)
        val authorization = sharedPref?.getString(KEY_AUTHORIZATION, "ERROR").toString()
        val json = "{\"picture\": \"$picture\" }"
        val body = RequestBody.create(JSON, json)

        val request = Request.Builder()
            .url(resources?.getString(R.string.URL) + resources?.getString(R.string.URL_user_picture))
            .header("Accept", "application/json")
            .header("Content-Type", "application/json")
            .header("Authorization", authorization)
            .put(body)
            .build()

        client.newCall(request).enqueue(this)

    }

    override fun onFailure(call: Call, e: IOException) {

    }

    override fun onResponse(call: Call, response: Response) {
        if (response.isSuccessful) {

            onBackPressed()

        } else {
            Toast.makeText(this, "There went something wrong the Get request", Toast.LENGTH_SHORT)
                .show()
        }
    }
}
