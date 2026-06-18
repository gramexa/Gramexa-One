package com.example.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface VillageDao {
    @Query("SELECT * FROM complaints ORDER BY timestamp DESC")
    fun getAllComplaints(): Flow<List<Complaint>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertComplaint(complaint: Complaint): Long

    @Query("UPDATE complaints SET status = :status, comment = :comment WHERE id = :id")
    suspend fun updateComplaintStatus(id: Int, status: String, comment: String)

    @Query("SELECT * FROM krishi_record ORDER BY lastUpdate DESC")
    fun getAllKrishiRecords(): Flow<List<KrishiRecord>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertKrishiRecord(record: KrishiRecord)

    @Query("SELECT * FROM health_record ORDER BY timestamp DESC")
    fun getAllHealthRecords(): Flow<List<HealthRecord>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHealthRecord(record: HealthRecord)

    @Query("SELECT * FROM tank_monitoring ORDER BY tankName ASC")
    fun getAllTanks(): Flow<List<TankMonitoring>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTank(tank: TankMonitoring)

    @Query("SELECT * FROM energy_grid ORDER BY sectionName ASC")
    fun getAllEnergyGrids(): Flow<List<EnergyGrid>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEnergyGrid(energy: EnergyGrid)
}
