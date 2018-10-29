package com.example.yervand.ftstest.utill

import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import androidx.databinding.adapters.ListenerUtil
import com.google.android.material.textfield.TextInputEditText
import android.view.View
import android.widget.TextView
import com.example.yervand.ftstest.R
import com.example.yervand.ftstest.viewmodel.base.Command
import android.graphics.Typeface
import android.text.style.TextAppearanceSpan
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import android.text.*
import android.text.method.LinkMovementMethod
import android.util.Log
import androidx.databinding.ObservableArrayList
import com.example.yervand.ftstest.db.model.CodexEntity
import com.example.yervand.ftstest.db.model.CodexEntityType.*
import java.util.ArrayList


object BindingAdapters {

    @BindingAdapter(value = ["command"])
    @JvmStatic
    fun bindViewCommand(view: View, command: Command) {
        view.setOnClickListener { command.execute(null) }
    }

    @BindingAdapter(value = ["isVisible"])
    @JvmStatic
    fun bindViewVisibility(view: View, isVisible: Any) {
        val visible = getVisibility(isVisible)
        view.visibility = if (visible) View.VISIBLE else View.INVISIBLE
    }

    private fun getVisibility(visibility: Any?): Boolean {
        var isVisible = true
        when (visibility) {
            null -> isVisible = false
            is Boolean -> isVisible = (visibility as Boolean?)!!
            is String -> isVisible = !visibility.isEmpty()
        }
        return isVisible
    }

    @BindingAdapter(value = ["bindableText", "textChanged"], requireAll = false)
    @JvmStatic
    fun getBindableText(editText: TextInputEditText, message: String, listener: InverseBindingListener) {
        if (editText.text!!.toString() == message) return
        editText.setText(message)
        editText.setSelection(editText.text!!.length)
        val watcher = object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                listener.onChange()
            }

            override fun afterTextChanged(editable: Editable) {

            }
        }
        val oldValue = ListenerUtil.trackListener(editText, watcher, R.id.textWatcher)
        if (oldValue != null) {
            editText.removeTextChangedListener(oldValue)
        }
        editText.addTextChangedListener(watcher)
    }

    @InverseBindingAdapter(attribute = "bindableText", event = "textChanged")
    @JvmStatic
    fun TextInputEditText.setBindableText(): String {
        return this.text!!.toString()
    }

    @BindingAdapter("spannableText")
    @JvmStatic
    fun highlight(textView: TextView, spannableText: String) {

        val startPositions: MutableList<Int> = ArrayList()
        val endPositions: MutableList<Int> = ArrayList()

        var tagCount = 0
        for (index in spannableText.indices) {
            if (spannableText[index] == '<') {
                startPositions += index - tagCount++
            } else if (spannableText[index] == '>') {
                endPositions += index - tagCount++
            }
        }

        val cleanText = spannableText.replace(Regex("[<>]"), "")
        val spannable = SpannableStringBuilder(cleanText)
        val blueColor = ColorStateList(arrayOf(intArrayOf()), intArrayOf(Color.BLUE))

        startPositions.forEach {
            val highlightSpan = TextAppearanceSpan(null, Typeface.BOLD, -1, blueColor, null)
            spannable.setSpan(highlightSpan, it, endPositions[startPositions.indexOf(it)], Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }

        if (!startPositions.isEmpty()) {
            textView.text = spannable
        } else {
            textView.text = spannableText
        }
    }

    @BindingAdapter("htmlText")
    @JvmStatic
    fun highlightFromHTML(textView: TextView, htmlText: String) {
        textView.linksClickable = true
        textView.movementMethod = LinkMovementMethod.getInstance()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            textView.text = Html.fromHtml(htmlText, Html.FROM_HTML_MODE_COMPACT)
        } else {
            textView.text = Html.fromHtml(htmlText)
        }
    }

}
