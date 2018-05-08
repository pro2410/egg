package ru.taxcom.mobile.android.egais.presentation.waybills.views

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils.isEmpty
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.error_view.*
import kotlinx.android.synthetic.main.fragment_way_bills.*
import kotlinx.android.synthetic.main.fragment_way_bills.view.*
import ru.taxcom.mobile.android.egais.R
import ru.taxcom.mobile.android.egais.models.waybill.WayBillModel
import ru.taxcom.mobile.android.egais.presentation.waybill.WayBillActivity
import ru.taxcom.mobile.android.egais.presentation.waybills.presenter.WayBillsFragmentPresenter
import javax.inject.Inject

class WayBillsFragment : DaggerFragment(), WayBillsView,
        SwipeRefreshLayout.OnRefreshListener {

    @Inject
    lateinit var presenter: WayBillsFragmentPresenter

    private lateinit var adapter: WayBillsAdapter
    private lateinit var viewList: RecyclerView
    private lateinit var notificationsEmptyView: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_way_bills, container, false)
        viewList = view.findViewById(R.id.way_bill_list)
        notificationsEmptyView = view.findViewById(R.id.notifications_empty_view)
        activity!!.retry_button.setOnClickListener { presenter.loadAndShowWayBills() }
        view.swipe_refresh_layout.setOnRefreshListener(this)
        view.swipe_refresh_layout.isRefreshing = true
        initList()
        presenter.loadAndShowWayBills()
        return view
    }

    private fun initList() {
        val layoutManager = LinearLayoutManager(activity)
        adapter = WayBillsAdapter(activity!!.applicationContext)
        viewList.layoutManager = layoutManager
        viewList.adapter = adapter
        viewList.itemAnimator = null
        viewList.addItemDecoration(SectionItemDecoration(resources.getDimensionPixelSize(R.dimen.date_item_height)))
        viewList.itemAnimator = DefaultItemAnimator()
        adapter.onWayBillsClick = ::onWayBillsClick
        adapter.onLoadNextPageRetry = ::onLoadNextPageRetry

        viewList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            internal var visibleItems: Int = 0
            internal var firstVisibleItemPosition: Int = 0
            internal var offset: Int = 0

            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                visibleItems = layoutManager.childCount
                firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
                offset = layoutManager.itemCount

                if (dy >= 0) {
                    if (firstVisibleItemPosition + visibleItems >= offset) {
                        presenter.loadNextPage()
                    }
                }
            }
        })
    }

    override fun onStart() {
        super.onStart()
        activity?.title = getString(R.string.way_bills_label)
    }

    private fun onWayBillsClick(wayBillModel: WayBillModel) {
        WayBillActivity.start(activity, wayBillModel)
    }

    override fun onRefresh() {
        presenter.loadAndShowWayBills()
    }

    override fun showList(list: List<WayBillModel>) {
        adapter.update(list)
    }

    override fun clearList() {
        adapter.clear()
    }

    override fun showEmptyView(visible: Boolean) {
        notificationsEmptyView.visibility = if (visible) View.VISIBLE else View.GONE
        showErrorView(null)
    }

    override fun showErrorView(textError: String?) {
        error_view?.visibility = if (isEmpty(textError)) View.GONE else View.VISIBLE
        error_message?.text = textError
    }

    override fun hideRefresh() {
        swipe_refresh_layout?.isRefreshing = false
    }

    override fun showRefresh() {
        swipe_refresh_layout?.isRefreshing = true
    }

    override fun loadNextPage(newPageBillsList: List<WayBillModel>) {
        adapter.addPage(newPageBillsList)
    }

    override fun showNextPageProgress() {
        viewList.post({ adapter.showPageProgress() })
    }

    override fun hideNextPageProgress() {
        adapter.hidePageProgress()
    }

    override fun loadNextPageError() {
        adapter.showError()
    }

    override fun onLoadNextPageRetry() {
        adapter.hideError()
        presenter.loadNextPageRetry()
    }
}