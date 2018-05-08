package ru.taxcom.mobile.android.egais.presentation.waybills.views

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import ru.taxcom.mobile.android.egais.R
import ru.taxcom.mobile.android.egais.models.waybill.WayBillModel
import java.text.SimpleDateFormat
import java.util.*

class WayBillsAdapter(context: Context) : InputTrackingRecyclerViewAdapter<RecyclerView.ViewHolder>(context) {

    lateinit var onWayBillsClick: (wayBillModel: WayBillModel) -> Unit
    lateinit var onLoadNextPageRetry: () -> Unit

    var list = ArrayList<WayBillsListItem>()
    private val progressItem = WayBillsItemProgress()
    private val errorItem = WayBillsItemError()

    fun update(list: List<WayBillModel>) {
        this.list = list as ArrayList<WayBillsListItem>
        notifyDataSetChanged()
    }

    fun addPage(pageList: List<WayBillModel>?) {
        if (pageList != null) {
            val firstInsertPosition = list.size
            list.addAll(pageList)
            notifyItemRangeInserted(firstInsertPosition, pageList.size)
        }
    }

    fun clear() {
        list.clear()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView: View
        return when (WayBillsItemType.values()[viewType]) {
            WayBillsItemType.DATA -> {
                itemView = inflater.inflate(R.layout.item_way_bill, parent, false)
                WayBillsHolder(itemView)
            }
            WayBillsItemType.PROGRESS -> {
                itemView = inflater.inflate(R.layout.item_progress, parent, false)
                LoadingViewHolder(itemView)
            }
            WayBillsItemType.ERROR -> {
                itemView = inflater.inflate(R.layout.item_error, parent, false)
                ErrorViewHolder(itemView)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return list[position].itemType.ordinal
    }

    override fun getItemCount() = list.size

    fun showPageProgress() {
        val lastItemPosition = list.size - 1
        if (lastItemPosition >= 0 && list[lastItemPosition].itemType === WayBillsItemType.PROGRESS) return
        list.add(progressItem)
        val firstInsertPosition = list.size
        notifyItemRangeInserted(firstInsertPosition, 1)
    }

    fun hidePageProgress() {
        val lastItemPosition = list.size - 1
        if (lastItemPosition < 0 || list[lastItemPosition].itemType !== WayBillsItemType.PROGRESS) return
        list.removeAt(lastItemPosition)
        notifyItemRemoved(lastItemPosition)
    }

    fun showError() {
        val lastItemPosition = list.size - 1
        if (lastItemPosition < 0 || list[lastItemPosition].itemType === WayBillsItemType.ERROR) return
        if (list[lastItemPosition].itemType == WayBillsItemType.PROGRESS) {
            list.removeAt(lastItemPosition)
            list.add(errorItem)
            notifyItemChanged(lastItemPosition)
        } else {
            list.add(errorItem)
            notifyItemInserted(lastItemPosition)
        }
    }

    fun hideError() {
        val lastItemPosition = list.size - 1
        if (lastItemPosition < 0 || list[lastItemPosition].itemType !== WayBillsItemType.ERROR) return
        list.removeAt(lastItemPosition)
        list.add(progressItem)
        notifyItemRemoved(lastItemPosition)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = list[position]
        if (item is WayBillModel) {
            (holder as WayBillsHolder).bind(item)
            selectedItemSMARTDroid(holder, position)
        } else if (item is WayBillsItemError) {
            (holder as ErrorViewHolder).bind()
        }
    }

    inner class WayBillsHolder(view: View) : RecyclerView.ViewHolder(view) {
        private var rootView: View = itemView.findViewById(R.id.item_way_bill)
        private var wayBillNumber: TextView = itemView.findViewById(R.id.way_bill_number)
        private var wayBillOrganizationName: TextView = itemView.findViewById(R.id.way_bill_org)
        private var wayBillTime: TextView = itemView.findViewById(R.id.way_bill_time)

        fun bind(wayBillModel: WayBillModel) {
            wayBillNumber.text = wayBillModel.number ?: "-"
            wayBillOrganizationName.text = wayBillModel.orgNme ?: "-"
            wayBillTime.text = formatDate(wayBillModel.date)
            rootView.setOnClickListener { onWayBillsClick(wayBillModel) }
        }

        private fun formatDate(dateString: String?): String {
            if (TextUtils.isEmpty(dateString)) {
                return "-"
            }
            return try {
                val dateLong = dateString!!.toLong()
                val format = SimpleDateFormat("HH:mm", Locale.getDefault())
                format.format(dateLong * 1000)
            } catch (e: NumberFormatException) {
                "-"
            }
        }
    }

    inner class LoadingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    inner class ErrorViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var retryLoadNextPage: View = itemView.findViewById(R.id.retry_page_loading_btn)
        fun bind() {
            retryLoadNextPage.setOnClickListener { onLoadNextPageRetry() }
        }
    }

    private fun selectedItemSMARTDroid(holder: WayBillsHolder, position: Int) {
        if (Build.BRAND == "PiTECH") {
            if (position == getSelectedItem()) {
                holder.itemView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.itemListSelected))
            } else {
                holder.itemView.setBackgroundColor(Color.TRANSPARENT)
            }
        }
    }
}