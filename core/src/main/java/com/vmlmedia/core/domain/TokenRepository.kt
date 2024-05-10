package com.vmlmedia.core.domain

interface TokenRepository {

    fun getToken() : String?
    fun deleteToken()
    fun saveNewToken(token : String)
    fun getNewToken() : String?
    fun deleteNewToken()
    fun updateToken(token : String)
    suspend fun refreshToken(refreshToken : String) : String
}