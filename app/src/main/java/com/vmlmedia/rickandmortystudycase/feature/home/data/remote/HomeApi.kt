package com.vmlmedia.rickandmortystudycase.feature.home.data.remote

import com.vmlmedia.rickandmortystudycase.feature.home.data.dto.response.GetCharactersDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface HomeApi {

    @GET(END_POINT_GET_CHARACTERS)
    suspend fun getCharacters(
        @Query("page") page: Int
    ): Response<GetCharactersDto>


    private companion object{
        const val END_POINT_GET_CHARACTERS = "/api/character/"
    }

}