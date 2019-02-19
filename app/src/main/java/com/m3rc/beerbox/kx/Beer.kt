package com.m3rc.beerbox.kx

import android.graphics.Color
import com.m3rc.beerbox.data.Beer

fun Beer.type(): List<BeerType> {
    return ebc?.let {
        when (it) {
            in 0.0..5.9 -> listOf(BeerType.PALE_LAGER, BeerType.WITBIER, BeerType.PILSENER)
            in 6.0..7.9 -> listOf(BeerType.BLONDE_ALE)
            in 8.0..11.9 -> listOf(BeerType.WEISSBIER)
            in 12.0..15.9 -> listOf(BeerType.PALE_ALE)
            in 16.0..19.9 -> listOf(BeerType.SAISON)
            in 20.0..25.9 -> listOf(BeerType.EBS)
            in 26.0..32.9 -> listOf(BeerType.DOUBLE_IPA)
            in 33.0..38.9 -> listOf(BeerType.DARK_LAGER, BeerType.AMBER_ALE)
            in 39.0..46.9 -> listOf(BeerType.BROWN_ALE)
            in 47.0..78.9 -> listOf(BeerType.STOUT)
            else -> listOf(BeerType.IMPERIAL_STOUT)
        }
    } ?: listOf(BeerType.UNKNOWN)
}

fun Beer.color() = beerTypeColorMap[type().first()]

private val beerTypeColorMap = mapOf(
    BeerType.UNKNOWN to Color.parseColor("#f6f370"),
    BeerType.PALE_LAGER to Color.parseColor("#f8f570"),
    BeerType.WITBIER to Color.parseColor("#f8f570"),
    BeerType.PILSENER to Color.parseColor("#f8f570"),
    BeerType.BLONDE_ALE to Color.parseColor("#f6f370"),
    BeerType.WEISSBIER to Color.parseColor("#ebe450"),
    BeerType.PALE_ALE to Color.parseColor("#d1bb48"),
    BeerType.SAISON to Color.parseColor("#b9934b"),
    BeerType.EBS to Color.parseColor("#b68347"),
    BeerType.DOUBLE_IPA to Color.parseColor("#b16b3e"),
    BeerType.DARK_LAGER to Color.parseColor("#854f37"),
    BeerType.AMBER_ALE to Color.parseColor("#854f37"),
    BeerType.BROWN_ALE to Color.parseColor("#58351f"),
    BeerType.STOUT to Color.parseColor("#0b0b0a"),
    BeerType.IMPERIAL_STOUT to Color.parseColor("#030403")
)

enum class BeerType {
    UNKNOWN,
    PALE_LAGER,
    WITBIER,
    PILSENER,
    BLONDE_ALE,
    WEISSBIER,
    PALE_ALE,
    SAISON,
    EBS,
    DOUBLE_IPA,
    DARK_LAGER,
    AMBER_ALE,
    BROWN_ALE,
    STOUT,
    IMPERIAL_STOUT
}



