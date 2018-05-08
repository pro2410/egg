package ru.taxcom.mobile.android.egais.presentation.waybill

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import android.view.View
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_way_bill.*
import kotlinx.android.synthetic.main.fragment_scanned_result.*
import kotlinx.android.synthetic.main.item_way_bill.*
import ru.taxcom.mobile.android.egais.R
import ru.taxcom.mobile.android.egais.models.excise_stamp.ItemExciseStamp
import ru.taxcom.mobile.android.egais.models.waybill.WayBillModel
import ru.taxcom.mobile.android.egais.presentation.scanner.ScannerActivity
import ru.taxcom.mobile.android.egais.presentation.waybill.presenter.WayBillPresenter
import ru.taxcom.mobile.android.egais.presentation.waybill.views.WayBillAdapter
import ru.taxcom.mobile.android.egais.presentation.waybill.views.WayBillView
import javax.inject.Inject

class WayBillActivity : DaggerAppCompatActivity(), WayBillView {

    private lateinit var wayBillModel: WayBillModel

    private lateinit var adapter: WayBillAdapter

    private lateinit var broadCastReceiver: BroadcastReceiver

    @Inject
    lateinit var wayBillPresenter: WayBillPresenter

    companion object {
        private const val ACTION_SCANNER_INPUT_PLUGIN = "com.hht.emdk.datawedge.api.ACTION_SCANNERINPUTPLUGIN"
        private const val ACTION_SOFT_SCAN_TRIGGER = "com.hht.emdk.datawedge.api.ACTION_SOFTSCANTRIGGER"
        private const val EXTRA_PARAMETER = "com.hht.emdk.datawedge.api.EXTRA_PARAMETER"
        private const val DATA_STRING_KEY = "com.hht.emdk.datawedge.data_string"
        private const val ENABLE_PLUGIN = "ENABLE_PLUGIN"
        private const val ENABLE_TRIGGERBUTTON = "ENABLE_TRIGGERBUTTON"
        private const val ENABLE_BEEP = "ENABLE_BEEP"
        private const val DISABLE_PLUGIN = "DISABLE_PLUGIN"
        private const val DISABLE_TRIGGERBUTTON = "DISABLE_TRIGGERBUTTON"
        private const val DISABLE_BEEP = "DISABLE_BEEP"
        private const val WAY_BILL_ARG = "way_bill_model"
        private const val DOC_FLOW_ID_KEY = "docflowId"
        private const val REQUEST_CODE = 111
        private const val PDF417_ARG = "pdf417"
        fun start(activity: FragmentActivity?, wayBillModel: WayBillModel) {
            val intent = Intent(activity, WayBillActivity::class.java)
            intent.putExtra(WAY_BILL_ARG, wayBillModel)
            activity?.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_way_bill)
        notification_divider.visibility = View.GONE
        initToolbar()
        initList()
        wayBillModel = intent.extras.getParcelable(WAY_BILL_ARG)
        wayBillPresenter.shaping(wayBillModel)
        fab.setOnClickListener({ _ -> startScannerActivity() })
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowHomeEnabled(false)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    override fun onResume() {
        super.onResume()
        if (Build.BRAND == "PiTECH") {
            broadcastBuild(true)
            broadCastReceiver = object : BroadcastReceiver() {
                override fun onReceive(context: Context, intent: Intent) {
                    val data = intent.extras.getString(DATA_STRING_KEY)
                    wayBillPresenter.smartDroidScan(data, wayBillModel.docflowId ?: "")
                }
            }

            registerReceiver(broadCastReceiver, IntentFilter("DATA_SCAN"))
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initList() {
        val layoutManager = LinearLayoutManager(this)
        adapter = WayBillAdapter()
        barcode_list.layoutManager = layoutManager
        barcode_list.adapter = adapter
        barcode_list.itemAnimator = null
        barcode_list.itemAnimator = DefaultItemAnimator()
    }

    override fun fillScreen(dateTime: String, date: String, number: String, orgName: String) {
        date_section.text = date
        way_bill_number.text = number
        way_bill_time.text = dateTime
        way_bill_org.text = orgName
    }

    override fun showExciseStampList(exciseStampList: List<ItemExciseStamp>) {
        adapter.update(exciseStampList)
    }

    override fun showFab(isPiTECH: Boolean) {
        if (isPiTECH) fab.visibility = View.GONE else fab.visibility = View.VISIBLE
    }

    override fun showSmartDroidTextHint(b: Boolean) {
        if (b) smart_droid_hint.visibility = View.VISIBLE else smart_droid_hint.visibility = View.GONE
    }

    private fun startScannerActivity() {
        startActivityForResult(
                Intent(WayBillActivity@ this, ScannerActivity::class.java)
                        .apply {
                            putExtra(DOC_FLOW_ID_KEY, wayBillModel.docflowId)
                        },
                REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val scanData = data?.extras?.getParcelableArrayList(PDF417_ARG)
                    ?: ArrayList<ItemExciseStamp>()
            adapter.add(scanData)
        }
    }

    override fun hideResultScreen() {
        scanned_result.visibility = View.GONE
        ll.visibility = View.VISIBLE
    }

    override fun showResultScreen(title: String, message: String, color: Int) {
        ll.visibility = View.GONE
        scanned_result.visibility = View.VISIBLE
        scanned_result.setBackgroundColor(color)
        scanned_result_title.text = title
        scanned_result_text.text = message
    }

    override fun addExciseStamp(itemsExciseStamp: ItemExciseStamp) {
        smart_droid_hint.visibility = View.GONE
        adapter.addItem(itemsExciseStamp)
    }

    override fun onPause() {
        super.onPause()
        if (Build.BRAND == "PiTECH") {
            broadcastBuild(false)
            unregisterReceiver(broadCastReceiver)
        }
    }

    private fun broadcastBuild(enable: Boolean) {
        val scannerInputPluginIntent = Intent(ACTION_SCANNER_INPUT_PLUGIN)
        scannerInputPluginIntent.putExtra(EXTRA_PARAMETER, if (enable) ENABLE_PLUGIN else DISABLE_PLUGIN)
        sendBroadcast(scannerInputPluginIntent)
        var actionSoftScanTriggerIntent = Intent(ACTION_SOFT_SCAN_TRIGGER)
        actionSoftScanTriggerIntent.putExtra(EXTRA_PARAMETER, if (enable) ENABLE_TRIGGERBUTTON else DISABLE_TRIGGERBUTTON)
        sendBroadcast(actionSoftScanTriggerIntent)
        actionSoftScanTriggerIntent = Intent(ACTION_SOFT_SCAN_TRIGGER)
        actionSoftScanTriggerIntent.putExtra(EXTRA_PARAMETER, if (enable) ENABLE_BEEP else DISABLE_BEEP)
        sendBroadcast(actionSoftScanTriggerIntent)
    }
}