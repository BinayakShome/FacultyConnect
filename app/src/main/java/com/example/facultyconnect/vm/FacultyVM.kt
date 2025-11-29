package com.example.facultyconnect.vm

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
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

    private val _networkError = MutableStateFlow(false)
    val networkError = _networkError

    private val database = FirebaseDatabase.getInstance(
        "https://facultyconnect-aec8f-default-rtdb.firebaseio.com/"
    ).reference


    // 🔍 Check Internet Availability
    @SuppressLint("MissingPermission")
    private fun isInternetAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val network = connectivityManager.activeNetwork ?: return false
        val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

        return activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
    }


    // 🔄 Fetch Faculty Data with Network Check
    fun fetchFaculty(context: Context) {
        Log.d("FacultyVM", "Fetching faculty data...")

        if (!isInternetAvailable(context)) {
            Log.e("FacultyVM", "❌ No Internet Connection!")
            _networkError.value = true
            return
        }

        _networkError.value = false // Reset state if connected

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


    // 🔍 Get Single Faculty by ID
    fun getFacultyById(id: String): Faculty? {
        Log.d("FacultyVM", "Searching for faculty with ID: $id")
        return _facultyList.value.firstOrNull {
            it.Sl_No.toString() == id
        }
    }
}