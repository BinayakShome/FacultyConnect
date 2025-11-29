package com.example.facultyconnect.vm

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.facultyconnect.data.Faculty
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class FacultyViewModel : ViewModel() {

    private val _facultyList = MutableStateFlow<List<Faculty>>(emptyList())
    val facultyList = _facultyList

    private val database = FirebaseDatabase.getInstance(
        "https://facultyconnect-aec8f-default-rtdb.firebaseio.com/"
    ).reference

    fun fetchFaculty(context: Context) {
        Log.d("FacultyVM", "Fetching faculty data...")
        viewModelScope.launch {
            try {
                val data = database.get().await()
                val list = data.children.mapNotNull {
                    it.getValue(Faculty::class.java)
                }
                Log.d("FacultyVM", "Fetched ${list.size} faculty entries")
                _facultyList.value = list
            } catch (e: Exception) {
                Log.e("FacultyVM", "Firebase Error: ${e.message}")
            }
        }
    }

    fun getFacultyById(id: String): Faculty? {
        Log.d("FacultyVM", "Searching for faculty with ID: $id")
        return _facultyList.value.firstOrNull { it.Sl_No.toString() == id }
    }
}
