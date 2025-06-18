package com.pianomusicdrumpad.pianokeyboard.Piano.views

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import com.pianomusicdrumpad.pianokeyboard.Piano.Activity.C0885b
import com.pianomusicdrumpad.pianokeyboard.R

class HomeBgView : View {
    private var f239a: Bitmap? = null

    constructor(context: Context?) : super(context)

    constructor(context: Context?, attributeSet: AttributeSet?) : super(context, attributeSet)

    constructor(context: Context?, attributeSet: AttributeSet?, i: Int) : super(
        context,
        attributeSet,
        i
    )


    public override fun onDraw(canvas: Canvas) {
        canvas.drawBitmap(
            f239a!!,
            null as Rect?,
            RectF(0.0f, 0.0f, width.toFloat(), height.toFloat()),
            null as Paint?
        )
    }


    public override fun onLayout(z: Boolean, i: Int, i2: Int, i3: Int, i4: Int) {
        super.onLayout(z, i, i2, i3, i4)
        this.f239a = C0885b.m77a(resources, R.drawable.chose_bg, width, height)
    }
}
