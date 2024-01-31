package com.example.lorettocashback.presentation.qr_code

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.graphics.Rect
import android.graphics.RectF
import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnTouchListener
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lorettocashback.R
import com.example.lorettocashback.core.BaseActivity
import com.example.lorettocashback.data.entity.qr_code.CashbackQrCode
import com.example.lorettocashback.data.model.SimpleData
import com.example.lorettocashback.databinding.ActivityQrCodeBinding
import com.example.lorettocashback.presentation.SapMobileApplication
import com.example.lorettocashback.presentation.adapter.QrCodeAdapter
import com.example.lorettocashback.presentation.history.HistoryViewModel
import com.google.common.util.concurrent.ListenableFuture
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


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

    private lateinit var mViewModel: QrCodeViewModel
    private lateinit var binding: ActivityQrCodeBinding
    private lateinit var rvAdapter: QrCodeAdapter

    private lateinit var cameraExecutor: ExecutorService
    private lateinit var barcodeScanner: BarcodeScanner

    private lateinit var list: ArrayList<CashbackQrCode>


    override fun init(savedInstanceState: Bundle?) {
        binding = ActivityQrCodeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        permission()

        mViewModel = ViewModelProvider(this).get(QrCodeViewModel::class.java)
        cameraExecutor = Executors.newSingleThreadExecutor()
        barcodeScanner = createBarcodeScanner()
        list = ArrayList()
        rvAdapter = QrCodeAdapter()

        rvAdapter.setClickListener {
            val view: View = LayoutInflater.from(this).inflate(R.layout.dialog_qr_code, null)
            val dialog = AlertDialog.Builder(this)
                .setView(view)
                .create()
            dialog.show()
            dialog.window!!.setBackgroundDrawable(null)
        }

        binding.list.adapter = rvAdapter
        binding.list.layoutManager = LinearLayoutManager(
            this,
            RecyclerView.VERTICAL,
            false
        )


        mViewModel.data.observe(this, dataObserve)
        mViewModel.listData.observe(this, listDataObserve)


    }

    private val dataObserve = Observer<CashbackQrCode> {
        Log.d("DATAGGG", "$it")
        //list.add(it)
        mViewModel.getDataList(it)
    }

    private val listDataObserve = Observer<List<CashbackQrCode>> {
        rvAdapter.submitList(it)


    }

    private fun permission() {
        if (ActivityCompat.checkSelfPermission(
                this@QrCodeActivity,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            activityResultLauncher.launch(Manifest.permission.CAMERA)
        } else {
            startCamera()
        }
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

    @SuppressLint("ClickableViewAccessibility")
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
                    BarcodeAnalyzer(mViewModel, barcodeScanner, binding.scView)
                )

                cameraProvider.unbindAll()

                cameraProvider.bindToLifecycle(
                    this as LifecycleOwner,
                    cameraSelector,
                    preview,
                    imageAnalysis
                )
                animation()
            } catch (e: Exception) {
                // Xatolarni qaytarish
                showSnackbar(
                    binding.container,
                    "Ошибка $e",
                    R.color.red
                )
                Log.e("QrCodeActivity7", "Kamera ni boshlashda xatolik", e)
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun animation() {
        val animation = AnimationUtils.loadAnimation(this@QrCodeActivity, R.anim.anim)
        animation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {
                binding.bar.visibility = View.GONE
            }

            override fun onAnimationRepeat(animation: Animation) {
            }
        })
        animation.duration = 2000
        binding.bar.visibility = View.VISIBLE
        binding.bar.startAnimation(animation)

    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }
}

class BarcodeAnalyzer(
    private val mViewModel: QrCodeViewModel,
    private val barcodeScanner: BarcodeScanner,
    private val scView: View
) : ImageAnalysis.Analyzer {

    val scope = CoroutineScope(Dispatchers.Main)

    @SuppressLint("UnsafeOptInUsageError")
    override fun analyze(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)

            barcodeScanner.process(image)
                .addOnSuccessListener { barcodes ->
                    if (barcodes.isNotEmpty()) {
                        for (barcode in barcodes) {

                            val value = barcode.displayValue
                            val barcodeBoundingBox = barcode.boundingBox

                            Log.d("PSP", "$value")
                            if (barcode.format == Barcode.FORMAT_CODE_128 || barcode.format == Barcode.FORMAT_QR_CODE) {
                                Log.d("OOII", "$value")
                                mViewModel.getData(value.toString())
                            }

                        }
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
}
