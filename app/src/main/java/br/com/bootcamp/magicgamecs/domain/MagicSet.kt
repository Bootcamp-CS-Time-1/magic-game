package br.com.bootcamp.magicgamecs.domain

data class MagicSet(
    val code: String,
    val name: String,
    val categorizedCards: List<CategorizedCards>
)