package com.avinashpatil.app.youtube.ui.adapters.callbacks

import androidx.recyclerview.widget.DiffUtil
import com.avinashpatil.app.youtube.api.obj.ContentItem

object SearchCallback : DiffUtil.ItemCallback<ContentItem>() {
    override fun areItemsTheSame(oldItem: ContentItem, newItem: ContentItem): Boolean {
        return oldItem.url == newItem.url
    }

    override fun areContentsTheSame(oldItem: ContentItem, newItem: ContentItem) = true
}
