package com.m3rc.beerbox.kx

import android.graphics.Color
import com.m3rc.beerbox.data.Beer

fun Beer.type(): BeerType {
    return ebc?.let {
        when (it) {
            in 0.0f..5.9f -> BeerType.PALE_LAGER
            in 6.0f..7.9f -> BeerType.BLONDE_ALE
            in 8.0f..11.9f -> BeerType.WEISSBIER
            in 12.0f..15.9f -> BeerType.PALE_ALE
            in 16.0f..19.9f -> BeerType.SAISON
            in 20.0f..25.9f -> BeerType.EBS
            in 26.0f..32.9f -> BeerType.DOUBLE_IPA
            in 33.0f..38.9f -> BeerType.AMBER_ALE
            in 39.0f..46.9f -> BeerType.BROWN_ALE
            in 47.0f..78.9f -> BeerType.STOUT
            else -> BeerType.IMPERIAL_STOUT
        }
    } ?: BeerType.UNKNOWN
}

fun Beer.color() = beerTypeColorMap[type()]

fun BeerType.range() = beerTypeEbcRangesMap[this]

private val beerTypeColorMap = mapOf(
    BeerType.UNKNOWN to Color.parseColor("#f6f370"),
    BeerType.PALE_LAGER to Color.parseColor("#f8f570"),
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

private val beerTypeEbcRangesMap = mapOf(
    BeerType.UNKNOWN to -1.0f..0.0f,
    BeerType.PALE_LAGER to 0.0f..5.9f,
    BeerType.BLONDE_ALE to 6.0f..7.9f,
    BeerType.WEISSBIER to 8.0f..11.9f,
    BeerType.PALE_ALE to 12.0f..15.9f,
    BeerType.SAISON to 16.0f..19.9f,
    BeerType.EBS to 20.0f..25.9f,
    BeerType.DOUBLE_IPA to 26.0f..32.9f,
    BeerType.DARK_LAGER to 33.0f..38.9f,
    BeerType.AMBER_ALE to 33.0f..38.9f,
    BeerType.BROWN_ALE to 39.0f..46.9f,
    BeerType.STOUT to 47.0f..78.9f,
    BeerType.IMPERIAL_STOUT to 79.0f..400.0f
)

enum class BeerType {
    UNKNOWN,
    PALE_LAGER,
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



