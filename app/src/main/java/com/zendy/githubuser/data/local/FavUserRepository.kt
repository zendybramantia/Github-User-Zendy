package com.zendy.githubuser.data.local

import android.app.Application
import androidx.lifecycle.LiveData
import com.zendy.githubuser.data.local.FavUser
import com.zendy.githubuser.data.local.FavUserDao
import com.zendy.githubuser.data.local.FavUserDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FavUserRepository(application: Application) {
    private val favUserDao: FavUserDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = FavUserDatabase.getDatabase(application)
        favUserDao = db.favUserDao()
    }

    fun getAllFavUser(): LiveData<List<FavUser>> = favUserDao.getAllFavUser()
    fun insert(favUser: FavUser) {
        executorService.execute { favUserDao.insert(favUser) }
    }

    fun delete(favUser: FavUser) {
        executorService.execute { favUserDao.delete(favUser) }
    }

    fun isFav(login: String) : LiveData<Boolean> = favUserDao.isFav(login)
}