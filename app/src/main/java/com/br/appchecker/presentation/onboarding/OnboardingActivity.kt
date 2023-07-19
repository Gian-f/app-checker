package com.br.appchecker.presentation.onboarding

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.br.appchecker.databinding.ActivityOnboardingBinding
import com.br.appchecker.domain.OnBoardingPrefManager
import com.br.appchecker.presentation.login.auth.LoginActivity

class OnboardingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnboardingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val prefManager = OnBoardingPrefManager(this)
        if (prefManager.isFirstTimeLaunch) {
            binding = ActivityOnboardingBinding.inflate(layoutInflater)
            setContentView(binding.root)
        } else {
            navigateToLogin()
        }
    }

    private fun navigateToLogin() {
        finish()
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }
}