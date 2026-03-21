package fr.ines.elisa.disney

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class UniverseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_universe)

        val container = findViewById<LinearLayout>(R.id.container)
        val universe = intent.getStringExtra("universe") ?: ""

        val header = TextView(this)
        header.text = universe
        header.textSize = 23f
        header.setTypeface(null, Typeface.BOLD)
        header.setTextColor(getColor(R.color.text_main))
        header.setPadding(8, 0, 8, 18)
        container.addView(header)

        val db = FirebaseFirestore.getInstance()

        db.collection("films")
            .whereEqualTo("universe", universe)
            .get()
            .addOnSuccessListener { result ->
                container.removeViews(2, maxOf(0, container.childCount - 2))

                for (document in result) {
                    val title = document.getString("title") ?: "Unknown title"
                    val releaseDate = document.getString("releaseDate") ?: "Unknown date"
                    val category = document.getString("category") ?: "Unknown category"

                    val textView = TextView(this)
                    textView.text = "$title\n$releaseDate\n$category"
                    textView.textSize = 17f
                    textView.setTextColor(getColor(R.color.text_main))
                    textView.setBackgroundColor(getColor(R.color.card_white))
                    textView.setPadding(28, 24, 28, 24)
                    textView.elevation = 2f

                    val params = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )
                    params.setMargins(0, 0, 0, 14)
                    textView.layoutParams = params

                    textView.setOnClickListener {
                        val detailIntent = Intent(this, FilmDetailActivity::class.java)
                        detailIntent.putExtra("filmId", document.id)
                        startActivity(detailIntent)
                    }

                    container.addView(textView)
                }
            }
    }
}