package com.ilnur.cardsnew

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.Group
import com.bumptech.glide.Glide
import com.google.android.material.textfield.TextInputLayout
import com.ilnur.cardsnew.utils.Session
import com.ilnur.cardsnew.utils.SessionState
import com.ilnur.cardsnew.utils.Settings
import com.ilnur.cardsnew.viewmodel.LoginState
import com.ilnur.cardsnew.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    //lateinit var viewModel: LoginViewModel
    private val viewModel: LoginViewModel by viewModels()
    lateinit var mEmailView: AutoCompleteTextView
    private var mPasswordView: EditText? = null
    //Placeholder holder;
    internal lateinit var group_main: Group
    internal lateinit var group_anim: Group

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        Log.d("login", "asdas")

        startGlide()

        setupViews()


        viewModel.loginResult.observe(this, {
            it?.let {
                Log.d("loginRES OBS", it.toString())
                when(it){
                    LoginState.SUCCESS -> {
                        startActivity(Intent(this, MainActivity::class.java))
                    }
                    LoginState.ERROR -> {}
                    LoginState.NO_INTERNET -> {}
                    LoginState.WRONG_LOG_OR_PAS -> {}
                    else -> Log.d("Nothing has happened", "уляля")
                }
                Log.d("THe last", it.toString())
            }
        })

        viewModel.loginFormState.observe(this, {
            it?.let {
                Log.d("loginFORM OBS", it.toString())
            }
        })



    }

    private val loginWatcher = object : TextWatcher{
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            viewModel.updateLoginState(viewModel.loginFormState.value!!.copy(login = p0.toString()))
        }

        override fun afterTextChanged(p0: Editable?) {
        }

    }
    private val passwordWatcher = object : TextWatcher{
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            viewModel.updateLoginState(viewModel.loginFormState.value!!.copy(password = p0.toString()))
        }

        override fun afterTextChanged(p0: Editable?) {
        }

    }

    fun setupViews(){
        group_main = findViewById(R.id.group_main)
        group_anim = findViewById(R.id.group_anim)
        mEmailView = findViewById<View>(R.id.email) as AutoCompleteTextView

        mEmailView.addTextChangedListener(loginWatcher)

        val mEmailSignInButton = findViewById<View>(R.id.email_sign_in_button) as Button
        val mSkipButton = findViewById<View>(R.id.skip_button) as Button
        val signUpButton = findViewById<View>(R.id.sign_up) as Button
        mPasswordView = findViewById<View>(R.id.password) as EditText

        mPasswordView!!.addTextChangedListener(passwordWatcher)

        mEmailView.setText(Settings.getLogin(this))

        mPasswordView!!.setText(Settings.getPassword(this))

        mPasswordView!!.setOnEditorActionListener { _, id, _ ->
            if (id == EditorInfo.IME_ACTION_DONE) {
                mEmailSignInButton.performClick()
                true
            }
            false
        }
        viewModel.loginFormState.observe(this, {
            when (it.checking){
                true -> if (mEmailSignInButton.isEnabled) {
                    mEmailSignInButton.isEnabled = false
                }
                false -> {
                    if (!mEmailSignInButton.isEnabled)
                        mEmailSignInButton.isEnabled = true
                }
            }
        })
        //mEmailSignInButton.
        mEmailSignInButton.setOnClickListener { view ->
            val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS)

            ///mEmailSignInButton.isClickable = false //turned off button
            viewModel.updateLoginState(viewModel.loginFormState.value!!.copy(checking = true))

            viewModel.login(viewModel.loginFormState.value!!.login, viewModel.loginFormState.value!!.password)
            attemptLogin()
        }

        mSkipButton.setOnClickListener {
            val sessionObject = Session("", SessionState.anonymus)

            Settings.setSession(sessionObject, this)
            Settings.setLoginAndPassword("", "", this)
            try {
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                if (currentFocus != null)
                    imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            //Update(context,true).doInBackground()
            //finish()
        }

        signUpButton.setOnClickListener {
            /*val intent = Intent(this, SignUpActivity::class.java)
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this)
            startActivity(intent, options.toBundle())*/
        }
    }

    private fun startGlide(){
        Glide.with(this)
            .load(R.drawable.ball)
            .timeout(10)
            //.apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE))
            .into(findViewById<View>(R.id.logo) as ImageView)
        Glide.with(this)
            .load(R.drawable.ball1)
            .timeout(10)
            //.apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE))
            .into(findViewById<View>(R.id.packman) as ImageView)
    }

    private fun attemptLogin(){
        // нужно добавить блокировку кнопок на время аутентификации

        val logg = findViewById<TextInputLayout>(R.id.input_email)
        val pass = findViewById<TextInputLayout>(R.id.input_password)

        // Reset errors.
        //mEmailView.setError(null);
        //mPasswordView.setError(null);
        logg.error = null
        pass.error = null

        // Store values at the time of the login attempt.
        val email = mEmailView!!.text.toString()
        val password = mPasswordView!!.text.toString()

        var cancel = false
        var focusView: View? = null

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            pass.error = getString(R.string.error_field_required)
            focusView = mPasswordView
            cancel = true
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            logg.error = getString(R.string.error_field_required)
            focusView = mEmailView
            cancel = true
        } else if (!isEmailValid(email)) {
            logg.error = getString(R.string.error_invalid_email)
            focusView = mEmailView
            cancel = true
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView!!.requestFocus()
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            try {
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                if (currentFocus != null)
                    imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            //logg.setVisibility(View.GONE);
            //pass.setVisibility(View.GONE);

            /*showProgress(true, false)
            mAuthTask = UserLoginTask(email, password)
            mAuthTask!!.execute(null as Void?)*/
        }
    }


    private fun isEmailValid(email: String): Boolean {
        //TODO: Replace this with your own logic
        return email.contains("@")
    }
}



       /* login.afterTextChanged {
            viewModel.loginDataChanged(
                login.text.toString(),
                password.text.toString()
            )
        }

        password.apply {
            afterTextChanged {
                viewModel.loginDataChanged(
                    login.text.toString(),
                    password.text.toString()
                )
            }

            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE ->
                        viewModel.login(
                            login.text.toString(),
                            password.text.toString()
                        )
                }
                false
            }


        }*/


