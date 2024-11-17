package com.example.forum.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.forum.data.models.UserModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SearchViewModel : ViewModel() {
    val db = FirebaseDatabase.getInstance()
    val users = db.getReference("users")

    private val _users = MutableLiveData<List<UserModel>>()
    val userList : LiveData<List<UserModel>> = _users

    init {
        fetchUserList{
            _users.value = it
        }
    }

    private fun fetchUserList(onResult: (List<UserModel>) -> Unit){

        users.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val result = mutableListOf<UserModel>()
                for( userSnapshot in snapshot.children){
                    val users = userSnapshot.getValue(UserModel::class.java)
                    result.add(users!!)
                }
                onResult(result)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

    }
}