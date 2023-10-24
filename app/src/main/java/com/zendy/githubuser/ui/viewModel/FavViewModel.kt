package com.zendy.githubuser.ui.viewModel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.zendy.githubuser.data.local.FavUser
import com.zendy.githubuser.data.local.FavUserRepository

class FavViewModel(application: Application) : ViewModel() {
    private val favUserRepository: FavUserRepository = FavUserRepository(application)

    fun getAllFavUser(): LiveData<List<FavUser>> = favUserRepository.getAllFavUser()
}