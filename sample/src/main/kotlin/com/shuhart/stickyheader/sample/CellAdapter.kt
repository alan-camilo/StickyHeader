package com.shuhart.stickyheader.sample

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.shuhart.stickyheader.StickyHeaderAdapter
import java.lang.IllegalArgumentException

class CellAdapter : PagingDataAdapter<Cell, RecyclerView.ViewHolder>(CELL_DIFF_UTIL),
    StickyHeaderAdapter {

    companion object {
        private val CELL_DIFF_UTIL = object : DiffUtil.ItemCallback<Cell>() {
            override fun areItemsTheSame(oldItem: Cell, newItem: Cell): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Cell, newItem: Cell): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItem(position)!!.type) {
            Cell.HEADER -> (holder as HeaderViewHolder).textView.text = "Header $position"
            Cell.ITEM -> (holder as ItemViewHolder).textView.text = "Item $position"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == Cell.HEADER) {
            HeaderViewHolder(inflater.inflate(R.layout.recycler_view_header_item, parent, false))
        } else {
            ItemViewHolder(inflater.inflate(R.layout.recycler_view_item, parent, false))
        }
    }

    override fun getHeaderPositionForItem(itemPosition: Int): Int {
        val items: List<Cell> = snapshot().items
        return when(val item = getItem(itemPosition)) {
            is Cell.Header -> itemPosition
            is Cell.Item -> items.indexOf(item.header!!)
            else -> throw IllegalArgumentException("No item at this position")
        }
    }

    override fun onBindHeaderViewHolder(holder: RecyclerView.ViewHolder, headerPosition: Int) {
        (holder as HeaderViewHolder).textView.text = "Header $headerPosition"
    }

    override fun onCreateHeaderViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return super.createViewHolder(parent, Cell.HEADER)
    }

    class HeaderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var textView: TextView = view.findViewById(R.id.text_view)
    }

    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var textView: TextView = view.findViewById(R.id.text_view)
    }
}