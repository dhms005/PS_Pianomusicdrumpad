package com.pianomusicdrumpad.pianokeyboard.Piano.viewgroup

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup

class HomeViewGroup : ViewGroup {
    constructor(context: Context?) : super(context)

    constructor(context: Context?, attributeSet: AttributeSet?) : super(context, attributeSet)

    constructor(context: Context?, attributeSet: AttributeSet?, i: Int) : super(
        context,
        attributeSet,
        i
    )


    public override fun onLayout(z: Boolean, i: Int, i2: Int, i3: Int, i4: Int) {
        if (childCount == 3) {
            getChildAt(0).layout(0, 0, measuredWidth, getChildAt(0).measuredHeight)
            getChildAt(1).layout(
                0,
                getChildAt(0).measuredHeight,
                measuredWidth,
                getChildAt(0).measuredHeight + getChildAt(1).measuredHeight
            )
            getChildAt(2).layout(
                0, measuredHeight - getChildAt(2).measuredHeight,
                measuredWidth, measuredHeight
            )
        }
    }


    public override fun onMeasure(i: Int, i2: Int) {
        super.onMeasure(i, i2)
        if (childCount == 3) {
            measureChild(
                getChildAt(0),
                i,
                MeasureSpec.makeMeasureSpec(
                    MeasureSpec.getSize((measuredHeight * 310) / 1280),
                    MeasureSpec.getMode(i2)
                )
            )
            measureChild(
                getChildAt(1),
                i,
                MeasureSpec.makeMeasureSpec(
                    MeasureSpec.getSize((measuredHeight * 661) / 1280),
                    MeasureSpec.getMode(i2)
                )
            )
            measureChild(
                getChildAt(2),
                i,
                MeasureSpec.makeMeasureSpec(
                    MeasureSpec.getSize((measuredHeight * 309) / 1280),
                    MeasureSpec.getMode(i2)
                )
            )
        }
    }
}
