    package com.example.forum.viewmodels

    import android.annotation.SuppressLint
    import android.content.Context
    import android.net.Uri
    import android.util.Log
    import androidx.lifecycle.LiveData
    import androidx.lifecycle.MutableLiveData
    import androidx.lifecycle.ViewModel
    import com.example.forum.data.models.UserModel
    import com.example.forum.utils.SharedPref
    import com.google.firebase.auth.FirebaseAuth
    import com.google.firebase.auth.FirebaseUser
    import com.google.firebase.database.DataSnapshot
    import com.google.firebase.database.DatabaseError
    import com.google.firebase.database.FirebaseDatabase
    import com.google.firebase.database.ValueEventListener
    import com.google.firebase.firestore.ktx.firestore
    import com.google.firebase.ktx.Firebase
    import com.google.firebase.storage.ktx.storage
    import java.util.UUID

    class AuthViewModel : ViewModel() {
        val auth = FirebaseAuth.getInstance()
        private val db = FirebaseDatabase.getInstance()
        val userRef = db.getReference("users")

        private val storageRef = Firebase.storage.reference
        private val imageRef = storageRef.child("users/${UUID.randomUUID()}.jpg")

        private val _firebaseUser = MutableLiveData<FirebaseUser>()
        val firebaseUser : LiveData<FirebaseUser> = _firebaseUser

        val _error = MutableLiveData<String>()
        val error : LiveData<String> = _error

        init {
            _firebaseUser.value = auth.currentUser
        }

        fun login(email: String, password: String, context: Context){
            auth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener {
                    if(it.isSuccessful){
                        _firebaseUser.postValue(auth.currentUser)
                        getData(auth.currentUser!!.uid,context)
                    }else{
                        _error.postValue(it.exception!!.message)
                    }
                }
        }

        private fun getData(uid: String,context: Context) {
            userRef.child(uid).addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val userData = snapshot.getValue(UserModel::class.java)
                    SharedPref.storeDetails(userData!!.email,userData.name,userData.userName,userData.imageUri,context)
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
        }

        fun register(email : String , password: String, name : String, userName:String , image: Uri,context: Context){
            auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener{
                    if(it.isSuccessful) {
                        _firebaseUser.postValue(auth.currentUser)
                        saveImage(email,password,name,userName,image,auth.currentUser?.uid, context)
                    }
                    else{
                        _error.postValue("Something went wrong")
                    }
                }

        }

        private fun saveImage(email: String, password: String, name: String, userName: String, image: Uri, uid: String?,context: Context) {
            val uploadTask = imageRef.putFile(image)
            uploadTask.addOnSuccessListener {
                imageRef.downloadUrl.addOnSuccessListener {
                    saveData(email,password,name,userName,it.toString(),uid,context)
                }
            }
        }

        private fun saveData(email: String, password: String, name :String,userName: String, imageUri: String, uid: String?,context: Context){

            val firestoreDb = Firebase.firestore
            val followersRef = firestoreDb.collection("followers").document(uid!!)
            val followingRef = firestoreDb.collection("following").document(uid!!)

            followersRef.set(mapOf("followerIds" to listOf<String>()))
            followingRef.set(mapOf("followingIds" to listOf<String>()))



            val userData = UserModel(email,password,name,userName,imageUri,uid!!)
            userRef.child(uid!!).setValue(userData)
                .addOnSuccessListener {
                    SharedPref.storeDetails(name,email,userName,imageUri, context)
                }.addOnFailureListener {
                    _error.postValue("Failed to save user data")
                }
        }

        @SuppressLint("NullSafeMutableLiveData")
        fun logout(){
            auth.signOut()
            _firebaseUser.postValue(null)
        }

    }