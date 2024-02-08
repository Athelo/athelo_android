package com.athelohealth.mobile.network.api

import com.athelohealth.mobile.network.dto.appointment.AppointmentsDto
import com.athelohealth.mobile.network.dto.appointment.BookAppointmentDto
import com.athelohealth.mobile.network.dto.appointment.ProvidersAvailabilityDto
import com.athelohealth.mobile.network.dto.appointment.ProvidersDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

interface AppointmentApi {

    @GET
    suspend fun getAppointments(
        @Url url: String = "api/v1/appointments/"
    ): AppointmentsDto

    @GET
    suspend fun loadProviders(
        @Url url: String = "api/v1/providers"
    ): ProvidersDto

    @GET("api/v1/providers/1/availability/")
    suspend fun getProvidersAvailability(
        @Query(value = "date") date: String,
        @Query("tz") timeZone: String
    ): ProvidersAvailabilityDto

    @POST
    suspend fun bookAppointment(
        @Url url: String = "api/v1/appointments/",
        @Body body: BookAppointmentDto
    ): AppointmentsDto

    @DELETE("api/v1/appointment/{id}/")
    suspend fun deleteAppointment(
        @Path("id") id: String
    ): Response<Unit>
}