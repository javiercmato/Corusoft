package com.corusoft.ticketmanager

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import android.Manifest
import android.content.ContentValues
import android.content.pm.PackageManager
import android.os.Build
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.ImageCapture
import androidx.camera.video.Recorder
import androidx.camera.video.Recording
import androidx.camera.video.VideoCapture
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import android.widget.Toast
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.core.Preview
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.video.FallbackStrategy
import androidx.camera.video.MediaStoreOutputOptions
import androidx.camera.video.Quality
import androidx.camera.video.QualitySelector
import androidx.camera.video.VideoRecordEvent
import androidx.core.content.PermissionChecker
import java.nio.ByteBuffer
import java.text.SimpleDateFormat
import java.util.Locale

typealias LumaListener = (luma: Double) -> Unit


class BottomBarFragment : Fragment() {

    private var imageCapture: ImageCapture? = null
    private var recording: Recording? = null
    private lateinit var cameraExecutor: ExecutorService

    companion object {
        private const val TAG = "CameraXApp"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS =
            mutableListOf (
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO
            ).apply {
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                    add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
            }.toTypedArray()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflar el archivo de diseño del Fragment
        val view = inflater.inflate(R.layout.fragment_bottom_bar, container, false)
        // Configurar el BottomNavigationView u otros elementos aquí si es necesario


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Obtener referencia al BottomNavigationView dentro del Fragment
        val bottomNavigationView = view.findViewById<BottomNavigationView>(R.id.bottom_navigation)

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.page_1 -> {
                    Log.d("BottomNavigation", "Pulsado botón Home")
                    val intent = Intent(requireActivity(), Landing::class.java)
                    startActivity(intent)
                }
                R.id.page_2 -> {
                    Log.d("BottomNavigation", "Pulsado botón Tickets")
                    val intent = Intent(requireActivity(), MyTickets::class.java)
                    startActivity(intent)
                }
                R.id.page_3 -> {
                    Log.d("BottomNavigation", "Pulsado botón Scan")
                    Log.d("BottomNavigation", "Abriendo cámara...")
                    // Request camera permissions
                    if (allPermissionsGranted()) {
                        startCamera()
                    } else {
                        ActivityCompat.requestPermissions(
                            requireActivity(), REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
                    }
                    takePhoto()

                    cameraExecutor = Executors.newSingleThreadExecutor()
                }
                R.id.page_4 -> {
                    Log.d("BottomNavigation", "Pulsado botón 4")
                    val intent = Intent(requireActivity(), AddTicket::class.java)
                    startActivity(intent)
                }
                R.id.page_5 -> {
                    Log.d("BottomNavigation", "Pulsado botón profile")
                    val intent = Intent(requireActivity(), UserProfile::class.java)
                    startActivity(intent)
                }
            }
            true
        }
    }

    private fun takePhoto() {}

    private fun startCamera() {}

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            requireContext(), it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults:
        IntArray) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera()
            } else {
                Toast.makeText(requireContext(),
                    "Permissions not granted by the user.",
                    Toast.LENGTH_SHORT).show()
                requireActivity().finish()
            }
        }
    }


}
