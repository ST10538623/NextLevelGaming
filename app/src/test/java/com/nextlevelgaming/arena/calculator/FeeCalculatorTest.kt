package com.nextlevelgaming.arena.calculator

import com.nextlevelgaming.arena.data.OfferingCatalog
import org.junit.Assert.assertEquals
import org.junit.Test

class FeeCalculatorTest {
    private val gamerPass = OfferingCatalog.findById("ultimate-gamer-pass")!!
    private val vipPass = OfferingCatalog.findById("vip-gaming-experience")!!
    private val esportsPass = OfferingCatalog.findById("esports-training")!!
    private val birthdayPass = OfferingCatalog.findById("birthday-party")!!
    private val vr = OfferingCatalog.findById("virtual-reality")!!
    private val racing = OfferingCatalog.findById("racing-simulator")!!

    @Test
    fun zeroSelectionsHaveNoDiscountOrFees() {
        assertEquals(
            FeeSummary(0, 0, 0, 0, 0),
            FeeCalculator.calculate(emptyList())
        )
    }

    @Test
    fun oneR750SelectionHasNoDiscount() {
        assertEquals(
            FeeSummary(1, 750, 0, 0, 750),
            FeeCalculator.calculate(listOf(vr))
        )
    }

    @Test
    fun twoR1500SelectionsReceiveFivePercentDiscount() {
        assertEquals(
            FeeSummary(2, 3000, 5, 150, 2850),
            FeeCalculator.calculate(listOf(gamerPass, vipPass))
        )
    }

    @Test
    fun threeR1500SelectionsReceiveTenPercentDiscount() {
        assertEquals(
            FeeSummary(3, 4500, 10, 450, 4050),
            FeeCalculator.calculate(listOf(gamerPass, vipPass, esportsPass))
        )
    }

    @Test
    fun fourR1500SelectionsReceiveFifteenPercentDiscount() {
        assertEquals(
            FeeSummary(4, 6000, 15, 900, 5100),
            FeeCalculator.calculate(listOf(gamerPass, vipPass, esportsPass, birthdayPass))
        )
    }

    @Test
    fun mixed1500AndTwo750SelectionsReceiveTenPercentDiscount() {
        assertEquals(
            FeeSummary(3, 3000, 10, 300, 2700),
            FeeCalculator.calculate(listOf(gamerPass, vr, racing))
        )
    }

    @Test
    fun repeatedOfferingIdIsCountedOnlyOnce() {
        assertEquals(
            FeeSummary(1, 1500, 0, 0, 1500),
            FeeCalculator.calculate(listOf(gamerPass, gamerPass))
        )
    }
}
