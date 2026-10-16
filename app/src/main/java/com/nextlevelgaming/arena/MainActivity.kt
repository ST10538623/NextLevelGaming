package com.nextlevelgaming.arena

import android.app.Activity
import android.app.AlertDialog
import android.os.Bundle
import android.text.InputType
import android.util.Patterns
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import com.nextlevelgaming.arena.calculator.FeeCalculator
import com.nextlevelgaming.arena.data.Offering
import com.nextlevelgaming.arena.data.OfferingCatalog
import com.nextlevelgaming.arena.data.OfferingType
import java.util.Locale

class MainActivity : Activity() {
    private lateinit var screenContent: LinearLayout
    private lateinit var backButton: Button
    private val backStack = mutableListOf<String>()
    private val selectedOfferingIds = mutableSetOf<String>()
    private var currentRoute = ROUTE_HOME

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        screenContent = findViewById(R.id.screen_content)
        backButton = findViewById(R.id.back_button)
        backButton.setOnClickListener { goBack() }

        findViewById<Button>(R.id.nav_home).setOnClickListener { showRoot(ROUTE_HOME) }
        findViewById<Button>(R.id.nav_packages).setOnClickListener { showRoot(ROUTE_PACKAGES) }
        findViewById<Button>(R.id.nav_experiences).setOnClickListener { showRoot(ROUTE_EXPERIENCES) }
        findViewById<Button>(R.id.nav_calculator).setOnClickListener { showRoot(ROUTE_CALCULATOR) }
        findViewById<Button>(R.id.nav_contact).setOnClickListener { showRoot(ROUTE_CONTACT) }

