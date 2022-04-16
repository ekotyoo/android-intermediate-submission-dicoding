package com.ekotyoo.storyapp.ui

import android.content.Context
import android.graphics.Canvas
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Patterns
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.ekotyoo.storyapp.R

class EmailEditText : AppCompatEditText {

    private val roundedBackground =
        ContextCompat.getDrawable(context, R.drawable.bg_custom_edittext)

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    private fun init() {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(text: Editable?) {
                text?.let {
                    if (text.isNotEmpty() && !Patterns.EMAIL_ADDRESS.matcher(it).matches()) error =
                        context.applicationContext.getString(R.string.email_not_valid)
                }
            }
        })
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        background = roundedBackground
        hint = "Email"
        textSize = 14F
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
    }
}