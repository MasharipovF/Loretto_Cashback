package com.example.lorettocashback.presentation.qr_code

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.OptIn
import androidx.appcompat.app.AlertDialog
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lorettocashback.R
import com.example.lorettocashback.core.BaseActivity
import com.example.lorettocashback.databinding.ActivityQrCodeBinding
import com.example.lorettocashback.presentation.SapMobileApplication
import com.example.lorettocashback.presentation.adapter.QrCodeAdapter
import com.example.lorettocashback.presentation.history.list
import com.google.common.util.concurrent.ListenableFuture
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import androidx.camera.core.Camera

class QrCodeActivity : BaseActivity() {

    private val activityResultLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        if (ActivityCompat.checkSelfPermission(
                this@QrCodeActivity,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            startCamera()
        }
    }


    private lateinit var binding: ActivityQrCodeBinding
    private lateinit var rvAdapter: QrCodeAdapter

    private lateinit var cameraExecutor: ExecutorService
    private lateinit var barcodeScanner: BarcodeScanner


    override fun init(savedInstanceState: Bundle?) {
        binding = ActivityQrCodeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        cameraExecutor = Executors.newSingleThreadExecutor()
        barcodeScanner = createBarcodeScanner()


        if (ActivityCompat.checkSelfPermission(
                this@QrCodeActivity,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            activityResultLauncher.launch(Manifest.permission.CAMERA)
        } else {
            startCamera()
        }

//        rvAdapter = QrCodeAdapter()
//
//        binding.list.adapter = rvAdapter
//        binding.list.layoutManager = LinearLayoutManager(
//            this,
//            RecyclerView.VERTICAL,
//            false
//        )

//        rvAdapter.submitList(list)

//        rvAdapter.setClickListener {
//            val view: View = LayoutInflater.from(this).inflate(R.layout.dialog_qr_code, null)
//            val dialog = AlertDialog.Builder(this)
//                .setView(view)
//                .create()
//            dialog.show()
//            dialog.window!!.setBackgroundDrawable(null)
//        }
    }

    private fun createBarcodeScanner(): BarcodeScanner {
        val options = BarcodeScannerOptions.Builder()
            .setBarcodeFormats(
                Barcode.FORMAT_CODE_128,
                Barcode.FORMAT_QR_CODE
            )
            .build()
        return BarcodeScanning.getClient(options)
    }

    private fun startCamera() {
        val cameraProviderFuture: ListenableFuture<ProcessCameraProvider> =
            ProcessCameraProvider.getInstance(this)


        cameraProviderFuture.addListener({
            try {
                val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

                val preview = Preview.Builder().build()
                val cameraSelector =
                    CameraSelector.Builder()
                        .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                        .build()

                preview.setSurfaceProvider(binding.scaner.surfaceProvider)

                val imageAnalysis = ImageAnalysis.Builder()
                    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                    .build()
                imageAnalysis.setAnalyzer(
                    cameraExecutor,
                    BarcodeAnalyzer(binding.textView, barcodeScanner)
                )

                cameraProvider.unbindAll()

                cameraProvider.bindToLifecycle(
                    this as LifecycleOwner,
                    cameraSelector,
                    preview,
                    imageAnalysis
                )

            } catch (e: Exception) {
                // Xatolarni qaytarish
                Log.e("QrCodeActivity7", "Kamera ni boshlashda xatolik", e)
            }
        }, ContextCompat.getMainExecutor(this))
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }
}

class BarcodeAnalyzer(
    private val textView: TextView,
    private val barcodeScanner: BarcodeScanner
) : ImageAnalysis.Analyzer {

    @SuppressLint("UnsafeOptInUsageError")
    override fun analyze(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            val image =
                InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)

            barcodeScanner.process(image)
                .addOnSuccessListener { barcodes ->
                    if (barcodes.isNotEmpty()) {
                        processBarcodeResult(barcodes)
                    } else {
                        Log.d("BarcodeAnalyzer", "barcode topilmadi")
                    }
                }
                .addOnFailureListener { e ->
                    Log.e("BarcodeAnalyzer", "Xatolikni tekshirishda xatolik", e)
                }
                .addOnCompleteListener { imageProxy.close() }
        }
    }

    private fun processBarcodeResult(barcodes: List<Barcode>) {
        for (barcode in barcodes) {
            val value = barcode.displayValue
            textView.post { textView.text = value }

            Log.d("BarcodeAnalyzer", value.toString())
        }
    }
}