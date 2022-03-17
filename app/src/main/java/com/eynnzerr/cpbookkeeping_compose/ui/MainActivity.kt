package com.eynnzerr.cpbookkeeping_compose.ui

import android.content.DialogInterface
import android.hardware.biometrics.BiometricPrompt
import android.os.Build
import android.os.Bundle
import android.os.CancellationSignal
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import com.eynnzerr.cpbookkeeping_compose.utils.NEED_FINGERPRINT
import com.eynnzerr.cpbookkeeping_compose.utils.getBoolData
import com.eynnzerr.cpbookkeeping_compose.utils.getFloatData
import com.google.accompanist.insets.ProvideWindowInsets
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import java.util.concurrent.Executor

private const val TAG = "MainActivity"

@ExperimentalFoundationApi
@ExperimentalAnimationApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var cancellationSignal: CancellationSignal
    private lateinit var authenticationCallback: BiometricPrompt.AuthenticationCallback

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            ProvideWindowInsets(windowInsetsAnimationsEnabled = true) {
                BookkeepingApp()
            }
        }
        executor = mainExecutor
        biometricPrompt = BiometricPrompt.Builder(this)
            .setTitle("指纹验证")
            .setDescription("验证指纹以访问账单")
            .setNegativeButton("取消", executor) { _, _ ->
                Log.d(TAG, "onCreate: biometric cancelled.")
                finish()
            }
            .build()
        cancellationSignal = CancellationSignal().apply {
            setOnCancelListener {
                Log.d(TAG, "onCreate: cancellation signal.")
                finish()
            }
        }
        authenticationCallback = object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence?) {
                super.onAuthenticationError(errorCode, errString)
                Log.d(TAG, "onAuthenticationError: $errString")
                Toast.makeText(applicationContext, errString, Toast.LENGTH_SHORT).show()
                finish()
            }

            override fun onAuthenticationHelp(helpCode: Int, helpString: CharSequence?) {
                super.onAuthenticationHelp(helpCode, helpString)
                Log.d(TAG, "onAuthenticationHelp: $helpString")
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult?) {
                super.onAuthenticationSucceeded(result)
                Toast.makeText(applicationContext, "指纹验证成功", Toast.LENGTH_SHORT).show()
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                Log.d(TAG, "onAuthenticationFailed: failed.")
                Toast.makeText(applicationContext, "指纹验证失败", Toast.LENGTH_SHORT).show()
            }
        }
        lifecycleScope.launch {
            val needFingerPrint = getBoolData(NEED_FINGERPRINT, false)
            if (needFingerPrint) biometricPrompt.authenticate(cancellationSignal, executor, authenticationCallback)
        }
    }
}