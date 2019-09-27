package br.com.bootcamp.magicgamecs.core.widgets

import android.graphics.Bitmap
import com.bumptech.glide.load.Key
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import com.bumptech.glide.load.resource.bitmap.TransformationUtils
import com.bumptech.glide.util.Util
import java.nio.ByteBuffer
import java.security.MessageDigest
import kotlin.math.roundToInt

class RoundedCornersTransformation(
    private val roundingRadius: Float
) : BitmapTransformation() {

    companion object {
        private val ID =
            "br.com.bootcamp.magicgamecs.core.widgets.CardImageView\$RoundedCornersTransformation"
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
        if (other is RoundedCornersTransformation) {
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