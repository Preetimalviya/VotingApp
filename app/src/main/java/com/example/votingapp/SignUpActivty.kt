package com.example.votingapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.votingapp.databinding.ActivitySignUpActivtyBinding
import com.google.firebase.auth.FirebaseAuth

class SignUpActivty : AppCompatActivity() {
    private val binding: ActivitySignUpActivtyBinding by lazy {
        ActivitySignUpActivtyBinding.inflate(layoutInflater)
    }

    private lateinit var auth: FirebaseAuth



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()




        binding.signInButton.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
        binding.registerButton.setOnClickListener {

            val email = binding.email.text.toString()
            val name = binding.username.text.toString()
            val password = binding.password.text.toString()
            val confirmpassword = binding.confirmPassword.text.toString()


            if (email.isEmpty() || name.isEmpty() || password.isEmpty() || confirmpassword.isEmpty()) {
                Toast.makeText(this, "PLease Fill All The Detail", Toast.LENGTH_SHORT).show()
            } else if (password != confirmpassword) {
                Toast.makeText(this, "Confirm Password Must be Same", Toast.LENGTH_SHORT).show()
            } else {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Register Successfull", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this, LoginActivity::class.java))
                            finish()
                        } else {
                            Toast.makeText(
                                this,
                                "Register Failed: ${task.exception?.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            }
        }
    }



}
