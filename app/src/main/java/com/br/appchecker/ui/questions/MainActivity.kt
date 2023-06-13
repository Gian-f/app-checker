package com.br.appchecker.ui.questions

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.br.appchecker.R
import com.br.appchecker.databinding.ActivityMainBinding
import com.br.appchecker.ui.questions.interfaces.ProgressBarListener
import com.br.appchecker.util.showBottomSheet

class MainActivity : AppCompatActivity(), ProgressBarListener {

    private val binding: ActivityMainBinding by
    lazy { ActivityMainBinding.inflate(layoutInflater) }

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupNavController()
        setupListeners()
        onUpdateProgressBar(1, "1 de 6")
    }

    private fun setupNavController() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
            navController = navHostFragment.navController
    }

    private fun setupListeners() {
        binding.continueButton.setOnClickListener {
            when (navController.currentDestination?.id) {
                R.id.FirstFragment ->  navigateToSecondFragment()
                R.id.SecondFragment -> navigateToThirdFragment()
                R.id.ThirdFragment ->  navigateToFourthFragment()
                R.id.FourthFragment -> navigateToFifthFragment()
                R.id.FifthFragment ->  navigateToSixthFragment()
                R.id.SixthFragment -> showBottomSheet(message = R.string.error_generic)
            }
        }

        binding.backButton.setOnClickListener {
            navigateUp()
        }

    }

    private fun navigateToFourthFragment() {
        binding.backButton.visibility = View.VISIBLE
        binding.continueButton.layoutParams.width = resources.getDimensionPixelSize(R.dimen.button_width_180dp)
        val action = ThirdFragmentDirections.actionThirdFragmentToFourthFragment()
        navController.navigate(action)
    }

    private fun navigateToFifthFragment() {
        binding.backButton.visibility = View.VISIBLE
        binding.continueButton.layoutParams.width = resources.getDimensionPixelSize(R.dimen.button_width_180dp)
        val action = FourthFragmentDirections.actionFourthFragmentToFifthFragment()
        navController.navigate(action)
    }

    private fun navigateToSixthFragment() {
        binding.backButton.visibility = View.VISIBLE
        binding.continueButton.layoutParams.width = resources.getDimensionPixelSize(R.dimen.button_width_180dp)
        val action = FifthFragmentDirections.actionFifthFragmentToSixthFragment()
        navController.navigate(action)
    }

    private fun navigateToSecondFragment() {
        binding.backButton.visibility = View.VISIBLE
        binding.continueButton.layoutParams.width = resources.getDimensionPixelSize(R.dimen.button_width_180dp)
        val action = FirstFragmentDirections.actionFirstFragmentToSecondFragment()
        navController.navigate(action)
    }

    private fun navigateToThirdFragment() {
        binding.backButton.visibility = View.VISIBLE
        binding.continueButton.layoutParams.width = resources.getDimensionPixelSize(R.dimen.button_width_180dp)
        val action = SecondFragmentDirections.actionSecondFragmentToThirdFragment()
        navController.navigate(action)
    }

    private fun navigateUp() {
        binding.continueButton.layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT

        // Verificar o destino anterior
        val previousDestination = navController.previousBackStackEntry?.destination?.id

        if (previousDestination == R.id.SecondFragment ||
            previousDestination == R.id.ThirdFragment  ||
            previousDestination == R.id.FourthFragment ||
            previousDestination == R.id.FifthFragment) {
            // Mostrar o botão de voltar
            binding.backButton.visibility = View.VISIBLE
            binding.continueButton.layoutParams.width =
                resources.getDimensionPixelSize(R.dimen.button_width_180dp)
        } else {
            // Esconder o botão de voltar
            binding.backButton.visibility = View.GONE
        }
        navController.navigateUp()
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun onUpdateProgressBar(progress: Int, step: String) {
        binding.progressIndicator.progress = progress
        binding.questionTextView.text = step
    }
}