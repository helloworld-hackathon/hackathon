package com.example.helloworld

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

// 생성자를 통해 context 객체 받아옴
class DatabaseHelper(context: Context) :
    // 상속 (부모 클래스 생성자 호출)
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    // 테이블
    private val CREATE_USER_TABLE = ("CREATE TABLE " + TABLE_USER + "("
                + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_USER_NAME + " TEXT,"
                + COLUMN_USER_EMAIL + " TEXT," + COLUMN_USER_PASSWORD + " TEXT" + ")")
    private val DROP_USER_TABLE = "DROP TABLE IF EXISTS " + TABLE_USER

    // 테이블 생성
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_USER_TABLE)
    }

    //테이블 업데이트
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(DROP_USER_TABLE) //테이블 있는 경우 삭제
        onCreate(db) // 테이블 생성
    }

    // 테이블에 값 삽입
    fun addUser(user: User) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_USER_NAME, user.name)
        values.put(COLUMN_USER_EMAIL, user.email)
        values.put(COLUMN_USER_PASSWORD, user.password)
        db.insert(TABLE_USER, null, values)
        db.close()
    }

    //사용자 확인
    fun checkUser(email: String): Boolean {
        val columns = arrayOf(
            COLUMN_USER_ID
        )
        val db = this.writableDatabase
        val selection = COLUMN_USER_EMAIL + " = ?"
        val selectionArgs = arrayOf(email)
        val cursor = db.query(
            TABLE_USER,
            columns,
            selection,
            selectionArgs,
            null,
            null,
            null
        )
        val cursorCount = cursor.count
        cursor.close()
        db.close()
        return cursorCount > 0
    }

    //사용자 확인
    fun checkUser(email: String, password: String): Boolean {
        val columns = arrayOf(
            COLUMN_USER_ID
        )
        val db = this.writableDatabase
        val selection = COLUMN_USER_EMAIL + " = ?" + " AND " + COLUMN_USER_PASSWORD + " =?"
        val selectionArgs = arrayOf(email, password)
        val cursor = db.query(
            TABLE_USER,
            columns,
            selection,
            selectionArgs,
            null,
            null,
            null
        )
        val cursorCount = cursor.count
        cursor.close()
        db.close()
        return cursorCount > 0
    }

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "UserManager.db" // 데이터베이스 이름
        private const val TABLE_USER = "user"              // 테이블 이름
        private const val COLUMN_USER_ID = "user_id"       // 아이디
        private const val COLUMN_USER_NAME = "user_name"   // 이름
        private const val COLUMN_USER_EMAIL = "user_email" // 이메일
        private const val COLUMN_USER_PASSWORD = "user_password" // 비밀번호
    }
}