package com.ayushunleashed.ardor.daos

import com.ayushunleashed.ardor.models.CollegeModel
import com.ayushunleashed.ardor.models.StudentModel
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class CollegeDao {
    private val db = FirebaseFirestore.getInstance()
    private val collegesCollection = db.collection("Colleges")

    fun addColleges(user: CollegeModel?)
    {
        GlobalScope.launch (Dispatchers.IO){
            //if user is not null
            user?.let{
                collegesCollection.document(user.uid).set(it)
            }
        }
    }

    fun getUserById(uId:String): Task<DocumentSnapshot>
    {
        return collegesCollection.document(uId).get()
    }
}