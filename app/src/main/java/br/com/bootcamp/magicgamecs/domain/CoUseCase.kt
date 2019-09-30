package br.com.bootcamp.magicgamecs.domain

/**
 * Classe base para os UseCase
 *
 * Para `UseCases` com parametros, estender `UseCase<Params, Source>()`
 * Onde `Params` será a classe que possuirá as informações do parametro e `Source` o retorno do `UseCase`
 * Exemplo:
 * ```
 * class SearchFooByTermSorted(val fooRepository: FooRepository) : UseCase<SearchFooByTermSorted.Params, List<Foo>> {
 *      override suspend fun run(params: SearchFooByTermSorted.Params): List<Foo> {
 *          // Regras de negócio
 *      }
 *
 *      data class Params(val searchTerm: String, val sortBy: String)
 * }
 * ```
 *
 * uso:
 *
 * ```
 * class FooViewModel(private val searchFooByTermSorted: SearchFooByTermSorted) {
 *      fun search(term: String, sortBy: String) {
 *          // Some code
 *          val foos = searchFooByTermSorted(SearchFooByTermSorted.Params(term, sortBy))
 *          // Remaining code
 *      }
 * }
 * ```
 *
 *
 * Para `UseCases` que não necessitam de parametros estender `UseCase.NoParam<Source>`
 * Exemplo:
 * ```
 * class ListAllBar(val barRepository: BarRepository) : UseCase.NoParam<List<Bar>> {
 *      override suspend fun run(): List<Bar> {
 *          // Regras de negócio
 *      }
 * }
 * ```
 */
abstract class CoUseCase<Params, Source> {
    abstract suspend fun run(params: Params): Source

    @Throws(Exception::class)
    suspend operator fun invoke(params: Params) = run(params)

    abstract class NoParam<Source> : CoUseCase<None, Source>() {
        abstract suspend fun run(): Source

        final override suspend fun run(params: None) =
            throw UnsupportedOperationException()

        @Throws(Exception::class)
        suspend operator fun invoke(): Source = run()
    }

    object None
}