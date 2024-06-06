package com.example.votingapp

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class VotingActivity : AppCompatActivity() {
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private var selectedCandidate: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_voting)

        database = FirebaseDatabase.getInstance().reference
        auth = FirebaseAuth.getInstance()

        val candidateRadioGroup: RadioGroup = findViewById(R.id.candidateRadioGroup)
        val submitVoteButton: Button = findViewById(R.id.submitVoteButton)
        val logoutButton: Button = findViewById(R.id.logoutButton)

        candidateRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            selectedCandidate = when (checkedId) {
                R.id.candidate1 -> "candidate1"
                R.id.candidate2 -> "candidate2"
                R.id.candidate3 -> "candidate3"
                R.id.candidate4 -> "candidate4"
                else -> null
            }
        }

        submitVoteButton.setOnClickListener { submitVote() }
        logoutButton.setOnClickListener { logout() }
    }

    private fun submitVote() {
        val userId = auth.currentUser?.uid
        if (userId == null) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
            return
        }

        if (selectedCandidate == null) {
            Toast.makeText(this, "Please select a candidate", Toast.LENGTH_SHORT).show()
            return
        }

        val userRef = database.child("users").child(userId)
        userRef.get().addOnSuccessListener { dataSnapshot ->
            if (dataSnapshot.exists()) {
                if (dataSnapshot.child("hasVoted").value == true) {
                    Toast.makeText(this, "You already voted", Toast.LENGTH_SHORT).show()
                } else {
                    userRef.child("hasVoted").setValue(true)
                    database.child("votes").child(selectedCandidate!!).get().addOnSuccessListener { voteSnapshot ->
                        val currentCount = voteSnapshot.getValue(Int::class.java) ?: 0
                        database.child("votes").child(selectedCandidate!!).setValue(currentCount + 1)
                        Toast.makeText(this, "Vote submitted", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                userRef.child("hasVoted").setValue(true)
                database.child("votes").child(selectedCandidate!!).get().addOnSuccessListener { voteSnapshot ->
                    val currentCount = voteSnapshot.getValue(Int::class.java) ?: 0
                    database.child("votes").child(selectedCandidate!!).setValue(currentCount + 1)
                    Toast.makeText(this, "Vote submitted", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun logout() {
        auth.signOut()
        finish()
    }
}
