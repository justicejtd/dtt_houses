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
import com.example.dtthouses.ui.about.AboutFragment.AboutFragmentConstants.SPAN_DTT_LINK_END
import com.example.dtthouses.ui.about.AboutFragment.AboutFragmentConstants.SPAN_DTT_LINK_START
import java.lang.RuntimeException

/**
 * Fragment for showing information of DTT.
 */
class AboutFragment : Fragment() {
    private lateinit var aboutFragmentCallback: AboutFragmentCallback

    /**
     * Constants values of AboutFragment.
     */
    object AboutFragmentConstants {
        /**
         * Span start value for DTT link name.
         */
        const val SPAN_DTT_LINK_START = 0

        /**
         * Span end value for DTT link name.
         */
        const val SPAN_DTT_LINK_END = 7
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is AboutFragmentCallback) {
            aboutFragmentCallback = context
        } else {
            throw RuntimeException(
                context.toString().plus(" ")
                    .plus(getString(R.string.about_fragment_runtime_exception))
            )
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
        val ss = SpannableString(getString(R.string.dtt_link_name))
        val clickableSpan: ClickableSpan = object : ClickableSpan() {
            override fun onClick(textView: View) {
                aboutFragmentCallback.showBrowser()
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
            }
        }
        ss.setSpan(
            clickableSpan,
            SPAN_DTT_LINK_START,
            SPAN_DTT_LINK_END,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        val textView = view.findViewById<TextView>(R.id.tvLink)
        textView.text = ss
        textView.movementMethod = LinkMovementMethod.getInstance()
        textView.highlightColor = Color.TRANSPARENT
    }
}