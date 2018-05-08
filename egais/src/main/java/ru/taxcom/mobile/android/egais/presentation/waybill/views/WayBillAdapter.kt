package ru.taxcom.mobile.android.egais.presentation.waybill.views

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import ru.taxcom.mobile.android.egais.R
import ru.taxcom.mobile.android.egais.models.excise_stamp.ItemExciseStamp
import java.text.SimpleDateFormat
import java.util.*

class WayBillAdapter : RecyclerView.Adapter<WayBillAdapter.WayBillHolder>() {

    var list = mutableListOf<ItemExciseStamp>()

    fun update(list: List<ItemExciseStamp>) {
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }

    fun add(items: List<ItemExciseStamp>) {
        this.list.addAll(items)
        notifyDataSetChanged()
    }

    fun addItem(item: ItemExciseStamp) {
        this.list.add(item)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: WayBillHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WayBillHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_barcode, parent, false)
        return WayBillHolder(view)
    }

    inner class WayBillHolder(view: View) : RecyclerView.ViewHolder(view) {

        var barcode: TextView = itemView.findViewById(R.id.item_text)
        var time: TextView = itemView.findViewById(R.id.item_time)

        fun bind(item: ItemExciseStamp) {
            barcode.text = item.code
            time.text = formatDate(item.date)
        }

        private fun formatDate(dateString: String): String {
            val dateLong = dateString.toLong()
            val format = SimpleDateFormat("HH:mm", Locale.getDefault())
            return format.format(dateLong)
        }
    }
}