package com.pianomusicdrumpad.pianokeyboard.Piano.viewgroup

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup

class PianoRecordsScreenViewGroup : ViewGroup {
    constructor(context: Context?) : super(context)

    constructor(context: Context?, attributeSet: AttributeSet?) : super(context, attributeSet)

    constructor(context: Context?, attributeSet: AttributeSet?, i: Int) : super(
        context,
        attributeSet,
        i
    )


    public override fun onLayout(z: Boolean, i: Int, i2: Int, i3: Int, i4: Int) {
        val childAt = getChildAt(0)
        childAt.layout(0, 0, childAt.measuredWidth, childAt.measuredHeight)
        val childAt2 = getChildAt(1)
        childAt2.layout(0, getChildAt(0).measuredHeight, childAt2.measuredWidth, measuredHeight)
    }


    public override fun onMeasure(i: Int, i2: Int) {
        setMeasuredDimension(MeasureSpec.getSize(i), MeasureSpec.getSize(i2))
        val makeMeasureSpec = MeasureSpec.makeMeasureSpec(
            MeasureSpec.getSize((measuredHeight * 194) / 1280),
            MeasureSpec.getMode(i2)
        )
        val makeMeasureSpec2 = MeasureSpec.makeMeasureSpec(measuredWidth, MeasureSpec.getMode(i))
        measureChild(getChildAt(0), makeMeasureSpec2, makeMeasureSpec)
        measureChild(
            getChildAt(1),
            makeMeasureSpec2,
            MeasureSpec.makeMeasureSpec(
                measuredHeight - getChildAt(0).measuredHeight,
                MeasureSpec.getMode(i2)
            )
        )
    }
}
