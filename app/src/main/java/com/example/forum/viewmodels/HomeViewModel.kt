package com.example.forum.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.forum.data.models.ForumModel
import com.example.forum.data.models.UserModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HomeViewModel: ViewModel() {
    private val db = FirebaseDatabase.getInstance()
    val forum = db.getReference("forum")

    private var _forumsAndUsers = MutableLiveData<List<Pair<ForumModel, UserModel>>>()
    val forumsAndUsers : LiveData<List<Pair<ForumModel,UserModel>>> = _forumsAndUsers

    init{
        fetchForumsAndUsers {
            _forumsAndUsers.value = it
        }
    }

    private fun fetchForumsAndUsers(onFetched: (List<Pair<ForumModel,UserModel>>) -> Unit){
        forum.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val result = mutableListOf<Pair<ForumModel,UserModel>>()
                for (forumSnapshot in snapshot.children){
                    val forum = forumSnapshot.getValue(ForumModel::class.java)
                    forum.let {
                        fetchUserFromForum(it!!){
                            user -> result.add(0,it to user)
                            if (result.size == snapshot.childrenCount.toInt()){
                                onFetched(result)
                            }
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    fun fetchUserFromForum(forum : ForumModel , onFetched : (UserModel)-> Unit){
        db.getReference("users").child(forum.userId)
            .addListenerForSingleValueEvent(object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val user = snapshot.getValue(UserModel::class.java)
                    user?.let(onFetched)
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })

    }
}