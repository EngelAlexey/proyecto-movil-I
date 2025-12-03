package Entity

import com.google.gson.annotations.SerializedName

data class DTOClock(
    @SerializedName("idClock") val idClock: String? = null,
    @SerializedName("idPerson") val idPerson: String,
    @SerializedName("dateClock") val dateClock: String,
    @SerializedName("type") val type: String,
    @SerializedName("address") val address: String,
    @SerializedName("latitude") val latitude: Int,
    @SerializedName("longitude") val longitude: Int,
    @SerializedName("photo") val photo: String
)