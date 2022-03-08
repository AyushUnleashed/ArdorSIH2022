package com.ayushunleashed.ardor.daos

import android.widget.Toast
import com.ayushunleashed.ardor.MainActivity
import com.ayushunleashed.ardor.models.StudentModel
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class StudentDao {
    private val db = FirebaseFirestore.getInstance()
    private val studentsCollection = db.collection("Students")

    fun addStudents(user: StudentModel?)
    {
        GlobalScope.launch (Dispatchers.IO){
            //if user is not null
            user?.let{
                studentsCollection.document(user.uid).set(it)
            }
        }
    }

    fun getUserById(uId:String): Task<DocumentSnapshot>
    {
        return studentsCollection.document(uId).get()
    }
}