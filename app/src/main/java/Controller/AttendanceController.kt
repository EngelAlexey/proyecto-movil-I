package Controller

import Entity.Attendances
import Entity.DTOAttendance
import Util.ClockerAPIService
import Util.Util
import android.content.Context
import android.util.Log
import com.example.clocker.R
import java.time.LocalDate

class AttendanceController(private val context: Context) {

    suspend fun addAttendance(attendance: Attendances) { /* ... */
        try {
            val response = ClockerAPIService.api.createAttendance(attendance.toDTO())
            if (!response.isSuccessful) throw Exception(context.getString(R.string.ErrorMsgAdd))
        } catch (e: Exception) { throw e }
    }
    suspend fun updateAttendance(attendance: Attendances) { /* ... */
        try {
            val response = ClockerAPIService.api.updateAttendance(attendance.toDTO())
            if (!response.isSuccessful) throw Exception(context.getString(R.string.ErrorMsgUpdate))
        } catch (e: Exception) { throw e }
    }
    suspend fun deleteAttendance(id: String) { /* ... */
        try {
            val response = ClockerAPIService.api.deleteAttendance(id)
            if (!response.isSuccessful) throw Exception(context.getString(R.string.ErrorMsgRemove))
        } catch (e: Exception) { throw e }
    }
    suspend fun getAllAttendances(): List<Attendances> {
        return try {
            val response = ClockerAPIService.api.getAllAttendances()
            response.body()?.map { it.toModel() } ?: emptyList()
        } catch (e: Exception) { emptyList() }
    }

    suspend fun getAttendanceByDate(personId: String, date: LocalDate): Attendances? {
        val allAttendances = getAllAttendances()
        return allAttendances.find {
            it.idPerson == personId && it.DateAttendance.isEqual(date)
        }
    }

    private fun Attendances.toDTO(): DTOAttendance {
        return DTOAttendance(
            idAttendance = if (this.idAttendance.isNotEmpty()) this.idAttendance else null,
            idPerson = this.idPerson,
            dateAttendance = Util.formatDate(this.DateAttendance),
            timeEntry = Util.formatDateTime(this.timeEntry),
            timeExit = Util.formatDateTime(this.timeExit),
            entryID = this.entryID,
            exitID = this.exitID
        )
    }

    private fun DTOAttendance.toModel(): Attendances {
        val date = Util.parseStringToDate(this.dateAttendance)
        val tEntry = Util.parseStringToDateTime(this.timeEntry)
        val tExit = Util.parseStringToDateTime(this.timeExit)

        return Attendances(
            IDAttendance = this.idAttendance ?: "",
            dateAttendance = date,
            IDPerson = this.idPerson,
            TimeEntry = tEntry,
            TimeExit = tExit,
            EntryID = this.entryID,
            ExitID = this.exitID
        )
    }
}