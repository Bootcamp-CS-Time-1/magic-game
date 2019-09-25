package br.com.bootcamp.magicgamecs.models.pojo

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Card(
    val id: String,
    val imageUrl: String?,
    val type: String = "",
    val name: String = "",
    val types: List<String> = listOf()
) : Parcelable