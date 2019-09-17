package br.com.bootcamp.magicgamecs.features.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.PageKeyedDataSource
import br.com.bootcamp.magicgamecs.domain.Card
import br.com.bootcamp.magicgamecs.domain.CategorizedCards
import br.com.bootcamp.magicgamecs.domain.LoadMagicSetsByPage
import br.com.bootcamp.magicgamecs.domain.MagicSet
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

    lateinit var setsDataSource: SetsDataSource

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        setsDataSource = SetsDataSource(loadMagicSetsByPage, dispatcher)
    }

    @Test
    fun loadInitial() {
        // Arrange
        val card0 = Card("1", "image1")
        val card1 = Card("2", "image2")
        val card2 = Card("3", "image3")
        val card3 = Card("4", "image4")
        val card4 = Card("5", "image5")

        val categorizedCards0 = CategorizedCards("typeA", listOf(card0, card1))
        val categorizedCards1 = CategorizedCards("typeB", listOf(card2, card3, card4))

        val magicSet0 = MagicSet("a", "name", listOf(categorizedCards0, categorizedCards1))

        coEvery {
            loadMagicSetsByPage.invoke(LoadMagicSetsByPage.Params(1))
        } returns listOf(magicSet0)

        val callback = spyk<PageKeyedDataSource.LoadInitialCallback<Int, ItemSet>>()

        // Act
        setsDataSource.loadInitial(PageKeyedDataSource.LoadInitialParams(1, true), callback)

        // Verify
        coVerify {
            callback.onResult(
                listOf(
                    TitleItemSet(magicSet0.name),
                    SubtitleItemSet(categorizedCards0.type),
                    CardItemSet(card0.id, card0.image),
                    CardItemSet(card1.id, card1.image),
                    SubtitleItemSet(categorizedCards1.type),
                    CardItemSet(card2.id, card2.image),
                    CardItemSet(card3.id, card3.image),
                    CardItemSet(card4.id, card4.image)
                ), null, 2
            )
        }
    }

    @Test
    fun loadAfter() {
    }
}