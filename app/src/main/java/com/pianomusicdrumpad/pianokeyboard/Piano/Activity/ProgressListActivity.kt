package com.pianomusicdrumpad.pianokeyboard.Piano.Activity

import android.app.ListActivity
import android.content.res.Resources
import android.os.Bundle
import android.view.View
import android.widget.ListView
import com.pianomusicdrumpad.pianokeyboard.Piano.adapter.ProgressListAdapter
import com.pianomusicdrumpad.pianokeyboard.R
import com.pianomusicdrumpad.pianokeyboard.Utils.ConstantAd
import com.pianomusicdrumpad.pianokeyboard.Utils.SharePrefUtils.getString
import com.pianomusicdrumpad.pianokeyboard.Utils.SharePrefUtils.putString

class ProgressListActivity : ListActivity(), View.OnClickListener {
    private var adapter: ProgressListAdapter? = null

    fun goBack() {
        onBackPressed()
    }

    override fun onClick(view: View) {
        if (view.id == R.id.back_arrow_view) {
            onBackPressed()
        }
    }

    public override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)

        if (getString(ConstantAd.AD_NAV_BAR, "1") == "0") {
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        }
        setContentView(R.layout.progress_list)
        listView.isFastScrollEnabled = true


        val progressListAdapter = ProgressListAdapter(this)
        this.adapter = progressListAdapter
        listAdapter = progressListAdapter
        findViewById<View>(R.id.back_arrow_view).setOnClickListener(this)
    }

    public override fun onListItemClick(listView: ListView, view: View, i: Int, j: Long) {
        super.onListItemClick(listView, view, i, j)
    }

    public override fun onResume() {
        super.onResume()
        putString(ConstantAd.AD_CHECK_RESUME, "1")
        ProgressListAdapter.readProgressMap(applicationContext)
        adapter!!.notifyDataSetChanged()
    }


    override fun onBackPressed() {
        finish()
    }

    override fun onPause() {
        putString(ConstantAd.AD_CHECK_RESUME, "0")
        super.onPause()
    }

    companion object {
        const val LOG_TAG: String = "themelodymaster"
        var resources: Resources? = null
    }
}
