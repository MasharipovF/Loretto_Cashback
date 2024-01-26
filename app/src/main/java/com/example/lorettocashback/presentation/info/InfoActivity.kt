package com.example.lorettocashback.presentation.info

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.lorettocashback.R
import com.example.lorettocashback.core.BaseActivity
import com.example.lorettocashback.databinding.ActivityHistoryBinding
import com.example.lorettocashback.databinding.ActivityInfoBinding

class InfoActivity : BaseActivity() {
    private lateinit var binding: ActivityInfoBinding

    override fun init(savedInstanceState: Bundle?) {
        binding = ActivityInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}