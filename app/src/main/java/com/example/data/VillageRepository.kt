package com.example.data

import kotlinx.coroutines.flow.Flow

class VillageRepository(private val dao: VillageDao) {
    val allComplaints: Flow<List<Complaint>> = dao.getAllComplaints()
    val allKrishiRecords: Flow<List<KrishiRecord>> = dao.getAllKrishiRecords()
    val allHealthRecords: Flow<List<HealthRecord>> = dao.getAllHealthRecords()
    val allTanks: Flow<List<TankMonitoring>> = dao.getAllTanks()
    val allEnergyGrids: Flow<List<EnergyGrid>> = dao.getAllEnergyGrids()

    suspend fun insertComplaint(complaint: Complaint): Long {
        return dao.insertComplaint(complaint)
    }

    suspend fun updateComplaintStatus(id: Int, status: String, comment: String) {
        dao.updateComplaintStatus(id, status, comment)
    }

    suspend fun insertKrishiRecord(record: KrishiRecord) {
        dao.insertKrishiRecord(record)
    }

    suspend fun insertHealthRecord(record: HealthRecord) {
        dao.insertHealthRecord(record)
    }

    suspend fun insertTank(tank: TankMonitoring) {
        dao.insertTank(tank)
    }

    suspend fun insertEnergyGrid(energy: EnergyGrid) {
        dao.insertEnergyGrid(energy)
    }
}
