package com.zendy.githubuser.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "fav_user")
data class FavUser(
    @field:ColumnInfo(name = "login")
    @field:PrimaryKey
    val login: String,

    @field:ColumnInfo(name = "avatar_url")
    val avatarUrl: String? = null,

    @field:ColumnInfo(name = "followers_url")
    val followersUrl: String? = null,

    @field:ColumnInfo(name = "following_url")
    val following_url: String? = null,

    @field:ColumnInfo(name = "name")
    val name: String? = null,

    @field:ColumnInfo(name = "company")
    val company: String? = null,

    @field:ColumnInfo(name = "location")
    val location: String? = null,

    @field:ColumnInfo(name = "public_repos")
    val publicRepos: Int? = null,

    @field:ColumnInfo(name = "followers")
    val followers: Int? = null,

    @field:ColumnInfo(name = "following")
    val following: Int? = null
)