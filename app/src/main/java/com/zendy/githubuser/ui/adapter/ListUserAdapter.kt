package com.zendy.githubuser.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.zendy.githubuser.data.remote.UserDetailResponse
import com.zendy.githubuser.databinding.ItemRowUserBinding

class ListUserAdapter(private val listUser: List<UserDetailResponse>) :
    RecyclerView.Adapter<ListUserAdapter.ListViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    class ListViewHolder(var binding: ItemRowUserBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemRowUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        Glide.with(holder.itemView.context)
            .load(listUser[position].avatarUrl)
            .into(holder.binding.imgItemAvatar)
        holder.binding.tvItemUsername.text = listUser[position].login
        holder.itemView.setOnClickListener { onItemClickCallback.onItemClicked(listUser[holder.adapterPosition].login) }
    }

    override fun getItemCount(): Int = listUser.size

    interface OnItemClickCallback {
        fun onItemClicked(data: String)
    }
}