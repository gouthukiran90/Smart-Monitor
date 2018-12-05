package com.example.videodemo

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.text.TextUtils
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_login.*

/**
 * A login screen that offers login via email/password.
 */
class LoginActivity : BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        // Set up the login form.
        etForPwdAtSignIn.setOnEditorActionListener(TextView.OnEditorActionListener { _, id, _ ->
            if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                attemptLogin()
                return@OnEditorActionListener true
            }
            false
        })

        var isPwdVisible = true
        etForPwdAtSignIn.setOnTouchListener(View.OnTouchListener { v, event ->
            etForPwdAtSignIn.setFocusable(true)
            etForPwdAtSignIn.requestFocus()
            val DRAWABLE_RIGHT = 2
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= etForPwdAtSignIn.getRight() - etForPwdAtSignIn.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width()) {
                    // your action here
                    if (isPwdVisible) {
                        isPwdVisible = false
                        etForPwdAtSignIn.setInputType(InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS)
                        etForPwdAtSignIn.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_visibility_off_grey, 0)
                        etForPwdAtSignIn.setSelection(etForPwdAtSignIn.getText().length)
                    } else {
                        etForPwdAtSignIn.setInputType(InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD)
                        etForPwdAtSignIn.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_visibility_grey, 0)
                        etForPwdAtSignIn.setSelection(etForPwdAtSignIn.getText().length)
                        isPwdVisible = true
                    }
                }

            }
            false
        })

        signInBtnAtSignin.setOnClickListener { attemptLogin() }

        if (sharedPreferences.getBoolean(AppConstants.isLoggedIn, false)) {
            var intentToHome = Intent(this, HomeActivity::class.java)
            intentToHome.putExtra("username", sharedPreferences.getString(AppConstants.username, ""))
            startActivity(intentToHome)
            finish()
        }
    }


    public fun goToSignup(view: View) {
        var intentToSignup = Intent(this, SignUpActivity::class.java)
        startActivity(intentToSignup)
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private fun attemptLogin() {


        // Reset errors.
        etForUnameAtSignin.error = null
        etForPwdAtSignIn.error = null

        // Store values at the time of the login attempt.
        val emailStr = etForUnameAtSignin.text.toString()
        val passwordStr = etForPwdAtSignIn.text.toString()

        var cancel = false
        var focusView: View? = null


        // Check for a valid email address.
        if (TextUtils.isEmpty(emailStr) || !isEmailValid(emailStr)) {
            etForUnameAtSignin.error = getString(R.string.error_invalid_email)
            focusView = etForUnameAtSignin
            cancel = true
        } else if (TextUtils.isEmpty(passwordStr) || !isPasswordValid(passwordStr)) { // Check for a valid password, if the user entered one.
            etForPwdAtSignIn.error = getString(R.string.error_invalid_password)
            focusView = etForPwdAtSignIn
            cancel = true
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView?.requestFocus()
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
//            showProgress(true)
            var intentToHome = Intent(this, HomeActivity::class.java)
            intentToHome.putExtra("username", emailStr)
            startActivity(intentToHome)
            finish()
            var editor = sharedPreferences.edit()
            editor.putBoolean(AppConstants.isLoggedIn, true)
            editor.putString(AppConstants.username, emailStr)
            editor.commit()

        }
    }

    private fun isEmailValid(email: String): Boolean {
        //TODO: Replace this with your own logic
        return email.contains("@")
    }

    private fun isPasswordValid(password: String): Boolean {
        //TODO: Replace this with your own logic
        return password.length > 4
    }


    companion object {

    }
}