package com.kt.myproject.base

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.kt.myproject.databinding.HomeBinding
import com.kt.myproject.databinding.ItemHomeBinding

class BaseViewHolder<B : ViewBinding>(val bind: B) : RecyclerView.ViewHolder(bind.root)
typealias ItemInflating = (LayoutInflater, ViewGroup, Boolean) -> ViewBinding

fun ItemInflating?.invokeItem(parent: ViewGroup): ViewBinding? {
    return this?.invoke(LayoutInflater.from(parent.context), parent, false)
}

class GoneViewHolder(parent: ViewGroup) :
    RecyclerView.ViewHolder(View(parent.context).also { it.visibility = View.GONE })

abstract class BaseBindRecyclerView<T> : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun getItemCount(): Int {
        return size + 1
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int /* also it position */
    ): RecyclerView.ViewHolder {
        when {
            dataIsEmpty -> blankInflating().invokeItem(parent)?.also {
                return BaseViewHolder(it)
            }
            dataNotEmpty && viewType == size -> footerInflating().invokeItem(parent)?.also {
                if (viewType > lastBindIndex) onFooterIndexChanged(viewType)
                return BaseViewHolder(it)
            }
            else -> get(viewType)?.also { item ->
                itemInflating(item, viewType).invokeItem(parent)?.also {
                    return BaseViewHolder(it)
                }
            }
        }
        return GoneViewHolder(parent)
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        val model = get(position) ?: return
        when (viewHolder) {
            is BaseViewHolder<*> -> viewHolder.bind.apply {
                onBindItem(model, position)
                root.setOnClickListener {
                    onItemClick(model, position)
                }
                root.setOnLongClickListener {
                    onItemLongClick(model, position)
                    true
                }
                lastBindIndex = position
            }
        }
    }

    /**
     *
     */
    open var onItemClick: (T, Int) -> Unit = { _, _ -> }
    open var onItemLongClick: (T, Int) -> Unit = { _, _ -> }
    open var onFooterIndexChanged: (Int) -> Unit = {}
    open var currentList: MutableList<T> = mutableListOf()
    open var lastBindIndex: Int = -1
    open val lastIndex: Int get() = currentList.lastIndex
    open val size: Int get() = currentList.size
    open val dataIsEmpty: Boolean get() = currentList.isNullOrEmpty()
    open val dataNotEmpty: Boolean get() = !dataIsEmpty
    protected open fun blankInflating(): ItemInflating? = null
    protected open fun footerInflating(): ItemInflating? = null
    protected abstract fun itemInflating(item: T, position: Int): ItemInflating
    protected abstract fun ViewBinding.onBindItem(item: T, position: Int)
    open fun get(position: Int): T? {
        return currentList.getOrNull(position)
    }

    open fun set(collection: Collection<T>?) {
        currentList = collection?.toMutableList() ?: mutableListOf()
        lastBindIndex = -1
        notifyDataSetChanged()
    }

    open fun set(list: MutableList<T>?) {
        currentList = list ?: mutableListOf()
        lastBindIndex = -1
        notifyDataSetChanged()
    }

    open fun set(array: Array<T>?) {
        currentList = array?.toMutableList() ?: mutableListOf()
        lastBindIndex = -1
        notifyDataSetChanged()
    }

    open fun setElseEmpty(collection: Collection<T>?) {
        if (collection.isNullOrEmpty()) return
        set(collection)
    }

    open fun setElseEmpty(list: MutableList<T>?) {
        if (list.isNullOrEmpty()) return
        set(list)
    }

    open fun setElseEmpty(array: Array<T>?) {
        if (array == null || array.isEmpty()) return
        set(array)
    }

    open fun add(collection: Collection<T>?) {
        if (collection.isNullOrEmpty()) return
        currentList.addAll(collection)
        notifyDataSetChanged()
    }

    open fun add(array: Array<T>?) {
        if (array.isNullOrEmpty()) return
        currentList.addAll(array)
        notifyDataSetChanged()
    }

    open fun add(model: T?) {
        model ?: return
        currentList.add(model)
        notifyItemRangeChanged(size, size + 1)
    }

    open fun add(position: Int, model: T?) {
        model ?: return
        currentList.add(position, model)
        notifyDataSetChanged()
    }

    open fun edit(position: Int, model: T?) {
        model ?: return
        if (position in 0..lastIndex) {
            currentList[position] = model
            notifyItemChanged(position)
        }
    }

    open fun remove(index: Int) {
        currentList.removeAt(index)
        notifyItemRemoved(index)
    }

    open fun remove(model: T?) {
        model ?: return
        val position = currentList.indexOf(model)
        remove(position)
    }

    open fun clear() {
        currentList = mutableListOf()
        notifyDataSetChanged()
    }

    open fun bind(recyclerView: RecyclerView, block: LinearLayoutManager.() -> Unit = {}) {
        val lm = LinearLayoutManager(recyclerView.context)
        lm.block()
        recyclerView.layoutManager = lm
        recyclerView.adapter = this
    }

    open fun bind(
        recyclerView: RecyclerView,
        spanCount: Int,
        block: GridLayoutManager.() -> Unit = {}
    ) {
        val lm = GridLayoutManager(recyclerView.context, spanCount)
        lm.block()
        lm.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (dataIsEmpty || position == size) lm.spanCount
                else 1
            }
        }
        recyclerView.layoutManager = lm
        recyclerView.adapter = this
    }
}