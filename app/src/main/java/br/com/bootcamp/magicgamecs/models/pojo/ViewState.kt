package br.com.bootcamp.magicgamecs.models.pojo

sealed class ViewState<out T> {

    object FirstLaunch : ViewState<Nothing>()
    object Empty : ViewState<Nothing>()

    sealed class Loading<T> : ViewState<T>() {
        object FromEmpty : Loading<Nothing>()
        data class FromPrevious<T>(val previous: T) : Loading<T>()
    }

    sealed class Failed<T> : ViewState<T>() {
        data class FromEmpty(val reason: Throwable) : Failed<Nothing>()
        data class FromPrevious<T>(val reason: Throwable, val previous: T) : Failed<T>()
    }

    data class Success<T>(val value: T) : ViewState<T>()
}