package br.com.bootcamp.magicgamecs.features.home

import br.com.bootcamp.magicgamecs.models.pojo.Card

sealed class ItemSet(val spanSize: Int)

data class EditionItemSet(val text: String) : ItemSet(3)

data class TypeItemSet(val setCode: String, val text: String) : ItemSet(3)

data class CardItemSet(val content: Card) : ItemSet(1)

object Placeholder : ItemSet(3)
