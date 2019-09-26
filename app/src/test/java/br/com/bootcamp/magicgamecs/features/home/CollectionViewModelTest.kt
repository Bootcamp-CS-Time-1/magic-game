package br.com.bootcamp.magicgamecs.features.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import br.com.bootcamp.magicgamecs.domain.FetchCollectionPage
import br.com.bootcamp.magicgamecs.models.pojo.*
import br.com.bootcamp.magicgamecs.models.pojo.Collection
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class CollectionViewModelTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = TestCoroutineDispatcher()

    @MockK
    lateinit var fetchCollectionPage: FetchCollectionPage

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun givenInit_shouldReceiveFirstLaunch() {
        // Arrange
        val collectionViewModel = CollectionViewModel(fetchCollectionPage)
        val observer = spyk<Observer<ViewState<*>>>()

        // Act
        collectionViewModel.collectionsState.observeForever(observer)

        // Assert
        verify {
            observer.onChanged(ViewState.FirstLaunch)
        }
        confirmVerified(observer)
    }

    @Test
    fun givenThrows_whenInitialLoad_shouldReceiveFailed() {
        // Arrange
        val initialState = ViewState.FirstLaunch
        val collectionViewModel = CollectionViewModel(fetchCollectionPage)
        val observer = spyk<Observer<ViewState<*>>>()

        val error = Exception()

        coEvery {
            fetchCollectionPage.invoke(FetchCollectionPage.Params(0))
        } throws error

        collectionViewModel.collectionsState.observeForever(observer)

        // Act
        collectionViewModel.loadInitial()

        // Assert
        coVerifySequence {
            observer.onChanged(initialState)
            observer.onChanged(ViewState.Loading.FromEmpty)
            fetchCollectionPage.invoke(FetchCollectionPage.Params(0))
            observer.onChanged(ViewState.Failed.FromEmpty(error))
        }
        confirmVerified(observer, fetchCollectionPage)
    }

    @Test
    fun givenThrows_whenFetchMore_shouldReceiveFailedFromPrevious() {
        // Arrange
        val initialState = ViewState.Success<List<CollectionItem>>(listOf())
        val nextPage = 1
        val collectionViewModel =
            CollectionViewModel(fetchCollectionPage, initialState, nextPage)
        val observer = spyk<Observer<ViewState<*>>>()

        val error = Exception()

        coEvery {
            fetchCollectionPage.invoke(FetchCollectionPage.Params(nextPage))
        } throws error

        collectionViewModel.collectionsState.observeForever(observer)

        // Act
        collectionViewModel.fetchMoreItems()

        // Assert
        coVerifySequence {
            observer.onChanged(initialState)
            observer.onChanged(ViewState.Loading.FromPrevious(initialState.value))
            fetchCollectionPage.invoke(FetchCollectionPage.Params(nextPage))
            observer.onChanged(ViewState.Failed.FromPrevious(error, listOf<CollectionItem>()))
        }
        confirmVerified(observer, fetchCollectionPage)
    }

    @Test
    fun givenTryInitialLoad_whenAlreadyInitialized_shouldDoNothing() {
        // Arrange
        val initialState = ViewState.Success(listOf<CollectionItem>())
        val collectionViewModel = CollectionViewModel(fetchCollectionPage, initialState, 1)

        val observer = spyk<Observer<ViewState<*>>>()
        collectionViewModel.collectionsState.observeForever(observer)

        // Act
        collectionViewModel.loadInitial()

        // Assert
        verify {
            observer.onChanged(initialState)
        }
        confirmVerified(observer, fetchCollectionPage)
    }

    @Test
    fun givenFetchMore_whenNotInitialized_shouldDoNothing() {
        // Arrange
        val initialState = ViewState.FirstLaunch
        val collectionViewModel = CollectionViewModel(fetchCollectionPage, initialState, 1)

        val observer = spyk<Observer<ViewState<*>>>()
        collectionViewModel.collectionsState.observeForever(observer)

        // Act
        collectionViewModel.fetchMoreItems()

        // Assert
        verify {
            observer.onChanged(initialState)
        }
        confirmVerified(observer, fetchCollectionPage)
    }

    @Test
    fun givenFetchMore_whenReachEnd_shouldDoNothing() {
        // Arrange
        val initialState = ViewState.Success(listOf<CollectionItem>())
        val collectionViewModel = CollectionViewModel(fetchCollectionPage, initialState, null)

        val observer = spyk<Observer<ViewState<*>>>()
        collectionViewModel.collectionsState.observeForever(observer)

        // Act
        collectionViewModel.fetchMoreItems()

        // Assert
        verify {
            observer.onChanged(initialState)
        }
        confirmVerified(observer, fetchCollectionPage)
    }

    @Test
    fun givenCollection_whenInitialLoad_shouldReceiveSuccess() {
        // Arrange
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

        val magicSet0 = Collection(
            "a",
            "name",
            mutableListOf(typedCard0, typedCard1)
        )

        val transformed = listOf(
            NameCollectionItem(magicSet0.name),
            CardTypeItem(magicSet0.code, typedCard0.type),
            CardItem(card0),
            CardItem(card1),
            CardTypeItem(magicSet0.code, typedCard1.type),
            CardItem(card2),
            CardItem(card3),
            CardItem(card4)
        )

        val collectionViewModel = CollectionViewModel(fetchCollectionPage)

        val observer = spyk<Observer<ViewState<*>>>()
        collectionViewModel.collectionsState.observeForever(observer)

        coEvery {
            fetchCollectionPage.invoke(FetchCollectionPage.Params(0))
        } returns PageResult(listOf(magicSet0), 1)

        // Act
        collectionViewModel.loadInitial()

        // Assert
        coVerifySequence {
            observer.onChanged(ViewState.FirstLaunch)
            observer.onChanged(ViewState.Loading.FromEmpty)
            fetchCollectionPage.invoke(FetchCollectionPage.Params(0))
            observer.onChanged(ViewState.Success(transformed))
        }
        confirmVerified(observer, fetchCollectionPage)
    }

    @Test
    fun givenCollection_whenFetchMore_shouldReceiveSuccess() {
        // Arrange
        val card0 = Card("1", "image1")
        val card1 = Card("2", "image2")
        val card2 = Card("3", "image3")
        val card3 = Card("4", "image4")
        val card4 = Card("5", "image5")

        val typedCard0 =
            CardType("typeA", listOf(card0))
        val typedCard1 = CardType(
            "typeB",
            listOf(card1, card2)
        )
        val typedCard2 = CardType(
            "typeC",
            listOf(card3, card4)
        )

        val magicSet0 = Collection(
            "a",
            "nameA",
            mutableListOf(typedCard0)
        )

        val magicSet1 = Collection(
            "b",
            "nameB",
            mutableListOf(typedCard1, typedCard2)
        )

        val transformed0 = listOf(
            NameCollectionItem(magicSet0.name),
            CardTypeItem(magicSet0.code, typedCard0.type),
            CardItem(card0)
        )

        val transformed1 = listOf(
            NameCollectionItem(magicSet0.name),
            CardTypeItem(magicSet0.code, typedCard0.type),
            CardItem(card0),
            NameCollectionItem(magicSet1.name),
            CardTypeItem(magicSet1.code, typedCard1.type),
            CardItem(card1),
            CardItem(card2),
            CardTypeItem(magicSet1.code, typedCard2.type),
            CardItem(card3),
            CardItem(card4)
        )

        val collectionViewModel =
            CollectionViewModel(fetchCollectionPage, ViewState.Success(transformed0), 1)

        val observer = spyk<Observer<ViewState<*>>>()
        collectionViewModel.collectionsState.observeForever(observer)

        coEvery {
            fetchCollectionPage.invoke(FetchCollectionPage.Params(1))
        } returns PageResult(listOf(magicSet0, magicSet1), null)

        // Act
        collectionViewModel.fetchMoreItems()

        // Assert
        coVerifySequence {
            observer.onChanged(ViewState.Success(transformed0))
            observer.onChanged(ViewState.Loading.FromPrevious(transformed0))
            fetchCollectionPage.invoke(FetchCollectionPage.Params(1))
            observer.onChanged(ViewState.Success(transformed1))
        }
        confirmVerified(observer, fetchCollectionPage)
    }

    @Test
    fun givenCollection_whenReload_shouldSuccess() {
        // Arrange
        val collectionViewModel = CollectionViewModel(fetchCollectionPage)

        val observer = spyk<Observer<ViewState<*>>>()
        collectionViewModel.collectionsState.observeForever(observer)

        coEvery {
            fetchCollectionPage.invoke(FetchCollectionPage.Params(0))
        } returns PageResult(listOf(), null)

        // Act
        collectionViewModel.reload()

        // Assert
        coVerifySequence {
            observer.onChanged(ViewState.FirstLaunch)
            observer.onChanged(ViewState.Loading.FromEmpty)
            fetchCollectionPage.invoke(FetchCollectionPage.Params(0))
            observer.onChanged(ViewState.Success(listOf<CollectionItem>()))
        }
        confirmVerified(observer, fetchCollectionPage)
    }

    @Test
    fun givenAfterFailed_whenFetchMore_shouldSuccess() {
        // Arrange
        val error = Throwable()
        val initialState = ViewState.Failed.FromPrevious(error, listOf<CollectionItem>())
        val nextPage = 1
        val collectionViewModel = CollectionViewModel(fetchCollectionPage, initialState, nextPage)

        val observer = spyk<Observer<ViewState<*>>>()
        collectionViewModel.collectionsState.observeForever(observer)

        coEvery {
            fetchCollectionPage.invoke(FetchCollectionPage.Params(nextPage))
        } returns PageResult(listOf(), null)

        // Act
        collectionViewModel.fetchMoreItems()

        // Assert
        coVerifySequence {
            observer.onChanged(initialState)
            observer.onChanged(ViewState.Loading.FromPrevious(initialState.previous))
            fetchCollectionPage.invoke(FetchCollectionPage.Params(nextPage))
            observer.onChanged(ViewState.Success(listOf<CollectionItem>()))
        }
        confirmVerified(observer, fetchCollectionPage)
    }
}