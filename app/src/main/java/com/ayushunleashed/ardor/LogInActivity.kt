package com.ayushunleashed.ardor

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.ayushunleashed.ardor.daos.CollegeDao
import com.ayushunleashed.ardor.daos.StudentDao
import com.ayushunleashed.ardor.models.CollegeModel
import com.ayushunleashed.ardor.models.StudentModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class LogInActivity : AppCompatActivity() {

    private lateinit var etvEnterEmail: EditText
    private lateinit var etvEnterPassword: EditText
    private lateinit var btnLogIn: Button
    private lateinit var btnSignUpInstead: Button
    private lateinit var mAuth: FirebaseAuth
    private lateinit var db:FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)

        etvEnterEmail = findViewById(R.id.etvEnterEmail)
        etvEnterPassword = findViewById(R.id.etvEnterPassword)
        btnLogIn = findViewById(R.id.btnLogIn)
        btnSignUpInstead = findViewById(R.id.btnSignUpInstead)
        mAuth = Firebase.auth
        db = FirebaseFirestore.getInstance()


        btnLogIn.setOnClickListener {
            Toast.makeText(this, "Login Clicked", Toast.LENGTH_SHORT).show()
            logInUser()
        }
    }

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = mAuth.currentUser


    }

    fun logInUser()
    {  //Log.d("Cool","Loged in button clicked")
        //Toast.makeText(this,"logInUser()",Toast.LENGTH_SHORT).show()
        var email = etvEnterEmail.text.toString()
        var pass = etvEnterPassword.text.toString()

        if(!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            if(!pass.isEmpty())
            {
                mAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(this)
                { task ->
                    if(task.isSuccessful)
                    {
                        Toast.makeText(this,"Log in Successful", Toast.LENGTH_SHORT).show()
                        checkUserAccessLevel(mAuth.uid.toString())
                    }
                    else
                    {
                        Toast.makeText(this,"Log in Failed", Toast.LENGTH_SHORT).show()
                    }
                }

            }else
            { etvEnterPassword.setError("password cannot be empty")
                etvEnterPassword.requestFocus()
            }
        }
        else if(email.isEmpty())
        {
            etvEnterEmail.setError("Email cannot be empty")
        }
        else
        {
            etvEnterEmail.setError("Enter correct email")
        }

    }

    fun checkUserAccessLevel(uid:String)
    {
         val studentsCollection = db.collection("Students").document(uid)
         val collegesCollection = db.collection("Colleges").document(uid)

        studentsCollection.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val document = task.result
                if(document != null) {
                    if (document.exists()) {
                        Log.d("TAG", "Student already exists.")

                        val intent = Intent(this,HomeActivity::class.java)
                        startActivity(intent);
                    } else {
                        Log.d("TAG", "Student doesn't exist.")
                    }
                }
            } else {
                Log.d("TAG", "Error: ", task.exception)
            }
        }
        collegesCollection.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val document = task.result
                if(document != null) {
                    if (document.exists()) {
                        Log.d("TAG", "College already exists.")
                        val intent = Intent(this,AdminActivity::class.java)
                        startActivity(intent);


                    } else {
                        Log.d("TAG", "College doesn't exist.")
                    }
                }
            } else {
                Log.d("TAG", "Error: ", task.exception)
            }
        }
    }

    fun goToSignUpScreen(view: View) {
        Toast.makeText(this,"Going to Signup Page", Toast.LENGTH_SHORT).show()
        val intent = Intent(this,SignUpActivity::class.java)
        startActivity(intent);
    }


}
