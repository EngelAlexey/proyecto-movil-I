package Controller

import Entity.DTOPerson
import Entity.Person
import Util.ClockerAPIService
import android.content.Context
import android.util.Log
import com.example.clocker.R

class PersonController(private val context: Context) {

    suspend fun addPerson(person: Person) {
        try {
            val response = ClockerAPIService.api.createPerson(person.toDTO())
            if (!response.isSuccessful) {
                throw Exception(context.getString(R.string.ErrorMsgAdd))
            }
        } catch (e: Exception) {
            Log.e("API", R.string.ErrorMsgAdd.toString() + ": ${e.message}")
            throw e
        }
    }

    suspend fun updatePerson(person: Person) {
        try {
            val response = ClockerAPIService.api.updatePerson(person.toDTO())
            if (!response.isSuccessful) {
                throw Exception(context.getString(R.string.ErrorMsgUpdate))
            }
        } catch (e: Exception) {
            Log.e("API", R.string.ErrorMsgAdd.toString() + ": ${e.message}")
            throw e
        }
    }

    suspend fun removePerson(id: String) {
        try {
            val response = ClockerAPIService.api.deletePerson(id)
            if (!response.isSuccessful) {
                throw Exception(context.getString(R.string.ErrorMsgRemove))
            }
        } catch (e: Exception) {
            Log.e("API", R.string.ErrorMsgAdd.toString() + ": ${e.message}")
            throw e
        }
    }

    suspend fun getAllPeople(): List<Person> {
        return try {
            val response = ClockerAPIService.api.getAllPeople()
            if (response.isSuccessful) {
                response.body()?.map { it.toModel() } ?: emptyList()
            } else {
                throw Exception(context.getString(R.string.ErrorMsgGetAll))
            }
        } catch (e: Exception) {
            Log.e("API", R.string.ErrorMsgGetAll.toString() + ": ${e.message}")
            emptyList()
        }
    }

    suspend fun getByIdPerson(id: String): Person? {
        return try {
            val response = ClockerAPIService.api.getPersonById(id)
            if (response.isSuccessful) {
                response.body()?.toModel()
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e("API", R.string.ErrorMsgGetById.toString() + ": ${e.message}")
            null
        }
    }

    private fun Person.toDTO(): DTOPerson {
        return DTOPerson(
            id = this.ID,
            name = this.Name,
            fLastName = this.FLastName,
            sLastName = this.SLastName,
            nationality = this.Nationality,
            status = this.Status
        )
    }

    private fun DTOPerson.toModel(): Person {
        return Person(
            id = this.id,
            name = this.name,
            fLastName = this.fLastName,
            sLastName = this.sLastName,
            nationality = this.nationality,
            status = this.status
        )
    }
}