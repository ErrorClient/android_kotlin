package com.android.ututapp.presentation.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.android.ututapp.R
import com.android.data.models.ImageDataModel
import com.bumptech.glide.Glide

class TableAdapter(var imageList: List<ImageDataModel>, private val context: Fragment) :
    RecyclerView.Adapter<TableAdapter.ViewHolder>() {

    /***
     * Создаем свой ClickListener для работы с ImageView по клику из фрагмента
     */

    private lateinit var imageClickListener: OnImageClickListener
    var clickedImageView: ImageView? = null

    interface OnImageClickListener {
        fun onImageClick(position: Int)
    }

    fun setOnImageClickListener(listener: OnImageClickListener) {
        imageClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_in_table, parent, false)

        return ViewHolder(view, imageClickListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val imageUrl = imageList[position].url

        Glide.with(context)
            .load(imageUrl)
            .error(R.drawable.empty)
            .into(holder.imageView)
    }

    override fun getItemCount() = imageList.size

    fun addItems(newImageList: List<ImageDataModel>) {

        val diffCallback = ItemDiffUtilCall(this.imageList, newImageList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        diffResult.dispatchUpdatesTo(this)

        this.imageList = newImageList
    }

    private class ItemDiffUtilCall(
        private val oldItemList: List<ImageDataModel>,
        private val newItemList: List<ImageDataModel>
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int {
            return oldItemList.size
        }

        override fun getNewListSize(): Int {
            return newItemList.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {

            return oldItemList[oldItemPosition].id == newItemList[newItemPosition].id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {

            return oldItemList[oldItemPosition] == newItemList[newItemPosition]
        }
    }

    inner class ViewHolder(itemView: View, listener: OnImageClickListener) :
        RecyclerView.ViewHolder(itemView) {

        var imageView: ImageView = itemView.findViewById(R.id.small_image)

        init {
            imageView.setOnClickListener {
                clickedImageView = imageView
                imageView.transitionName = context.getString(R.string.transition_name)
                listener.onImageClick(absoluteAdapterPosition)
            }
        }
    }

}