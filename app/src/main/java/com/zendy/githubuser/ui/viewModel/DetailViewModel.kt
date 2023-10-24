package com.zendy.githubuser.ui.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zendy.githubuser.data.local.FavUser
import com.zendy.githubuser.data.remote.ApiConfig
import com.zendy.githubuser.data.local.FavUserRepository
import com.zendy.githubuser.data.remote.UserDetailResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel(application: Application) : ViewModel() {

    private val _userDetail = MutableLiveData<UserDetailResponse?>()
    val userDetail: MutableLiveData<UserDetailResponse?> = _userDetail

    private val _listFollowers = MutableLiveData<List<UserDetailResponse>?>()
    val listFollowers: MutableLiveData<List<UserDetailResponse>?> = _listFollowers

    private val _listFollowing = MutableLiveData<List<UserDetailResponse>?>()
    val listFollowing: MutableLiveData<List<UserDetailResponse>?> = _listFollowing

    private val _isLoadingDetail = MutableLiveData<Boolean>()
    val isLoadingDetail: LiveData<Boolean> = _isLoadingDetail

    private val _isLoadingFollow = MutableLiveData<Boolean>()
    val isLoadingFollow: LiveData<Boolean> = _isLoadingFollow

    private val favUserRepository: FavUserRepository = FavUserRepository(application)

    fun isFav(login: String): LiveData<Boolean> = favUserRepository.isFav(login)

    fun insert(favUser: FavUser) {
        favUserRepository.insert(favUser)
    }

    fun delete(favUser: FavUser) {
        favUserRepository.delete(favUser)
    }

    fun findUserDetail(username: String) {
        _isLoadingFollow.value = true
        _isLoadingDetail.value = true
        val client = ApiConfig.getApiService().getUserDetail(username)
        client.enqueue(object : Callback<UserDetailResponse> {
            override fun onResponse(
                call: Call<UserDetailResponse>,
                response: Response<UserDetailResponse>
            ) {
                _isLoadingDetail.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _userDetail.value = responseBody
                        getListFollowers(responseBody.login)
                        getListFollowing(responseBody.login)
                    }

                }
            }

            override fun onFailure(call: Call<UserDetailResponse>, t: Throwable) {
                _isLoadingDetail.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }

        })
    }

    fun getListFollowers(username: String) {
        val client = ApiConfig.getApiService().getListFollowers(username)
        client.enqueue(object : Callback<List<UserDetailResponse>> {
            override fun onResponse(
                call: Call<List<UserDetailResponse>>,
                response: Response<List<UserDetailResponse>>
            ) {
                _isLoadingFollow.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _listFollowers.value = responseBody
                    }
                }
            }

            override fun onFailure(call: Call<List<UserDetailResponse>>, t: Throwable) {
                _isLoadingFollow.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    fun getListFollowing(username: String) {
        val client = ApiConfig.getApiService().getListFollowing(username)
        client.enqueue(object : Callback<List<UserDetailResponse>> {
            override fun onResponse(
                call: Call<List<UserDetailResponse>>,
                response: Response<List<UserDetailResponse>>
            ) {
                _isLoadingFollow.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _listFollowing.value = responseBody
                    }
                }
            }

            override fun onFailure(call: Call<List<UserDetailResponse>>, t: Throwable) {
                _isLoadingFollow.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }

        })
    }

    companion object {
        private const val TAG = "DetailViewModel"
    }
}