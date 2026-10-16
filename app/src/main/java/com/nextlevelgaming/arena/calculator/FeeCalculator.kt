package com.nextlevelgaming.arena.calculator

import com.nextlevelgaming.arena.data.Offering
import kotlin.math.roundToInt

data class FeeSummary(
    val bookingCount: Int,
    val subtotalRand: Int,
    val discountPercent: Int,
    val discountRand: Int,
    val totalRand: Int
)

object FeeCalculator {
    fun discountPercentFor(bookingCount: Int): Int = when {
        bookingCount >= 4 -> 15
        bookingCount == 3 -> 10
        bookingCount == 2 -> 5
        else -> 0
    }

    fun calculate(selected: List<Offering>): FeeSummary {
        val distinctOfferings = selected.distinctBy { it.id }
        val subtotal = distinctOfferings.sumOf { it.priceRand }
        val discountPercent = discountPercentFor(distinctOfferings.size)
        val discount = (subtotal * discountPercent / 100.0).roundToInt()

        return FeeSummary(
            bookingCount = distinctOfferings.size,
            subtotalRand = subtotal,
            discountPercent = discountPercent,
            discountRand = discount,
            totalRand = subtotal - discount
        )
    }
}
