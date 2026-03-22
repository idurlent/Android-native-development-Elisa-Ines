package fr.ines.elisa.disney

import android.content.Intent
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val container = findViewById<LinearLayout>(R.id.container)
        val profileButton = findViewById<Button>(R.id.profileButton)
        val navHome = findViewById<Button>(R.id.navHome)
        val navProfile = findViewById<Button>(R.id.navProfile)

        profileButton.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }

        navHome.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
        }

        navProfile.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }

        val categories = listOf(

            Category(
                "GRANDES SAGAS",
                listOf(
                    "Star Wars",
                    "X-Men Cinematic Universe",
                    "Indiana Jones",
                    "Pirates des Caraïbes",
                    "Spider-Man"
                ),
                R.drawable.saga
            ),

            Category(
                "AUTRES FRANCHISES DISNEY",
                listOf(
                    "L'Incroyable Voyage",
                    "La Montagne Ensorcelée",
                    "Le Monde de Narnia",
                    "Les Petits Champions",
                    "Super Noël"
                ),
                R.drawable.diney
            ),

            Category(
                "AUTRES FRANCHISES 20TH CENTURY",
                listOf(
                    "La Nuit au Musée",
                    "Le Labyrinthe",
                    "Maman, J'ai Raté l'Avion"
                ),
                R.drawable.twenty
            ),

            Category(
                "AUTRES FRANCHISES MARVEL",
                listOf(
                    "Les Quatre Fantastiques",
                    "Men in Black"
                ),
                R.drawable.marvel
            ),

            Category(
                "TOUCHSTONE",
                listOf(
                    "Ernest",
                    "Sexy Dance"
                ),
                R.drawable.touch
            ),

            Category(
                "DIMENSION",
                listOf(
                    "Scary Movie",
                    "Scream"
                ),
                R.drawable.dimension
            ),

            Category(
                "FRANCHISES INTERNATIONALES",
                listOf(
                    "Hamilton",
                    "Les Football Kings"
                ),
                R.drawable.world
            )
        )

        for (category in categories) {

            val categoryCard = LinearLayout(this)
            categoryCard.orientation = LinearLayout.VERTICAL
            categoryCard.setPadding(dp(16), dp(16), dp(16), dp(16))
            categoryCard.background = GradientDrawable().apply {
                shape = GradientDrawable.RECTANGLE
                cornerRadius = dp(18).toFloat()
                setColor(ContextCompat.getColor(this@HomeActivity, R.color.card_white))
                setStroke(dp(1), ContextCompat.getColor(this@HomeActivity, R.color.rose_light))
            }

            val cardParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            cardParams.setMargins(0, 0, 0, dp(20))
            categoryCard.layoutParams = cardParams

            val categoryImage = ImageView(this)
            categoryImage.setImageResource(category.imageRes)
            categoryImage.adjustViewBounds = true
            categoryImage.scaleType = ImageView.ScaleType.FIT_CENTER

            val imageParams = LinearLayout.LayoutParams(dp(52), dp(52))
            imageParams.gravity = Gravity.CENTER_HORIZONTAL
            imageParams.setMargins(0, 0, 0, dp(10))
            categoryImage.layoutParams = imageParams

            val headerLayout = LinearLayout(this)
            headerLayout.orientation = LinearLayout.HORIZONTAL
            headerLayout.gravity = Gravity.CENTER_VERTICAL

            val headerParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            headerLayout.layoutParams = headerParams

            val title = TextView(this)
            title.text = category.title
            title.textSize = 18f
            title.setTypeface(null, Typeface.BOLD)
            title.setTextColor(ContextCompat.getColor(this, R.color.text_main))

            val titleParams = LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1f
            )
            title.layoutParams = titleParams

            val arrow = TextView(this)
            arrow.text = "▼"
            arrow.textSize = 18f
            arrow.setTypeface(null, Typeface.BOLD)
            arrow.setTextColor(ContextCompat.getColor(this, R.color.text_main))

            val universesContainer = LinearLayout(this)
            universesContainer.orientation = LinearLayout.VERTICAL
            universesContainer.visibility = View.GONE

            val universesParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            universesParams.setMargins(0, dp(14), 0, 0)
            universesContainer.layoutParams = universesParams

            for (universe in category.universes) {
                val button = Button(this)
                button.text = universe
                button.textSize = 14f
                button.isAllCaps = false
                button.gravity = Gravity.CENTER
                button.setTextColor(ContextCompat.getColor(this, R.color.text_main))
                button.backgroundTintList = ContextCompat.getColorStateList(this, R.color.rose_light)
                button.setPadding(dp(18), dp(16), dp(18), dp(16))
                button.elevation = 2f

                val buttonParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                buttonParams.setMargins(0, 0, 0, dp(12))
                button.layoutParams = buttonParams

                button.setOnClickListener {
                    val intent = Intent(this, UniverseActivity::class.java)
                    intent.putExtra("universe", universe)
                    startActivity(intent)
                }

                universesContainer.addView(button)
            }

            val toggleSection = {
                if (universesContainer.visibility == View.GONE) {
                    universesContainer.visibility = View.VISIBLE
                    arrow.text = "▲"
                } else {
                    universesContainer.visibility = View.GONE
                    arrow.text = "▼"
                }
            }

            categoryImage.setOnClickListener { toggleSection() }
            headerLayout.setOnClickListener { toggleSection() }
            title.setOnClickListener { toggleSection() }
            arrow.setOnClickListener { toggleSection() }

            headerLayout.addView(title)
            headerLayout.addView(arrow)

            categoryCard.addView(categoryImage)
            categoryCard.addView(headerLayout)
            categoryCard.addView(universesContainer)

            container.addView(categoryCard)
        }
    }

    private fun dp(value: Int): Int {
        return (value * resources.displayMetrics.density).toInt()
    }
}