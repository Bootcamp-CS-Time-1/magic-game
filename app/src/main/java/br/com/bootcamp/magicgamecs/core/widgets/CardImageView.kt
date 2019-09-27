package br.com.bootcamp.magicgamecs.core.widgets

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.TypedValue
import android.widget.FrameLayout
import android.widget.TextView
import br.com.bootcamp.magicgamecs.R
import br.com.bootcamp.magicgamecs.models.pojo.Card
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.Key
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import com.bumptech.glide.load.resource.bitmap.TransformationUtils
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.util.Util
import com.facebook.shimmer.Shimmer
import com.facebook.shimmer.ShimmerFrameLayout
import kotlinx.android.synthetic.main.widget_cardimage.view.*
import java.nio.ByteBuffer
import java.security.MessageDigest
import kotlin.math.roundToInt


class CardImageView
@JvmOverloads
constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    FrameLayout(context, attrs, defStyleAttr) {

    init {
        inflate(context, R.layout.widget_cardimage, this)
        attrs?.init()
    }

    private fun AttributeSet.init() {
        val typedArray = context.obtainStyledAttributes(this, R.styleable.CardImageView, 0, 0)
        val textSize =
            typedArray.getDimensionPixelSize(R.styleable.CardImageView_civ_captionTextSize, 0)
        text_card_placeholder.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize.toFloat())
        typedArray.recycle()
    }

    var card: Card? = null
        set(value) {
            field = value
            updateCard(value)
        }

    private fun updateCard(card: Card?) {
        if (card == null) return
        contentDescription = card.name
        Glide.with(context)
            .load(card.imageUrl)
            .placeholder(R.drawable.placeholder_card)
            .error(R.drawable.placeholder_card)
            .fallback(R.drawable.placeholder_card)
            .transform(RoundedCorners(0.05f))
            .addListener(
                CardImageRequestListener(
                    card,
                    shimmer_card,
                    text_card_placeholder
                )
            )
            .into(imageView_card_item)
    }

    class CardImageRequestListener(
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
                    .setDuration(1300)
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

    class RoundedCorners(
        private val roundingRadius: Float
    ) : BitmapTransformation() {

        companion object {
            private val ID =
                "br.com.bootcamp.magicgamecs.core.widgets.CardImageView\$RoundedCorners"
            private val ID_BYTES = ID.toByteArray(Key.CHARSET)
        }

        override fun transform(
            pool: BitmapPool,
            toTransform: Bitmap,
            outWidth: Int,
            outHeight: Int
        ): Bitmap =
            TransformationUtils.roundedCorners(
                pool,
                toTransform,
                (roundingRadius * toTransform.width).roundToInt()
            )

        override fun updateDiskCacheKey(messageDigest: MessageDigest) {
            messageDigest.update(ID_BYTES)

            val radiusData = ByteBuffer.allocate(4).putFloat(roundingRadius).array()
            messageDigest.update(radiusData)
        }

        override fun equals(other: Any?): Boolean {
            if (other is RoundedCorners) {
                return roundingRadius == other.roundingRadius
            }
            return false
        }

        override fun hashCode(): Int {
            return Util.hashCode(
                ID.hashCode(),
                Util.hashCode(roundingRadius)
            )
        }

    }
}