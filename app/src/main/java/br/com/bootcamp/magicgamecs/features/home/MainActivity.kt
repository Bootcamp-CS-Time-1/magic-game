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
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val setsViewModel by viewModel<SetsViewModel>()

    private val setsAdapter by lazy {
        SetsAdapter()
    }

    private val RecyclerView.gridLayoutManager: GridLayoutManager
        get() = layoutManager as GridLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
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
                state.error.printStackTrace()
                // TODO show error state
            }
        }
    }

    private fun setUpItemList() {
        recyclerView_main.setUp()
        setsAdapter.setOnCardClickListener { position, card ->
            navigateToCard(position, card)
        }
        setsViewModel.getItemsSet().observe(this, Observer {
            setsAdapter.submitList(it)
        })
    }

    private fun navigateToCard(position: Int, card: Card) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun RecyclerView.setUp() {
        adapter = setsAdapter
        gridLayoutManager.spanSizeLookup = setsAdapter.SpanSizeLookup()
        addOnScrollListener(
            EndlessRecyclerViewScrollListener(layoutManager!!) {
                setsViewModel.fetchMoreItems()
            }
        )
    }
}
