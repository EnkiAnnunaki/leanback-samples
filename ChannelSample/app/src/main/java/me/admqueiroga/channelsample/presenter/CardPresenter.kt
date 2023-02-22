package me.admqueiroga.channelsample.presenter

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.leanback.widget.ImageCardView
import androidx.leanback.widget.Presenter
import com.bumptech.glide.Glide
import me.admqueiroga.channelsample.R
import me.admqueiroga.channelsample.model.Category
import me.admqueiroga.channelsample.model.Movie
import kotlin.properties.Delegates

/**
 * A CardPresenter is used to generate Views and bind Objects to them on demand.
 * It contains an ImageCardView.
 */
class CardPresenter : Presenter() {
    private var mDefaultCardImage: Drawable? = null
    private var sSelectedBackgroundColor: Int by Delegates.notNull()
    private var sDefaultBackgroundColor: Int by Delegates.notNull()

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
        sDefaultBackgroundColor = ContextCompat.getColor(parent.context, R.color.default_background)
        sSelectedBackgroundColor = ContextCompat.getColor(parent.context, R.color.selected_background)
        mDefaultCardImage = ContextCompat.getDrawable(parent.context, R.drawable.movie)

        val cardView = object : ImageCardView(parent.context) {
            override fun setSelected(selected: Boolean) {
                updateCardBackgroundColor(this, selected)
                super.setSelected(selected)
            }
        }

        cardView.isFocusable = true
        cardView.isFocusableInTouchMode = true
        updateCardBackgroundColor(cardView, false)
        return ViewHolder(cardView)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, item: Any) {
        val cardView = viewHolder.view as ImageCardView
        when (item) {
            is Movie -> {
                if (item.cardImageUrl != null) {
                    cardView.titleText = item.title
                    cardView.contentText = item.studio
                    cardView.setMainImageDimensions(CARD_WIDTH, CARD_HEIGHT)
                    Glide.with(viewHolder.view.context)
                        .load(item.cardImageUrl)
                        .centerCrop()
                        .error(mDefaultCardImage)
                        .into(cardView.mainImageView)
                }

            }
            is Category -> {
                cardView.titleText = item.title
                cardView.contentText = "Add/Remove channel"
                Glide.with(viewHolder.view.context)
                    .load(R.drawable.baseline_save_alt_24)
                    .centerCrop()
                    .error(mDefaultCardImage)
                    .into(cardView.mainImageView)
                cardView.setMainImageDimensions(CARD_WIDTH, CARD_HEIGHT)
                cardView.mainImageView.setImageDrawable(null)
            }
        }
    }

    override fun onUnbindViewHolder(viewHolder: ViewHolder) {
        val cardView = viewHolder.view as ImageCardView
        // Remove references to images so that the garbage collector can free up memory
        cardView.badgeImage = null
        cardView.mainImage = null
    }

    private fun updateCardBackgroundColor(view: ImageCardView, selected: Boolean) {
        val color = if (selected) sSelectedBackgroundColor else sDefaultBackgroundColor
        // Both background colors should be set because the view"s background is temporarily visible
        // during animations.
        view.setBackgroundColor(color)
        view.setInfoAreaBackgroundColor(color)
    }

    companion object {
        private const val CARD_WIDTH = 313
        private const val CARD_HEIGHT = 176
    }
}