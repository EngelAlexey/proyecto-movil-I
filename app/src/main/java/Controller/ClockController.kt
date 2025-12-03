package Controller

import Entity.Attendances
import Entity.Clock
import Entity.DTOClock
import Util.ClockerAPIService
import Util.Util
import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import com.example.clocker.R
import java.time.LocalDate
import java.time.LocalDateTime

class ClockController(private val context: Context) {

    suspend fun addClock(clock: Clock) {
        try {
            val todayClocks = getClocksByPerson(clock.IDPerson).filter {
                it.DateClock.toLocalDate().isEqual(clock.DateClock.toLocalDate())
            }

            if (todayClocks.size >= 2) {
                throw Exception(context.getString(R.string.MsgDuplicateDate))
            }

            val isEntry = todayClocks.isEmpty()
            clock.Type = if (isEntry) Clock.TYPE_ENTRY else Clock.TYPE_DEPARTURE

            val response = ClockerAPIService.api.createClock(clock.toDTO())
            if (!response.isSuccessful) {
                throw Exception(context.getString(R.string.ErrorMsgAdd))
            }

            manageAutomaticAttendance(clock, isEntry)

        } catch (e: Exception) {
            Log.e("API", "${context.getString(R.string.ErrorMsgAdd)}: ${e.message}")
            throw e
        }
    }

    suspend fun removeClock(id: String) {
        try {
            val clockToDelete = getByIdClock(id) ?: return

            val response = ClockerAPIService.api.deleteClock(id)
            if (!response.isSuccessful) {
                throw Exception(context.getString(R.string.ErrorMsgRemove))
            }

            val attendanceController = AttendanceController(context)
            val attendance = attendanceController.getAttendanceByDate(
                clockToDelete.IDPerson,
                clockToDelete.DateClock.toLocalDate()
            )

            if (attendance != null) {
                if (clockToDelete.Type == Clock.TYPE_ENTRY) {
                    attendanceController.deleteAttendance(attendance.idAttendance)
                } else if (clockToDelete.Type == Clock.TYPE_DEPARTURE) {
                    attendance.timeExit = attendance.timeEntry
                    attendanceController.updateAttendance(attendance)
                }
            }
        } catch (e: Exception) {
            Log.e("API", "${context.getString(R.string.ErrorMsgRemove)}: ${e.message}")
            throw e
        }
    }

    suspend fun getAllClocks(): List<Clock> {
        return try {
            val response = ClockerAPIService.api.getAllClocks()
            response.body()?.map { it.toModel() } ?: emptyList()
        } catch (e: Exception) { emptyList() }
    }

    suspend fun getByIdClock(id: String): Clock? {
        return try {
            val response = ClockerAPIService.api.getClockById(id)
            response.body()?.toModel()
        } catch (e: Exception) { null }
    }

    suspend fun getClocksByPerson(personId: String): List<Clock> {
        return try {
            val response = ClockerAPIService.api.getClocksByPerson(personId)
            response.body()?.map { it.toModel() } ?: emptyList()
        } catch (e: Exception) { emptyList() }
    }

    private suspend fun manageAutomaticAttendance(clock: Clock, isEntry: Boolean) {
        val attendanceController = AttendanceController(context)

        if (isEntry) {
            val emptyDate = LocalDateTime.of(2000, 1, 1, 0, 0)
            val newAttendance = Attendances(
                IDAttendance = "",
                dateAttendance = clock.DateClock.toLocalDate(),
                IDPerson = clock.IDPerson,
                TimeEntry = clock.DateClock,
                TimeExit = emptyDate,
                EntryID = "",
                ExitID = ""
            )
            attendanceController.addAttendance(newAttendance)
        } else {
            val existingAttendance = attendanceController.getAttendanceByDate(
                clock.IDPerson,
                clock.DateClock.toLocalDate()
            )

            if (existingAttendance != null) {
                existingAttendance.timeExit = clock.DateClock
                attendanceController.updateAttendance(existingAttendance)
            }
        }
    }

    private fun Clock.toDTO(): DTOClock {
        val photoString = if (this.Photo != null) Util.toBase64(this.Photo!!) else ""
        return DTOClock(
            idClock = if (this.IDClock.isNotEmpty()) this.IDClock else null,
            idPerson = this.IDPerson,
            dateClock = Util.formatDateTime(this.DateClock),
            type = this.Type,
            address = this.Address,
            latitude = this.Latitude,
            longitude = this.Longitude,
            photo = photoString
        )
    }

    private fun DTOClock.toModel(): Clock {
        val date = Util.parseStringToDateTime(this.dateClock)
        val bitmap = if (this.photo.isNotEmpty()) Util.toBitmap(this.photo) else null
        val finalBitmap = bitmap ?: Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)

        return Clock(
            idClock = this.idClock ?: "",
            idPerson = this.idPerson,
            dateClock = date,
            type = this.type,
            address = this.address,
            latitude = this.latitude,
            longitude = this.longitude,
            photo = finalBitmap
        )
    }
}