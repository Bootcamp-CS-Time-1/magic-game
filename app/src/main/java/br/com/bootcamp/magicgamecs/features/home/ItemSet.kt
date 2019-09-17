package br.com.bootcamp.magicgamecs.features.home

interface ItemSet

data class TitleItemSet(val text: String) : ItemSet
data class SubtitleItemSet(val text: String) : ItemSet
data class CardItemSet(val id: String, val image: String) : ItemSet