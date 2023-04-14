package com.example.morningsqliteapp

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Email
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog

class MainActivity : AppCompatActivity() {
    lateinit var editName:EditText
    lateinit var editEmail:EditText
    lateinit var editNumber:EditText
    lateinit var btnSave:Button
    lateinit var btnView:Button
    lateinit var btnDelete:Button
    lateinit var db:SQLiteDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        editName = findViewById(R.id.mEditName)
        editEmail = findViewById(R.id.mEditEmail)
        editNumber = findViewById(R.id.mEditNumber)
        btnSave = findViewById(R.id.mBtnSave)
        btnView = findViewById(R.id.mBtnView)
        btnDelete = findViewById(R.id.mBtnDelete)
        // Create database called eMobilisDb
        db = openOrCreateDatabase("eMobilisDb", Context.MODE_PRIVATE, null)
        // Create a table Called users inside the database
        db.execSQL("CREATE TABLE IF NOT EXISTS users(jina VARCHAR, arafa VARCHAR, kitambulisho VARCHAR)")
        // Set onClick listeners to the button
        btnSave.setOnClickListener {
            // Receive data from the user
            var name = editName.text.toString().trim()
            var email = editEmail.text.toString().trim()
            var idNumber = editNumber.text.toString().trim()
            // Check if the user is submitting empty fields
            if (name.isEmpty() || email.isEmpty() || idNumber.isEmpty()) {
                // Display an error message using the defined message function
                message("EMPTY FIELDS!!!", "Please fill all inputs")
            } else {
                // Proceed to save the data
                db.execSQL("INSERT INTO users VALUES('"+name+"', '"+email+"', '"+idNumber+"')")
                clear()
                message("SUCCESS!!!","user saved successfully")
            }
        }
        btnView.setOnClickListener {
            // Use cursor to select all the users
            var cursor = db.rawQuery("SELECT * FROM users", null)
            // Check if there is any record in the db
            if (cursor.count == 0){
                message("NO RECORDS!!!", "Sorry, no users were found!!!")
            }else{
                // Use string buffer to append all available records using a loop
                var buffer = StringBuffer()
                while (cursor.moveToNext()){
                    var retrievedName = cursor.getString(0)
                    var retrievedEmail = cursor.getString(1)
                    var retrievedidNumber = cursor.getString(2)
                    buffer.append(retrievedName+"\n")
                    buffer.append(retrievedEmail+"\n")
                    buffer.append(retrievedidNumber+"\n")
                }
                message("USERS",buffer.toString())
            }
        }
        btnDelete.setOnClickListener {
            val idNumber = editNumber.text.toString().trim()
            if (idNumber.isEmpty()){
                message("EMPTY FIELDS", "Please enter an ID number")
            }else{
                // Use cursor to select the user with given ID
                var cursor = db.rawQuery("SELECT * FROM users WHERE kitambulisho='"+idNumber+"'", null)
                // check if the record provided exists
                if (cursor.count == 0){
                    message("NO RECORD!", "Sorry, no user with id"+idNumber)
                }else{
                    // proceed to delete the user
                    db.execSQL("DELETE FROM users WHERE kitambulisho='"+idNumber+"'")
                    clear()
                    message("SUCCESS!!!","User deleted successfully")
                }
            }

        }
    }
    fun message(title:String, message:String){
        val alertDialogue = AlertDialog.Builder(this)
        alertDialogue.setTitle(title)
        alertDialogue.setMessage(message)
        alertDialogue.setPositiveButton("Close", null)
        alertDialogue.create().show()
    }
    fun clear(){
        editName.setText("")
        editNumber.setText("")
        editEmail.setText("")
    }
}







