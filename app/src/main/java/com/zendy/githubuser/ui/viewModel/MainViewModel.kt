package com.zendy.githubuser.ui.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.zendy.githubuser.data.remote.ApiConfig
import com.zendy.githubuser.data.remote.UserDetailResponse
import com.zendy.githubuser.data.remote.UserSearchResponse
import com.zendy.githubuser.ui.preferences.SettingPreferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel() : ViewModel() {

    private var _listUser = MutableLiveData<List<UserDetailResponse>?>()
    val listUser: MutableLiveData<List<UserDetailResponse>?> = _listUser

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isNotFound = MutableLiveData<Boolean>()
    val isNotFound: LiveData<Boolean> = _isNotFound

    init {
        findUsers()
    }

    private fun findUsers() {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUsers()
        client.enqueue(object : Callback<List<UserDetailResponse>> {
            override fun onResponse(
                call: Call<List<UserDetailResponse>>,
                response: Response<List<UserDetailResponse>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _listUser.value = responseBody
                    } else {
                        Log.e(TAG, "onFailure: ${response.message()}")
                    }
                }
            }

            override fun onFailure(call: Call<List<UserDetailResponse>>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    fun searchUsers(key: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUserSearch(key)
        client.enqueue(object : Callback<UserSearchResponse> {
            override fun onResponse(
                call: Call<UserSearchResponse>,
                response: Response<UserSearchResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _isNotFound.value = responseBody.totalCount == 0
                        _listUser.value = responseBody.items
                    } else {

                        Log.e(TAG, "onFailure: ${response.message()}")
                    }
                }
            }

            override fun onFailure(call: Call<UserSearchResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }

        })
    }

    companion object {
        private const val TAG = "MainViewModel"
    }
}
