package com.br.appchecker.ui.login.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.br.appchecker.R
import com.br.appchecker.databinding.ActivityRecoverBinding
import com.br.appchecker.databinding.ActivityRegisterBinding

class RecoverActivity : AppCompatActivity() {

    private val binding: ActivityRecoverBinding by lazy {
        ActivityRecoverBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(binding.root)
        super.onCreate(savedInstanceState)
    }
}