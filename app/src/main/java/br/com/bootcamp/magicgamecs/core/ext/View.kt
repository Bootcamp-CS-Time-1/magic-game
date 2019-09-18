package br.com.bootcamp.magicgamecs.core.ext

import android.view.View

fun View.show() {
    visibility = View.VISIBLE
}

fun View.gone() {
    visibility = View.GONE
}