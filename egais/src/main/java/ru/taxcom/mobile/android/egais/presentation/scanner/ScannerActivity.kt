package ru.taxcom.mobile.android.egais.presentation.scanner

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_scanner.*
import kotterknife.bindView
import ru.taxcom.mobile.android.egais.presentation.scanner.presenter.ScannerPresenter
import ru.taxcom.mobile.android.egais.presentation.scanner.views.ScannerView
import ru.taxcom.mobile.android.egais.utils.dialogs.ErrorDialog
import ru.taxcom.mobile.android.egais.R
import ru.taxcom.mobile.android.egais.models.excise_stamp.ItemExciseStamp
import ru.taxcom.mobile.android.qrscanner.BarCodeMode
import ru.taxcom.mobile.android.qrscanner.DisplayMode
import ru.taxcom.mobile.android.qrscanner.QrScannerFragment
import javax.inject.Inject

class ScannerActivity : DaggerAppCompatActivity(), ScannerView,
        QrScannerFragment.InteractionListener,
        ErrorDialog.ActionListener {

    private val DOC_FLOW_ID_KEY = "docflowId"
    private val PDF417_ARG = "pdf417"

    private val cameraContainer by bindView<View>(R.id.cameraContainer)
    private val scannedResult by bindView<View>(R.id.scanned_result)
    private val scannedResultTitle by bindView<TextView>(R.id.scanned_result_title)
    private val scannedResultText by bindView<TextView>(R.id.scanned_result_text)
    private val scannedResultErrorHand by bindView<ImageView>(R.id.scanned_result_error_hand)
    private val progressLayout by bindView<View>(R.id.container_progress_bar)

    private var scanning = false
    private var backPressedBlock = false

    private lateinit var docflowId: String

    @Inject
    lateinit var scannerPresenter: ScannerPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scanner)
        docflowId = intent.extras.getString(DOC_FLOW_ID_KEY)
        if (savedInstanceState == null) {
            val displayMode = DisplayMode.WITHOUT_TITLE
            supportFragmentManager.beginTransaction()
                    .replace(R.id.cameraContainer, QrScannerFragment.newInstance(displayMode, BarCodeMode.PDF417))
                    .commit()
        }
        showToolbar()
    }

    private fun showToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar!!.setDisplayShowHomeEnabled(false)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolbar.visibility = View.VISIBLE
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_scanner, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_item_flashlight -> {
                // logic for flashlight
                false
            }
            android.R.id.home -> {
                scannerPresenter.closeScan()
                true
            }
            else -> false
        }
    }

    override fun onBackPressed() {
        if (backPressedBlock) return
        scannerPresenter.closeScan()
    }

    override fun showProgress() {
        scanning = true
        progressLayout.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        progressLayout.visibility = View.GONE
    }

    override fun onQrScanSucceeded(data: String) {
        if (scanning) return
        scannerPresenter.scan(data, docflowId)
    }

    override fun onQrScanCancelled() {
        //
    }

    override fun returnSuccessData(items: ArrayList<ItemExciseStamp>) {
        backPressedBlock = false
        val result = Intent()
        result.putParcelableArrayListExtra(PDF417_ARG, items)
        setResult(Activity.RESULT_OK, result)
        finish()
    }

    override fun closeScan() {
        finish()
    }

    override fun errorDialogDismissed() {
        scanning = false
    }

    override fun showResultScreen(isHandVisible: Boolean, title: String, message: String, color: Int) {
        cameraContainer.visibility = View.GONE
        scannedResult.visibility = View.VISIBLE
        scannedResult.setBackgroundColor(color)
        scannedResultTitle.text = title
        scannedResultText.text = message
        backPressedBlock = true
        scannedResultErrorHand.visibility = if (isHandVisible) View.VISIBLE else View.GONE
        scannedResult.setOnClickListener({
            run {
                hideResultScreen()
            }
        })
    }

    override fun hideResultScreen() {
        scanning = false
        backPressedBlock = false
        cameraContainer.visibility = View.VISIBLE
        scannedResult.visibility = View.GONE
        scannedResultErrorHand.visibility = View.GONE
    }
}