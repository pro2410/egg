package ru.taxcom.mobile.android.egais.presentation.waybills.views

import android.graphics.Canvas
import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.text.TextUtils.isEmpty
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import ru.taxcom.mobile.android.egais.R
import ru.taxcom.mobile.android.egais.models.waybill.WayBillModel
import java.text.SimpleDateFormat
import java.util.*

class SectionItemDecoration(private val headerOffset: Int) : RecyclerView.ItemDecoration() {

    private var headerView: View? = null
    private lateinit var date: TextView

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)

        val pos = parent.getChildAdapterPosition(view)
        if (pos != RecyclerView.NO_POSITION && isSection(pos, parent)) {
            outRect.top = (headerOffset - parent.context.resources.displayMetrics.density).toInt()
        }
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(c, parent, state)

        if (headerView == null) {
            headerView = inflateHeaderView(parent)
            date = headerView!!.findViewById(R.id.group_date)
            fixLayoutSize(headerView!!, parent)
        }

        var previousDate: CharSequence = ""
        for (i in 0 until parent.childCount) {
            val child = parent.getChildAt(i)
            val position = parent.getChildAdapterPosition(child)
            if (position == RecyclerView.NO_POSITION) {
                continue
            }
            val titleDate = getSectionDate(position, parent)
            date.text = titleDate
            if (previousDate != titleDate || isSection(position, parent)) {

                val space = child.top - headerView!!.height
                drawHeader(c, space)
                previousDate = titleDate
                if (i != 0 && space <= headerView!!.height) {
                    val child1 = parent.getChildAt(i - 1)
                    val position1 = parent.getChildAdapterPosition(child1)

                    val title1 = getSectionDate(position1, parent)
                    date.text = title1
                    c.save()
                    c.translate(0f, (Math.max(0, space) - headerView!!.height).toFloat())
                    headerView!!.draw(c)
                    c.restore()
                }
            }
        }
    }

    private fun isSection(pos: Int, parent: RecyclerView): Boolean = when {
        pos == 0 -> true
        getList(parent).isEmpty() -> false
        getList(parent)[pos].itemType != WayBillsItemType.DATA -> false
        else -> formatDate((getList(parent)[pos] as WayBillModel).date) != formatDate((getList(parent)[pos - 1] as WayBillModel).date)
    }

    private fun getList(parent: RecyclerView) =
            (parent.adapter as WayBillsAdapter).list

    private fun getSectionDate(position: Int, parent: RecyclerView): CharSequence {
        return when {
            position < 0 -> ""
            getList(parent)[position].itemType != WayBillsItemType.DATA -> if (position > 0) {
                firstUpperCase(formatDate((getList(parent)[position - 1] as? WayBillModel)?.date))
            } else {
                ""
            }
            else -> firstUpperCase(formatDate((getList(parent)[position] as WayBillModel).date))
        }
    }

    private fun drawHeader(c: Canvas, space: Int) {
        c.save()
        c.translate(0f, Math.max(0, space).toFloat())
        headerView!!.draw(c)
        c.restore()
    }

    private fun inflateHeaderView(parent: RecyclerView): View {
        return LayoutInflater.from(parent.context)
                .inflate(R.layout.item_notification_header, parent, false)
    }

    private fun fixLayoutSize(view: View, parent: ViewGroup) {
        val widthSpec = View.MeasureSpec.makeMeasureSpec(parent.width,
                View.MeasureSpec.EXACTLY)
        val heightSpec = View.MeasureSpec.makeMeasureSpec(parent.height,
                View.MeasureSpec.UNSPECIFIED)

        val childWidth = ViewGroup.getChildMeasureSpec(widthSpec,
                parent.paddingLeft + parent.paddingRight,
                view.layoutParams.width)
        val childHeight = ViewGroup.getChildMeasureSpec(heightSpec,
                parent.paddingTop + parent.paddingBottom,
                view.layoutParams.height)

        view.measure(childWidth, childHeight)

        view.layout(0, 0, view.measuredWidth, view.measuredHeight)
    }

    private fun formatDate(dateString: String?): String {
        if (isEmpty(dateString)) return ""
        return try {
            val dateLong = dateString!!.toLong()
            val format = SimpleDateFormat("EEE, yyyy.MM.dd", Locale.getDefault())
            format.format(dateLong * 1000)
        } catch (e: NumberFormatException) {
            ""
        }
    }

    private fun firstUpperCase(string: String): String {
        return when {
            string.isEmpty() -> ""
            string.length > 1 -> string.substring(0, 1).toUpperCase().plus(string.substring(1))
            else -> string.toUpperCase()
        }
    }
}
