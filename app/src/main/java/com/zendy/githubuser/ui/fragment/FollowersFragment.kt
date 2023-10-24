package com.zendy.githubuser.ui.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.zendy.githubuser.ui.viewModel.DetailViewModel
import com.zendy.githubuser.ui.adapter.ListUserAdapter
import com.zendy.githubuser.data.remote.UserDetailResponse
import com.zendy.githubuser.ui.activity.DetailActivity
import com.zendy.githubuser.databinding.FragmentFollowersBinding

class FollowersFragment : Fragment() {

    private val model: DetailViewModel by activityViewModels()

    private var _binding: FragmentFollowersBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFollowersBinding.inflate(inflater, container, false)
        binding.rvFollowers.layoutManager = LinearLayoutManager(activity)
        binding.rvFollowers.setHasFixedSize(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        model.listFollowers.observe(viewLifecycleOwner) {
            if (it != null) {
                showRecyclerList(it)
            }
        }
    }

    private fun showRecyclerList(listUsers: List<UserDetailResponse>) {
        val listUserAdapter = ListUserAdapter(listUsers)
        binding.rvFollowers.adapter = listUserAdapter
        listUserAdapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: String) {
                val intent = Intent(activity, DetailActivity::class.java)
                intent.putExtra(DetailActivity.EXTRA_USERNAME, data)
                startActivity(intent)
            }
        })
    }
}