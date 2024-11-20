package com.example.forum.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.forum.data.models.ForumModel
import com.example.forum.data.models.UserModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class UserInfoViewModel : ViewModel() {
    private val db = FirebaseDatabase.getInstance()
    val userRef = db.getReference("users")
    val forumRef = db.getReference("forum")

    private val _forums = MutableLiveData<List<ForumModel>>()
    val forumListPerUserId :LiveData<List<ForumModel>> get()  = _forums


    private val _followerList = MutableLiveData(listOf<String>())
    val followerList : LiveData<List<String>> get() = _followerList


    private val _followingList = MutableLiveData(listOf<String>())
    val followingList : LiveData<List<String>> get() = _followingList

    private val _users = MutableLiveData(UserModel())
    val users : LiveData<UserModel> get()  = _users

    fun fetchUser(uid : String){
        userRef.child(uid).addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(UserModel::class.java)
                _users.postValue(user)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    fun fetchForumList(uid: String){
        forumRef.orderByChild("userId").equalTo(uid).addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val forumList = snapshot.children.mapNotNull {
                    it.getValue(ForumModel::class.java)
                }
                _forums.postValue(forumList)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private val firestoreDb = Firebase.firestore
    fun followUsers(userId: String, currentUserId: String) {
        val followingRef = firestoreDb.collection("following").document(currentUserId)
        val followerRef = firestoreDb.collection("followers").document(userId)

        // Update following list
        followingRef.get().addOnSuccessListener { document ->
            if (!document.exists()) {
                followingRef.set(mapOf("followingIds" to listOf<String>()))
            }
            followingRef.update("followingIds", FieldValue.arrayUnion(userId))
                .addOnSuccessListener {
                    Log.d("FollowUsers", "Successfully updated following list")
                }
                .addOnFailureListener { error ->
                    Log.e("FollowUsers", "Error updating following list", error)
                }
        }

        // Update follower list
        followerRef.get().addOnSuccessListener { document ->
            if (!document.exists()) {
                followerRef.set(mapOf("followerIds" to listOf<String>()))
            }
            followerRef.update("followerIds", FieldValue.arrayUnion(currentUserId))
                .addOnSuccessListener {
                    Log.d("FollowUsers", "Successfully updated follower list")
                }
                .addOnFailureListener { error ->
                    Log.e("FollowUsers", "Error updating follower list", error)
                }
        }
    }

    fun getFollowers(userId: String) {
        firestoreDb.collection("followers").document(userId)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    Log.e("GetFollowers", "Error fetching followers", error)
                    return@addSnapshotListener
                }

                val followerIds = value?.get("followerIds") as? List<String> ?: listOf()
                if (followerIds != _followerList.value) { // Update only if data changes
                    _followerList.postValue(followerIds)
                }
            }
    }

    fun getFollowing(userId: String) {
        firestoreDb.collection("following").document(userId)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    Log.e("GetFollowing", "Error fetching following", error)
                    return@addSnapshotListener
                }

                val followingIds = value?.get("followingIds") as? List<String> ?: listOf()
                //if (followingIds != _followingList.value) { // Update only if data changes
                    _followingList.postValue(followingIds)
                //}
            }
    }

}