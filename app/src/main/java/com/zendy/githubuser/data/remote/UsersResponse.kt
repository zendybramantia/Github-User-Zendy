package com.zendy.githubuser.data.remote

import com.google.gson.annotations.SerializedName

data class UserSearchResponse(

    @field:SerializedName("total_count")
    val totalCount: Int,

    @field:SerializedName("items")
    val items: List<UserDetailResponse>

)

data class UserDetailResponse(

    @field:SerializedName("login")
    val login: String,

    @field:SerializedName("avatar_url")
    val avatarUrl: String,

    @field:SerializedName("followers_url")
    val followersUrl: String,

    @field:SerializedName("following_url")
    val followingUrl: String,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("company")
    val company: String,

    @field:SerializedName("location")
    val location: String,

    @field:SerializedName("public_repos")
    val publicRepos: Int,

    @field:SerializedName("followers")
    val followers: Int,

    @field:SerializedName("following")
    val following: Int,
)


