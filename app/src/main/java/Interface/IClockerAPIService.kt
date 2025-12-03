package Interface

import Entity.DTOAttendance
import Entity.DTOClock
import Entity.DTOPerson
import retrofit2.Response
import retrofit2.http.*

interface IClockerAPIService {
    @GET("/api/people")
    suspend fun getAllPeople(): Response<List<DTOPerson>>

    @GET("/api/people/{id}")
    suspend fun getPersonById(@Path("id") id: String): Response<DTOPerson>

    @POST("/api/people")
    suspend fun createPerson(@Body person: DTOPerson): Response<Void>

    @PUT("/api/people")
    suspend fun updatePerson(@Body person: DTOPerson): Response<Void>

    @DELETE("/api/people/{id}")
    suspend fun deletePerson(@Path("id") id: String): Response<Void>

    @GET("/api/clocks")
    suspend fun getAllClocks(): Response<List<DTOClock>>

    @POST("/api/clocks")
    suspend fun createClock(@Body clock: DTOClock): Response<Void>

    @PUT("/api/clocks")
    suspend fun updateClock(@Body clock: DTOClock): Response<Void>

    @DELETE("/api/clocks/{id}")
    suspend fun deleteClock(@Path("id") id: String): Response<Void>

    @GET("/api/clocks/{id}")
    suspend fun getClockById(@Path("id") id: String): Response<DTOClock>

    @GET("/api/clocks/person/{idPerson}")
    suspend fun getClocksByPerson(@Path("idPerson") idPerson: String): Response<List<DTOClock>>

    @GET("/api/clocks/search/date")
    suspend fun getClocksByDate(@Query("date") date: String): Response<List<DTOClock>>

    @GET("/api/clocks/search/type")
    suspend fun getClocksByType(@Query("type") type: String): Response<List<DTOClock>>

    @GET("/api/attendances")
    suspend fun getAllAttendances(): Response<List<DTOAttendance>>

    @GET("/api/attendances/{id}")
    suspend fun getAttendanceById(@Path("id") id: String): Response<DTOAttendance>

    @POST("/api/attendances")
    suspend fun createAttendance(@Body attendance: DTOAttendance): Response<Void>

    @PUT("/api/attendances")
    suspend fun updateAttendance(@Body attendance: DTOAttendance): Response<Void>

    @DELETE("/api/attendances/{id}")
    suspend fun deleteAttendance(@Path("id") id: String): Response<Void>

    @GET("/api/attendances/person/{idPerson}")
    suspend fun getAttendancesByPerson(@Path("idPerson") idPerson: String): Response<List<DTOAttendance>>

    @GET("/api/attendances/search/date")
    suspend fun getAttendancesByDate(@Query("date") date: String): Response<List<DTOAttendance>>
}