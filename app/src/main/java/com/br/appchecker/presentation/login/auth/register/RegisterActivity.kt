package com.br.appchecker.presentation.login.auth.register

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.br.appchecker.R
import com.br.appchecker.databinding.ActivityRegisterBinding
import com.br.appchecker.presentation.questions.MainActivity
import com.br.appchecker.util.LoadingUtils
import com.br.appchecker.util.LoadingUtils.dismissLoadingSheet
import com.br.appchecker.util.LoadingUtils.showErrorSheet
import com.br.appchecker.util.LoadingUtils.showLoadingSheet
import com.br.appchecker.util.showNotification
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.GoogleAuthProvider

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var googleSignInLauncher: ActivityResultLauncher<Intent>
    private var bottomSheetDialog: BottomSheetDialog? = null

    companion object {
        private const val RC_SIGN_IN = 9001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupListeners()
        configureGoogleSignIn()
        setupGoogleSignInLauncher()
    }

    private fun setupListeners() {
        binding.apply {
            google.setOnClickListener {
                loginWithGoogle()
            }

            mail.setOnClickListener {
                val intent = Intent(this@RegisterActivity, RegisterFormActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun setupGoogleSignInLauncher() {
        googleSignInLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val data: Intent? = result.data
                    val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                    try {
                        val account = task.getResult(ApiException::class.java)
                        account?.idToken?.let { idToken ->
                            firebaseAuthWithGoogle(idToken)
                        } ?: run {
                            showErrorSheet(
                                this,
                                message = "Login com o Google falhou"
                            )
                        }
                    } catch (e: ApiException) {
                        showErrorSheet(
                            this,
                            message = "Ocorreu um erro ao tentar realizar o login com o Google"
                        )
                    }
                }
            }
    }

    private fun loginWithGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        googleSignInLauncher.launch(signInIntent)
    }

    private fun configureGoogleSignIn() {
        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        val auth = FirebaseAuth.getInstance()
        showLoadingSheet(this, message = R.string.message_loading_bottom_sheet)

        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                // Remover o loading sheet após o processo ser concluído
                bottomSheetDialog?.let { dismissLoadingSheet(it) }

                if (task.isSuccessful) {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    val user = auth.currentUser
                    showNotification(
                        "Obrigado por fazer parte, ${user?.displayName}!",
                        "Sua conta foi cadastrada com sucesso!"
                    )
                } else {
                    when (task.exception) {
                        is FirebaseAuthInvalidCredentialsException -> {
                            showErrorSheet(
                                this,
                                message = "Credenciais inválidas. Verifique sua conta do Google."
                            )
                        }

                        is FirebaseAuthInvalidUserException -> {
                            showErrorSheet(
                                this,
                                message = "Usuário inválido. Verifique sua conta do Google."
                            )
                        }

                        else -> {
                            showErrorSheet(
                                this,
                                message = "Ocorreu um erro ao tentar realizar o login com o Google"
                            )
                        }
                    }
                }
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        bottomSheetDialog = null
    }
}