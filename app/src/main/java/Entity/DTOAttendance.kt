package Entity

import com.google.gson.annotations.SerializedName

data class DTOAttendance(
    @SerializedName("idAttendance") val idAttendance: String? = null,
    @SerializedName("idPerson") val idPerson: String,
    @SerializedName("dateAttendance") val dateAttendance: String,
    @SerializedName("timeEntry") val timeEntry: String,
    @SerializedName("timeExit") val timeExit: String,
    @SerializedName("entryID") val entryID: String,
    @SerializedName("exitID") val exitID: String
)