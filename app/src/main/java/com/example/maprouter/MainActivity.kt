// src/main/java/your/package/name/MainActivity.kt
package com.example.maprouter

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.sql.Connection
import java.sql.DriverManager
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException

class MainActivity : AppCompatActivity() {

    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var signupButton: Button // Add this line

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        usernameEditText = findViewById(R.id.username_input)
        passwordEditText = findViewById(R.id.password_input)
        loginButton = findViewById(R.id.login_btn)
        signupButton = findViewById(R.id.signup_btn) // Add this line

        // Login button click listener
        loginButton.setOnClickListener {
            LoginTask().execute()
        }

        // Sign up button click listener
        signupButton.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }
    }

    private inner class LoginTask : AsyncTask<Void, Void, Boolean>() {

        override fun doInBackground(vararg params: Void): Boolean {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()

            // Replace these with your actual database connection details
            val url = "jdbc:mysql://localhost:3306/store" // replace 'your_database_name' with your actual database name
            val user = "root" // replace with your actual username
            val pass = "" // replace with your actual password

            return try {
                Class.forName("com.mysql.jdbc.Driver")
                DriverManager.getConnection(url, user, pass).use { conn ->
                    val sql = "SELECT * FROM users WHERE username = ? AND password = ?"
                    conn.prepareStatement(sql).use { stmt ->
                        stmt.setString(1, username)
                        stmt.setString(2, password)
                        stmt.executeQuery().use { rs ->
                            rs.next() // If user exists, return true
                        }
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
                // Login successful
                Toast.makeText(this@MainActivity, "Login successful", Toast.LENGTH_SHORT).show()
                // Navigate to another activity if needed
            } else {
                // Login failed
                Toast.makeText(this@MainActivity, "Invalid username or password", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
