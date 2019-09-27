package br.com.bootcamp.magicgamecs.core.di.modules

import br.com.bootcamp.magicgamecs.domain.FetchCollectionPage
import br.com.bootcamp.magicgamecs.features.detail.DetailViewModel
import br.com.bootcamp.magicgamecs.features.home.CollectionViewModel
import br.com.bootcamp.magicgamecs.models.repository.MagicRepository
import br.com.bootcamp.magicgamecs.models.repository.impl.MagicRepositoryDb
import br.com.bootcamp.magicgamecs.models.repository.impl.MagicRepositoryImpl
import br.com.bootcamp.magicgamecs.models.room.AppDataBase
import br.com.bootcamp.magicgamecs.models.room.CardDao
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
        CollectionViewModel(get())
    }

    factory {
        FetchCollectionPage(get())
    }

    factory<MagicRepository> {
        MagicRepositoryImpl(get(), get())
    }

    factory {
        DetailViewModel(get())
    }

    single {
        AppDataBase.getInstance(get())
    }

    factory {
        get<AppDataBase>().cardDao()
    }



}