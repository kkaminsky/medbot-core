package com.kkaminsky.medbotcore.service

import com.kkaminsky.medbotcore.controller.DoctorController
import java.util.*

interface HealthReportService {

    fun createReportTasks(treatmentId: UUID)
    fun createReport(receiptId : UUID): UUID
    fun getReportsByTreatment(treatmentId: UUID): List<DoctorController.HealthReportViewDto>
}