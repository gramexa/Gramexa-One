package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        Complaint::class,
        KrishiRecord::class,
        HealthRecord::class,
        TankMonitoring::class,
        EnergyGrid::class
    ],
    version = 1,
    exportSchema = false
)
abstract class VillageDatabase : RoomDatabase() {
    abstract fun villageDao(): VillageDao

    companion object {
        @Volatile
        private var INSTANCE: VillageDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): VillageDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    VillageDatabase::class.java,
                    "village_operating_system.db"
                )
                .addCallback(DatabaseCallback(scope))
                .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class DatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch(Dispatchers.IO) {
                    populateInitialData(database.villageDao())
                }
            }
        }

        suspend fun populateInitialData(dao: VillageDao) {
            // Seed Complaints
            dao.insertComplaint(
                Complaint(
                    type = "Electricity",
                    description = "Main transformer sparkling near primary school",
                    location = "Chopal Chowk, Ward 3",
                    status = "In Progress",
                    comment = "Lineman dispatched. Safety alerts triggered."
                )
            )
            dao.insertComplaint(
                Complaint(
                    type = "Water Supply",
                    description = "Leakage in mainline pipeline causing mud accumulation",
                    location = "Near Community Well",
                    status = "Resolved",
                    comment = "Repaired by Panchayat plumbing team."
                )
            )

            // Seed Krishi
            dao.insertKrishiRecord(
                KrishiRecord(
                    farmerName = "Ramesh Kumar",
                    lotNumber = "Lot #4B",
                    cropType = "Wheat",
                    soilN = 45,
                    soilP = 30,
                    soilK = 25,
                    soilPh = 6.4f,
                    pestDetected = "None",
                    recommendations = "Slight potassium deficiency reported. AI recommends applying potash bio-fertilizers."
                )
            )
            dao.insertKrishiRecord(
                KrishiRecord(
                    farmerName = "Savitri Devi",
                    lotNumber = "Lot #9",
                    cropType = "Mustard",
                    soilN = 50,
                    soilP = 40,
                    soilK = 35,
                    soilPh = 6.8f,
                    pestDetected = "Aphids (माहू)",
                    recommendations = "Neem Oil Spray (5% solution) or Imidacloprid application recommended."
                )
            )

            // Seed Health
            dao.insertHealthRecord(
                HealthRecord(
                    patientName = "Lilavati Bai",
                    age = 62,
                    symptoms = "Mild fever and back pain over 3 days",
                    diagnosis = "Muscle fatigue with seasonal viral heat",
                    medsReminderJson = "Paracetamol (500mg) - Night | Ayush Kwath - 3 times daily"
                )
            )

            // Seed Tanks (Gramexa Jal)
            dao.insertTank(
                TankMonitoring(
                    tankName = "Main Village Tank (East)",
                    percentageLevel = 84,
                    healthStatus = "Clean - Chlorine Added",
                    isPumpRunning = false
                )
            )
            dao.insertTank(
                TankMonitoring(
                    tankName = "Irrigation Reservoir (North)",
                    percentageLevel = 42,
                    healthStatus = "Slight Silt - Filtration Active",
                    isPumpRunning = true
                )
            )

            // Seed Energy (Gramexa Energy)
            dao.insertEnergyGrid(
                EnergyGrid(
                    sectionName = "Panchayat Solar Array",
                    generationKW = 12.4f,
                    storageUsagePercentage = 78,
                    activeLights = 14
                )
            )
            dao.insertEnergyGrid(
                EnergyGrid(
                    sectionName = "Agricultural Microgrid",
                    generationKW = 24.8f,
                    storageUsagePercentage = 92,
                    activeLights = 8
                )
            )
        }
    }
}
