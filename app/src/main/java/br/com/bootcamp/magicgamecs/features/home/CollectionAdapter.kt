package br.com.bootcamp.magicgamecs.features.home

import android.animation.ValueAnimator
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import br.com.bootcamp.magicgamecs.R
import br.com.bootcamp.magicgamecs.core.base.ViewHolder
import br.com.bootcamp.magicgamecs.models.pojo.Card
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.facebook.shimmer.Shimmer
import com.facebook.shimmer.ShimmerFrameLayout
import kotlinx.android.synthetic.main.item_main_activity_card.view.*
import kotlinx.android.synthetic.main.item_main_activity_collection.view.*
import kotlinx.android.synthetic.main.item_main_activity_type.view.*


typealias OnCardClickListener = (position: Int, card: Card) -> Unit

class CollectionAdapter(
    private val onCardClickListener: OnCardClickListener? = null
) : ListAdapter<CollectionItem, ViewHolder<CollectionItem>>(DIFF_CALLBACK) {

    @Suppress("UNCHECKED_CAST")
    override fun onCreateViewHolder(
        parent: ViewGroup,
        layoutRes: Int
    ): ViewHolder<CollectionItem> {
        val view = LayoutInflater.from(parent.context)
            .inflate(layoutRes, parent, false)

        return when (layoutRes) {
            VIEW_TYPE_CARD -> CardViewHolder(view)
            VIEW_TYPE_SUBTITLE -> TypeViewHolder(view)
            VIEW_TYPE_TITLE -> SetViewHolder(view)
            VIEW_TYPE_PLACEHOLDER -> PlaceholderViewHolder(view)
            else -> error("Invalid view type")
        } as ViewHolder<CollectionItem>
    }

    override fun onBindViewHolder(holder: ViewHolder<CollectionItem>, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    override fun getItemViewType(position: Int) = when (getItem(position)) {
        is CardItem -> VIEW_TYPE_CARD
        is CardTypeItem -> VIEW_TYPE_SUBTITLE
        is NameCollectionItem -> VIEW_TYPE_TITLE
        is Placeholder -> VIEW_TYPE_PLACEHOLDER
        else -> error("Item type not supported")
    }

    private val now = System.currentTimeMillis()

    inner class CardViewHolder(view: View) : ViewHolder<CardItem>(view) {
        init {
            itemView.imageView_card_item.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    val card = getItem(adapterPosition) as CardItem
                    onCardClickListener?.invoke(adapterPosition, card.content)
                }
            }
        }

        override fun bind(item: CardItem) {
            itemView.run {
                val card = item.content
                Glide.with(context)
                    .load(card.imageUrl + "&time=$now")
                    .placeholder(R.drawable.placeholder_card)
                    .error(R.drawable.placeholder_card)
                    .addListener(
                        CardRequestListener(
                            card,
                            shimmer_card,
                            text_card_placeholder
                        )
                    )
                    //.transform(RoundedCorners(10))
                    .into(imageView_card_item)
            }
        }
    }

    class SetViewHolder(view: View) : ViewHolder<NameCollectionItem>(view) {
        override fun bind(item: NameCollectionItem) {
            itemView.textView_collection_item.text = item.text
        }
    }

    class TypeViewHolder(view: View) : ViewHolder<CardTypeItem>(view) {
        override fun bind(item: CardTypeItem) {
            itemView.textView_type_item.text = item.text
        }
    }

    class PlaceholderViewHolder(view: View) : ViewHolder<Placeholder>(view)

    companion object {

        private const val VIEW_TYPE_CARD = R.layout.item_main_activity_card
        private const val VIEW_TYPE_SUBTITLE = R.layout.item_main_activity_type
        private const val VIEW_TYPE_TITLE = R.layout.item_main_activity_collection
        private const val VIEW_TYPE_PLACEHOLDER = R.layout.item_main_activity_placeholder

        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<CollectionItem>() {
            override fun areItemsTheSame(oldItem: CollectionItem, newItem: CollectionItem) = oldItem == newItem

            override fun areContentsTheSame(oldItem: CollectionItem, newItem: CollectionItem) = true
        }
    }

    inner class SpanSizeLookup : GridLayoutManager.SpanSizeLookup() {
        override fun getSpanSize(position: Int) =
            getItem(position)?.spanSize ?: 1
    }

    class CardRequestListener(
        private val card: Card,
        private val shimmer: ShimmerFrameLayout,
        private val textPlaceholder: TextView
    ) : RequestListener<Drawable> {

        init {
            textPlaceholder.text = ""
            startShimmer()
        }

        override fun onLoadFailed(
            e: GlideException?,
            model: Any?,
            target: Target<Drawable>?,
            isFirstResource: Boolean
        ): Boolean {
            textPlaceholder.text = card.name
            stopShimmer()
            return false
        }

        override fun onResourceReady(
            resource: Drawable?,
            model: Any?,
            target: Target<Drawable>?,
            dataSource: DataSource?,
            isFirstResource: Boolean
        ): Boolean {
            stopShimmer()
            return false
        }

        private fun startShimmer() {
            shimmer.setShimmer(
                Shimmer.AlphaHighlightBuilder()
                    .setAutoStart(true)
                    .setDuration(2000)
                    .setRepeatMode(ValueAnimator.RESTART)
                    .build()
            )
        }

        private fun stopShimmer() {
            shimmer.stopShimmer()
            shimmer.setShimmer(null)
            shimmer.clearAnimation()
        }

    }
}