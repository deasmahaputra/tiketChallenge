package com.test.tiketchallenge.github.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.test.tiketchallenge.network.response.AccountGithubResponse
import kotlinx.android.synthetic.main.item_github_account.view.*
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import android.graphics.Bitmap
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.test.tiketchallenge.R


class GithubUserAdapter(val context: Context) : RecyclerView.Adapter<GithubUserAdapter.GithubUserViewHolder>(){

    private var listItem : MutableList<AccountGithubResponse.ItemsItem> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GithubUserViewHolder =
        GithubUserViewHolder(LayoutInflater.from(context).inflate(R.layout.item_github_account, parent, false))


    override fun getItemCount(): Int {
        return listItem.size
    }

    override fun onBindViewHolder(holder: GithubUserViewHolder, position: Int) {
        holder.bindItem(listItem[position])
    }

    fun setDataAccount(data : List<AccountGithubResponse.ItemsItem>){
        var size = data.size
        listItem.addAll(data)
        var newSize = listItem.size
        notifyItemRangeChanged(size, newSize)
    }

    fun clearData(){
        listItem.clear()
        notifyDataSetChanged()
    }


    class GithubUserViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        fun bindItem(item : AccountGithubResponse.ItemsItem){
            itemView.author_tv.text = item.login

            Glide.with(itemView.context).load(item.avatar_url).asBitmap().centerCrop()
                .into(object : BitmapImageViewTarget(itemView.imageAvatar) {
                    override fun setResource(resource: Bitmap?) {
                        val circularBitmapDrawable =
                            RoundedBitmapDrawableFactory.create(itemView.context.getResources(), resource)
                        circularBitmapDrawable.isCircular = true
                        itemView.imageAvatar.setImageDrawable(circularBitmapDrawable)
                    }
                })
        }
    }
}