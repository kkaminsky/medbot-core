package com.kkaminsky.medbotcore.controller

import com.kkaminsky.medbotcore.dto.UserPlatformInfoDto
import com.kkaminsky.medbotcore.dto.ampq.SyncAccountDto
import com.kkaminsky.medbotcore.service.HealthReportService
import com.kkaminsky.medbotcore.service.HealthReportTextService
import com.kkaminsky.medbotcore.service.PatientService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*


@RestController
@RequestMapping("/api/v1/bot")
class BotController(
        private val patientService: PatientService,
        private val healthReportService: HealthReportService,
        private val healthReportTextService: HealthReportTextService
) {

    @PostMapping(value = ["/user/sync-code"] )
    fun getSyncCode(@RequestBody payload: UserPlatformInfoDto): String {
        return patientService.generateSyncCode(payload)
    }

    @PostMapping(value = ["/bind"])
    fun bindPlatform(@RequestBody payload: SyncAccountDto): UUID {
        return patientService.bindPlatform(syncDto = payload)
    }

    @PostMapping(value = ["/save/report"])
    fun saveReportFromUser(@RequestBody payload: ReceiptIdDto): UUID{
        return healthReportService.createReport(receiptId = payload.receiptId)
    }

    @PostMapping("/save/report/text")
    fun saveTextReport(@RequestBody payload: TextReportDto): UUID{
        return healthReportTextService.createHealthReportText(payload.text)
    }

    data class ReceiptIdDto(
            val receiptId: UUID
    )

    data class TextReportDto(
            val text: String
    )
}