        showScreen(ROUTE_HOME)
    }

    @Deprecated("Use the screen's back button or system back gesture.")
    override fun onBackPressed() {
        if (backStack.isNotEmpty()) {
            goBack()
        } else {
            super.onBackPressed()
        }
    }

    private fun showRoot(route: String) {
        backStack.clear()
        showScreen(route)
    }

    private fun navigate(route: String) {
        backStack.add(currentRoute)
        showScreen(route)
    }

    private fun goBack() {
        if (backStack.isNotEmpty()) {
            showScreen(backStack.removeAt(backStack.lastIndex))
        } else {
            showRoot(ROUTE_HOME)
        }
    }

    private fun showScreen(route: String) {
        currentRoute = route
        screenContent.removeAllViews()
        backButton.visibility =
            if (route == ROUTE_HOME || route in ROOT_ROUTES) View.GONE else View.VISIBLE

        when {
            route == ROUTE_HOME -> renderHome()
            route == ROUTE_ABOUT -> renderAbout()
            route == ROUTE_PACKAGES -> renderCatalogue(
                title = "Gaming Packages",
                subtitle = "Find the right way to play. Tap any package to see all the details.",
                offerings = OfferingCatalog.ofType(OfferingType.PACKAGE)
            )
            route == ROUTE_EXPERIENCES -> renderCatalogue(
                title = "Individual Experiences",
                subtitle = "Try a new challenge, from immersive VR to an escape-room mission.",
                offerings = OfferingCatalog.ofType(OfferingType.EXPERIENCE)
            )
            route == ROUTE_CALCULATOR -> renderCalculator()
            route == ROUTE_CONTACT -> renderContact()
            route.startsWith(ROUTE_DETAIL_PREFIX) -> {
                val offering = OfferingCatalog.findById(route.removePrefix(ROUTE_DETAIL_PREFIX))
                if (offering == null) renderNotFound() else renderOfferingDetail(offering)
            }
            else -> renderNotFound()
        }

        updateNavigationSelection(route)
    }

    private fun renderHome() {
        addText("NEXT LEVEL STARTS HERE", 13, color(R.color.electric_blue), bold = true)
        addText("Play. Compete.\nLevel up.", 36, color(R.color.white), bold = true, bottom = 12)
        addText(
            "Your gaming and esports arena for all-day play, premium experiences and competitive training.",
            16,
            color(R.color.muted),
            bottom = 22
        )
        addButton("Explore gaming packages") { navigate(ROUTE_PACKAGES) }
        addOutlineButton("Browse individual experiences") { navigate(ROUTE_EXPERIENCES) }

        addSectionTitle("Choose your next level")
        addOfferingCards(OfferingCatalog.all.take(3))
        addSectionTitle("About the arena")
        addText(
            "A place to play your way, celebrate with friends or sharpen your esports skills.",
            15,
            color(R.color.muted),
            bottom = 10
        )
        addOutlineButton("About Next Level Gaming") { navigate(ROUTE_ABOUT) }
        addOutlineButton("Get in touch") { navigate(ROUTE_CONTACT) }
    }

    private fun renderAbout() {
        addEyebrow("WHO WE ARE")
        addText("About Us", 30, color(R.color.white), bold = true, bottom = 12)
        addText(
            "Next Level Gaming is a gaming and esports arena built for players, teams and celebrations.",
            16,
            color(R.color.muted),
            bottom = 20
        )
        addInfoCard(
            "Play your way",
            "Choose from all-day PC and console gaming, premium private play, immersive experiences and more."
        )
        addInfoCard(
            "Compete and improve",
            "Take part in tournaments or work on your competitive skills with a dedicated esports training package."
        )
        addInfoCard(
            "Celebrate together",
            "Bring friends together for multiplayer challenges and gaming-themed birthday celebrations."
        )
        addText(
            "Business phone, email and street address have not been provided. Please use the enquiry form to draft a message.",
            14,
            color(R.color.muted),
            top = 8,
            bottom = 14
        )
        addButton("Contact the arena") { navigate(ROUTE_CONTACT) }
    }

    private fun renderCatalogue(title: String, subtitle: String, offerings: List<Offering>) {
        addEyebrow("NEXT LEVEL EXPERIENCES")
        addText(title, 30, color(R.color.white), bold = true, bottom = 8)
        addText(subtitle, 15, color(R.color.muted), bottom = 20)
        addOfferingCards(offerings)
        addText(
            "Choose any combination in Calculate fees to see your multi-booking discount.",
            14,
            color(R.color.muted),
            top = 6,
            bottom = 10
        )
        addOutlineButton("Calculate total fees") { navigate(ROUTE_CALCULATOR) }
    }

    private fun addOfferingCards(offerings: List<Offering>) {
        offerings.forEach { offering ->
            val card = LayoutInflater.from(this)
                .inflate(R.layout.item_offering, screenContent, false)
            card.findViewById<TextView>(R.id.offering_name).text = offering.name
            card.findViewById<TextView>(R.id.offering_description).text = offering.purpose
            card.findViewById<TextView>(R.id.offering_price).text =
                "${formatRand(offering.priceRand)}  ·  View details"
            card.contentDescription =
                "${offering.name}, ${offering.purpose} Price ${formatRand(offering.priceRand)}. Double tap for details."
            card.setOnClickListener { navigate("$ROUTE_DETAIL_PREFIX${offering.id}") }
            screenContent.addView(card, verticalLayout(bottom = 12))
        }
    }

    private fun renderOfferingDetail(offering: Offering) {
        addEyebrow(if (offering.type == OfferingType.PACKAGE) "GAMING PACKAGE" else "INDIVIDUAL EXPERIENCE")
        addText(offering.name, 30, color(R.color.white), bold = true, bottom = 10)
        addText(offering.purpose, 16, color(R.color.muted), bottom = 18)
        addInfoCard("Price", formatRand(offering.priceRand), accent = true)
        addSectionTitle("What's included")
        offering.inclusions.forEach { item ->
            addText("•  $item", 16, color(R.color.white), bottom = 12)
        }
        addButton("Add to fee calculation") {
            selectedOfferingIds.add(offering.id)
            navigate(ROUTE_CALCULATOR)
        }
        addOutlineButton("Back to ${if (offering.type == OfferingType.PACKAGE) "packages" else "experiences"}") {
            goBack()
        }
    }

    private fun renderCalculator() {
        addEyebrow("PLAN YOUR VISIT")
        addText("Calculate total fees", 30, color(R.color.white), bold = true, bottom = 8)
        addText(
            "Select each offering you want to book. Your discount is based on the number of different offerings.",
            15,
            color(R.color.muted),
            bottom = 16
        )
        addSectionTitle("Select offerings")

        OfferingCatalog.all.forEach { offering ->
            val checkBox = CheckBox(this).apply {
                text = "${offering.name}  ·  ${formatRand(offering.priceRand)}"
                textSize = 15f
                setTextColor(color(R.color.white))
                buttonTintList = android.content.res.ColorStateList.valueOf(color(R.color.electric_blue))
                isChecked = offering.id in selectedOfferingIds
                minHeight = dp(54)
                contentDescription = "${offering.name}, ${formatRand(offering.priceRand)}"
            }
            checkBox.setOnCheckedChangeListener { _, checked ->
                if (checked) selectedOfferingIds.add(offering.id) else selectedOfferingIds.remove(offering.id)
                renderFeeSummary()
            }
            screenContent.addView(checkBox, verticalLayout(bottom = 4))
        }

        val summaryContainer = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(18), dp(18), dp(18), dp(18))
            background = getDrawable(R.drawable.card_background)
            contentDescription = "Fee calculation results"
        }
        summaryContainer.tag = FEE_SUMMARY_TAG
        screenContent.addView(summaryContainer, verticalLayout(top = 12, bottom = 12))
        renderFeeSummary()
        addOutlineButton("Reset selections") {
            selectedOfferingIds.clear()
            showScreen(ROUTE_CALCULATOR)
        }
        addText(
            "Discounts: 1 offering 0% · 2 offerings 5% · 3 offerings 10% · 4 or more 15%.",
            13,
            color(R.color.muted),
            top = 12
        )
    }

    private fun renderFeeSummary() {
        val summaryContainer = screenContent.findViewWithTag<LinearLayout>(FEE_SUMMARY_TAG) ?: return
        val selected = OfferingCatalog.all.filter { it.id in selectedOfferingIds }
        val summary = FeeCalculator.calculate(selected)
        summaryContainer.removeAllViews()
        summaryContainer.addView(makeText("Your estimate", 19, color(R.color.white), bold = true), verticalLayout(bottom = 12))
        addSummaryRow(summaryContainer, "Bookings", summary.bookingCount.toString())
        addSummaryRow(summaryContainer, "Subtotal", formatRand(summary.subtotalRand))
        addSummaryRow(summaryContainer, "Discount", "${summary.discountPercent}%  (${formatRand(summary.discountRand)})")
        addSummaryRow(summaryContainer, "Total", formatRand(summary.totalRand), emphasize = true)
    }

    private fun addSummaryRow(
        container: LinearLayout,
        label: String,
        value: String,
        emphasize: Boolean = false
    ) {
        val row = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
        }
        row.addView(
            makeText(label, if (emphasize) 16 else 14, color(if (emphasize) R.color.white else R.color.muted), emphasize),
            LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f)
        )
        row.addView(
            makeText(value, if (emphasize) 18 else 14, color(if (emphasize) R.color.electric_blue else R.color.white), true)
        )
        container.addView(row, verticalLayout(bottom = if (emphasize) 0 else 10))
    }

    private fun renderContact() {
        addEyebrow("WE'D LOVE TO HEAR FROM YOU")
        addText("Contact", 30, color(R.color.white), bold = true, bottom = 8)
        addText(
            "Tell us what you have in mind and our team can follow up once contact details are available.",
            15,
            color(R.color.muted),
            bottom = 16
        )
        addInfoCard(
            "Business information",
            "Next Level Gaming & Esports Arena\nPhone, email and street address have not been supplied."
        )
        addSectionTitle("Send an enquiry")
        val nameInput = addInput("Your name", InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_WORDS)
        val emailInput = addInput("Email address", InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS)
        val messageInput = addInput(
            "How can we help?",
            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_MULTI_LINE,
            multiline = true
        )
        addText(
            "Demo only: this form checks your entries on this device. It does not send or store messages.",
            13,
            color(R.color.muted),
            top = 2,
            bottom = 14
        )
        addButton("Review enquiry") {
            val name = nameInput.text.toString().trim()
            val email = emailInput.text.toString().trim()
            val message = messageInput.text.toString().trim()
            var valid = true

            if (name.isBlank()) {
                nameInput.error = "Enter your name"
                valid = false
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                emailInput.error = "Enter a valid email address"
                valid = false
            }
            if (message.isBlank()) {
                messageInput.error = "Enter a message"
                valid = false
            }

            if (valid) {
                AlertDialog.Builder(this)
                    .setTitle("Enquiry ready")
                    .setMessage("Thanks, $name. This demonstration form does not send messages. Connect a business contact address or backend to receive enquiries.")
                    .setPositiveButton("OK", null)
                    .show()
            }
        }
    }

    private fun addInput(hint: String, inputType: Int, multiline: Boolean = false): EditText {
        val editText = EditText(this).apply {
            this.hint = hint
            this.inputType = inputType
            textSize = 16f
            setTextColor(color(R.color.white))
            setHintTextColor(color(R.color.muted))
            backgroundTintList = android.content.res.ColorStateList.valueOf(color(R.color.electric_blue))
            minHeight = if (multiline) dp(120) else dp(56)
            contentDescription = hint
            if (multiline) {
                gravity = Gravity.TOP
                setPadding(dp(12), dp(12), dp(12), dp(12))
            }
        }
        screenContent.addView(editText, verticalLayout(bottom = 12))
        return editText
    }

    private fun renderNotFound() {
        addText("We couldn't find that screen.", 20, color(R.color.white), bold = true, bottom = 16)
        addButton("Return home") { showRoot(ROUTE_HOME) }
    }

    private fun addInfoCard(title: String, body: String, accent: Boolean = false) {
        val card = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(18), dp(18), dp(18), dp(18))
            background = getDrawable(R.drawable.card_background)
        }
        card.addView(
            makeText(title, 17, color(if (accent) R.color.electric_blue else R.color.white), bold = true),
            verticalLayout(bottom = 8)
        )
        card.addView(makeText(body, 14, color(R.color.muted)))
        screenContent.addView(card, verticalLayout(bottom = 12))
    }

    private fun addEyebrow(label: String) {
        addText(label, 12, color(R.color.purple), bold = true, bottom = 8)
    }

    private fun addSectionTitle(title: String) {
        addText(title, 21, color(R.color.white), bold = true, top = 18, bottom = 12)
    }

    private fun addText(
        value: String,
        sizeSp: Int,
        textColor: Int,
        bold: Boolean = false,
        top: Int = 0,
        bottom: Int = 0
    ) {
        screenContent.addView(
            makeText(value, sizeSp, textColor, bold),
            verticalLayout(top = top, bottom = bottom)
        )
    }

    private fun makeText(value: String, sizeSp: Int, textColor: Int, bold: Boolean = false): TextView =
        TextView(this).apply {
            text = value
            textSize = sizeSp.toFloat()
            setTextColor(textColor)
            if (bold) setTypeface(typeface, android.graphics.Typeface.BOLD)
            setLineSpacing(dp(2).toFloat(), 1f)
        }

    private fun addButton(label: String, action: () -> Unit) {
        addStyledButton(label, primary = true, action = action)
    }

    private fun addOutlineButton(label: String, action: () -> Unit) {
        addStyledButton(label, primary = false, action = action)
    }

    private fun addStyledButton(label: String, primary: Boolean, action: () -> Unit) {
        val button = Button(this).apply {
            text = label
            textSize = 15f
            isAllCaps = false
            setTextColor(color(R.color.white))
            minHeight = dp(52)
            setPadding(dp(16), dp(8), dp(16), dp(8))
            background = getDrawable(
                if (primary) R.drawable.primary_button_background else R.drawable.card_background
            )
            contentDescription = label
            setOnClickListener { action() }
        }
        screenContent.addView(button, verticalLayout(bottom = 10))
    }

    private fun updateNavigationSelection(route: String) {
        val selectedRoot = if (route.startsWith(ROUTE_DETAIL_PREFIX)) {
            backStack.lastOrNull()
        } else {
            route
        }
        val selectedId = when (selectedRoot) {
            ROUTE_PACKAGES -> R.id.nav_packages
            ROUTE_EXPERIENCES -> R.id.nav_experiences
            ROUTE_CALCULATOR -> R.id.nav_calculator
            ROUTE_CONTACT -> R.id.nav_contact
            else -> R.id.nav_home
        }
        listOf(R.id.nav_home, R.id.nav_packages, R.id.nav_experiences, R.id.nav_calculator, R.id.nav_contact)
            .forEach { id ->
                findViewById<Button>(id).setTextColor(
                    color(if (id == selectedId) R.color.electric_blue else R.color.muted)
                )
            }
    }

    private fun verticalLayout(top: Int = 0, bottom: Int = 0): LinearLayout.LayoutParams =
        LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        ).apply {
            topMargin = dp(top)
            bottomMargin = dp(bottom)
        }

    private fun formatRand(amount: Int): String = String.format(Locale.forLanguageTag("en-ZA"), "R %,d", amount)

    private fun color(resourceId: Int): Int = getColor(resourceId)

    private fun dp(value: Int): Int = (value * resources.displayMetrics.density).toInt()

    private companion object {
        const val ROUTE_HOME = "home"
        const val ROUTE_ABOUT = "about"
        const val ROUTE_PACKAGES = "packages"
        const val ROUTE_EXPERIENCES = "experiences"
        const val ROUTE_CALCULATOR = "calculator"
        const val ROUTE_CONTACT = "contact"
        const val ROUTE_DETAIL_PREFIX = "detail:"
        const val FEE_SUMMARY_TAG = "fee_summary"
        val ROOT_ROUTES = setOf(ROUTE_PACKAGES, ROUTE_EXPERIENCES, ROUTE_CALCULATOR, ROUTE_CONTACT)
    }
}
