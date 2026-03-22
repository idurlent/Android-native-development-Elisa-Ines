package fr.ines.elisa.disney

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val container = findViewById<LinearLayout>(R.id.container)
        val profileButton = findViewById<Button>(R.id.profileButton)
        val navHome = findViewById<Button>(R.id.navHome)
        val navProfile = findViewById<Button>(R.id.navProfile)

        profileButton.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }

        navHome.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
        }

        navProfile.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }

        val categories = listOf(

            Category("GRANDES SAGAS", listOf(
                "Star Wars",
                "X-Men Cinematic Universe",
                "Indiana Jones",
                "Pirates des Caraïbes",
                "Spider-Man",
            )),

            Category("AUTRES FRANCHISES DISNEY", listOf(
                "L'Incroyable Voyage",
                "La Montagne Ensorcelée",
                "Le Monde de Narnia",
                "Les Petits Champions",
                "Super Noël"
            )),

            Category("AUTRES FRANCHISES 20TH CENTURY", listOf(
                "La Nuit au Musée",
                "Le Labyrinthe",
                "Maman, J'ai Raté l'Avion",
            )),

            Category("AUTRES FRANCHISES MARVEL", listOf(
                "Les Quatre Fantastiques",
                "Men in Black"
            )),

            Category("TOUCHSTONE", listOf(
                "Ernest",
                "Sexy Dance"
            )),

            Category("DIMENSION", listOf(
                "Scary Movie",
                "Scream",
            )),

            Category("FRANCHISES INTERNATIONALES", listOf(
                "Hamilton",
                "Les Football Kings",
            ))
        )

        for (category in categories) {

            val title = TextView(this)
            title.text = category.title
            title.textSize = 19f
            title.setTypeface(null, Typeface.BOLD)
            title.setTextColor(getColor(R.color.text_main))
            title.setPadding(8, 28, 8, 12)
            container.addView(title)

            for (universe in category.universes) {

                val button = Button(this)
                button.text = universe
                button.textSize = 14f
                button.isAllCaps = false
                button.gravity = Gravity.CENTER
                button.setTextColor(getColor(R.color.text_main))
                button.backgroundTintList = getColorStateList(R.color.rose_light)
                button.setPadding(20, 18, 20, 18)
                button.elevation = 2f

                val params = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                params.setMargins(0, 0, 0, 14)
                button.layoutParams = params

                button.setOnClickListener {
                    val intent = Intent(this, UniverseActivity::class.java)
                    intent.putExtra("universe", universe)
                    startActivity(intent)
                }

                container.addView(button)
            }
        }
    }
}