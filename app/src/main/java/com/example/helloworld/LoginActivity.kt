package com.example.helloworld

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.widget.NestedScrollView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout


class LoginActivity : AppCompatActivity(), View.OnClickListener {

    private val activity: AppCompatActivity = this@LoginActivity
    private var nestedScrollView: NestedScrollView? = null

    // 아이디(이메일), 비밀번호
    private var textInputLayoutEmail: TextInputLayout? = null
    private var textInputLayoutPassword: TextInputLayout? = null
    private var textInputEditTextEmail: TextInputEditText? = null
    private var textInputEditTextPassword: TextInputEditText? = null

    private var appCompatButtonLogin: AppCompatButton? = null   // 로그인 버튼
    private var textViewLinkRegister: AppCompatTextView? = null // 회원가입 페이지로 이동
    private var inputValidation: InputValidation? = null        // 입력 유효성 검사
    private var databaseHelper: DatabaseHelper? = null          // 데이터 관리


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        initViews()
        initListeners()
        initObjects()
    }

    private fun initViews() {
        nestedScrollView = findViewById<View>(R.id.nestedScrollView) as NestedScrollView
        textInputLayoutEmail = findViewById<View>(R.id.textInputLayoutEmail) as TextInputLayout
        textInputLayoutPassword = findViewById<View>(R.id.textInputLayoutPassword) as TextInputLayout
        textInputEditTextEmail = findViewById<View>(R.id.textInputEditTextEmail) as TextInputEditText
        textInputEditTextPassword = findViewById<View>(R.id.textInputEditTextPassword) as TextInputEditText
        appCompatButtonLogin = findViewById<View>(R.id.appCompatButtonLogin) as AppCompatButton
        textViewLinkRegister = findViewById<View>(R.id.textViewLinkRegister) as AppCompatTextView
    }

    private fun initListeners() {
        appCompatButtonLogin!!.setOnClickListener(this) // 로그인 버튼
        textViewLinkRegister!!.setOnClickListener(this) // 회원가입 페이지로 이동
    }

    private fun initObjects() {
        databaseHelper = DatabaseHelper(activity)   // 데이터 관리
        inputValidation = InputValidation(activity) // 입력 유효성 검사
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.appCompatButtonLogin -> verifyFromSQLite() // 로그인 유효성 검사
            R.id.textViewLinkRegister -> {
                val intentRegister = Intent(applicationContext, RegisterActivity::class.java)
                startActivity(intentRegister)
                overridePendingTransition(0, 0); //애니메이션 없애기
            }
        }
    }

    private fun verifyFromSQLite() {
        // 이메일이 비어있는 경우
        if (!inputValidation!!.isInputEditTextFilled(
                textInputEditTextEmail!!,
                textInputLayoutEmail!!, "Enter Valid Email" // 경고 문구
            )
        ) {
            return
        }

        // 올바르지 않은 이메일 형식
        if (!inputValidation!!.isInputEditTextEmail(
                textInputEditTextEmail!!,
                textInputLayoutEmail!!, "Enter Valid Email" // 경고 문구
            )
        ) {
            return
        }

        // 비밀번호가 비어있는 경우
        if (!inputValidation!!.isInputEditTextFilled(
                textInputEditTextPassword!!,
                textInputLayoutPassword!!, "Enter Password" // 경고 문구
            )
        ) {
            return
        }

        // 아이디, 비밀번호 확인
        if (databaseHelper!!.checkUser(
                textInputEditTextEmail!!.text.toString().trim { it <= ' ' },  // 문자열 양 끝 공백 제외
                textInputEditTextPassword!!.text.toString().trim { it <= ' ' })
        ) {
            val accountsIntent = Intent(activity, MainActivity::class.java) // 메인화면 시작

            // 데이터 전달
            accountsIntent.putExtra(
                "EMAIL",
                textInputEditTextEmail!!.text.toString().trim { it <= ' ' }) // 문자열 양 끝 공백 제외
            emptyInputEditText() // null이 아님을 명시
            startActivity(accountsIntent) // 메인화면 시작
        }
        // 아이디, 비밀번호가 틀린경우
        else {
            // 팝업 문자 표시
            Snackbar.make(
                nestedScrollView!!,
                "Wrong Email or Password",
                Snackbar.LENGTH_LONG
            ).show()
        }
    }

    // Null 아님
    private fun emptyInputEditText() {
        textInputEditTextEmail!!.text = null     // 이메일
        textInputEditTextPassword!!.text = null  // 비밀번호
    }
}
