package com.nammayantra

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.nammayantra.ui.theme.NammaYantraTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NammaYantraTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation()
                }
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    var isLoggedIn by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        isLoggedIn = currentUser != null
    }

    NavHost(
        navController = navController,
        startDestination = if (isLoggedIn) "dashboard" else "login"
    ) {
        composable("login") { LoginScreen(navController) }
        composable("register") { RegisterScreen(navController) }
        composable("dashboard") { DashboardScreen(navController) }
        composable("add_equipment") { AddEquipmentScreen(navController) }
        composable("my_equipment") { MyEquipmentScreen(navController) }
        composable("browse_equipment") { BrowseEquipmentScreen(navController) }
        composable("my_requests") { MyRequestsScreen(navController) }
        composable("owner_requests") { OwnerRequestsScreen(navController) }
    }
}

@Composable
fun LoginScreen(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("🚜", style = MaterialTheme.typography.displayLarge)
                Text("Namma-Yantra", style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold)
                Text("Farm Machinery Rental", style = MaterialTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.height(32.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        if (email.isNotEmpty() && password.isNotEmpty()) {
                            isLoading = true
                            FirebaseAuth.getInstance()
                                .signInWithEmailAndPassword(email, password)
                                .addOnCompleteListener { task ->
                                    isLoading = false
                                    if (task.isSuccessful) {
                                        Toast.makeText(context, "Login Successful!", Toast.LENGTH_SHORT).show()
                                        navController.navigate("dashboard") {
                                            popUpTo("login") { inclusive = true }
                                        }
                                    } else {
                                        Toast.makeText(context, "Login Failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                                    }
                                }
                        } else {
                            Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
                        }
                    },
                    enabled = !isLoading,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (isLoading) CircularProgressIndicator(modifier = Modifier.size(24.dp))
                    else Text("LOGIN", fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(16.dp))
                TextButton(onClick = { navController.navigate("register") }) {
                    Text("Don't have an account? REGISTER HERE")
                }
            }
        }
    }
}

@Composable
fun RegisterScreen(navController: NavController) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var village by remember { mutableStateOf("") }
    var district by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var role by remember { mutableStateOf("farmer") }
    var isLoading by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text("Create Account", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = name, onValueChange = { name = it },
                    label = { Text("Full Name") }, modifier = Modifier.fillMaxWidth(), singleLine = true
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = email, onValueChange = { email = it },
                    label = { Text("Email") }, modifier = Modifier.fillMaxWidth(), singleLine = true
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = phone, onValueChange = { phone = it },
                    label = { Text("Phone") }, modifier = Modifier.fillMaxWidth(), singleLine = true
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = village, onValueChange = { village = it },
                    label = { Text("Village") }, modifier = Modifier.fillMaxWidth(), singleLine = true
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = district, onValueChange = { district = it },
                    label = { Text("District") }, modifier = Modifier.fillMaxWidth(), singleLine = true
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = password, onValueChange = { password = it },
                    label = { Text("Password (min 6)") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(), singleLine = true
                )
                Spacer(modifier = Modifier.height(16.dp))

                Text("I am a:", fontWeight = FontWeight.Medium)
                Row {
                    RadioButton(selected = role == "farmer", onClick = { role = "farmer" })
                    Text("Farmer", modifier = Modifier.padding(start = 8.dp, end = 16.dp))
                    RadioButton(selected = role == "owner", onClick = { role = "owner" })
                    Text("Equipment Owner", modifier = Modifier.padding(start = 8.dp))
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        when {
                            name.isEmpty() -> Toast.makeText(context, "Enter name", Toast.LENGTH_SHORT).show()
                            email.isEmpty() -> Toast.makeText(context, "Enter email", Toast.LENGTH_SHORT).show()
                            phone.isEmpty() -> Toast.makeText(context, "Enter phone", Toast.LENGTH_SHORT).show()
                            village.isEmpty() -> Toast.makeText(context, "Enter village", Toast.LENGTH_SHORT).show()
                            district.isEmpty() -> Toast.makeText(context, "Enter district", Toast.LENGTH_SHORT).show()
                            password.length < 6 -> Toast.makeText(context, "Password must be 6+ characters", Toast.LENGTH_SHORT).show()
                            else -> {
                                isLoading = true
                                FirebaseAuth.getInstance()
                                    .createUserWithEmailAndPassword(email, password)
                                    .addOnCompleteListener { task ->
                                        if (task.isSuccessful) {
                                            val uid = task.result.user?.uid ?: ""
                                            val userData = hashMapOf(
                                                "name" to name,
                                                "email" to email,
                                                "phone" to phone,
                                                "village" to village,
                                                "district" to district,
                                                "role" to role,
                                                "uid" to uid
                                            )
                                            FirebaseDatabase.getInstance().getReference("users")
                                                .child(uid).setValue(userData)
                                                .addOnCompleteListener {
                                                    isLoading = false
                                                    if (it.isSuccessful) {
                                                        Toast.makeText(context, "Registration Successful!", Toast.LENGTH_LONG).show()
                                                        navController.popBackStack()
                                                    } else {
                                                        Toast.makeText(context, "Failed to save user data", Toast.LENGTH_LONG).show()
                                                    }
                                                }
                                        } else {
                                            isLoading = false
                                            Toast.makeText(context, "Registration Failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                                        }
                                    }
                            }
                        }
                    },
                    enabled = !isLoading,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (isLoading) CircularProgressIndicator(modifier = Modifier.size(24.dp))
                    else Text("REGISTER", fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(16.dp))
                TextButton(onClick = { navController.popBackStack() }) {
                    Text("Already have an account? LOGIN")
                }
            }
        }
    }
}

