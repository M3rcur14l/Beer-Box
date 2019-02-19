package com.m3rc.beerbox.app.beer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.m3rc.beerbox.R
import com.m3rc.beerbox.app.beer.BeerViewHolder.Companion.glideOptions
import com.m3rc.beerbox.data.Beer
import kotlinx.android.synthetic.main.fragment_beer_details.*

class BeerDetailsBottomDialog : BottomSheetDialogFragment() {

    companion object {
        private const val BEER_EXTRA = "BEER_EXTRA"

        fun newInstance(beer: Beer): BeerDetailsBottomDialog {
            val fragment = BeerDetailsBottomDialog()
            val extras = Bundle()
            extras.putParcelable(BEER_EXTRA, beer)
            fragment.arguments = extras
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_beer_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            val beer = it.getParcelable<Beer?>(BEER_EXTRA)
            beer?.apply {
                Glide.with(beerImage)
                    .load(image)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .apply(glideOptions)
                    .into(beerImage)
                beerName.text = name
                beerTagLine.text = tagLine
                beerDescription.text = description
            }
        }

    }

    override fun getTheme(): Int = R.style.BottomSheetDialogTheme

}