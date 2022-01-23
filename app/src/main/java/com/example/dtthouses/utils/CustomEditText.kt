package com.example.dtthouses.utils

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.example.dtthouses.R

fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }
    })
}

/**
 * Add ability to clear input with right button
 *
 * Arguments:
 *  @param onIsNotEmpty - callback which is invoked when input is completed and is not empty. Is good for clearing error
 *  @param onCanceled - callbacks which is invoked when cancel button is clicked and input is cleared
 *  @param clearDrawable - right drawable which is used as cancel button to clear input
 */
fun EditText.makeClearableEditText(
    onIsNotEmpty: (() -> Unit)?,
    onCanceled: (() -> Unit)?,
    clearDrawable: Drawable
) {
    val icon = ResourcesCompat.getDrawable(
        resources,
        R.drawable.ic_search,
        null
    )

    icon?.setBounds(
        0, 0,
        icon.intrinsicWidth,
        icon.intrinsicHeight
    )

    val updateRightDrawable = {
        this.setCompoundDrawables(null, null, null, null)

        this.setCompoundDrawables(
            null, null,
            if (hasFocus()) clearDrawable else icon, null
        )
    }
    updateRightDrawable()

    this.setOnFocusChangeListener { _, _ ->
        updateRightDrawable()
    }

    this.afterTextChanged {
        if (it.isNotEmpty()) {
            onIsNotEmpty?.invoke()
        }
        updateRightDrawable()
    }
    this.onRightDrawableClicked {
            hideSoftKeyboard(this)
            this.text.clear()
            this.clearFocus()
            this.setCompoundDrawables(null, null, icon, null)
            onCanceled?.invoke()
    }
}

private const val COMPOUND_DRAWABLE_RIGHT_INDEX = 2

/**
 *
 * Calculate right compound drawable and in case it exists calls
 * @see EditText.makeClearableEditText
 *
 * Arguments:
 *  @param onIsNotEmpty - callback which is invoked when input is completed and is not empty. Is good for clearing error
 *  @param onCanceled - callbacks which is invoked when cancel button is clicked and input is cleared
 */
fun EditText.makeClearableEditText(onIsNotEmpty: (() -> Unit)?, onCanceled: (() -> Unit)?) {
    compoundDrawables[COMPOUND_DRAWABLE_RIGHT_INDEX]?.let { clearDrawable ->
        makeClearableEditText(onIsNotEmpty, onCanceled, clearDrawable)
    }
}

/**
 * Based on View.OnTouchListener. Be careful EditText replaces old View.OnTouchListener when setting new one
 */
@SuppressLint("ClickableViewAccessibility")
fun EditText.onRightDrawableClicked(onClicked: (view: EditText) -> Unit) {
    this.setOnTouchListener { v, event ->
        var hasConsumed = false
        if (v is EditText) {
            if (event.x >= v.width - v.totalPaddingRight) {
                if (event.action == MotionEvent.ACTION_UP) {
                    onClicked(this)
                }
                hasConsumed = true
            }
        }
        hasConsumed
    }
}

private fun hideSoftKeyboard(view: View) {
    // extension function to hide soft keyboard programmatically
    val imm = view.let {
        ContextCompat.getSystemService(
            it.context,
            InputMethodManager::class.java
        )
    }
    imm?.hideSoftInputFromWindow(view.windowToken, 0)
}