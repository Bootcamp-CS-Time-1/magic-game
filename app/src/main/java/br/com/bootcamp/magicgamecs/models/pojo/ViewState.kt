package br.com.bootcamp.magicgamecs.models.pojo

sealed class ViewState<out T> {

    object FirstLaunch : ViewState<Nothing>()

    sealed class Loading<T> : ViewState<T>() {
        object FromEmpty : Loading<Nothing>()
        data class FromPrevious<T>(val previous: T) : Loading<T>()
    }

    sealed class Failed<T>(val reason: Throwable) : ViewState<T>() {
        class FromEmpty(reason: Throwable) : Failed<Nothing>(reason)
        class FromPrevious<T>(reason: Throwable, val previous: T) : Failed<T>(reason)
    }

    data class Success<T>(val value: T) : ViewState<T>()
}