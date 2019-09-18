package br.com.bootcamp.magicgamecs.domain

import br.com.bootcamp.magicgamecs.models.MagicSet

class LoadMagicSetsByPage : UseCase<LoadMagicSetsByPage.Params, List<MagicSet>>() {
    override suspend fun run(params: Params): List<MagicSet> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    data class Params(val page: Int)
}