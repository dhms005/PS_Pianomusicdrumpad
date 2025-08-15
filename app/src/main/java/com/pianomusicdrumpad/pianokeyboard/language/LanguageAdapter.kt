package com.pianomusicdrumpad.pianokeyboard.language

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pianomusicdrumpad.pianokeyboard.R
import com.pianomusicdrumpad.pianokeyboard.Utils.ConstantAd
import com.pianomusicdrumpad.pianokeyboard.Utils.SharePrefUtils
import com.pianomusicdrumpad.pianokeyboard.language.LanguageAdapter.Viewholder

class LanguageAdapter(
    private val mContext: Context,
    private val languageName: Array<String>,
    private val languageImg: IntArray,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<Viewholder>() {
    private var selectedPosition = -1
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Viewholder {
        val inflater = LayoutInflater.from(mContext)
        val view = inflater.inflate(R.layout.item_language_list, parent, false)
        return Viewholder(view)
    }

    override fun onBindViewHolder(holder: Viewholder, @SuppressLint("RecyclerView") position: Int) {
        if (SharePrefUtils.getString(ConstantAd.LANGUAGE_CODE_FOR_SELECTION, "en") == "en") {
            selectedPosition = 0
        } else if (SharePrefUtils.getString(
                ConstantAd.LANGUAGE_CODE_FOR_SELECTION,
                "en"
            ) == "zh"
        ) {
            selectedPosition = 1
        } else if (SharePrefUtils.getString(
                ConstantAd.LANGUAGE_CODE_FOR_SELECTION,
                "en"
            ) == "es"
        ) {
            selectedPosition = 2
        } else if (SharePrefUtils.getString(
                ConstantAd.LANGUAGE_CODE_FOR_SELECTION,
                "en"
            ) == "fr"
        ) {
            selectedPosition = 3
        } else if (SharePrefUtils.getString(
                ConstantAd.LANGUAGE_CODE_FOR_SELECTION,
                "en"
            ) == "hi"
        ) {
            selectedPosition = 4
        } else if (SharePrefUtils.getString(
                ConstantAd.LANGUAGE_CODE_FOR_SELECTION,
                "en"
            ) == "in"
        ) {
            selectedPosition = 5
        } else if (SharePrefUtils.getString(
                ConstantAd.LANGUAGE_CODE_FOR_SELECTION,
                "en"
            ) == "ru"
        ) {
            selectedPosition = 6
        } else if (SharePrefUtils.getString(
                ConstantAd.LANGUAGE_CODE_FOR_SELECTION,
                "en"
            ) == "de"
        ) {
            selectedPosition = 7
        } else if (SharePrefUtils.getString(
                ConstantAd.LANGUAGE_CODE_FOR_SELECTION,
                "en"
            ) == "pt"
        ) {
            selectedPosition = 8
        }
        holder.itemView.setOnClickListener { listener.onItemClick(position) }
        if (selectedPosition == position) {
            Glide.with(mContext).load(R.drawable.fill_circle_vector).into(holder.btn_select_lan)
        } else {
//            holder.btn_select_lan.
//            Glide.with(mContext).load(R.drawable.blank_circle).into(holder.btn_select_lan)
            holder.btn_select_lan.setImageDrawable(null);
        }
        Glide.with(mContext).load(languageImg[position]).into(holder.flag_img)
        holder.txt_lan_name.text = languageName[position]
    }

    override fun getItemCount(): Int {
        return languageName.size
    }

    inner class Viewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val btn_select_lan: ImageView
        val flag_img: ImageView
        val txt_lan_name: TextView

        init {
            flag_img = itemView.findViewById(R.id.flag_img)
            btn_select_lan = itemView.findViewById(R.id.btn_select_lan)
            txt_lan_name = itemView.findViewById(R.id.txt_lan_name)
        }
    }

    interface OnItemClickListener {
        fun onItemClick(item: Int)
    }
}