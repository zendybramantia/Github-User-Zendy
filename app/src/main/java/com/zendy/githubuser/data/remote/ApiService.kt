package com.zendy.githubuser.data.remote

import com.zendy.githubuser.BuildConfig
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @Headers("Authorization: token ${BuildConfig.API_KEY}")
    @GET("users")
    fun getUsers(

    ): Call<List<UserDetailResponse>>

    @Headers("Authorization: token ${BuildConfig.API_KEY}")
    @GET("search/users")
    fun getUserSearch(
        @Query("q") query: String
    ): Call<UserSearchResponse>

    @Headers("Authorization: token ${BuildConfig.API_KEY}")
    @GET("users/{username}")
    fun getUserDetail(
        @Path("username") username: String
    ): Call<UserDetailResponse>

    @Headers("Authorization: token ${BuildConfig.API_KEY}")
    @GET("users/{username}/followers")
    fun getListFollowers(
        @Path("username") username: String
    ): Call<List<UserDetailResponse>>

    @Headers("Authorization: token ${BuildConfig.API_KEY}")
    @GET("users/{username}/following")
    fun getListFollowing(
        @Path("username") username: String
    ): Call<List<UserDetailResponse>>

}