package com.zendy.githubuser.data.local

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface FavUserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(favUser: FavUser)

    @Delete
    fun delete(favUser: FavUser)

    @Query("SELECT * from fav_user")
    fun getAllFavUser(): LiveData<List<FavUser>>

    @Query("SELECT EXISTS(SELECT * FROM fav_user WHERE login = :login)")
    fun isFav(login : String): LiveData<Boolean>
}