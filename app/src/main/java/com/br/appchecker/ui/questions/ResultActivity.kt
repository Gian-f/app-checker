package com.br.appchecker.ui.questions

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.br.appchecker.databinding.ActivityLoginBinding
import com.br.appchecker.databinding.ActivityResultBinding

class ResultActivity: AppCompatActivity() {

    private val binding: ActivityResultBinding by
    lazy { ActivityResultBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.tvResult.text = "testando"
        Toast.makeText(this, "testando", Toast.LENGTH_SHORT).show()
    }

}