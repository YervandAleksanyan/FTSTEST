package  com.example.yervand.ftstest.view.controls.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableList
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.example.yervand.ftstest.view.controls.adapter.binding.ItemBinder
import java.lang.ref.WeakReference

class BindingRecyclerViewAdapter<T>(var itemBinder: ItemBinder<T>, var items: ObservableList<T>?) : RecyclerView.Adapter<BindingRecyclerViewAdapter.ViewHolder>(), View.OnClickListener, View.OnLongClickListener {
    private val onListChangedCallback: WeakReferenceOnListChangedCallback<T>
    private lateinit var inflater: LayoutInflater
    var clickHandler: ClickHandler<T>? = null
    var longClickHandler: LongClickHandler<T>? = null

    init {
        this.onListChangedCallback = WeakReferenceOnListChangedCallback(this)
        setItems(items)
    }

    fun setItems(items: Collection<T>?) {
        if (this.items == items) {
            return
        }

        this.items?.let {
            it.removeOnListChangedCallback(onListChangedCallback)
            notifyItemRangeRemoved(0, it.size)
        }
        when {
            items is ObservableList<*> -> {
                this.items = items as ObservableList<T>?
                notifyItemRangeInserted(0, this.items!!.size)
                this.items?.addOnListChangedCallback(onListChangedCallback)
            }
            items != null -> {
                this.items = ObservableArrayList()
                this.items!!.addOnListChangedCallback(onListChangedCallback)
                this.items!!.addAll(items)
            }
            else -> this.items = null
        }
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        items?.removeOnListChangedCallback(onListChangedCallback)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, layoutId: Int): ViewHolder {
        inflater = LayoutInflater.from(viewGroup.context)
        val binding: ViewDataBinding = DataBindingUtil.inflate(inflater, layoutId, viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val item = items!![position]
        viewHolder.binding.setVariable(itemBinder.getBindingVariable(item), item)
        viewHolder.binding.root.setTag(ITEM_MODEL, item)
        viewHolder.binding.root.setOnClickListener(this)
        viewHolder.binding.root.setOnLongClickListener(this)
        viewHolder.binding.executePendingBindings()
    }

    override fun getItemViewType(position: Int): Int = itemBinder.getLayoutRes(items!![position])!!

    override fun getItemCount(): Int = items?.size ?: 0

    override fun onClick(v: View) {
        clickHandler?.let {
            val item = v.getTag(ITEM_MODEL) as T
            clickHandler!!.onClick(item)
        }
    }

    override fun onLongClick(v: View): Boolean {
        longClickHandler?.let {
            val item = v.getTag(ITEM_MODEL) as T
            longClickHandler!!.onLongClick(item)
            return true
        }
        return false
    }

    class ViewHolder(val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root)

    private class WeakReferenceOnListChangedCallback<T>(bindingRecyclerViewAdapter: BindingRecyclerViewAdapter<T>) : ObservableList.OnListChangedCallback<ObservableList<T>>() {

        private val adapterReference: WeakReference<BindingRecyclerViewAdapter<T>> = WeakReference(bindingRecyclerViewAdapter)

        override fun onChanged(sender: ObservableList<T>) {
            val adapter = adapterReference.get()
            adapter?.notifyDataSetChanged()
        }

        override fun onItemRangeChanged(sender: ObservableList<T>, positionStart: Int, itemCount: Int) {
            val adapter = adapterReference.get()
            adapter?.notifyItemRangeChanged(positionStart, itemCount)
        }

        override fun onItemRangeInserted(sender: ObservableList<T>, positionStart: Int, itemCount: Int) {
            val adapter = adapterReference.get()
            adapter?.notifyItemRangeInserted(positionStart, itemCount)
        }

        override fun onItemRangeMoved(sender: ObservableList<T>, fromPosition: Int, toPosition: Int, itemCount: Int) {
            val adapter = adapterReference.get()
            adapter?.notifyItemMoved(fromPosition, toPosition)
        }

        override fun onItemRangeRemoved(sender: ObservableList<T>, positionStart: Int, itemCount: Int) {
            val adapter = adapterReference.get()
            adapter?.notifyItemRangeRemoved(positionStart, itemCount)
        }
    }

    companion object {
        private const val ITEM_MODEL = -124
    }
}