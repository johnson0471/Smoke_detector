package com.example.smoke_detector


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.smoke_detector.databinding.ActivityLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class Activity_login : AppCompatActivity() {

    companion object {
        private const val RC_SIGN_IN = 1
    }


    private val TAG = javaClass.simpleName
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var binding: ActivityLoginBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        auth = FirebaseAuth.getInstance()
        setContentView(binding.root)

        val intent_registered = Intent(this, Activity_registered::class.java)
        val intent_forgot = Intent(this, Activity_forgot::class.java)

        binding.btnLogin.setOnClickListener {

            when {
                binding.etEmailLogin.text.toString().isEmpty() -> {
                    binding.inputEmailLogin.error = "請輸入正確的電子郵件"
                }
                !Patterns.EMAIL_ADDRESS.matcher(binding.etEmailLogin.text.toString()).matches() -> {
                    binding.inputEmailLogin.error = "電子郵件輸入格式有誤，請再輸入一次"
                }
                else -> binding.inputEmailLogin.error = null
            }

            when {
                binding.etPasswordLogin.text.toString().isEmpty() -> {
                    binding.inputPasswordLogin.error = "請輸入正確的密碼"
                }
                binding.etPasswordLogin.text.toString().length < 6 -> {
                    binding.inputPasswordLogin.error = "請輸入正確的密碼，且密碼必須大於6字"
                }

                else -> binding.etPasswordLogin.error = null
            }

            if (binding.etEmailLogin.text.toString()
                    .isNotEmpty() && binding.etPasswordLogin.text.toString().isNotEmpty()
            ) {
                signInUser()
            }

        }

        binding.btnRegistered.setOnClickListener {

            startActivity(intent_registered)
            binding.etEmailLogin.text!!.clear()
            binding.etPasswordLogin.text!!.clear()
        }
        binding.btnForgotPassword.setOnClickListener {
            startActivity(intent_forgot)
        }

        binding.btnGoogle.setOnClickListener {
            signInWithGoogle()
        }

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("803200239820-58alb3u7gr0pe50d59monuvf8u09mr14.apps.googleusercontent.com")
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)
    }


    private fun signInUser() {

        val email = findViewById<TextInputEditText>(R.id.et_email_login)
        val password = findViewById<TextInputEditText>(R.id.et_password_login)
        val input_email = findViewById<TextInputLayout>(R.id.input_email_login)
        val input_password = findViewById<TextInputLayout>(R.id.input_password_login)

        auth.signInWithEmailAndPassword(email.text.toString(), password.text.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.e(TAG, "Login Successful")
                    input_email.error = null
                    input_password.error = null
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    Log.e("Task message", "signInWithEmail:failure", task.exception)
                    val user_auth = auth.currentUser
                    user_auth.let {
                        val email_auth = user_auth?.email
                        if (email_auth != email.text.toString()) {
                            input_email.error = "請重新輸入電子郵件"
                        } else input_email.error = null
                    }
                    updateUI(null)
                }
            }
    }

    private fun updateUI(currentUser: FirebaseUser?) {
        val intent = Intent(this, Control_Fragment_Activity::class.java)
        val input_password = binding.inputPasswordLogin
        val password = binding.etPasswordLogin.text.toString()

        if (currentUser != null) {
            if (currentUser.isEmailVerified) {
                Log.e(TAG, currentUser.email.toString())
                Toast.makeText(this, "恭喜會員登入成功~~~", Toast.LENGTH_SHORT).show()
                startActivity(intent)
                finish()
                database = Firebase.database.reference
                val email = currentUser.email.toString()
                database.child("已登入").child("email").setValue(email)
                database.child("已登入").child("password").setValue(password)
                database.child("已登入").child("name").setValue(currentUser.displayName)

            } else {
                Toast.makeText(this, "請驗證您的電子郵件", Toast.LENGTH_SHORT).show()
                alertDialogVerifyEmail()
            }

        } else {
            Toast.makeText(this, "密碼有誤，請重新檢查", Toast.LENGTH_SHORT).show()
            input_password.error = "請輸入正確的密碼，且密碼必須大於6字"
        }
    }

    private fun alertDialogVerifyEmail() {

        val bottomSheetDialog = BottomSheetDialog(this)
        val bottomSheet = layoutInflater.inflate(R.layout.bottom_sheet, null, false)
        val dialog = AlertDialog.Builder(this)

        bottomSheetDialog.setContentView(bottomSheet)


        dialog.setTitle("驗證電子郵件")
        dialog.setMessage("要驗證您的電子郵件嗎?")
        dialog.setCancelable(false)
        dialog.setPositiveButton("確定") {
                dialog1, which ->
            bottomSheet()
            val user = auth.currentUser
            user?.sendEmailVerification()
                ?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.e(TAG, "Email sent")
                        Toast.makeText(this, "驗證信已送出", Toast.LENGTH_SHORT).show()
                    } else {
                        Log.e(TAG, "Email sent error" + task.exception)
                        Toast.makeText(this, "驗證信送出失敗", Toast.LENGTH_SHORT).show()
                    }
                }

        }
        dialog.setNegativeButton("取消") {

                dialog1, which ->
            dialog1.dismiss()

        }
        dialog.show()
    }

    private fun bottomSheet() {

        val bottomSheetDialog = BottomSheetDialog(this)
        val bottomSheet = layoutInflater.inflate(R.layout.bottom_sheet, null, false)
        val btn_cancel = bottomSheet.findViewById<MaterialButton>(R.id.btn_cancel_bottomSheet)
        val btn_gmail = bottomSheet.findViewById<LinearLayout>(R.id.btn_gmail)
        val btn_yahoo = bottomSheet.findViewById<LinearLayout>(R.id.btn_yahoo)


        bottomSheetDialog.setContentView(bottomSheet)
        bottomSheetDialog.show()

        btn_cancel.setOnClickListener {
            bottomSheetDialog.dismiss()
        }

        btn_gmail.setOnClickListener {
            val gotoGmail = packageManager.getLaunchIntentForPackage("com.google.android.gm")
            startActivity(gotoGmail)
            bottomSheetDialog.dismiss()
        }
        btn_yahoo.setOnClickListener {
            val gotoYahoo =
                packageManager.getLaunchIntentForPackage("com.yahoo.mobile.client.android.mail")
            startActivity(gotoYahoo)
            bottomSheetDialog.dismiss()
        }
    }

    private fun signInWithGoogle() {
        // Configure Google Sign In
        val user = auth.currentUser
        Log.e(TAG, user?.email.toString())
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val exception = task.exception
            if (task.isSuccessful) {
                try {
                    // Google Sign In was successful, authenticate with Firebase
                    val account = task.getResult(ApiException::class.java)
                    Log.e(TAG, "firebaseAuthWithGoogle:" + account.id)
                    firebaseAuthWithGoogle(account.idToken!!)
                } catch (e: ApiException) {
                    // Google Sign In failed, update UI appropriately
                    Log.e(TAG, "Google sign in failed", e)
                }
            } else Log.e(TAG, "$exception")
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.e(TAG, "signInWithCredential:success")
                    val intent = Intent(this, Control_Fragment_Activity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.e(TAG, "signInWithCredential:failure", task.exception)

                }
            }
    }

}










