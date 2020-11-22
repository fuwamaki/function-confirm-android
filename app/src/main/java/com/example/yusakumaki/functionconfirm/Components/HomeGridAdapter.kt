package com.example.yusakumaki.functionconfirm.Components

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.yusakumaki.functionconfirm.Entity.GridItemEntity
import com.example.yusakumaki.functionconfirm.Entity.gridItems
import com.example.yusakumaki.functionconfirm.R

class HomeGridAdapter(private val context: Context) : BaseAdapter() {

    override fun getCount(): Int {
        return gridItems.size
    }

    override fun getItem(position: Int): GridItemEntity {
        return gridItems[position]
    }

    override fun getItemId(position: Int): Long {
        return getItem(position).id.toLong()
    }

    override fun getView(position: Int, view: View?, viewGroup: ViewGroup?): View? {
        val containerView = view
                ?: LayoutInflater.from(context).inflate(R.layout.view_home_grid_item, viewGroup, false)
        val itemImageView = containerView.findViewById<ImageView>(R.id.item_image_view)
        itemImageView.setImageResource(getItem(position).image)
        itemImageView.maxHeight = itemImageView.width
        val name = containerView.findViewById<TextView>(R.id.item_text_view)
        name.text = getItem(position).title
        return containerView
    }
}