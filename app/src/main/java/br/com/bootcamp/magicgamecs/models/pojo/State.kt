package br.com.bootcamp.magicgamecs.models.pojo

sealed class State {
    object Loading : State()
    object Loaded : State()
    data class Failed(val error: Throwable) : State()
}