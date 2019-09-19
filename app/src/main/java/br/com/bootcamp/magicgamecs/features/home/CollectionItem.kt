package br.com.bootcamp.magicgamecs.features.home

import br.com.bootcamp.magicgamecs.models.pojo.Card

sealed class CollectionItem(val spanSize: Int)

data class NameCollectionItem(val text: String) : CollectionItem(3)

data class CardTypeItem(val setCode: String, val text: String) : CollectionItem(3)

data class CardItem(val content: Card) : CollectionItem(1)

object Placeholder : CollectionItem(3)
