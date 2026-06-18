package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "complaints")
data class Complaint(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val type: String, // Road, Electricity, Water Supply, Animals, Scheme Help
    val description: String,
    val location: String,
    val imageUrl: String? = null,
    val status: String, // "Pending", "In Progress", "Resolved"
    val comment: String = "",
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "krishi_record")
data class KrishiRecord(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val farmerName: String,
    val lotNumber: String,
    val cropType: String,
    val soilN: Int, // Nitrogen (mg/kg)
    val soilP: Int, // Phosphorus (mg/kg)
    val soilK: Int, // Potassium (mg/kg)
    val soilPh: Float,
    val pestDetected: String,
    val recommendations: String,
    val lastUpdate: Long = System.currentTimeMillis()
)

@Entity(tableName = "health_record")
data class HealthRecord(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val patientName: String,
    val age: Int,
    val symptoms: String,
    val diagnosis: String,
    val medsReminderJson: String,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "tank_monitoring")
data class TankMonitoring(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val tankName: String,
    val percentageLevel: Int,
    val healthStatus: String,
    val isPumpRunning: Boolean,
    val lastChecked: Long = System.currentTimeMillis()
)

@Entity(tableName = "energy_grid")
data class EnergyGrid(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val sectionName: String,
    val generationKW: Float,
    val storageUsagePercentage: Int,
    val activeLights: Int,
    val lastChecked: Long = System.currentTimeMillis()
)
