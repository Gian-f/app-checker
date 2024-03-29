package com.br.appchecker.presentation.questions

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.br.appchecker.R
import com.br.appchecker.databinding.ActivityMainBinding
import com.br.appchecker.presentation.questions.interfaces.ProgressBarListener

class MainActivity : AppCompatActivity(), ProgressBarListener {

    private val binding: ActivityMainBinding by
    lazy { ActivityMainBinding.inflate(layoutInflater) }

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupNavController()
        onUpdateProgressBar(1, 6)
    }

    private fun setupNavController() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun onUpdateProgressBar(progress: Int, max: Int) {
        with(binding) {
            progressIndicator.progress = progress
            questionTextView.text = "$progress de $max"
            progressIndicator.max = max
        }
    }
}