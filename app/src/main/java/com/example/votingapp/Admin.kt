package com.example.votingapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Admin : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var tvCandidate1Votes: TextView
    private lateinit var tvCandidate2Votes: TextView
    private lateinit var tvCandidate3Votes: TextView
    private lateinit var tvCandidate4Votes: TextView
    private lateinit var btnLogout: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        database = FirebaseDatabase.getInstance().reference

        tvCandidate1Votes = findViewById(R.id.tvCandidate1Votes)
        tvCandidate2Votes = findViewById(R.id.tvCandidate2Votes)
        tvCandidate3Votes = findViewById(R.id.tvCandidate3Votes)
        tvCandidate4Votes = findViewById(R.id.tvCandidate4Votes)
        btnLogout = findViewById(R.id.btnLogout)

        btnLogout.setOnClickListener {
            finish()
        }

        database.child("votes").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val candidate1Votes = snapshot.child("candidate1").getValue(Int::class.java) ?: 0
                val candidate2Votes = snapshot.child("candidate2").getValue(Int::class.java) ?: 0
                val candidate3Votes = snapshot.child("candidate3").getValue(Int::class.java) ?: 0
                val candidate4Votes = snapshot.child("candidate4").getValue(Int::class.java) ?: 0

                tvCandidate1Votes.text = "CANDIDATE 1: $candidate1Votes votes"
                tvCandidate2Votes.text = "CANDIDATE 2: $candidate2Votes votes"
                tvCandidate3Votes.text = "CANDIDATE 3: $candidate3Votes votes"
                tvCandidate4Votes.text = "CANDIDATE 4: $candidate4Votes votes"
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@Admin, "Failed to load results", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
