package com.ayushunleashed.ardor

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import com.ayushunleashed.ardor.daos.CollegeDao
import com.ayushunleashed.ardor.daos.StudentDao
import com.ayushunleashed.ardor.models.CollegeModel
import com.ayushunleashed.ardor.models.StudentModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignUpActivity : AppCompatActivity() {
    private lateinit var etvEnterEmail: EditText
    private lateinit var etvEnterPassword: EditText
    private lateinit var etvEnterFullName: EditText
    private lateinit var btnSignUp: Button
    private lateinit var btnLogInInstead: Button
    private lateinit var isCollegeBox: CheckBox
    private lateinit var isStudentBox: CheckBox

    private lateinit var mAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        etvEnterEmail = findViewById(R.id.etvEnterEmail)
        etvEnterPassword = findViewById(R.id.etvEnterPassword)
        etvEnterFullName = findViewById(R.id.etvEnterFullName)
        btnSignUp = findViewById(R.id.btnSignUp)
        btnLogInInstead = findViewById(R.id.btnLogInInstead)
        isCollegeBox = findViewById(R.id.isCollege)
        isStudentBox = findViewById(R.id.isStudent)
        mAuth = Firebase.auth

       accessCheckBoxLogic()

        btnSignUp.setOnClickListener {
            Toast.makeText(this, "Clicked on Signup", Toast.LENGTH_SHORT).show()
            createUser();
        }
    }

    fun accessCheckBoxLogic() {
        isStudentBox.setOnCheckedChangeListener { buttonView, isChecked ->

            if(isChecked)
            {
                isCollegeBox.isChecked = false
            }
        }

        isCollegeBox.setOnCheckedChangeListener { buttonView, isChecked ->

            if(isChecked)
            {
                isStudentBox.isChecked = false
            }
        }
    }

    fun createUser() {
        Log.d("Cool", "create user running")
        var email = etvEnterEmail.text.toString()
        var pass = etvEnterPassword.text.toString()
        if (!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            if (!pass.isEmpty()) {
                mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(this)
                { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Succesfully registerd", Toast.LENGTH_SHORT).show()

                        updateUI(mAuth.currentUser)
                    } else {
                        Toast.makeText(this, "Signup Failed", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                etvEnterPassword.setError("password cannot be empty")
                etvEnterPassword.requestFocus()
            }
        } else if (email.isEmpty()) {
            etvEnterEmail.setError("Email cannot be empty")
        } else {
            etvEnterEmail.setError("Enter correct email")
        }


    }

    fun openLogInPage(view: View) {
        Toast.makeText(this, "Going to Login Page", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, LogInActivity::class.java)
        startActivity(intent);
    }

    fun updateUI(firebaseuser: FirebaseUser?) {
        Toast.makeText(this,"Updating UI",Toast.LENGTH_SHORT).show()

        if(firebaseuser!=null)
        {
            val fullName = etvEnterFullName.text.toString()
            Toast.makeText(this,"Firebase!=null",Toast.LENGTH_SHORT).show()
            if(isStudentBox.isChecked == true)
            {

                //if student
                val student = StudentModel(firebaseuser.uid,fullName,false);
                val studentDao = StudentDao()
                studentDao.addStudents(student)

                val intent = Intent(this,HomeActivity::class.java)
                startActivity(intent);
            }

            if(isCollegeBox.isChecked == true)
            {
                //if college
                val college = CollegeModel(firebaseuser.uid,fullName,true);
                val collegeDao = CollegeDao()
                collegeDao.addColleges(college)
                Toast.makeText(this,"College Added to Firestore",Toast.LENGTH_SHORT).show()
                val intent = Intent(this,AdminActivity::class.java)
                startActivity(intent);
            }
            finish()
        }
        else
        {
            Toast.makeText(this,"Can't add to Database",Toast.LENGTH_SHORT).show()
        }
    }
}