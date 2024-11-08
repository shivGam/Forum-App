package com.example.forum.viewmodels

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.forum.data.models.ForumModel
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.util.UUID

class PostForumViewModel :ViewModel() {
    private val db = FirebaseDatabase.getInstance()
    val userRef = db.getReference("forum")

    private val storageRef = Firebase.storage.reference
    val imageRef = storageRef.child("forum/${UUID.randomUUID()}.jpg")

    private val _isPosted = MutableLiveData<Boolean>()
    var isPosted :LiveData<Boolean> = _isPosted


    fun saveImage(
        forum : String,
        attachmentFile : Uri,
        userId : String
    ){
        val uploadTask = imageRef.putFile(attachmentFile)
        uploadTask.addOnSuccessListener {
            imageRef.downloadUrl.addOnSuccessListener {
                saveData(forum,it.toString(),userId)
            }
        }
    }

    fun saveData(forum: String, attachmentFile: String, userId: String) {
        val forumData = ForumModel(forum,attachmentFile,userId,System.currentTimeMillis().toString())
        userRef.child(userRef.push().key!!).setValue(forumData)
            .addOnSuccessListener {
                _isPosted.postValue(true)
            }
            .addOnFailureListener{
                _isPosted.postValue(false)
            }
    }
}























