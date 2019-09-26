package br.com.bootcamp.magicgamecs.models.repository.impl

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import br.com.bootcamp.magicgamecs.models.pojo.Card
import br.com.bootcamp.magicgamecs.models.pojo.CardsResponse
import br.com.bootcamp.magicgamecs.models.repository.MagicRepository
import br.com.bootcamp.magicgamecs.models.retrofit.services.WebServices
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerifyOrder
import io.mockk.confirmVerified
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class MagicRepositoryTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = TestCoroutineDispatcher()

    @MockK
    lateinit var webServices: WebServices

    private lateinit var repository: MagicRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(testDispatcher)
        repository = MagicRepositoryImpl(webServices, 5)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun givenListCards_whenFirstPageIsLessThanLimit_shouldReturnFistPageWithoutCheckNext() {
        runBlocking {
            val cards = (1..4).map { Card(id = it.toString(), set = "a") }

            coEvery {
                webServices.listCards(null, 1, 5, "set", null)
            } returns CardsResponse(cards)

            val result = repository.searchCards(1)

            coVerifyOrder {
                webServices.listCards(null, 1, 5, "set", null)
            }

            confirmVerified(webServices)

            assertEquals(cards, result.data)
            assertEquals(null, result.nextPage)
        }
    }

    @Test
    fun givenListCards_whenNextPageEmpty_shouldCheckNextAndReturnOnlyFirstPage() {
        runBlocking {
            val cards = (1..5).map { Card(id = it.toString(), set = "a") }

            coEvery {
                webServices.listCards(null, 1, 5, "set", null)
            } returns CardsResponse(cards)

            coEvery {
                webServices.listCards(null, 2, 5, "set", null)
            } returns CardsResponse(listOf())

            val result = repository.searchCards(1)

            coVerifyOrder {
                webServices.listCards(null, 1, 5, "set", null)
                webServices.listCards(null, 2, 5, "set", null)
            }

            confirmVerified(webServices)

            assertEquals(cards, result.data)
            assertEquals(null, result.nextPage)
        }
    }

    @Test
    fun givenListCards_whenNextPage_shouldReturnBothPages() {
        runBlocking {
            val cards0 = (1..5).map { Card(id = it.toString(), set = "a") }
            val cards1 = (6..9).map { Card(id = it.toString(), set = "a") }
            val cards = cards0 + cards1

            coEvery {
                webServices.listCards(null, 1, 5, "set", null)
            } returns CardsResponse(cards0)

            coEvery {
                webServices.listCards(null, 2, 5, "set", null)
            } returns CardsResponse(cards1)

            val result = repository.searchCards(1)

            coVerifyOrder {
                webServices.listCards(null, 1, 5, "set", null)
                webServices.listCards(null, 2, 5, "set", null)
            }

            confirmVerified(webServices)

            assertEquals(cards, result.data)
            assertEquals(null, result.nextPage)
        }
    }

    @Test
    fun givenListCards_whenMultipleSetsInSamePage_shouldReturnOnlyFromFirstSet() {
        runBlocking {
            val cards = (1..4).map { Card(id = it.toString(), set = "a") }
            val response = cards + Card(id = "5", set = "b")

            coEvery {
                webServices.listCards(null, 1, 5, "set", null)
            } returns CardsResponse(response)

            val result = repository.searchCards(1)

            coVerifyOrder {
                webServices.listCards(null, 1, 5, "set", null)
            }

            confirmVerified(webServices)

            assertEquals(cards, result.data)
            assertEquals(1, result.nextPage)
        }
    }

    @Test
    fun givenSecondPageOtherSet() {
        runBlocking {
            val cards0 = (1..5).map { Card(id = it.toString(), set = "a") }
            val cards1 = (6..10).map { Card(id = it.toString(), set = "b") }

            coEvery {
                webServices.listCards(null, 1, 5, "set", null)
            } returns CardsResponse(cards0)

            coEvery {
                webServices.listCards(null, 2, 5, "set", null)
            } returns CardsResponse(cards1)

            val result = repository.searchCards(1)

            coVerifyOrder {
                webServices.listCards(null, 1, 5, "set", null)
                webServices.listCards(null, 2, 5, "set", null)
            }

            confirmVerified(webServices)

            assertEquals(cards0, result.data)
            assertEquals(2, result.nextPage)
        }
    }

    @Test
    fun givenSecondPageMultiSet() {
        runBlocking {
            val cards0 = (1..5).map { Card(id = it.toString(), set = "a") }
            val cards1 = listOf(Card(id = "6", set = "a")) + (7..10).map {
                Card(
                    id = it.toString(),
                    set = "b"
                )
            }
            val expected = cards0 + cards1.first()

            coEvery {
                webServices.listCards(null, 1, 5, "set", null)
            } returns CardsResponse(cards0)

            coEvery {
                webServices.listCards(null, 2, 5, "set", null)
            } returns CardsResponse(cards1)

            val result = repository.searchCards(1)

            coVerifyOrder {
                webServices.listCards(null, 1, 5, "set", null)
                webServices.listCards(null, 2, 5, "set", null)
            }

            confirmVerified(webServices)

            assertEquals(expected, result.data)
            assertEquals(2, result.nextPage)
        }
    }

    @Test
    fun shouldLoadNextPagePartial() {
        runBlocking {
            val cards0 = (1..4).map { Card(id = it.toString(), set = "a") }
            val cards1 = listOf(Card(id = "5", set = "b"))
            val response = cards0 + cards1

            coEvery {
                webServices.listCards(null, 2, 5, "set", null)
            } returns CardsResponse(response)

            coEvery {
                webServices.listCards(null, 3, 5, "set", null)
            } returns CardsResponse(listOf())

            val result = repository.searchCards(2, lastSet = "a")

            coVerifyOrder {
                webServices.listCards(null, 2, 5, "set", null)
                webServices.listCards(null, 3, 5, "set", null)
            }

            confirmVerified(webServices)

            assertEquals(cards1, result.data)
            assertEquals(null, result.nextPage)
        }
    }

    @Test
    fun shouldLoadNextPageComplete() {
        runBlocking {
            val cards = (1..5).map { Card(id = it.toString(), set = "b") }

            coEvery {
                webServices.listCards(null, 2, 5, "set", null)
            } returns CardsResponse(cards)

            coEvery {
                webServices.listCards(null, 3, 5, "set", null)
            } returns CardsResponse(listOf())

            val result = repository.searchCards(2, lastSet = "a")

            coVerifyOrder {
                webServices.listCards(null, 2, 5, "set", null)
                webServices.listCards(null, 3, 5, "set", null)
            }

            confirmVerified(webServices)

            assertEquals(cards, result.data)
            assertEquals(null, result.nextPage)
        }
    }
}