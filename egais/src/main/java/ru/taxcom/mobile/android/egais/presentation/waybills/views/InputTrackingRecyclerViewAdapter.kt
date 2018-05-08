package ru.taxcom.mobile.android.egais.presentation.waybills.views

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.KeyEvent
import android.view.View


abstract class InputTrackingRecyclerViewAdapter<VH : RecyclerView.ViewHolder>(private val context: Context) : RecyclerView.Adapter<VH>() {

    private var selectedItem: Int = 0
    private lateinit var recyclerView: RecyclerView

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
        this.recyclerView.setOnKeyListener(View.OnKeyListener { view: View, keyCode: Int, event: KeyEvent ->
            val lm: RecyclerView.LayoutManager = this.recyclerView.layoutManager

            if (event.action == KeyEvent.ACTION_DOWN) {
                if (isConfirmButton(event)) {
                    if ((event.flags and KeyEvent.FLAG_LONG_PRESS) == KeyEvent.FLAG_LONG_PRESS) {
                        this.recyclerView.findViewHolderForAdapterPosition(selectedItem).itemView.performClick()
                    } else {
                        event.startTracking()
                    }
                    return@OnKeyListener true
                } else {
                    if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                        return@OnKeyListener tryMoveSelection(lm, 1)
                    } else if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
                        return@OnKeyListener tryMoveSelection(lm, -1)
                    }
                }
            } else if (event.action == KeyEvent.ACTION_UP && isConfirmButton(event)
                    && ((event.flags and KeyEvent.FLAG_LONG_PRESS) != KeyEvent.FLAG_LONG_PRESS)) {
                this.recyclerView.findViewHolderForAdapterPosition(selectedItem).itemView.performClick()
                return@OnKeyListener true
            }
            return@OnKeyListener false
        })
    }

    private fun tryMoveSelection(lm: RecyclerView.LayoutManager, direction: Int): Boolean {
        val nextSelectItem = selectedItem + direction

        // If still within valid bounds, move the selection, notify to redraw, and scroll
        if (nextSelectItem in 0..(itemCount - 1)) {
            notifyItemChanged(selectedItem)
            selectedItem = nextSelectItem
            notifyItemChanged(selectedItem)
            lm.scrollToPosition(selectedItem)
            return true
        }

        return false
    }

    private fun isConfirmButton(event: KeyEvent): Boolean {
        return when (event.keyCode) {
            KeyEvent.KEYCODE_ENTER, KeyEvent.KEYCODE_DPAD_CENTER, KeyEvent.KEYCODE_BUTTON_A -> true
            else -> false
        }
    }

    fun getContext(): Context {
        return context
    }

    fun getSelectedItem(): Int {
        return selectedItem
    }

    fun setSelectedItem(selectedItem: Int) {
        this.selectedItem = selectedItem
    }

    fun getRecyclerView(): RecyclerView {
        return recyclerView
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        onBindViewHolder(holder, position)
    }
}