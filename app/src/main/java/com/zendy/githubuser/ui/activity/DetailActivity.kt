package com.zendy.githubuser.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import com.zendy.githubuser.ui.viewModel.DetailViewModel
import com.zendy.githubuser.R
import com.zendy.githubuser.data.local.FavUser
import com.zendy.githubuser.ui.adapter.SectionsPagerAdapter
import com.zendy.githubuser.data.remote.UserDetailResponse
import com.zendy.githubuser.ui.viewModel.ViewModelFactory
import com.zendy.githubuser.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {
    private lateinit var detailViewModel: DetailViewModel
    private lateinit var binding: ActivityDetailBinding

    private var isFavUser: Boolean? = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        title = getString(R.string.title_detail)
        detailViewModel = obtainViewModel(this@DetailActivity)

        val username = intent.getStringExtra(EXTRA_USERNAME)
        if (username != null) {
            detailViewModel.findUserDetail(username)
            isFavUser = detailViewModel.isFav(username).value
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_new_24)

        subscribe(username)
        loadTabLayout()

        binding.fabFav.setOnClickListener {
            val userDetail = detailViewModel.userDetail.value
            val favUser = FavUser(
                userDetail?.login ?: "",
                userDetail?.avatarUrl,
                userDetail?.followersUrl,
                userDetail?.followingUrl,
                userDetail?.name,
                userDetail?.company,
                userDetail?.location,
                userDetail?.publicRepos,
                userDetail?.followers,
                userDetail?.following
            )
            if (it.id == R.id.fab_fav) {
                if (isFavUser == true) {
                    showAlertDialog(TYPE_DELETE, favUser)
                } else if (isFavUser == false) {
                    showAlertDialog(TYPE_ADD, favUser)
                }
            }
        }

    }

    private fun showAlertDialog(type: Int, favUser: FavUser) {
        val isDialogAdd = type == TYPE_ADD
        val dialogTitle: String
        val dialogMessage: String
        if (isDialogAdd) {
            dialogTitle = getString(R.string.add)
            dialogMessage = getString(R.string.message_add)
        } else {
            dialogMessage = getString(R.string.message_remove)
            dialogTitle = getString(R.string.delete)
        }
        val alertDialogBuilder = AlertDialog.Builder(this)
        with(alertDialogBuilder) {
            setTitle(dialogTitle)
            setMessage(dialogMessage)
            setCancelable(false)
            setPositiveButton(getString(R.string.yes)) { _, _ ->
                if (isDialogAdd) {
                    detailViewModel.insert(favUser)
                }
                if (!isDialogAdd) {
                    detailViewModel.delete(favUser)
                }
            }
            setNegativeButton(getString(R.string.no)) { dialog, _ -> dialog.cancel() }
        }
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    private fun loadTabLayout() {
        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        binding.viewPager.adapter = sectionsPagerAdapter
        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
        supportActionBar?.elevation = 0f
    }

    private fun subscribe(username: String?) {
        detailViewModel.userDetail.observe(this) {
            if (it != null) {
                init(it)
            }
        }

        detailViewModel.isLoadingDetail.observe(this) {
            showLoadingDetail(it)
        }

        detailViewModel.isLoadingFollow.observe(this) {
            showLoadingFollow(it)
        }

        if (username != null) {
            detailViewModel.isFav(username).observe(this) {
                isFavUser = if (it) {
                    binding.fabFav.setImageResource(R.drawable.ic_baseline_favorite_24)
                    it
                } else {
                    binding.fabFav.setImageResource(R.drawable.ic_baseline_favorite_border_24)
                    it
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu_detail, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.fav_menu -> {
                val moveIntent = Intent(this@DetailActivity, FavouriteActivity::class.java)
                startActivity(moveIntent)
                return true
            }
            R.id.setting_menu -> {
                val moveIntent = Intent(this@DetailActivity, SettingActivity::class.java)
                startActivity(moveIntent)
                return true
            }
            android.R.id.home -> {
                finish()
                return true
            }

            else -> return true
        }
    }


    private fun init(user: UserDetailResponse) {
        Glide.with(this)
            .load(user.avatarUrl)
            .into(binding.imgAvatar)
        binding.apply {
            tvUsername.text = user.login
            tvName.text = user.name
            tvLocation.text = user.location
            tvRepositories.text = user.publicRepos.toString()
            tvCompany.text = user.company
            tvFollowers.text = user.followers.toString()
            tvFollowing.text = user.following.toString()
        }
    }

    private fun showLoadingDetail(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBarDetail.visibility = View.VISIBLE
        } else {
            binding.fabFav.visibility = View.VISIBLE
            binding.progressBarDetail.visibility = View.GONE
        }
    }

    private fun showLoadingFollow(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBarFollow.visibility = View.VISIBLE
        } else {
            binding.progressBarFollow.visibility = View.GONE
        }
    }

    private fun obtainViewModel(activity: AppCompatActivity): DetailViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[DetailViewModel::class.java]
    }

    companion object {
        const val EXTRA_USERNAME = "extra_username"
        const val TYPE_DELETE = 1
        const val TYPE_ADD = 2

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_followers,
            R.string.tab_following
        )
    }
}