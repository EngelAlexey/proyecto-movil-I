package Entity

import java.util.Date

class Attendances {

    private var IDAttendance:       String = ""
    private var dateAttendance:     Date = Date()
    private var IDPerson:           String = ""
    private var TimeEntry:          Date = Date()
    private var TimeExit:           Date = Date()
    private var EntryID:            String = ""
    private var ExitID:             String = ""

    constructor(
        IDAttendance: String,
        dateAttendance: Date,
        IDPerson: String,
        TimeEntry: Date,
        TimeExit: Date,
        EntryID: String,
        ExitID: String)

    {
        this.IDAttendance = IDAttendance
        this.dateAttendance = dateAttendance
        this.IDPerson = IDPerson
        this.TimeEntry = TimeEntry
        this.TimeExit = TimeExit
        this.EntryID = EntryID
        this.ExitID = ExitID
    }

    var idAttendance: String
        get() = this.IDAttendance
        set(value) { this.IDAttendance = value }

    var DateAttendance: Date
        get() = this.dateAttendance
        set(value) { this.dateAttendance = value }

    var idPerson: String
        get() = this.IDPerson
        set(value) { this.IDPerson = value }

    var timeEntry: Date
        get() = this.TimeEntry
        set(value) { this.TimeEntry = value }

    var timeExit: Date
        get() = this.TimeExit
        set(value) { this.TimeExit = value }

    var entryID: String
        get() = this.EntryID
        set(value) { this.EntryID = value }

    var exitID: String
        get() = this.ExitID
        set(value) { this.ExitID = value }

    fun HoursAttendance(): Long {
        return this.TimeExit.time - this.TimeEntry.time
    }

}