package br.com.bootcamp.magicgamecs.features.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.bootcamp.magicgamecs.R
import br.com.bootcamp.magicgamecs.models.pojo.Card
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_main_activity_card.view.*
import kotlinx.android.synthetic.main.item_main_activity_edition.view.*
import kotlinx.android.synthetic.main.item_main_activity_type.view.*

typealias OnCardClickListener = (position: Int, card: Card) -> Unit

class SetsAdapter : PagedListAdapter<ItemSet, SetsAdapter.ViewHolder<ItemSet>>(DIFF_CALLBACK) {

    private var onCardClickListener: OnCardClickListener? = null

    fun setOnCardClickListener(listener: OnCardClickListener) {
        this.onCardClickListener = listener
    }

    @Suppress("UNCHECKED_CAST")
    override fun onCreateViewHolder(
        parent: ViewGroup,
        layoutRes: Int
    ): ViewHolder<ItemSet> {
        val view = LayoutInflater.from(parent.context)
            .inflate(layoutRes, parent, false)

        return when (layoutRes) {
            R.layout.item_main_activity_card -> CardViewHolder(view)
            R.layout.item_main_activity_type -> TypeViewHolder(view)
            R.layout.item_main_activity_edition -> SetViewHolder(view)
            else -> error("Invalid view type")
        } as ViewHolder<ItemSet>
    }

    override fun onBindViewHolder(holder: ViewHolder<ItemSet>, position: Int) {
        val item = getItem(position)
        if (item != null)
            holder.bindTo(item)
    }

    override fun getItemViewType(position: Int) = when (getItem(position)) {
        is CardItemSet -> R.layout.item_main_activity_card
        is TypeItemSet -> R.layout.item_main_activity_type
        is EditionItemSet -> R.layout.item_main_activity_edition
        else -> error("Item type not supported")
    }

    abstract class ViewHolder<T : ItemSet>(view: View) : RecyclerView.ViewHolder(view) {
        abstract fun bindTo(item: T)
    }

    inner class CardViewHolder(view: View) : ViewHolder<CardItemSet>(view) {
        init {
            itemView.imageView_card_item.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    val card = getItem(adapterPosition) as CardItemSet
                    onCardClickListener?.invoke(adapterPosition, card.content)
                }
            }
        }

        override fun bindTo(item: CardItemSet) {
            val card = item.content
            Glide
                .with(itemView.context)
                .load(card.imageUrl)
                .centerCrop()
                .into(itemView.imageView_card_item)
        }
    }

    class SetViewHolder(view: View) : ViewHolder<EditionItemSet>(view) {
        override fun bindTo(item: EditionItemSet) {
            itemView.textView_edition_item.text = item.text
        }
    }

    class TypeViewHolder(view: View) : ViewHolder<TypeItemSet>(view) {
        override fun bindTo(item: TypeItemSet) {
            itemView.textView_type_item.text = item.text
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ItemSet>() {
            override fun areItemsTheSame(oldItem: ItemSet, newItem: ItemSet) = oldItem == newItem

            override fun areContentsTheSame(oldItem: ItemSet, newItem: ItemSet) = true
        }
    }

    inner class SpanSizeLookup : GridLayoutManager.SpanSizeLookup() {
        override fun getSpanSize(position: Int) =
            getItem(position)?.spanSize ?: 1
    }
}