package br.com.bootcamp.magicgamecs.models.pojo

import android.os.Parcelable
import com.google.gson.annotations.Expose
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Collection(
    val code: String,
    val name: String,
    @Expose(deserialize = false)
    val typedCards: MutableList<CardType> = mutableListOf()
) : Parcelable