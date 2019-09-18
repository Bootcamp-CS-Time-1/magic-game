package br.com.bootcamp.magicgamecs.core.di.modules

import br.com.bootcamp.magicgamecs.domain.LoadMagicSetsByPage
import br.com.bootcamp.magicgamecs.features.home.SetsDataSourceFactory
import br.com.bootcamp.magicgamecs.features.home.SetsViewModel
import br.com.bootcamp.magicgamecs.models.repository.MagicRepository
import br.com.bootcamp.magicgamecs.models.repository.impl.MagicRepositoryImpl
import org.koin.dsl.module

/**
 * Para declarar dependências de ViewModel
 * no modulo definir da seguinte maneira:
 *
 * ```
 * viewModel {
 *      FooBarViewModel(get())
 * }
 * ```
 *
 * para utilizar na Activity ou Fragment utilizar:
 * ```
 * private val fooBarViewModel by viewModel<FooBarViewModel>()
 * ```
 *
 * onde `get()` prove a dependência necessária para o construtor da ViewModel em questão que deve ser declarada também
 *
 * ```
 * factory {
 *      FooBarUseCase(get())
 * }
 * ```
 *
 * ou se for singleton:
 *
 * ```
 * single {
 *      FooBarUseCase(get())
 * }
 * ```
 *
 * Para interfaces utilizar a sintaxe semelhante à:
 *
 * ```
 * factory<FooBarRepository> {
 *      FooBarRepositoryImpl(get(), get())
 * }
 * ```
 *
 * Para dependencias na Activity, Fragment e etc, que não são ViewModels, utilizar o `inject<Type>()`
 * ```
 * private val fooBarAdapter by inject<FooBarAdapter>()
 * ```
 *
 */
val appModule = module {
    factory {
        SetsViewModel(get())
    }

    factory {
        SetsDataSourceFactory(get())
    }

    factory {
        LoadMagicSetsByPage(get())
    }

    factory<MagicRepository> {
        MagicRepositoryImpl(get())
    }
}