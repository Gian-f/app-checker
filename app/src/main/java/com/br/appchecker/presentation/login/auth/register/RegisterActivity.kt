package com.br.appchecker.presentation.login.auth.register

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.br.appchecker.R
import com.br.appchecker.databinding.ActivityRegisterBinding
import com.br.appchecker.util.showBottomSheet

class RegisterActivity : AppCompatActivity() {

    private val binding: ActivityRegisterBinding by lazy {
        ActivityRegisterBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupListeners()
    }

    private fun setupListeners() {
        binding.apply {

            google.setOnClickListener {
                showBottomSheet(message = R.string.error_not_implemented_yet)
            }

            mail.setOnClickListener {
                val intent = Intent(applicationContext, RegisterFormActivity::class.java)
                startActivity(intent)
            }
        }
    }
}