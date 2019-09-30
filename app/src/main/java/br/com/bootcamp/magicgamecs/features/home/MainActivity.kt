package br.com.bootcamp.magicgamecs.features.home

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.bootcamp.magicgamecs.R
import br.com.bootcamp.magicgamecs.core.ext.gone
import br.com.bootcamp.magicgamecs.core.ext.show
import br.com.bootcamp.magicgamecs.core.listeners.EndlessRecyclerViewScrollListener
import br.com.bootcamp.magicgamecs.features.detail.CardDetailActivity
import br.com.bootcamp.magicgamecs.models.pojo.Card
import br.com.bootcamp.magicgamecs.models.pojo.ViewState
import br.com.bootcamp.magicgamecs.models.pojo.ViewState.*
import br.com.bootcamp.magicgamecs.models.pojo.ViewState.Failed
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.status_error.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity(), CollectionAdapter.UserInteraction {

    private val collectionsViewModel by viewModel<CollectionViewModel>()

    private val collectionAdapter by lazy {
        CollectionAdapter(this)
    }

    private val RecyclerView.gridLayoutManager: GridLayoutManager
        get() = layoutManager as GridLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btTentarNovamente.setOnClickListener {
            collectionsViewModel.loadInitial()
        }
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        recyclerView_main.setUp()
        collectionsViewModel.collectionsState
            .observe(this, Observer { state -> onViewStateChanged(state) })
        collectionsViewModel.loadInitial()
    }

    private fun onViewStateChanged(state: ViewState<List<CollectionItem>>) {
        when (state) {
            is Loading.FromEmpty -> onFirstLoading()
            is Loading.FromPrevious -> onLoadingFromPrevious(state.previous)
            is Success -> onSuccessLoad(state.value)
            is Failed.FromEmpty -> onLoadFailed(state.reason)
            is Failed.FromPrevious -> onFailedLoadFromPrevious(state.previous, state.reason)
        }
    }

    private fun onFailedLoadFromPrevious(previous: List<CollectionItem>, reason: Throwable) {
        collectionAdapter.submitListWithError(previous, reason)
    }

    private fun onLoadingFromPrevious(previous: List<CollectionItem>) {
        collectionAdapter.submitListWithLoading(previous)
    }

    private fun onSuccessLoad(value: List<CollectionItem>) {
        progressBar.gone()
        recyclerView_main.show()
        collectionAdapter.submitList(value)
    }

    private fun onFirstLoading() {
        progressBar.show()
    }

    private fun onLoadFailed(error: Throwable) {
        error.printStackTrace()
        tvDescricao.text = error.message
        tela_erro.show()
    }

    override fun onCardClick(card: Card) {
        navigateToCard(card)
    }

    override fun onRetryClick() {
        collectionsViewModel.fetchMoreItems()
    }

    private fun navigateToCard(card: Card) {
        val intent = Intent(this, CardDetailActivity::class.java)
        intent.putExtra("CARD", card)
        startActivity(intent)
    }

    private fun RecyclerView.setUp() {
        adapter = collectionAdapter
        gridLayoutManager.spanSizeLookup = collectionAdapter.SpanSizeLookup()
        addOnScrollListener(
            EndlessRecyclerViewScrollListener(layoutManager!!) {
                collectionsViewModel.fetchMoreItems()
            }
        )
    }
}
