// src/main/java/your/package/name/SignupActivity.kt
package com.example.maprouter

import android.os.AsyncTask
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.sql.Connection
import java.sql.DriverManager
import java.sql.PreparedStatement
import java.sql.SQLException

class SignupActivity : AppCompatActivity() {

    private lateinit var signupUsernameInput: EditText
    private lateinit var signupPasswordInput: EditText
    private lateinit var signupEmailInput: EditText
    private lateinit var signupButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        signupUsernameInput = findViewById(R.id.signup_username_input)
        signupPasswordInput = findViewById(R.id.signup_password_input)
        signupEmailInput = findViewById(R.id.signup_email_input)
        signupButton = findViewById(R.id.signup_btn)

        // Signup button click listener
        signupButton.setOnClickListener {
            SignupTask().execute()
        }
    }

    private inner class SignupTask : AsyncTask<Void, Void, Boolean>() {

        override fun doInBackground(vararg params: Void): Boolean {
            val username = signupUsernameInput.text.toString()
            val password = signupPasswordInput.text.toString()
            val email = signupEmailInput.text.toString()

            // Replace these with your actual database connection details
            val url = "jdbc:mysql://localhost:3306/store" // replace 'store' with your actual database name
            val user = "root" // replace with your actual username
            val pass = "" // replace with your actual password

            return try {
                Class.forName("com.mysql.jdbc.Driver")
                DriverManager.getConnection(url, user, pass).use { conn ->
                    val sql = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)"
                    conn.prepareStatement(sql).use { stmt ->
                        stmt.setString(1, username)
                        stmt.setString(2, password)
                        stmt.setString(3, email)
                        stmt.executeUpdate() > 0 // If user is inserted, return true
                    }
                }
            } catch (e: SQLException) {
                e.printStackTrace()
                false
            } catch (e: ClassNotFoundException) {
                e.printStackTrace()
                false
            }
        }

        override fun onPostExecute(result: Boolean) {
            if (result) {
                // Signup successful
                Toast.makeText(this@SignupActivity, "Signup successful", Toast.LENGTH_SHORT).show()
                // Navigate to another activity if needed
            } else {
                // Signup failed
                Toast.makeText(this@SignupActivity, "Signup failed", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
