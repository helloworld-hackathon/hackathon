package com.example.helloworld

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.widget.NestedScrollView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class RegisterActivity : AppCompatActivity(), View.OnClickListener {

    private val activity: AppCompatActivity = this@RegisterActivity
    private var nestedScrollView: NestedScrollView? = null

    // 아이디(이메일), 비밀번호
    private var textInputLayoutName: TextInputLayout? = null
    private var textInputLayoutEmail: TextInputLayout? = null
    private var textInputLayoutPassword: TextInputLayout? = null
    private var textInputLayoutConfirmPassword: TextInputLayout? = null
    private var textInputEditTextName: TextInputEditText? = null
    private var textInputEditTextEmail: TextInputEditText? = null
    private var textInputEditTextPassword: TextInputEditText? = null
    private var textInputEditTextConfirmPassword: TextInputEditText? = null

    private var appCompatButtonRegister: AppCompatButton? = null      // 회원가입 버튼
    private var appCompatTextViewLoginLink: AppCompatTextView? = null // 로그인 페이지로 이동
    private var inputValidation: InputValidation? = null              // 입력 유효성 검사
    private var databaseHelper: DatabaseHelper? = null                // 데이터 관리
    private var user: User? = null                                    // 데이터 형식


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        initViews()
        initListeners()
        initObjects()
    }

    private fun initViews() {
        nestedScrollView = findViewById<View>(R.id.nestedScrollView) as NestedScrollView
        textInputLayoutName = findViewById<View>(R.id.textInputLayoutName) as TextInputLayout
        textInputLayoutEmail = findViewById<View>(R.id.textInputLayoutEmail) as TextInputLayout
        textInputLayoutPassword = findViewById<View>(R.id.textInputLayoutPassword) as TextInputLayout
        textInputLayoutConfirmPassword = findViewById<View>(R.id.textInputLayoutConfirmPassword) as TextInputLayout
        textInputEditTextName = findViewById<View>(R.id.textInputEditTextName) as TextInputEditText
        textInputEditTextEmail = findViewById<View>(R.id.textInputEditTextEmail) as TextInputEditText
        textInputEditTextPassword = findViewById<View>(R.id.textInputEditTextPassword) as TextInputEditText
        textInputEditTextConfirmPassword = findViewById<View>(R.id.textInputEditTextConfirmPassword) as TextInputEditText
        appCompatButtonRegister = findViewById<View>(R.id.appCompatButtonRegister) as AppCompatButton
        appCompatTextViewLoginLink = findViewById<View>(R.id.appCompatTextViewLoginLink) as AppCompatTextView
    }

    private fun initListeners() {
        appCompatButtonRegister!!.setOnClickListener(this)    // 회원가입 버튼
        appCompatTextViewLoginLink!!.setOnClickListener(this) // 로그인 페이지로 이동
    }

    private fun initObjects() {
        inputValidation = InputValidation(activity) // 데이터 관리
        databaseHelper = DatabaseHelper(activity)   // 입력 유효성 검사
        user = User()                               // 데이터 형식
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.appCompatButtonRegister -> postDataToSQLite()  // 회원가입 유효성 검사
            R.id.appCompatTextViewLoginLink -> finish()         // 로그인 페이지로 이동
        }
    }

    // 회원가입 유효성 검사
    private fun postDataToSQLite() {
        // Name이 비어있는 경우
        if (!inputValidation!!.isInputEditTextFilled(
                textInputEditTextName!!,
                textInputLayoutName!!, "Enter Full Name"
            )
        ) {
            return
        }
        // 이메일이 비어있는 경우
        if (!inputValidation!!.isInputEditTextFilled(
                textInputEditTextEmail!!,
                textInputLayoutEmail!!, "Enter Email"
            )
        ) {
            return
        }
        // 올바르지 않은 이메일 형식
        if (!inputValidation!!.isInputEditTextEmail(
                textInputEditTextEmail!!,
                textInputLayoutEmail!!, "Enter Valid Email"
            )
        ) {
            return
        }
        // 비밀번호가 비어있는 경우
        if (!inputValidation!!.isInputEditTextFilled(
                textInputEditTextPassword!!,
                textInputLayoutPassword!!, "Enter Password"
            )
        ) {
            return
        }

        // 입력된 두 비밀번호가 다른 경우
        if (!inputValidation!!.isInputEditTextMatches(
                textInputEditTextPassword!!, textInputEditTextConfirmPassword!!,
                textInputLayoutConfirmPassword!!, "Password Does Not Matches"
            )
        ) {
            return
        }

        // 회원가입 입력 유효성 검사
        if (!databaseHelper!!.checkUser(
                textInputEditTextEmail!!.text.toString().trim { it <= ' ' })
        ) {
            user!!.name = textInputEditTextName!!.text.toString().trim { it <= ' ' }
            user!!.email = textInputEditTextEmail!!.text.toString().trim { it <= ' ' }
            user!!.password = textInputEditTextPassword!!.text.toString().trim { it <= ' ' }
            databaseHelper!!.addUser(user!!)

            // 회원 가입 완료 시
            Snackbar.make(
                nestedScrollView!!,
                "Registration Successful!", // 팝업 문자
                Snackbar.LENGTH_LONG
            ).show()
            emptyInputEditText()
        } else {
            // 이미 가입되어있는 경우 에러 문자 표시
            Snackbar.make(
                nestedScrollView!!,
                "Email Already Exists!", // 팝업 문자
                Snackbar.LENGTH_LONG
            ).show()
        }
    }

    // Null 아님
    private fun emptyInputEditText() {
        textInputEditTextName!!.text = null               // 이름
        textInputEditTextEmail!!.text = null              // 이메일
        textInputEditTextPassword!!.text = null           // 비밀번호
        textInputEditTextConfirmPassword!!.text = null    // 비밀번호 확인
    }
}