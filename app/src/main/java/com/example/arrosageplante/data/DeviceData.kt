package com.example.arrosageplante.data

import android.util.Log
import androidx.compose.runtime.Composable
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore


// Créer une classe pour représenter les données
data class DeviceData(
    val humidity: Double = 0.0,
    val pumpOn: Boolean = false,
    val soilMoisture : Double = 0.0,
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
