package Entity

import com.google.gson.annotations.SerializedName

data class DTOPerson(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("fLastName") val fLastName: String,
    @SerializedName("sLastName") val sLastName: String,
    @SerializedName("nationality") val nationality: String,
    @SerializedName("status") val status: Boolean
)