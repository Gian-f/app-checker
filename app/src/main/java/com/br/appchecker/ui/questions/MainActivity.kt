package com.br.appchecker.ui.questions

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.navigation.NavController
import androidx.navigation.NavDirections
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
//        setupListeners()
        onUpdateProgressBar(1, "1 de 6")
    }

    private fun setupNavController() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
    }

//    private fun setupListeners() {
//        with(binding) {
//            continueButton.setOnClickListener {
//                when (navController.currentDestination?.id) {
//                    R.id.FirstFragment ->  navigateToFragment(FirstFragmentDirections.actionFirstFragmentToSecondFragment())
//                    R.id.SecondFragment -> navigateToFragment(SecondFragmentDirections.actionSecondFragmentToThirdFragment())
//                    R.id.ThirdFragment ->  navigateToFragment(ThirdFragmentDirections.actionThirdFragmentToFourthFragment())
//                    R.id.FourthFragment -> navigateToFragment(FifthFragmentDirections.actionFifthFragmentToSixthFragment())
//                    R.id.FifthFragment ->  navigateToFragment(FifthFragmentDirections.actionFifthFragmentToSixthFragment())
//                    R.id.SixthFragment ->  showBottomSheet(message = R.string.error_generic)
//                }
//            }
//
//            backButton.setOnClickListener {
//                navigateUp()
//            }
//
//        }
//    }

//    private fun navigateToFragment(action: NavDirections) {
//        with(binding) {
//            backButton.visibility = View.VISIBLE
//            continueButton.layoutParams.width =
//                resources.getDimensionPixelSize(R.dimen.button_width_180dp)
//            navController.navigate(action)
//        }
//    }

//    private fun navigateUp() {
//        with(binding) {
//            continueButton.layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
//            val previousDestination = navController.previousBackStackEntry?.destination?.id
//            if (previousDestination == R.id.SecondFragment ||
//                previousDestination == R.id.ThirdFragment  ||
//                previousDestination == R.id.FourthFragment ||
//                previousDestination == R.id.FifthFragment) {
//                backButton.visibility = View.VISIBLE
//                continueButton.layoutParams.width =
//                    resources.getDimensionPixelSize(R.dimen.button_width_180dp)
//            } else {
//                backButton.visibility = View.GONE
//            }
//            navController.navigateUp()
//        }
//    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun onUpdateProgressBar(progress: Int, step: String) {
        with(binding) {
            progressIndicator.progress = progress
            questionTextView.text = step
        }
    }
}