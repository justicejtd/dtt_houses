package com.example.dtthouses.ui.about

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.text.method.LinkMovementMethod
import android.content.Context
import android.widget.TextView
import android.text.Spanned
import android.text.TextPaint
import android.graphics.Color
import android.text.style.ClickableSpan
import android.text.SpannableString
import com.example.dtthouses.R
import java.lang.RuntimeException


class AboutFragment : Fragment() {

    private lateinit var aboutFragmentCallback: AboutFragmentCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is AboutFragmentCallback) {
            aboutFragmentCallback = context
        } else {
            throw RuntimeException(context.toString().plus(" must implement AboutFragmentCallback"))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_about, container, false)

        // Sett clickable link
        setDTTLink(view)

        return view
    }

    private fun setDTTLink(view: View) {
        val ss = SpannableString("d-tt.nl")
        val clickableSpan: ClickableSpan = object : ClickableSpan() {
            override fun onClick(textView: View) {
                aboutFragmentCallback.showBrowser()
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
            }
        }
        ss.setSpan(clickableSpan, 0, 7, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        val textView = view.findViewById<TextView>(R.id.tvLink)
        textView.text = ss
        textView.movementMethod = LinkMovementMethod.getInstance()
        textView.highlightColor = Color.TRANSPARENT
    }
}