package com.pianomusicdrumpad.pianokeyboard.callafterscreen.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.pianomusicdrumpad.pianokeyboard.callafterscreen.fragment.CallCutMessageFragment
import com.pianomusicdrumpad.pianokeyboard.callafterscreen.fragment.CallCutMoreFragment
import com.pianomusicdrumpad.pianokeyboard.callafterscreen.fragment.CallCutRecentFragment
import com.pianomusicdrumpad.pianokeyboard.callafterscreen.fragment.CallCutRecordFragment
import com.pianomusicdrumpad.pianokeyboard.callafterscreen.fragment.ReminderFragment

class MyFragmentStatePagerAdapter(appCompatActivity: AppCompatActivity) :
    FragmentStateAdapter(appCompatActivity) {


    override fun getItemCount(): Int {
        return 4
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
//            0 -> CallCutRecentFragment()
            0 -> CallCutRecordFragment()
            1 -> CallCutMessageFragment()
            2 -> ReminderFragment()
            3 -> CallCutMoreFragment()
            else -> CallCutRecentFragment()
        }
    }
}