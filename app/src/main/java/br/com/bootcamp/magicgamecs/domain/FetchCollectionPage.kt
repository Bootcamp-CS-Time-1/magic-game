package br.com.bootcamp.magicgamecs.domain

import br.com.bootcamp.magicgamecs.models.pojo.CardType
import br.com.bootcamp.magicgamecs.models.pojo.Collection
import br.com.bootcamp.magicgamecs.models.pojo.PageResult
import br.com.bootcamp.magicgamecs.models.repository.MagicRepository

class FetchCollectionPage(
    private val repository: MagicRepository
) : UseCase<FetchCollectionPage.Params, PageResult<List<Collection>>>() {

    private val sets = mutableListOf<Collection>()

    override suspend fun run(params: Params): PageResult<List<Collection>> {
        val magicSets = getSets()
        val magicSet = magicSets[params.page]

        if (magicSet.typedCards.isEmpty()) {
            val grouped = repository.getAllCardsBySetCode(magicSet.code)
                .groupBy { it.type }
                .map { CardType(it.key, it.value) }

            magicSet.typedCards.addAll(grouped)
        }

        return PageResult(
            // carrega os dados a partir da primeira pagina ate a solicitada
            data = magicSets.slice(0..params.page),
            total = sets.size,
            // passar nulo caso a proxima pagina nao exista
            nextPage = params.page.plus(1).takeIf { it < sets.size }
        )
    }

    private suspend fun getSets(): List<Collection> {
        if (sets.isEmpty()) {
            sets.addAll(repository.getSets())
        }
        return sets
    }

    data class Params(val page: Int = 0)
}