package br.com.bootcamp.magicgamecs.features.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import br.com.bootcamp.magicgamecs.R
import br.com.bootcamp.magicgamecs.core.base.ViewHolder
import br.com.bootcamp.magicgamecs.core.widgets.CardImageView
import br.com.bootcamp.magicgamecs.models.pojo.Card
import kotlinx.android.synthetic.main.item_main_activity_card.view.*
import kotlinx.android.synthetic.main.item_main_activity_collection.view.*
import kotlinx.android.synthetic.main.item_main_activity_failed.view.*
import kotlinx.android.synthetic.main.item_main_activity_type.view.*

class CollectionAdapter(
    private val listener: UserInteraction? = null
) : ListAdapter<CollectionItem, ViewHolder<CollectionItem>>(DIFF_CALLBACK) {

    // region Update list methods
    fun submitListWithError(list: List<CollectionItem>, reason: Throwable) {
        submitList(list + Failed(reason))
    }

    fun submitListWithLoading(list: List<CollectionItem>) {
        submitList(list + Placeholder)
    }
    // endregion

    // region Adapter overrides
    @Suppress("UNCHECKED_CAST")
    override fun onCreateViewHolder(
        parent: ViewGroup,
        layoutRes: Int
    ): ViewHolder<CollectionItem> {
        val view = LayoutInflater.from(parent.context)
            .inflate(layoutRes, parent, false)

        return when (layoutRes) {
            VIEW_TYPE_CARD -> CardViewHolder(view, listener)
            VIEW_TYPE_SUBTITLE -> TypeViewHolder(view)
            VIEW_TYPE_TITLE -> CollectionViewHolder(view)
            VIEW_TYPE_PLACEHOLDER -> PlaceholderViewHolder(view)
            VIEW_TYPE_FAILED -> ErrorViewHolder(view, listener)
            else -> error("Invalid view type")
        } as ViewHolder<CollectionItem>
    }

    override fun onBindViewHolder(holder: ViewHolder<CollectionItem>, position: Int) {
        getItem(position)?.let { item -> holder.bind(item) }
    }

    override fun getItemViewType(position: Int) = when (getItem(position)) {
        is CardItem -> VIEW_TYPE_CARD
        is CardTypeItem -> VIEW_TYPE_SUBTITLE
        is NameCollectionItem -> VIEW_TYPE_TITLE
        is Placeholder -> VIEW_TYPE_PLACEHOLDER
        is Failed -> VIEW_TYPE_FAILED
        else -> error("Item type not supported")
    }
    // endregion

    // region Inner classes

    // region ViewHolders
    class CardViewHolder(view: View, private val listener: UserInteraction?) :
        ViewHolder<CardItem>(view) {
        init {
            itemView.card_image.setOnClickListener { v ->
                (v as? CardImageView)?.card?.let { card ->
                    listener?.onCardClick(card)
                }
            }
        }

        override fun bind(item: CardItem) {
            itemView.card_image.card = item.content
        }
    }

    class CollectionViewHolder(view: View) : ViewHolder<NameCollectionItem>(view) {
        override fun bind(item: NameCollectionItem) {
            itemView.contentDescription =
                itemView.resources.getString(R.string.description_collection, item.text)
            itemView.textView_collection_item.text = item.text
        }
    }

    class TypeViewHolder(view: View) : ViewHolder<CardTypeItem>(view) {
        override fun bind(item: CardTypeItem) {
            itemView.contentDescription =
                itemView.resources.getString(R.string.description_card_type, item.text)
            itemView.textView_type_item.text = item.text
        }
    }

    class PlaceholderViewHolder(view: View) : ViewHolder<Placeholder>(view)

    class ErrorViewHolder(view: View, private val listener: UserInteraction?) :
        ViewHolder<Failed>(view) {
        init {
            itemView.btn_retry.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    listener?.onRetryClick()
                }
            }
        }
    }
    // endregion

    inner class SpanSizeLookup : GridLayoutManager.SpanSizeLookup() {
        override fun getSpanSize(position: Int) =
            getItem(position)?.spanSize ?: 1
    }

    interface UserInteraction {
        fun onCardClick(card: Card)
        fun onRetryClick()
    }
    // endregion

    companion object {

        // region View types
        private const val VIEW_TYPE_CARD = R.layout.item_main_activity_card
        private const val VIEW_TYPE_SUBTITLE = R.layout.item_main_activity_type
        private const val VIEW_TYPE_TITLE = R.layout.item_main_activity_collection
        private const val VIEW_TYPE_PLACEHOLDER = R.layout.item_main_activity_placeholder
        private const val VIEW_TYPE_FAILED = R.layout.item_main_activity_failed
        // endregion

        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<CollectionItem>() {
            override fun areItemsTheSame(oldItem: CollectionItem, newItem: CollectionItem) =
                oldItem == newItem

            override fun areContentsTheSame(oldItem: CollectionItem, newItem: CollectionItem) = true
        }
    }

}