package com.example.lorettocashback.presentation.screens.qr_code

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
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
import com.example.lorettocashback.databinding.ActivityQrCodeBinding
import com.example.lorettocashback.databinding.DialogHistoryBinding
import com.example.lorettocashback.databinding.DialogQrCodeBinding
import com.example.lorettocashback.presentation.adapter.QrCodeAdapter
import com.example.lorettocashback.util.Utils
import com.google.common.util.concurrent.ListenableFuture
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
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
    private lateinit var find: MediaPlayer
    private lateinit var vibrator: Vibrator
    override fun init(savedInstanceState: Bundle?) {
        binding = ActivityQrCodeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        permission()

        mViewModel = ViewModelProvider(this).get(QrCodeViewModel::class.java)
        cameraExecutor = Executors.newSingleThreadExecutor()
        barcodeScanner = createBarcodeScanner()
        list = ArrayList()
        find = MediaPlayer.create(this, R.raw.correct)
        vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        rvAdapter = QrCodeAdapter()

        binding.list.adapter = rvAdapter
        binding.list.layoutManager = LinearLayoutManager(
            this,
            RecyclerView.VERTICAL,
            false
        )
        onClick()

        mViewModel.data.observe(this, dataObserve)
        mViewModel.listData.observe(this, listDataObserve)
        mViewModel.loading.observe(this, loadingObserve)
        mViewModel.errorData.observe(this, errorDataObserve)

    }

    @SuppressLint("SetTextI18n")
    private fun onClick() {
        rvAdapter.setClickListener { data ->
            val view: View = LayoutInflater.from(this).inflate(R.layout.dialog_qr_code, null)

            val binding = DialogQrCodeBinding.bind(view)
            binding.itemNameDg.text = data.itemName
            binding.cashbackAmountDg.text =
                Utils.getNumberWithThousandSeparator(data.cashbackAmount) + "$"
            binding.itemGroupNameDg.text = data.itemsGroupName
            binding.itemCodeTextDg.text = data.itemCode

            val dialog = AlertDialog.Builder(this)
                .setView(view)
                .create()
            dialog.show()
            dialog.window!!.setBackgroundDrawable(null)

            binding.deleteBtn.setOnClickListener {
                list.remove(data)
                mViewModel.getDataList(list)
                dialog.dismiss()
            }
        }

        rvAdapter.setDeleteClickListener {
            list.remove(it)
            mViewModel.getDataList(list)
        }
    }

    private val dataObserve = Observer<CashbackQrCode> {
        list.add(it)
        mViewModel.getDataList(list)
        find.start()
    }

    private val listDataObserve = Observer<List<CashbackQrCode>> {
        rvAdapter.submitList(it)
        rvAdapter.notifyDataSetChanged()
    }

    private val loadingObserve = Observer<Boolean> {
        if (it) {
            binding.progressBar.visibility = View.VISIBLE
            binding.textLoading.visibility = View.VISIBLE
            binding.image.setBackgroundColor(Color.parseColor("#72FFFFFF"))
        } else {
            binding.progressBar.visibility = View.GONE
            binding.textLoading.visibility = View.GONE
            binding.image.setBackgroundColor(Color.parseColor("#00FFFFFF"))
        }
    }

    private val errorDataObserve = Observer<String> {
        showSnackbar(
            binding.container,
            "Ошибка $it",
            R.color.red
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val vibrationEffect =
                VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE)
            vibrator.vibrate(vibrationEffect)
        } else {
            vibrator.vibrate(100)
        }
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
                    BarcodeAnalyzer(mViewModel, barcodeScanner)
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
) : ImageAnalysis.Analyzer {

    val scope = CoroutineScope(Dispatchers.Main)

    var time = 0L

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
                            if (time == 0L) {
                                mViewModel.getData(value.toString())
                                time = SystemClock.elapsedRealtime()
                            } else {
                                val currentTime = SystemClock.elapsedRealtime()
                                if (currentTime - time >= 3000) {
                                    mViewModel.getData(value.toString())
                                    time = currentTime
                                }
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
