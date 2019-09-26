package br.com.bootcamp.magicgamecs.features.detail

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.bootcamp.magicgamecs.models.pojo.Card

class DetailViewModelFactory(val context: Context, val card: Card) :
    ViewModelProvider.Factory {


    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return DetailViewModel(context, card) as T
    }

}