package com.example.coroutine.userapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.coroutine.userapp.model.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MvvmActivityViewModel : ViewModel() {


    private var usersRepository = UserRepository()

    var users = liveData(Dispatchers.IO) {
        val result = usersRepository.getUsers()
        emit(result)
    }


    // var users: MutableLiveData<List<User>> = MutableLiveData()

//    fun getUsers() {
//        viewModelScope.launch {
//            var result: List<User>? = null
//            withContext(Dispatchers.IO) {
//                result = usersRepository.getUsers()
//            }
//            users.value = result
//        }
//    }
}

/**
/*
// Custom Scope , not prefered way
private val myJob = Job()
private val myScope = CoroutineScope(Dispatchers.IO + myJob)

fun getUserData(){
myScope.launch {
// Some Code..
}
}

override fun onCleared() {
super.onCleared()
myJob.cancel()
}
*/

// viewModelScope : Its a CoroutineScope tied to the ViewModel

fun getUserData(){
viewModelScope.launch {
// Some code...
}
}

// lifecycleScope : Its tied to the lifecycle of the components i.e. activity,fragment
// Check MVVMActivity

 **/
