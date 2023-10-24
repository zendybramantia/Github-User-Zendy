package com.zendy.githubuser.ui.activity

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.zendy.githubuser.ui.viewModel.MainViewModel
import com.zendy.githubuser.R
import com.zendy.githubuser.data.remote.UserDetailResponse
import com.zendy.githubuser.databinding.ActivityMainBinding
import com.zendy.githubuser.ui.adapter.ListUserAdapter

class MainActivity : AppCompatActivity() {

    private val mainViewModel by viewModels<MainViewModel>()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        title = "List User"

        binding.rvUser.layoutManager = LinearLayoutManager(this)
        binding.rvUser.setHasFixedSize(true)

        subscribe()
    }

    private fun subscribe() {
        mainViewModel.listUser.observe(this) {
            if (it != null) {
                showRecyclerList(it)
            }
        }

        mainViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        mainViewModel.isNotFound.observe(this) {
            showNotFound(it)
        }
    }

    private fun showRecyclerList(listUsers: List<UserDetailResponse>) {

        val listUserAdapter = ListUserAdapter(listUsers)
        binding.rvUser.adapter = listUserAdapter

        listUserAdapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: String) {
                val intent = Intent(this@MainActivity, DetailActivity::class.java)
                intent.putExtra(DetailActivity.EXTRA_USERNAME, data)
                startActivity(intent)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String): Boolean {
                mainViewModel.searchUsers(query)
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.fav_menu -> {
                val moveIntent = Intent(this@MainActivity, FavouriteActivity::class.java)
                startActivity(moveIntent)
                true
            }
            R.id.setting_menu -> {
                val moveIntent = Intent(this@MainActivity, SettingActivity::class.java)
                startActivity(moveIntent)
                true
            }
            else -> true
        }
    }

    private fun showNotFound(isNotfound: Boolean) {
        if (isNotfound) {
            binding.tvNotFound.visibility = View.VISIBLE
        } else {
            binding.tvNotFound.visibility = View.GONE
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}