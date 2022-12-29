package com.example.helloworld

import android.app.Activity
import android.content.Context
import android.util.Patterns
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

// 입력 유효성 검사
class InputValidation(private val context: Context) {

    // EditText 클릭 시 Hint 값이 TextInputLayout의 라벨로 이동
    fun isInputEditTextFilled(
        textInputEditText: TextInputEditText,
        textInputLayout: TextInputLayout,
        message: String?
    ): Boolean {
        val value = textInputEditText.text.toString().trim { it <= ' ' }

        // 비어있는 경우 에러 메시지
        if (value.isEmpty()) {
            textInputLayout.error = message
            hideKeyboardFrom(textInputEditText) // 키보드 숨기기
            return false
        } else {
            textInputLayout.isErrorEnabled = false
        }
        return true
    }

    // EditText 클릭 시 Hint 값이 TextInputLayout의 라벨로 이동
    fun isInputEditTextEmail(
        textInputEditText: TextInputEditText,
        textInputLayout: TextInputLayout,
        message: String?
    ): Boolean {
        val value = textInputEditText.text.toString().trim { it <= ' ' }

        // 비어있는 경우 & 이메일 형식이 아닌경우 에러 메시지
        if (value.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(value).matches()) {
            textInputLayout.error = message
            hideKeyboardFrom(textInputEditText) // 키보드 숨기기
            return false
        } else {
            textInputLayout.isErrorEnabled = false
        }
        return true
    }

    // 비밀번호 일치하는지 확인
    fun isInputEditTextMatches(
        textInputEditText1: TextInputEditText,
        textInputEditText2: TextInputEditText,
        textInputLayout: TextInputLayout,
        message: String?
    ): Boolean {
        val value1 = textInputEditText1.text.toString().trim { it <= ' ' } // 문자열 양 끝 공백 제외
        val value2 = textInputEditText2.text.toString().trim { it <= ' ' }

        // 입력된 두 비밀번호가 다르면 에러 메시지
        if (!value1.contentEquals(value2)) {
            textInputLayout.error = message
            hideKeyboardFrom(textInputEditText2) // 키보드 숨기기
            return false
        } else {
            textInputLayout.isErrorEnabled = false
        }
        return true
    }


    // 키보드 숨기기
    private fun hideKeyboardFrom(view: View) {
        val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(
            view.windowToken,
            WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        )
    }
}