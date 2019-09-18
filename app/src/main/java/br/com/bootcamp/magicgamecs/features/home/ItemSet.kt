package br.com.bootcamp.magicgamecs.features.home

import br.com.bootcamp.magicgamecs.models.Card

sealed class ItemSet(val spanSize: Int)

data class EditionItemSet(val text: String) : ItemSet(3)

data class TypeItemSet(val text: String) : ItemSet(3)

data class CardItemSet(val content: Card) : ItemSet(1)
