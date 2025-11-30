package Entity

import java.time.LocalDate
import java.time.LocalDateTime

class Attendances {
    var idAttendance: String = ""
    var idPerson: String = ""
    var DateAttendance: LocalDate
    var timeEntry: LocalDateTime
    var timeExit: LocalDateTime
    var entryID: String = ""
    var exitID: String = ""

    constructor(
        IDAttendance: String,
        dateAttendance: LocalDate,
        IDPerson: String,
        TimeEntry: LocalDateTime,
        TimeExit: LocalDateTime,
        EntryID: String,
        ExitID: String
    ) {
        this.idAttendance = IDAttendance
        this.DateAttendance = dateAttendance
        this.idPerson = IDPerson
        this.timeEntry = TimeEntry
        this.timeExit = TimeExit
        this.entryID = EntryID
        this.exitID = ExitID
    }
}