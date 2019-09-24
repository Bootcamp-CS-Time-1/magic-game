package br.com.bootcamp.magicgamecs.domain

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import br.com.bootcamp.magicgamecs.models.pojo.Card
import br.com.bootcamp.magicgamecs.models.pojo.CardType
import br.com.bootcamp.magicgamecs.models.pojo.Collection
import br.com.bootcamp.magicgamecs.models.pojo.PageResult
import br.com.bootcamp.magicgamecs.models.repository.MagicRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class FetchCollectionPageTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = TestCoroutineDispatcher()

    @MockK
    lateinit var repository: MagicRepository

    lateinit var fetchCollectionPage: FetchCollectionPage

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(testDispatcher)
        fetchCollectionPage = FetchCollectionPage(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun givenFetchFirstPage_whenOnlyOnePage_shouldLoadData() {
        runBlocking {
            // Arrange
            val collection = Collection("code", "Name")
            val card0 = Card("1", null, "type A")
            val card1 = Card("2", null, "type A")

            coEvery {
                repository.getAllSets()
            } returns listOf(collection)

            coEvery {
                repository.getAllCardsBySetCode("code")
            } returns listOf(card0, card1)

            // Act
            val result = fetchCollectionPage(FetchCollectionPage.Params(0))

            // Assert
            val expectedResult = Collection(
                "code", "Name", mutableListOf(
                    CardType(
                        "type A", listOf(
                            card0, card1
                        )
                    )
                )
            )
            assertEquals(PageResult(listOf(expectedResult), 1, null), result)
        }
    }

    @Test
    fun givenFetchFirstPage_whenMoreThanOnePage_shouldLoadData() {
        runBlocking {
            // Arrange
            val collection0 = Collection("code 1", "Name 1")
            val collection1 = Collection("code 2", "Name 2")
            val card0 = Card("1", null, "type A")
            val card1 = Card("2", null, "type A")

            coEvery {
                repository.getAllSets()
            } returns listOf(collection0, collection1)

            coEvery {
                repository.getAllCardsBySetCode("code 1")
            } returns listOf(card0, card1)

            // Act
            val result = fetchCollectionPage(FetchCollectionPage.Params(0))

            // Assert
            val expectedResult = Collection(
                collection0.code, collection0.name, mutableListOf(
                    CardType(
                        "type A", listOf(
                            card0, card1
                        )
                    )
                )
            )
            assertEquals(PageResult(listOf(expectedResult), 2, 1), result)
        }
    }

    @Test
    fun givenFetchSecondPage_whenMoreThanOnePage_shouldLoadData() {
        runBlocking {
            // Arrange
            val collection0 = Collection("code 1", "Name 1")
            val collection1 = Collection("code 2", "Name 2")
            val card0 = Card("1", null, "type A")
            val card1 = Card("2", null, "type A")
            val card2 = Card("3", null, "type B")

            coEvery {
                repository.getAllSets()
            } returns listOf(collection0, collection1)

            coEvery {
                repository.getAllCardsBySetCode("code 1")
            } returns listOf(card0, card1)

            coEvery {
                repository.getAllCardsBySetCode("code 2")
            } returns listOf(card2)

            fetchCollectionPage(FetchCollectionPage.Params(0))

            // Act
            val result = fetchCollectionPage(FetchCollectionPage.Params(1))

            // Assert
            val expectedResult = listOf(
                Collection(
                    collection0.code, collection0.name, mutableListOf(
                        CardType("type A", listOf(card0, card1))
                    )
                ),
                Collection(
                    collection1.code, collection1.name, mutableListOf(
                        CardType("type B", listOf(card2))
                    )
                )
            )
            assertEquals(PageResult(expectedResult, 2, null), result)
        }
    }

    @Test
    fun givenFetchPage_whenMultipleTypeCards_shouldLoadData() {
        runBlocking {
            // Arrange
            val collection = Collection("code 1", "Name 1")
            val card0 = Card("1", null, "type A")
            val card1 = Card("2", null, "type B")
            val card2 = Card("3", null, "type C")
            val card3 = Card("4", null, "type C")

            coEvery {
                repository.getAllSets()
            } returns listOf(collection)

            coEvery {
                repository.getAllCardsBySetCode("code 1")
            } returns listOf(card0, card1, card2, card3)

            // Act
            val result = fetchCollectionPage(FetchCollectionPage.Params(0))

            // Assert
            val expectedResult = listOf(
                Collection(
                    collection.code, collection.name, mutableListOf(
                        CardType("type A", listOf(card0)),
                        CardType("type B", listOf(card1)),
                        CardType("type C", listOf(card2, card3))
                    )
                )
            )
            assertEquals(PageResult(expectedResult, 1, null), result)
        }
    }

    @Test(expected = IllegalArgumentException::class)
    fun givenFetchPage_whenNegativePage_shouldThrowError() {
        runBlocking {
            // Arrange
            val collection = Collection("code 1", "Name 1")

            coEvery {
                repository.getAllSets()
            } returns listOf(collection)

            // Act
            fetchCollectionPage(FetchCollectionPage.Params(-1))
        }
    }

    @Test(expected = IllegalArgumentException::class)
    fun givenFetchPage_whenInvalidPage_shouldThrowError() {
        runBlocking {
            // Arrange
            val collection = Collection("code 1", "Name 1")

            coEvery {
                repository.getAllSets()
            } returns listOf(collection)

            // Act
            fetchCollectionPage(FetchCollectionPage.Params(1))
        }
    }
}