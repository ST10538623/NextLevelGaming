package com.nextlevelgaming.arena.data

enum class OfferingType {
    PACKAGE,
    EXPERIENCE
}

data class Offering(
    val id: String,
    val name: String,
    val type: OfferingType,
    val priceRand: Int,
    val purpose: String,
    val inclusions: List<String>
)

object OfferingCatalog {
    val all: List<Offering> = listOf(
        Offering(
            id = "ultimate-gamer-pass",
            name = "Ultimate Gamer Pass",
            type = OfferingType.PACKAGE,
            priceRand = 1500,
            purpose = "Unlimited gaming access for a full day.",
            inclusions = listOf(
                "PC gaming",
                "Console gaming",
                "High-speed internet",
                "Snack voucher",
                "Tournament entry"
            )
        ),
        Offering(
            id = "vip-gaming-experience",
            name = "VIP Gaming Experience",
            type = OfferingType.PACKAGE,
            priceRand = 1500,
            purpose = "Premium gaming with exclusive facilities.",
            inclusions = listOf(
                "Private gaming booth",
                "Premium gaming equipment",
                "Food and drinks",
                "Priority booking",
                "Personal gaming assistant"
            )
        ),
        Offering(
            id = "esports-training",
            name = "Esports Training Package",
            type = OfferingType.PACKAGE,
            priceRand = 1500,
            purpose = "Improve competitive gaming skills with professional coaching.",
            inclusions = listOf(
                "Strategy coaching",
                "Team communication",
                "Match analysis",
                "Practice sessions",
                "Performance feedback"
            )
        ),
        Offering(
            id = "birthday-party",
            name = "Birthday Party Package",
            type = OfferingType.PACKAGE,
            priceRand = 1500,
            purpose = "Host a gaming-themed birthday celebration.",
            inclusions = listOf(
                "Reserved gaming area",
                "Multiplayer competitions",
                "Party decorations",
                "Catering",
                "Tournament prizes"
            )
        ),
        Offering(
            id = "virtual-reality",
            name = "Virtual Reality Experience",
            type = OfferingType.EXPERIENCE,
            priceRand = 750,
            purpose = "Explore immersive virtual reality games.",
            inclusions = listOf("VR headset", "Choice of games", "Staff assistance")
        ),
        Offering(
            id = "racing-simulator",
            name = "Racing Simulator Challenge",
            type = OfferingType.EXPERIENCE,
            priceRand = 750,
            purpose = "Race on professional driving simulators.",
            inclusions = listOf("Racing simulator", "Choice of tracks", "Leaderboard competition")
        ),
        Offering(
            id = "escape-room",
            name = "Escape Room Challenge",
            type = OfferingType.EXPERIENCE,
            priceRand = 750,
            purpose = "Solve puzzles and escape before time runs out.",
            inclusions = listOf("Team challenge", "Themed escape room", "Digital scorecard")
        )
    )

    fun findById(id: String): Offering? = all.firstOrNull { it.id == id }

    fun ofType(type: OfferingType): List<Offering> = all.filter { it.type == type }
}