@Composable
fun DashboardScreen(navController: NavController) {
    var userName by remember { mutableStateOf("") }
    var userRole by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        currentUser?.uid?.let { uid ->
            FirebaseDatabase.getInstance().getReference("users").child(uid)
                .get().addOnSuccessListener { snapshot ->
                    userName = snapshot.child("name").getValue(String::class.java) ?: "User"
                    userRole = snapshot.child("role").getValue(String::class.java) ?: ""
                }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(modifier = Modifier.padding(32.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text("👋", style = MaterialTheme.typography.displayMedium)
                Text("Welcome, $userName!", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Role: ${if (userRole == "owner") "Equipment Owner" else "Farmer"}", style = MaterialTheme.typography.bodyMedium)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (userRole == "owner") {
            Button(
                onClick = { navController.navigate("add_equipment") },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Add New Equipment", fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(12.dp))
            Button(
                onClick = { navController.navigate("my_equipment") },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3))
            ) {
                Icon(Icons.Default.List, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("My Equipment", fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(12.dp))
            Button(
                onClick = { navController.navigate("owner_requests") },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF9800))
            ) {
                Icon(Icons.Default.Notifications, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Rental Requests", fontWeight = FontWeight.Bold)
            }
        } else {
            Button(
                onClick = { navController.navigate("browse_equipment") },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
            ) {
                Icon(Icons.Default.Search, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Browse Equipment", fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(12.dp))
            Button(
                onClick = { navController.navigate("my_requests") },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3))
            ) {
                Icon(Icons.Default.List, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("My Requests", fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                FirebaseAuth.getInstance().signOut()
                navController.navigate("login") {
                    popUpTo("dashboard") { inclusive = true }
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.ExitToApp, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Logout")
        }
    }
}

@Composable
fun AddEquipmentScreen(navController: NavController) {
    var name by remember { mutableStateOf("") }
    var type by remember { mutableStateOf("") }
    var hourlyRate by remember { mutableStateOf("") }
    var dailyRate by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val currentUser = FirebaseAuth.getInstance().currentUser

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text("Add New Equipment", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Equipment Name") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = type, onValueChange = { type = it }, label = { Text("Equipment Type") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = hourlyRate, onValueChange = { hourlyRate = it }, label = { Text("Hourly Rate (₹)") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), modifier = Modifier.fillMaxWidth(), singleLine = true)
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = dailyRate, onValueChange = { dailyRate = it }, label = { Text("Daily Rate (₹)") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), modifier = Modifier.fillMaxWidth(), singleLine = true)
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = location, onValueChange = { location = it }, label = { Text("Location") }, modifier = Modifier.fillMaxWidth(), singleLine = true)

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        if (name.isNotEmpty() && type.isNotEmpty() && location.isNotEmpty()) {
                            isLoading = true
                            val ownerId = currentUser?.uid ?: ""
                            val ownerName = currentUser?.email ?: ""

                            val equipmentData = mapOf<String, Any>(
                                "name" to name,
                                "type" to type,
                                "hourlyRate" to (hourlyRate.toDoubleOrNull() ?: 0.0),
                                "dailyRate" to (dailyRate.toDoubleOrNull() ?: 0.0),
                                "location" to location,
                                "ownerId" to ownerId,
                                "ownerName" to ownerName,
                                "isAvailable" to true,
                                "createdAt" to System.currentTimeMillis()
                            )
                            FirebaseDatabase.getInstance().getReference("equipment")
                                .push()
                                .setValue(equipmentData)
                                .addOnCompleteListener { task ->
                                    isLoading = false
                                    if (task.isSuccessful) {
                                        Toast.makeText(context, "Equipment Added!", Toast.LENGTH_SHORT).show()
                                        navController.popBackStack()
                                    } else {
                                        Toast.makeText(context, "Failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                                    }
                                }
                        } else {
                            Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
                        }
                    },
                    enabled = !isLoading,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (isLoading) CircularProgressIndicator(modifier = Modifier.size(24.dp))
                    else Text("ADD EQUIPMENT", fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(16.dp))
                TextButton(onClick = { navController.popBackStack() }) { Text("Cancel") }
            }
        }
    }
}

@Composable
fun MyEquipmentScreen(navController: NavController) {
    var equipmentList by remember { mutableStateOf<List<Map<String, Any>>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    val context = LocalContext.current
    val currentUser = FirebaseAuth.getInstance().currentUser

    LaunchedEffect(Unit) {
        val uid = currentUser?.uid ?: return@LaunchedEffect
        val query = FirebaseDatabase.getInstance().getReference("equipment")
            .orderByChild("ownerId").equalTo(uid)

        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = mutableListOf<Map<String, Any>>()
                for (child in snapshot.children) {
                    val equipment = mutableMapOf<String, Any>()
                    equipment["id"] = child.key ?: ""
                    child.children.forEach { field ->
                        val key = field.key ?: return@forEach
                        val value = field.value ?: return@forEach
                        when (key) {
                            "name", "type", "location", "ownerName" -> equipment[key] = value.toString()
                            "hourlyRate", "dailyRate" -> equipment[key] = value
                            "isAvailable" -> equipment[key] = value as? Boolean ?: true
                        }
                    }
                    if (equipment.containsKey("name")) {
                        list.add(equipment)
                    }
                }
                equipmentList = list
                isLoading = false
            }

            override fun onCancelled(error: DatabaseError) {
                isLoading = false
                Toast.makeText(context, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        Text("My Equipment", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))

        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (equipmentList.isEmpty()) {
            Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp)) {
                Column(modifier = Modifier.padding(32.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("📭", style = MaterialTheme.typography.displayMedium)
                    Text("No equipment added yet", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Click + to add your first equipment!", style = MaterialTheme.typography.bodyMedium)
                }
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(equipmentList) { equipment ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(equipment["name"] as? String ?: "", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                            Text("🔧 Type: ${equipment["type"]}", style = MaterialTheme.typography.bodyMedium)
                            val hourlyRate = equipment["hourlyRate"] as? Number
                            val dailyRate = equipment["dailyRate"] as? Number
                            Text("💰 Hourly: ₹${hourlyRate?.toString() ?: "0"} | Daily: ₹${dailyRate?.toString() ?: "0"}", style = MaterialTheme.typography.bodyMedium)
                            Text("📍 Location: ${equipment["location"]}", style = MaterialTheme.typography.bodyMedium)
                            val isAvailable = equipment["isAvailable"] as? Boolean ?: true
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (isAvailable) Color(0xFF4CAF50) else Color(0xFFF44336)
                            ) {
                                Text(
                                    if (isAvailable) "Available" else "Rented",
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { navController.popBackStack() }, modifier = Modifier.fillMaxWidth()) {
            Text("Back to Dashboard")
        }
    }
}

@Composable
fun BrowseEquipmentScreen(navController: NavController) {
    var equipmentList by remember { mutableStateOf<List<Map<String, Any>>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    val context = LocalContext.current
    val currentUser = FirebaseAuth.getInstance().currentUser

    LaunchedEffect(Unit) {
        val query = FirebaseDatabase.getInstance().getReference("equipment")
            .orderByChild("isAvailable").equalTo(true)

        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = mutableListOf<Map<String, Any>>()
                for (child in snapshot.children) {
                    val equipment = mutableMapOf<String, Any>()
                    equipment["id"] = child.key ?: ""
                    child.children.forEach { field ->
                        val key = field.key ?: return@forEach
                        val value = field.value ?: return@forEach
                        when (key) {
                            "name", "type", "location", "ownerName", "ownerId" -> equipment[key] = value.toString()
                            "hourlyRate", "dailyRate" -> equipment[key] = value
                            "isAvailable" -> equipment[key] = value as? Boolean ?: true
                        }
                    }
                    if (equipment.containsKey("name")) {
                        list.add(equipment)
                    }
                }
                equipmentList = list
                isLoading = false
                if (list.isEmpty()) {
                    Toast.makeText(context, "No equipment available", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                isLoading = false
                Toast.makeText(context, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        Text("Browse Equipment", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))

        when {
            isLoading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator()
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Loading equipment...")
                    }
                }
            }

            equipmentList.isEmpty() -> {
                Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp)) {
                    Column(modifier = Modifier.padding(32.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("🔍", style = MaterialTheme.typography.displayMedium)
                        Text("No equipment available", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Please check back later!", style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }

            else -> {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(equipmentList) { equipment ->
                        FarmerEquipmentCard(
                            equipment = equipment,
                            currentUser = currentUser,
                            onRequestSent = {
                                Toast.makeText(context, "✅ Request Sent!", Toast.LENGTH_SHORT).show()
                                equipmentList = equipmentList.filter { it["id"] != equipment["id"] }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun FarmerEquipmentCard(
    equipment: Map<String, Any>,
    currentUser: FirebaseUser?,
    onRequestSent: () -> Unit
) {
    var isRequesting by remember { mutableStateOf(false) }
    val context = LocalContext.current

    val name = equipment["name"] as? String ?: "Unknown"
    val type = equipment["type"] as? String ?: "Unknown"
    val hourlyRateValue = equipment["hourlyRate"]
    val dailyRateValue = equipment["dailyRate"]
    val hourlyRate = when (hourlyRateValue) {
        is Number -> hourlyRateValue.toDouble()
        is String -> hourlyRateValue.toDoubleOrNull() ?: 0.0
        else -> 0.0
    }
    val dailyRate = when (dailyRateValue) {
        is Number -> dailyRateValue.toDouble()
        is String -> dailyRateValue.toDoubleOrNull() ?: 0.0
        else -> 0.0
    }
    val location = equipment["location"] as? String ?: "Unknown"
    val ownerName = equipment["ownerName"] as? String ?: "Unknown"
    val ownerId = equipment["ownerId"] as? String ?: ""
    val equipmentId = equipment["id"] as? String ?: ""

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(name, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)

            Surface(shape = RoundedCornerShape(8.dp), color = MaterialTheme.colorScheme.primaryContainer) {
                Text(type, modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp))
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Person, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color.Gray)
                Spacer(modifier = Modifier.width(4.dp))
                Text("Owner: $ownerName", style = MaterialTheme.typography.bodyMedium)
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.LocationOn, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color.Gray)
                Spacer(modifier = Modifier.width(4.dp))
                Text("Location: $location", style = MaterialTheme.typography.bodyMedium)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Surface(shape = RoundedCornerShape(8.dp), color = Color(0xFFE8F5E9)) {
                    Text("💰 Hourly: ₹${hourlyRate.toInt()}", modifier = Modifier.padding(8.dp), fontWeight = FontWeight.Bold, color = Color(0xFF2E7D32))
                }
                Surface(shape = RoundedCornerShape(8.dp), color = Color(0xFFE3F2FD)) {
                    Text("📅 Daily: ₹${dailyRate.toInt()}", modifier = Modifier.padding(8.dp), fontWeight = FontWeight.Bold, color = Color(0xFF1565C0))
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = {
                    if (currentUser == null) {
                        Toast.makeText(context, "Please login first", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    isRequesting = true

                    val requestData = mapOf<String, Any>(
                        "equipmentId" to equipmentId,
                        "equipmentName" to name,
                        "ownerId" to ownerId,
                        "ownerName" to ownerName,
                        "farmerId" to currentUser.uid,
                        "farmerName" to (currentUser.email ?: ""),
                        "status" to "pending",
                        "createdAt" to System.currentTimeMillis()
                    )

                    FirebaseDatabase.getInstance().getReference("requests")
                        .push()
                        .setValue(requestData)
                        .addOnSuccessListener {
                            FirebaseDatabase.getInstance().getReference("equipment")
                                .child(equipmentId)
                                .child("isAvailable")
                                .setValue(false)
                                .addOnSuccessListener {
                                    isRequesting = false
                                    onRequestSent()
                                }
                                .addOnFailureListener {
                                    isRequesting = false
                                    onRequestSent()
                                }
                        }
                        .addOnFailureListener { exception ->
                            isRequesting = false
                            Toast.makeText(context, "Failed: ${exception.message}", Toast.LENGTH_LONG).show()
                        }
                },
                enabled = !isRequesting,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3))
            ) {
                if (isRequesting) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Sending...", color = Color.White)
                } else {
                    Icon(Icons.Default.Send, contentDescription = null, tint = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Request Rent", fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
        }
    }
}

@Composable
fun MyRequestsScreen(navController: NavController) {
    var requests by remember { mutableStateOf<List<Map<String, Any>>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    val context = LocalContext.current
    val currentUser = FirebaseAuth.getInstance().currentUser

    LaunchedEffect(Unit) {
        val uid = currentUser?.uid ?: return@LaunchedEffect
        val query = FirebaseDatabase.getInstance().getReference("requests")
            .orderByChild("farmerId").equalTo(uid)

        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = mutableListOf<Map<String, Any>>()
                for (child in snapshot.children) {
                    val request = mutableMapOf<String, Any>()
                    request["id"] = child.key ?: ""
                    child.children.forEach { field ->
                        val key = field.key ?: return@forEach
                        val value = field.value ?: return@forEach
                        when (key) {
                            "equipmentName", "ownerName", "status" -> request[key] = value.toString()
                        }
                    }
                    list.add(request)
                }
                requests = list
                isLoading = false
            }

            override fun onCancelled(error: DatabaseError) {
                isLoading = false
                Toast.makeText(context, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        Text("My Requests", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))

        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (requests.isEmpty()) {
            Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp)) {
                Column(modifier = Modifier.padding(32.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("📭", style = MaterialTheme.typography.displayMedium)
                    Text("No requests yet", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Browse equipment and send rental requests!", style = MaterialTheme.typography.bodyMedium)
                }
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(requests) { request ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(request["equipmentName"] as? String ?: "", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                                val status = request["status"] as? String ?: ""
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = when (status) {
                                        "accepted" -> Color(0xFF4CAF50)
                                        "declined" -> Color(0xFFF44336)
                                        else -> Color(0xFFFF9800)
                                    }
                                ) {
                                    Text(status.uppercase(), modifier = Modifier.padding(8.dp), color = Color.White, fontWeight = FontWeight.Bold)
                                }
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("👤 Owner: ${request["ownerName"]}", style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { navController.popBackStack() }, modifier = Modifier.fillMaxWidth()) {
            Text("Back to Dashboard")
        }
    }
}

@Composable
fun OwnerRequestsScreen(navController: NavController) {
    var requests by remember { mutableStateOf<List<Map<String, Any>>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    val context = LocalContext.current
    val currentUser = FirebaseAuth.getInstance().currentUser

    fun updateStatus(requestId: String, request: Map<String, Any>, status: String) {
        val updates = mapOf("status" to status)
        FirebaseDatabase.getInstance().getReference("requests").child(requestId)
            .updateChildren(updates)
            .addOnSuccessListener {
                Toast.makeText(context, "Request $status", Toast.LENGTH_SHORT).show()
                if (status == "accepted") {
                    val equipmentId = request["equipmentId"] as? String
                    if (equipmentId != null) {
                        FirebaseDatabase.getInstance().getReference("equipment")
                            .child(equipmentId)
                            .child("isAvailable")
                            .setValue(false)
                    }
                }
            }
            .addOnFailureListener {
                Toast.makeText(context, "Failed to update status", Toast.LENGTH_SHORT).show()
            }
    }

    LaunchedEffect(Unit) {
        val uid = currentUser?.uid ?: return@LaunchedEffect
        val query = FirebaseDatabase.getInstance().getReference("requests")
            .orderByChild("ownerId").equalTo(uid)

        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = mutableListOf<Map<String, Any>>()
                for (child in snapshot.children) {
                    val request = mutableMapOf<String, Any>()
                    request["id"] = child.key ?: ""
                    child.children.forEach { field ->
                        val key = field.key ?: return@forEach
                        val value = field.value ?: return@forEach
                        when (key) {
                            "equipmentName", "equipmentId", "farmerName", "farmerId", "status" -> request[key] = value.toString()
                        }
                    }
                    list.add(request)
                }
                requests = list
                isLoading = false
            }

            override fun onCancelled(error: DatabaseError) {
                isLoading = false
                Toast.makeText(context, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        Text("Rental Requests", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))

        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (requests.isEmpty()) {
            Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp)) {
                Column(modifier = Modifier.padding(32.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("📭", style = MaterialTheme.typography.displayMedium)
                    Text("No requests yet", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Farmers will send rental requests here", style = MaterialTheme.typography.bodyMedium)
                }
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(requests) { request ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(request["equipmentName"] as? String ?: "", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("👤 Farmer: ${request["farmerName"]}", style = MaterialTheme.typography.bodyMedium)

                            Spacer(modifier = Modifier.height(8.dp))

                            val status = request["status"] as? String ?: ""
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = when (status) {
                                    "accepted" -> Color(0xFF4CAF50)
                                    "declined" -> Color(0xFFF44336)
                                    else -> Color(0xFFFF9800)
                                }
                            ) {
                                Text("Status: ${status.uppercase()}", modifier = Modifier.padding(8.dp), color = Color.White, fontWeight = FontWeight.Bold)
                            }

                            if (status == "pending") {
                                Spacer(modifier = Modifier.height(12.dp))
                                Row {
                                    Button(
                                        onClick = { updateStatus(request["id"] as String, request, "accepted") },
                                        modifier = Modifier.weight(1f).padding(end = 8.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
                                    ) { Text("✓ Accept", color = Color.White, fontWeight = FontWeight.Bold) }
                                    Button(
                                        onClick = { updateStatus(request["id"] as String, request, "declined") },
                                        modifier = Modifier.weight(1f),
                                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF44336))
                                    ) { Text("✗ Decline", color = Color.White, fontWeight = FontWeight.Bold) }
                                }
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { navController.popBackStack() }, modifier = Modifier.fillMaxWidth()) {
            Text("Back to Dashboard")
        }
    }
}