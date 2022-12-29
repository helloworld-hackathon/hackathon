package com.example.helloworld

import android.annotation.SuppressLint
import java.io.FileInputStream
import java.io.FileOutputStream

import android.view.View
import android.os.Bundle
import android.widget.Button
import android.widget.CalendarView
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.pow

class Calendar : AppCompatActivity() {
    var userID: String = "userID"
    lateinit var fname: String
    lateinit var str: String
    lateinit var str2: String
    lateinit var calendarView: CalendarView
    lateinit var updateBtn: Button
    lateinit var deleteBtn: Button
    lateinit var saveBtn: Button
    lateinit var h_text: TextView
    lateinit var w_text: TextView
    lateinit var title: TextView
    lateinit var BMI_text: TextView
    lateinit var heightEdit: EditText
    lateinit var weightText: EditText
    lateinit var resultText : String
    lateinit var content : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)

        // UI값 생성
        calendarView = findViewById(R.id.calendarView)
        saveBtn = findViewById(R.id.saveBtn)
        deleteBtn = findViewById(R.id.deleteBtn)
        updateBtn = findViewById(R.id.updateBtn)
        h_text = findViewById(R.id.h_text)
        w_text = findViewById(R.id.w_text)
        title = findViewById(R.id.title)
        heightEdit = findViewById(R.id.heightEdit)
        weightText = findViewById(R.id.weightText)
        BMI_text = findViewById(R.id.BMI_text)

        title.text = "철수 Calendar"

        calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            saveBtn.visibility = View.VISIBLE
            heightEdit.visibility = View.VISIBLE
            weightText.visibility = View.VISIBLE
            updateBtn.visibility = View.INVISIBLE
            deleteBtn.visibility = View.INVISIBLE
            BMI_text.visibility = View.INVISIBLE
            heightEdit.setText("")
            weightText.setText("")
            h_text.setText("키(cm): ")
            w_text.setText("몸무게(kg): ")
            checkDay(year, month, dayOfMonth, userID)
        }

        saveBtn.setOnClickListener {
            saveDiary(fname)
            heightEdit.visibility = View.INVISIBLE
            weightText.visibility = View.INVISIBLE
            saveBtn.visibility = View.INVISIBLE
            updateBtn.visibility = View.VISIBLE
            deleteBtn.visibility = View.VISIBLE
            BMI_text.visibility = View.VISIBLE
            h_text.text = "키(cm): " + heightEdit.text.toString()
            w_text.text = "몸무게(kg): " + weightText.text.toString()
            h_text.visibility = View.VISIBLE
            w_text.visibility = View.VISIBLE
            BMI(heightEdit.text.toString(), weightText.text.toString())
        }
    }

    fun BMI (height : String, weight : String){
        val height = height.toDouble()
        val weight = weight.toDouble()
        val bmi = weight / (height / 100).pow(2.0)
        resultText = when{
            bmi >= 35.0 -> "고도 비만"
            bmi >= 30.0 -> "중도 비만"
            bmi >= 25.0 -> "경도 비만"
            bmi >= 23.0 -> "과체중"
            bmi >= 18.5 -> "정상"
            else -> "저체중"
        }
        BMI_text.text = "BMI 결과 : " + resultText
    }
    // 달력 내용 조회, 수정
    fun checkDay(cYear: Int, cMonth: Int, cDay: Int, userID: String) {
        //저장할 파일 이름설정
        fname = "" + userID + cYear + "-" + (cMonth + 1) + "" + "-" + cDay + ".txt"

        var fileInputStream: FileInputStream
        try {
            fileInputStream = openFileInput(fname)
            val fileData = ByteArray(fileInputStream.available())
            fileInputStream.read(fileData)
            fileInputStream.close()
            str = String(fileData)
            heightEdit.visibility = View.INVISIBLE
            weightText.visibility = View.INVISIBLE
            h_text.visibility = View.VISIBLE
            w_text.visibility = View.INVISIBLE
            h_text.text = str
            saveBtn.visibility = View.INVISIBLE
            updateBtn.visibility = View.VISIBLE
            deleteBtn.visibility = View.VISIBLE

            updateBtn.setOnClickListener {
                heightEdit.visibility = View.VISIBLE
                weightText.visibility = View.VISIBLE
                h_text.visibility = View.VISIBLE
                w_text.visibility = View.VISIBLE
                heightEdit.setText(str)
                h_text.setText("키(cm) : ")
                heightEdit.visibility = View.VISIBLE
                w_text.setText("몸무게(kg) : ")
                BMI_text.visibility = View.INVISIBLE
                saveBtn.visibility = View.VISIBLE
                updateBtn.visibility = View.INVISIBLE
                deleteBtn.visibility = View.INVISIBLE
            }

            deleteBtn.setOnClickListener {
                h_text.visibility = View.INVISIBLE
                w_text.visibility = View.INVISIBLE
                updateBtn.visibility = View.INVISIBLE
                deleteBtn.visibility = View.INVISIBLE
                heightEdit.setText("")
                h_text.setText("키(cm) : ")
                heightEdit.visibility = View.VISIBLE
                weightText.setText("")
                w_text.setText("몸무게(kg) : ")
                content = ""
                BMI_text.visibility = View.INVISIBLE
                weightText.visibility = View.VISIBLE
                saveBtn.visibility = View.VISIBLE
                h_text.visibility = View.VISIBLE
                w_text.visibility = View.VISIBLE
                removeDiary(fname)
            }
            if (content == "") {
                h_text.visibility = View.INVISIBLE
                w_text.visibility = View.INVISIBLE
                updateBtn.visibility = View.INVISIBLE
                deleteBtn.visibility = View.INVISIBLE
                saveBtn.visibility = View.VISIBLE
                heightEdit.visibility = View.VISIBLE
                weightText.visibility = View.VISIBLE
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // 달력 내용 제거
    @SuppressLint("WrongConstant")
    fun removeDiary(readDay: String?) {
        var fileOutputStream: FileOutputStream
        try {
            fileOutputStream = openFileOutput(readDay, MODE_NO_LOCALIZED_COLLATORS)
            content = ""
            fileOutputStream.write(content.toByteArray())
            fileOutputStream.close()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    // 달력 내용 추가
    @SuppressLint("WrongConstant")
    fun saveDiary(readDay: String?) {
        var fileOutputStream: FileOutputStream
        try {
            fileOutputStream = openFileOutput(readDay, MODE_NO_LOCALIZED_COLLATORS)
            content = "키(cm) : "+ heightEdit.text.toString() +
                          "\n몸무게(kg) : " + weightText.text.toString() +
                          "\nBMI 결과 : " + resultText
            fileOutputStream.write(content.toByteArray())
            fileOutputStream.close()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }
}

