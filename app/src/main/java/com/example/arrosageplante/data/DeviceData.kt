package com.example.arrosageplante.data

import android.util.Log
import androidx.compose.runtime.Composable
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import androidx.compose.ui.Modifier
import java.util.Date
import androidx.compose.foundation.layout.fillMaxSize
import com.example.arrosageplante.utils.LineChart

// Créer une classe pour représenter les données
data class DeviceData(
    val humidity: Double = 0.0,
    val pumpOn: Boolean = false,
    val soilmoisture : Double = 0.0,
    val temperature: Double = 0.0,
    val waterlvl: Double = 50.0
)


// Fonction pour récupérer les données
@Composable
fun fetchDeviceData(onDataFetched: (DeviceData) -> Unit) {
    // Référence à la base de données
    val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    val myRef: DatabaseReference = database.getReference("devices/esp32_1")

    // Attacher un listener pour récupérer les données
    myRef.addValueEventListener(object : ValueEventListener {

        override fun onDataChange(snapshot: DataSnapshot) {
            val deviceData = snapshot.getValue(DeviceData::class.java)
            if (deviceData != null) {
                Log.d("DeviceData", "Données récupérées: $deviceData")
                // Passer les données récupérées à la fonction de rappel
                onDataFetched(deviceData)
                saveDeviceDataFireStore(deviceData)
            }


        }

        override fun onCancelled(error: DatabaseError) {
            // Gestion des erreurs
            println("Erreur lors de la récupération des données: ${error.message}")
        }
    })
}

fun updatePumpStatus(pumpOn: Boolean, devicePath: String = "devices/esp32_1") {
    // Get Firebase Database reference
    val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    val myRef: DatabaseReference = database.getReference(devicePath)

    // Create a map to update only the pumpOn field
    val updates = mapOf("pumpOn" to pumpOn)

    // Update the database
    myRef.updateChildren(updates)
        .addOnSuccessListener {
            Log.d("PumpControl", "Pump status updated successfully to: $pumpOn")
        }
        .addOnFailureListener { exception ->
            Log.e("PumpControl", "Failed to update pump status", exception)
        }
}



fun saveWateringFrequency(frequency: Int, enable: Boolean, devicePath: String = "devices/esp32_1") {


    // Get Firebase Database reference
    val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    val myRef: DatabaseReference = database.getReference(devicePath)

    // Create a map to update only the pumpOn field
    val updates = mapOf(
        "numHour" to frequency * 24,
        "automaticplantingOn" to enable)

    // Update the database
    myRef.updateChildren(updates)
        .addOnSuccessListener {
            Log.d("Frequency", "Frequency and WateringBool changed")
        }
        .addOnFailureListener { exception ->
            Log.e("PumpControl", "Failed to update pump status", exception)
        }
}

fun saveDeviceDataFireStore(deviceData: DeviceData) {
    val db = FirebaseFirestore.getInstance()
    val deviceDataRef = db.collection("DeviceData")

    val firestoreData = mapOf(
        "humidity" to deviceData.humidity,
        "temperature" to deviceData.temperature,
        "waterlvl" to deviceData.waterlvl,
        "soilmoisture" to deviceData.soilmoisture,
        "timestamp" to FieldValue.serverTimestamp()
    )

    deviceDataRef.add(firestoreData)
        .addOnSuccessListener { documentReference ->
            Log.d("Firestore", "DocumentSnapshot added with ID: ${documentReference.id}")
        }
        .addOnFailureListener { e ->
            Log.w("Firestore", "Error adding document", e)
        }
}

// Fonction pour sauvegarder les données dans le FireStore
fun saveWateringFrequencyFireStore(frequency: Int, enable: Boolean) {


    val db = FirebaseFirestore.getInstance()
    val wateringRef = db.collection("watering").document("esp_1")

    val wateringData = mapOf(
        "frequency" to frequency,
        "enable" to true)

    val frequencyRef = db.collection("watering")


    db.runTransaction { transaction ->
        val snapshot = transaction.get(wateringRef)

        if (snapshot.exists()) {
            // Document exists, update it
            transaction.update(wateringRef, wateringData)
        } else {
            // Document doesn't exist, create it
            transaction.set(wateringRef, wateringData)
        }
    }
        .addOnSuccessListener {
            Log.d("Firestore", "Document successfully processed!")
        }
        .addOnFailureListener { e ->
            Log.w("Firestore", "Error processing document", e)
        }

}

// POUR LES GRAPHES
// Data class to hold our fetched device data
data class DeviceDataPoint(
    val timestamp: Date? = null,
    val temperature: Double = 0.0,
    val humidity: Double = 0.0,
    val waterlvl: Double = 0.0,
    val soilmoisture: Double = 0.0
)

@Composable
fun DeviceDataGraph() {
    // State to hold our fetched data points
    val dataPoints = remember { mutableStateOf<List<DeviceDataPoint>>(emptyList()) }

    // Launched effect to fetch data when the composable is first created
    LaunchedEffect(Unit) {
        dataPoints.value = fetchDeviceDataFromFirestore()
    }

    // Create different graph types based on the data
    LineChart(
        modifier = Modifier.fillMaxSize(),
        entries = convertToLineEntries(dataPoints.value),
        label = "Temperature Over Time"
    )
}

// Function to fetch data from Firestore
suspend fun fetchDeviceDataFromFirestore(): List<DeviceDataPoint> {
    val db = FirebaseFirestore.getInstance()
    val deviceDataRef = db.collection("DeviceData")

    return try {
        // Fetch last 50 documents, sorted by timestamp in descending order
        val querySnapshot = deviceDataRef
            .orderBy("timestamp", Query.Direction.ASCENDING)
            .limit(50)
            .get()
            .await()

        // Convert query results to DeviceDataPoint objects
        querySnapshot.documents.mapNotNull { document ->
            DeviceDataPoint(
                timestamp = document.getDate("timestamp"),
                temperature = document.getDouble("temperature") ?: 0.0,
                humidity = document.getDouble("humidity") ?: 0.0,
                waterlvl = document.getDouble("waterlvl") ?: 0.0,
                soilmoisture = document.getDouble("soilmoisture") ?: 0.0
            )
        }
    } catch (e: Exception) {
        Log.e("Firestore", "Error fetching device data", e)
        emptyList()
    }
}

// Helper function to convert data points to chart entries
fun convertToLineEntries(
    dataPoints: List<DeviceDataPoint>,
    valueSelector: (DeviceDataPoint) -> Double = { it.temperature }
): List<Entry> {
    return dataPoints.mapIndexed { index, dataPoint ->
        Entry(index.toFloat(), valueSelector(dataPoint).toFloat())
    }
}
