package com.ayushunleashed.ardor.models

data class ProjectModel(
    val projectTitle:String = "",
    val projectDescription:String = "",
    val collegeName:String ="",
    val hostedLink:String="",
    val learningResources:String="",
    val technologyUsed:String="",
    val createdBy: StudentModel = StudentModel(),
    val createdAt: Long =0L,
    val likedBy: ArrayList<String> = ArrayList()
)
