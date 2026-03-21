package fr.ines.elisa.disney

import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class UniverseActivity : AppCompatActivity() {

    private lateinit var container: LinearLayout
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_universe)

        container = findViewById(R.id.container)

        val universe = intent.getStringExtra("universe")

        db.collection("films")
            .whereEqualTo("universe", universe)
            .get()
            .addOnSuccessListener { result ->

                for (document in result) {
                    val title = document.getString("title")
                    val date = document.getString("releaseDate")
                    val category = document.getString("category")

                    val textView = TextView(this)
                    textView.text = "$title\n$date\n$category\n"

                    container.addView(textView)
                }
            }
    }
}