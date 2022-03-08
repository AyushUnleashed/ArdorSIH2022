package com.ayushunleashed.ardor.daos

import com.ayushunleashed.ardor.models.ProjectModel
import com.ayushunleashed.ardor.models.StudentModel
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ProjectDao {

    val db = FirebaseFirestore.getInstance()
    val projectCollection = db.collection("Projects")
    val auth = Firebase.auth

    fun addProject(projectTitle:String,projectDescription:String,
                   collegeName:String,
                   hostedLink:String, learningResources:String,technologyUsed:String)
    {
        //Log.d("RAJNI","PostCreated")
        //Toast.makeText(this@PostDao,"adding post", Toast.LENGTH_LONG).show()
        GlobalScope.launch {
            val currentUserId = auth.currentUser!!.uid

            val studentDao = StudentDao()

            val student = studentDao.getUserById(currentUserId).await().toObject(StudentModel::class.java)!!

            val currentTime = System.currentTimeMillis()
            val project = ProjectModel(projectTitle,projectDescription,
                collegeName,hostedLink,learningResources,technologyUsed,student,currentTime)
            projectCollection.document().set(project)
        }
    }

    fun getProjectById(postId: String): Task<DocumentSnapshot>
    {
        return projectCollection.document(postId).get()
    }

//    fun updateLikes(projectId: String)
//    {
//        //getting the post through its id
//        GlobalScope.launch {
//            //getting current user id
//            val currentUserId = auth.currentUser!!.uid
//
//            //getting our post of which we need to increase likes
//            val project = getProjectById(projectId).await().toObject(ProjectModel::class.java)
//
//            // if user is present in likedBy array list //contains function on arraylist
//            val isLiked = project?.likedBy?.contains(currentUserId)
//
//            if(isLiked == true) //user has already like the post
//            {
//                project.likedBy.remove(currentUserId) //then unlike | remove user
//            }
//            else //user have not like the post
//            {
//                if (project != null) {
//                    project.likedBy.add(currentUserId)
//                } //then like the post | add user
//            }
//
//            if (project != null) {
//                projectCollection.document(projectId).set(project)
//            }
//        }
//    }
}