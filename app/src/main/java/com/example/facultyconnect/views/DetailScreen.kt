package com.example.facultyconnect.views

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.facultyconnect.vm.FacultyViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    navController: NavController,
    facultyId: String,
    facultyViewModel: FacultyViewModel
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Profile",
                        fontSize = 24.sp,
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.DarkGray
                )
            )
        }
    ) {
    val facultyList by facultyViewModel.facultyList.collectAsState()
    Log.d("DetailScreen", "Faculty list size: ${facultyList.size}")

    val context = LocalContext.current
    LaunchedEffect(Unit) {
        facultyViewModel.fetchFaculty(context)
    }

    val faculty = facultyViewModel.getFacultyById(facultyId)
    Log.d("DetailScreen", "Faculty found? ${faculty != null}")

    when {
        facultyList.isEmpty() -> {
            Box(Modifier.fillMaxSize(), Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        faculty == null -> {
            Box(Modifier.fillMaxSize(), Alignment.Center) {
                Text("No Faculty Found")
            }
        }

        else -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp, top = 60.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Text("Name: ${faculty.Name}", style = MaterialTheme.typography.titleLarge)
                Text("Email: ${faculty.Email_ID}")
                Text("Mobile: ${faculty.Mobile_Number}")
                Text("Chamber: ${faculty.Room_No}")

                Button(onClick = {
                    val intent = Intent(Intent.ACTION_DIAL).apply {
                        data = Uri.parse("tel:${faculty.Mobile_Number}")
                    }
                    context.startActivity(intent)
                }) {
                    Text("Call")
                }

                Button(onClick = {
                    val intent = Intent(Intent.ACTION_SENDTO).apply {
                        data = Uri.parse("mailto:${faculty.Email_ID}")
                    }
                    context.startActivity(intent)
                }) {
                    Text("Send Email")
                }

                // 📌 Share Button
                Button(onClick = {
                    val shareText = """
                Faculty Contact:
                Name: ${faculty.Name}
                Email: ${faculty.Email_ID}
                Mobile: ${faculty.Mobile_Number}
            """.trimIndent()

                    val intent = Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(Intent.EXTRA_TEXT, shareText)
                        setPackage("com.whatsapp") // specifically WhatsApp
                    }

                    try {
                        context.startActivity(intent)
                    } catch (e: Exception) {
                        // If WhatsApp not installed → open normal share sheet
                        val genericShareIntent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_TEXT, shareText)
                        }
                        context.startActivity(
                            Intent.createChooser(genericShareIntent, "Share via")
                        )
                    }
                }) {
                    Text("Share")
                }
            }
            }
        }
    }
}

