package com.zendy.githubuser.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.zendy.githubuser.R
import com.zendy.githubuser.data.local.FavUser
import com.zendy.githubuser.databinding.ActivityFavouriteBinding
import com.zendy.githubuser.ui.adapter.ListFavAdapter
import com.zendy.githubuser.ui.viewModel.FavViewModel
import com.zendy.githubuser.ui.viewModel.ViewModelFactory

class FavouriteActivity : AppCompatActivity() {

    private lateinit var favViewModel: FavViewModel
    private lateinit var binding: ActivityFavouriteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavouriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        favViewModel = obtainViewModel(this@FavouriteActivity)
        title = getString(R.string.favourite)
        binding.rvFav.layoutManager = LinearLayoutManager(this)
        binding.rvFav.setHasFixedSize(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_new_24)

        subscribe()
    }

    private fun subscribe() {
        favViewModel.getAllFavUser().observe(this) {
            showRecyclerList(it)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> true
        }
    }

    private fun showRecyclerList(listUsers: List<FavUser>) {

        val listFavAdapter = ListFavAdapter(listUsers)
        binding.rvFav.adapter = listFavAdapter

        listFavAdapter.setOnItemClickCallback(object : ListFavAdapter.OnItemClickCallback {
            override fun onItemClicked(data: String) {
                val intent = Intent(this@FavouriteActivity, DetailActivity::class.java)
                intent.putExtra(DetailActivity.EXTRA_USERNAME, data)
                startActivity(intent)
            }
        })
    }

    private fun obtainViewModel(activity: AppCompatActivity): FavViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[FavViewModel::class.java]
    }
}