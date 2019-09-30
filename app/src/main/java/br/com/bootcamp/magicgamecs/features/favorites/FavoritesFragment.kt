package br.com.bootcamp.magicgamecs.features.favorites

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.bootcamp.magicgamecs.R
import br.com.bootcamp.magicgamecs.core.ext.gone
import br.com.bootcamp.magicgamecs.core.ext.show
import br.com.bootcamp.magicgamecs.features.detail.CardDetailActivity
import br.com.bootcamp.magicgamecs.features.home.CollectionAdapter
import br.com.bootcamp.magicgamecs.features.home.CollectionItem
import br.com.bootcamp.magicgamecs.features.home.CollectionViewModel
import br.com.bootcamp.magicgamecs.models.pojo.Card
import br.com.bootcamp.magicgamecs.models.pojo.ViewState
import kotlinx.android.synthetic.main.fragment_collection.progressBar
import kotlinx.android.synthetic.main.fragment_collection.recyclerView_main
import kotlinx.android.synthetic.main.fragment_collection.tela_erro
import kotlinx.android.synthetic.main.fragment_favorites.*
import kotlinx.android.synthetic.main.status_error.*
import kotlinx.android.synthetic.main.status_error.view.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class FavoritesFragment : Fragment(R.layout.fragment_favorites), CollectionAdapter.UserInteraction {

    private val collectionsViewModel by sharedViewModel<FavoritesViewModel>()

    private val collectionAdapter by lazy {
        CollectionAdapter(this)
    }

    private val RecyclerView.gridLayoutManager: GridLayoutManager
        get() = layoutManager as GridLayoutManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.btRetry.setOnClickListener {
            tela_erro.gone()
            progressBar.show()
            collectionsViewModel.loadInitial()
        }

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setUpList()
        collectionsViewModel.collectionsState
            .observe(this, Observer { state -> onViewStateChanged(state) })
        collectionsViewModel.loadInitial()
    }

    private fun onViewStateChanged(state: ViewState<List<CollectionItem>>) {
        when (state) {
            is ViewState.Loading.FromEmpty -> onFirstLoading()
            is ViewState.Loading.FromPrevious -> onLoadingFromPrevious(state.previous)
            is ViewState.Success -> onSuccessLoad(state.value)
            is ViewState.Failed.FromEmpty -> onLoadFailed(state.reason)
            is ViewState.Empty -> showEmptyState()
            is ViewState.Failed.FromPrevious -> onFailedLoadFromPrevious(
                state.previous,
                state.reason
            )
        }
    }

    private fun showEmptyState() {
        progressBar.gone()
        emptyView.show()
        collectionAdapter.submitList(listOf())
    }

    private fun onFailedLoadFromPrevious(previous: List<CollectionItem>, reason: Throwable) {
        emptyView.gone()
        collectionAdapter.submitListWithError(previous, reason)
    }

    private fun onLoadingFromPrevious(previous: List<CollectionItem>) {
        emptyView.gone()
        collectionAdapter.submitListWithLoading(previous)
    }

    private fun onSuccessLoad(value: List<CollectionItem>) {
        emptyView.gone()
        progressBar.gone()
        recyclerView_main.show()
        collectionAdapter.submitList(value)
    }

    private fun onFirstLoading() {
        progressBar.show()
    }

    private fun onLoadFailed(error: Throwable) {
        error.printStackTrace()
        tvDescription.text = error.message
        progressBar.gone()
        tela_erro.show()
    }

    override fun onCardClick(card: Card) {
        navigateToCard(card)
    }

    override fun onRetryClick() {
        collectionsViewModel.loadInitial()
    }

    private fun navigateToCard(card: Card) {
        val intent = Intent(context, CardDetailActivity::class.java)
        intent.putExtra("CARD", card)
        startActivity(intent)
    }

    private fun setUpList() = with(recyclerView_main) {
        adapter = collectionAdapter
        gridLayoutManager.spanSizeLookup = collectionAdapter.SpanSizeLookup()
    }

}
