package br.com.bootcamp.magicgamecs.core.base

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class ViewHolder<T>(view: View) : RecyclerView.ViewHolder(view) {
    open fun bind(item: T) {
    }
}