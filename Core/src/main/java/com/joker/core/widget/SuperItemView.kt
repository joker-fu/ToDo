package com.joker.core.widget

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.RippleDrawable
import android.os.Build
import android.text.InputType
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.widget.*
import com.joker.core.ext.*
import com.joker.core.R
import kotlinx.android.synthetic.main.layout_super_item_view.view.*
import org.jetbrains.anko.dip
import org.jetbrains.anko.sp

/**
 * SuperItemView
 *
 * @author joker
 * @date 2019/6/16.
 */
class SuperItemView @JvmOverloads constructor(
        context: Context,
        attributeSet: AttributeSet? = null,
        defStyleAttr: Int = 0
) : LinearLayout(context, attributeSet, defStyleAttr) {

    //控件
    val leftImageView: ImageView get() = ivLeftImage
    val leftTextView: TextView get() = tvLeftLabel
    val leftSubTextView: TextView get() = tvLeftSubLabel
    val centerTextView: TextView get() = tvCenterLabel
    val editTextView: EditText get() = etCenterLabel
    val rightTextView: TextView get() = tvRightLabel
    val switchButtonView: Switch get() = sbRightButton
    val rightImageView: ImageView get() = ivRightImage

    private var childMarginStart: Int = dip(5)
    private var childMarginEnd: Int = childMarginStart
    //左侧图片控件属性
    private var leftImageDrawable: Drawable? = null
    private var leftImageSize: Int = -2
    //左侧文字控件属性
    private var leftText: String? = null
    private var leftTextColor: Int = color(R.color.black_grace)
    private var leftTextSize: Int = sp(16)
    //左侧子文字控件属性
    private var leftSubText: String? = null
    private var leftSubTextColor: Int = color(R.color.black_grace)
    private var leftSubTextSize: Int = sp(16)
    //中间文字控件属性
    private var centerText: String? = null
    private var centerTextColor: Int = color(R.color.grey)
    private var centerTextSize: Int = sp(14)
    //编辑框
    private var editHintText: String? = null
    private var editHintTextColor: Int = color(R.color.grey)
    private var editText: String? = null
    private var editTextColor: Int = color(R.color.black_grace)
    private var editTextSize: Int = sp(14)
    private var editInputType: Int = InputType.TYPE_CLASS_TEXT
    private var editGravity: Int = Gravity.END
    //右侧文字控件属性
    private var rightText: String? = null
    private var rightTextColor: Int = color(R.color.grey)
    private var rightTextBackground: Drawable? = null
    private var rightTextSize: Int = sp(14)
    //右侧右侧Switch控件属性
    private var rightSwitchChecked: Boolean = false
    private var rightSwitchText: String? = null
    private var rightSwitchTextColor: Int = color(R.color.grey)
    private var rightSwitchTextSize: Int = sp(14)
    //中间图片控件属性
    private var centerImageDrawable: Drawable? = null
    private var centerImageSize: Int = -2
    //右侧图片控件属性
    private var rightImageDrawable: Drawable? = null
    private var rightImageSize: Int = -2
    //是否开启水波纹效果
    private var enableRipple = true
    private var rippleColor = color(R.color.ripple_color)

    init {
        initTypedArray(context, attributeSet)
        inflate(context, R.layout.layout_super_item_view, this)
        initView()
    }

    private fun initTypedArray(context: Context, attributeSet: AttributeSet?) {
        val a = context.obtainStyledAttributes(attributeSet, R.styleable.SuperItemView)

        childMarginStart = a.getDimensionPixelSize(R.styleable.SuperItemView_siv_childMarginStart, childMarginStart)
        childMarginEnd = a.getDimensionPixelSize(R.styleable.SuperItemView_siv_childMarginEnd, childMarginEnd)

        leftImageDrawable = a.getDrawable(R.styleable.SuperItemView_siv_leftImageSrc)
        leftImageSize = a.getDimensionPixelSize(R.styleable.SuperItemView_siv_leftImageSize, leftImageSize)

        leftText = a.getString(R.styleable.SuperItemView_siv_leftText)
        leftTextColor = a.getColor(R.styleable.SuperItemView_siv_leftTextColor, leftTextColor)
        leftTextSize = a.getDimensionPixelSize(R.styleable.SuperItemView_siv_leftTextSize, leftTextSize)

        leftSubText = a.getString(R.styleable.SuperItemView_siv_leftSubText)
        leftSubTextColor = a.getColor(R.styleable.SuperItemView_siv_leftSubTextColor, leftTextColor)
        leftSubTextSize = a.getDimensionPixelSize(R.styleable.SuperItemView_siv_leftSubTextSize, leftTextSize)

        centerText = a.getString(R.styleable.SuperItemView_siv_centerText)
        centerTextColor = a.getColor(R.styleable.SuperItemView_siv_centerTextColor, centerTextColor)
        centerTextSize = a.getDimensionPixelSize(R.styleable.SuperItemView_siv_centerTextSize, centerTextSize)

        editHintText = a.getString(R.styleable.SuperItemView_siv_editHintText)
        editHintTextColor = a.getColor(R.styleable.SuperItemView_siv_editHintTextColor, editHintTextColor)
        editText = a.getString(R.styleable.SuperItemView_siv_editText)
        editTextColor = a.getColor(R.styleable.SuperItemView_siv_editTextColor, editTextColor)
        editTextSize = a.getDimensionPixelSize(R.styleable.SuperItemView_siv_editTextSize, editTextSize)
        editInputType = a.getInt(R.styleable.SuperItemView_siv_editInputType, editInputType)
        editGravity = a.getInt(R.styleable.SuperItemView_siv_editGravity, editGravity)

        rightText = a.getString(R.styleable.SuperItemView_siv_rightText)
        rightTextColor = a.getColor(R.styleable.SuperItemView_siv_rightTextColor, rightTextColor)
        rightTextSize = a.getDimensionPixelSize(R.styleable.SuperItemView_siv_rightTextSize, rightTextSize)
        rightTextBackground = a.getDrawable(R.styleable.SuperItemView_siv_rightTextBackground)

        rightSwitchChecked = a.getBoolean(R.styleable.SuperItemView_siv_rightSwitchChecked, rightSwitchChecked)
        rightSwitchText = a.getString(R.styleable.SuperItemView_siv_rightSwitchText)
        rightSwitchTextColor = a.getColor(R.styleable.SuperItemView_siv_rightSwitchTextColor, rightSwitchTextColor)
        rightSwitchTextSize = a.getDimensionPixelSize(R.styleable.SuperItemView_siv_rightSwitchTextSize, rightSwitchTextSize)

        centerImageDrawable = a.getDrawable(R.styleable.SuperItemView_siv_centerImageSrc)
        centerImageSize = a.getDimensionPixelSize(R.styleable.SuperItemView_siv_centerImageSize, centerImageSize)

        rightImageDrawable = a.getDrawable(R.styleable.SuperItemView_siv_rightImageSrc)
        rightImageSize = a.getDimensionPixelSize(R.styleable.SuperItemView_siv_rightImageSize, rightImageSize)

        enableRipple = a.getBoolean(R.styleable.SuperItemView_siv_enableRipple, enableRipple)
        rippleColor = a.getColor(R.styleable.SuperItemView_siv_rippleColor, rippleColor)

        a.recycle()
    }

    private fun initView() {
        orientation = HORIZONTAL
        gravity = Gravity.CENTER_VERTICAL
        if (Build.VERSION.SDK_INT >= 21 && enableRipple) {
            val bg = if (background != null) background else ColorDrawable(Color.TRANSPARENT)
            val rippleDrawable = RippleDrawable(ColorStateList.valueOf(rippleColor), bg, null)
            background = rippleDrawable
        }
        updateChildren()
    }

    fun setup(leftImageDrawable: Drawable? = this.leftImageDrawable,
              leftText: String? = this.leftText,
              leftSubText: String? = this.leftSubText,
              centerText: String? = this.centerText,
              editHintText: String? = this.editHintText,
              editText: String? = this.editText,
              rightText: String? = this.rightText,
              rightSwitchText: String? = this.rightSwitchText,
              centerImageDrawable: Drawable? = this.centerImageDrawable,
              rightImageDrawable: Drawable? = this.rightImageDrawable) {
        this.leftImageDrawable = leftImageDrawable
        this.leftText = leftText
        this.leftSubText = leftSubText
        this.centerText = centerText
        this.editHintText = editHintText
        this.editText = editText
        this.rightText = rightText
        this.rightSwitchText = rightSwitchText
        this.centerImageDrawable = centerImageDrawable
        this.rightImageDrawable = rightImageDrawable
        updateChildren()
    }

    private fun updateChildren() {
        //左侧图片
        if (leftImageDrawable == null) {
            ivLeftImage.gone()
        } else {
            ivLeftImage.visible()
            ivLeftImage.setImageDrawable(leftImageDrawable)
            ivLeftImage.widthHeight(leftImageSize, leftImageSize)
        }
        //左侧文字
        if (leftText == null) {
            tvLeftLabel.gone()
        } else {
            tvLeftLabel.visible()
            tvLeftLabel.text = leftText
            tvLeftLabel.setTextSize(TypedValue.COMPLEX_UNIT_PX, leftTextSize.toFloat())
            tvLeftLabel.setTextColor(leftTextColor)
        }
        if (leftSubText == null) {
            tvLeftSubLabel.gone()
        } else {
            tvLeftSubLabel.visible()
            tvLeftSubLabel.text = leftSubText
            tvLeftSubLabel.setTextSize(TypedValue.COMPLEX_UNIT_PX, leftSubTextSize.toFloat())
            tvLeftSubLabel.setTextColor(leftSubTextColor)
        }
        //中间文字
        if (centerText == null) {
            tvCenterLabel.gone()
        } else {
            tvCenterLabel.visible()
            tvCenterLabel.text = centerText
            tvCenterLabel.setTextSize(TypedValue.COMPLEX_UNIT_PX, centerTextSize.toFloat())
            tvCenterLabel.setTextColor(centerTextColor)
        }
        //编辑框
        if (editHintText == null && editText == null) {
            etCenterLabel.invisible()
            etCenterLabel.height = 0
            etCenterLabel.setPadding(0, 0, 0, 0)
        } else {
            etCenterLabel.visible()
            etCenterLabel.hint = editHintText
            etCenterLabel.setHintTextColor(editHintTextColor)
            etCenterLabel.setText(editText)
            etCenterLabel.setTextColor(editTextColor)
            etCenterLabel.setTextSize(TypedValue.COMPLEX_UNIT_PX, editTextSize.toFloat())
            etCenterLabel.gravity = editGravity
            etCenterLabel.inputType = editInputType
        }
        //右侧文字
        if (rightText == null) {
            tvRightLabel.gone()
        } else {
            tvRightLabel.visible()
            tvRightLabel.text = rightText
            tvRightLabel.setTextSize(TypedValue.COMPLEX_UNIT_PX, rightTextSize.toFloat())
            tvRightLabel.setTextColor(rightTextColor)
            tvRightLabel.background = rightTextBackground
        }
        //右侧Switch
        if (rightSwitchText == null) {
            sbRightButton.gone()
        } else {
            sbRightButton.visible()
            sbRightButton.isChecked = rightSwitchChecked
            if (!rightSwitchText.isNullOrEmpty()) {
                sbRightButton.text = rightSwitchText
                sbRightButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, rightSwitchTextSize.toFloat())
                sbRightButton.setTextColor(rightSwitchTextColor)
                sbRightButton.switchPadding = dip(2)
            }
        }
        //中间图片
        if (centerImageDrawable == null) {
            ivCenterImage.gone()
        } else {
            ivCenterImage.visible()
            ivCenterImage.setImageDrawable(centerImageDrawable)
            ivCenterImage.widthHeight(centerImageSize, centerImageSize)
        }
        //右侧图片
        if (rightImageDrawable == null) {
            ivRightImage.gone()
        } else {
            ivRightImage.visible()
            ivRightImage.setImageDrawable(rightImageDrawable)
            ivRightImage.widthHeight(rightImageSize, rightImageSize)
        }
        children.filter { it.isVisible() }.apply {
            forEachIndexed { index, view ->
                when (index) {
                    0 -> view.margin(null, null, childMarginEnd, null)
                    size - 1 -> view.margin(childMarginStart, null, null, null)
                    else -> view.margin(childMarginStart, null, childMarginEnd, null)
                }
            }
        }
    }
}