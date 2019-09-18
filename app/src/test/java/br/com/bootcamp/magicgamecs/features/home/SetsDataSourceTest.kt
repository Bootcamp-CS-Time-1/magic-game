package br.com.bootcamp.magicgamecs.features.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import androidx.paging.PageKeyedDataSource.LoadInitialParams
import br.com.bootcamp.magicgamecs.models.pojo.CardType
import br.com.bootcamp.magicgamecs.domain.LoadMagicSetsByPage
import br.com.bootcamp.magicgamecs.models.pojo.Card
import br.com.bootcamp.magicgamecs.models.pojo.MagicSet
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SetsDataSourceTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()
    private val dispatcher = Dispatchers.Unconfined

    @MockK
    lateinit var loadMagicSetsByPage: LoadMagicSetsByPage

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun givenMagicSets_whenInitialLoad_shouldTransformToItemSet() {
        // Arrange
        val initialLoadState = spyk<MutableLiveData<State>>()
        val paginatedLoadState = spyk<MutableLiveData<State>>()
        val setsDataSource =
            SetsDataSource(loadMagicSetsByPage, initialLoadState, paginatedLoadState, dispatcher)

        val card0 = Card("1", "image1")
        val card1 = Card("2", "image2")
        val card2 = Card("3", "image3")
        val card3 = Card("4", "image4")
        val card4 = Card("5", "image5")

        val typedCard0 =
            CardType("typeA", listOf(card0, card1))
        val typedCard1 = CardType(
            "typeB",
            listOf(card2, card3, card4)
        )

        val magicSet0 = MagicSet(
            "a",
            "name",
            mutableListOf(typedCard0, typedCard1)
        )

        /*
        Edtion: List
            - code
            - name
            - types: List
                - name
                - cards: List
                    - id
                    - image
         */

        val transformed = listOf(
            EditionItemSet(magicSet0.name),
            TypeItemSet(typedCard0.type),
            CardItemSet(card0),
            CardItemSet(card1),
            TypeItemSet(typedCard1.type),
            CardItemSet(card2),
            CardItemSet(card3),
            CardItemSet(card4)
        )

        coEvery {
            loadMagicSetsByPage.invoke(LoadMagicSetsByPage.Params(1))
        } returns magicSet0

        val callback = spyk<PageKeyedDataSource.LoadInitialCallback<Int, ItemSet>>()

        // Act
        setsDataSource.loadInitial(LoadInitialParams(1, true), callback)

        // Assert
        coVerify {
            callback.onResult(transformed, null, 2)
        }
    }

    @Test
    fun givenInitialLoadState_whenInitialLoad_shouldNotifyLoad() {
        // Arrange
        val initialLoadState = spyk<MutableLiveData<State>>()
        val paginatedLoadState = spyk<MutableLiveData<State>>()
        val setsDataSource =
            SetsDataSource(loadMagicSetsByPage, initialLoadState, paginatedLoadState, dispatcher)

        val magicSet0 = MagicSet(
            "a",
            "name",
            mutableListOf()
        )

        coEvery {
            loadMagicSetsByPage.invoke(LoadMagicSetsByPage.Params(1))
        } returns magicSet0

        val callback = spyk<PageKeyedDataSource.LoadInitialCallback<Int, ItemSet>>()

        // Act
        setsDataSource.loadInitial(LoadInitialParams(1, true), callback)

        // Assert
        verify {
            initialLoadState.setValue(State.Loading)
            initialLoadState.setValue(State.Loaded)
        }
    }

    @Test
    fun loadAfter() {
    }
}