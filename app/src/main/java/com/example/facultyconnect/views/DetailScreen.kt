package com.example.facultyconnect.views

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
    val context = LocalContext.current
    val facultyList by facultyViewModel.facultyList.collectAsState()

    LaunchedEffect(Unit) {
        if (facultyList.isEmpty()) {
            facultyViewModel.fetchFaculty(context)
        }
    }

    val faculty = facultyViewModel.getFacultyById(facultyId)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Faculty Details",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyLarge
                    )},
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->

        when {
            facultyList.isEmpty() -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) { CircularProgressIndicator() }
            }

            faculty == null -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) { Text("No Faculty Found") }
            }

            else -> {
                Column(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {

                    // Profile Name Section
                    Text(
                        text = faculty.Name,
                        style = MaterialTheme.typography.headlineMedium,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold
                    )

                    // Info Card UI
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(5.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            InfoRow(label = "Email:", value = faculty.Email_ID)
                            InfoRow(label = "Mobile:", value = faculty.Mobile_Number)
                            InfoRow(label = "Chamber:", value = "${faculty.Room_No}, Campus 25")

                            Button(
                                onClick = {
                                    val url = buildFacultyProfileUrl(faculty.Name)
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                                    context.startActivity(intent)
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Open Profile")
                            }
                        }
                    }

                    // Action Buttons
                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier.fillMaxWidth()
                    ) {

                        ActionButton(
                            icon = Icons.Default.Call,
                            text = "Call"
                        ) {
                            val intent = Intent(Intent.ACTION_DIAL).apply {
                                data = Uri.parse("tel:${faculty.Mobile_Number}")
                            }
                            context.startActivity(intent)
                        }

                        ActionButton(
                            icon = Icons.Default.Email,
                            text = "Email"
                        ) {
                            val intent = Intent(Intent.ACTION_SENDTO).apply {
                                data = Uri.parse("mailto:${faculty.Email_ID}")
                            }
                            context.startActivity(intent)
                        }

                        ActionButton(
                            icon = Icons.Default.Share,
                            text = "Share"
                        ) {
                            val shareText = """
                                Faculty Contact:
                                Name: ${faculty.Name}
                                Email: ${faculty.Email_ID}
                                Mobile: ${faculty.Mobile_Number}
                                Chamber: ${faculty.Room_No}, Campus 25

                                ~FacultyConnect
                            """.trimIndent()

                            val intent = Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(Intent.EXTRA_TEXT, shareText)
                            }
                            context.startActivity(Intent.createChooser(intent, "Share via"))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontWeight = FontWeight.Bold)
        Text(text = value)
    }
}

@Composable
fun ActionButton(icon: ImageVector, text: String, onClick: () -> Unit) {
    ElevatedButton(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.width(100.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(icon, contentDescription = text)
            Text(text, fontSize = 12.sp)
        }
    }
}

fun buildFacultyProfileUrl(name: String): String {
    Log.d("PROFILE_DEBUG", "ORIGINAL = \"$name\"")

    var s = name.replace(Regex("(?i)(prof|dr|mr|ms|mrs|sir|shri|sri)(?=[A-Za-z])")) { m ->
        m.value + " "
    }.trim()

    Log.d("PROFILE_DEBUG", "NORMALIZED = \"$s\"")

    val prefixes = listOf("mr", "ms", "mrs", "dr", "prof", "sir", "shri", "sri")

    val tokens = s.split(Regex("[\\s.()]+")).filter { it.isNotBlank() }

    Log.d("PROFILE_DEBUG", "TOKENS = $tokens")

    val filtered = tokens.filter { token ->
        token.lowercase() !in prefixes
    }

    Log.d("PROFILE_DEBUG", "FILTERED TOKENS = $filtered")

    val formatted = filtered.joinToString("-") { token ->
        token.lowercase().replaceFirstChar { it.titlecase() }
    }

    val url = "https://cse.kiit.ac.in/profiles/$formatted/"

    Log.d("PROFILE_DEBUG", "URL = $url")

    return url
}