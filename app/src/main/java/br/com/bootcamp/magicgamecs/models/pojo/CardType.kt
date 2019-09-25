package br.com.bootcamp.magicgamecs.models.pojo

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CardType(
    val type: String,
    val cards: List<Card>
) : Parcelable