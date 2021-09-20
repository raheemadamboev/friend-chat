package xyz.teamgravity.friendchat.helper.extension

import android.view.View
import com.google.android.material.textfield.TextInputLayout

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun View.disable() {
    isEnabled = false
}

fun View.enable() {
    isEnabled = true
}

fun TextInputLayout.clearError() {
    error = null
}

fun TextInputLayout.clearErrorFocus() {
    error = null
    clearFocus()
}

fun TextInputLayout.text(): String {
    return editText?.text.toString().trim()
}

fun TextInputLayout.setText(text: String) {
    editText?.setText(text)
}

fun TextInputLayout.error(error: String) {
    this.error = error
    requestFocus()
}