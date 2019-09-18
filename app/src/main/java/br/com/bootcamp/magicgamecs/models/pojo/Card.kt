package br.com.bootcamp.magicgamecs.models.pojo

data class Card(
    val id: String,
    val imageUrl: String,
    val type: String = "",
    val name: String = ""
)