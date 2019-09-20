package br.com.bootcamp.magicgamecs.features.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.bootcamp.magicgamecs.R
import br.com.bootcamp.magicgamecs.core.ext.gone
import br.com.bootcamp.magicgamecs.core.ext.show
import br.com.bootcamp.magicgamecs.core.listeners.EndlessRecyclerViewScrollListener
import br.com.bootcamp.magicgamecs.models.pojo.Card
import br.com.bootcamp.magicgamecs.models.pojo.State
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.status_error.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val setsViewModel by viewModel<CollectionViewModel>()

    private val collectionAdapter by lazy {
        CollectionAdapter(::navigateToCard)
    }

    private val RecyclerView.gridLayoutManager: GridLayoutManager
        get() = layoutManager as GridLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btTentarNovamente.setOnClickListener {
            setUpItemList()
        }
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        setUpItemList()
        setsViewModel.initialLoadState
            .observe(this, Observer { state ->
                onInitialStateChanged(state)
            })
    }

    private fun onInitialStateChanged(state: State) {
        when (state) {
            is State.Loading -> progressBar.show()
            is State.Loaded -> {
                progressBar.gone()
                recyclerView_main.show()
            }
            is State.Failed -> {
                progressBar.gone()
                showError(state.error)
            }
        }
    }

    private fun setUpItemList() {
        tela_erro.gone()
        recyclerView_main.setUp()
        setsViewModel.getItemsSet().observe(this, Observer {
            collectionAdapter.submitList(it)
        })
    }

    private fun showError(error: Throwable) {
        error.printStackTrace()
        tvDescricao.text = error.message
        tela_erro.show()
    }

    private fun navigateToCard(position: Int, card: Card) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun RecyclerView.setUp() {
        adapter = collectionAdapter
        gridLayoutManager.spanSizeLookup = collectionAdapter.SpanSizeLookup()
        addOnScrollListener(
            EndlessRecyclerViewScrollListener(layoutManager!!) {
                setsViewModel.fetchMoreItems()
            }
        )
    }
}
