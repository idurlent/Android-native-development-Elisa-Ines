package fr.ines.elisa.disney

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProfileActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    private lateinit var emailTextView: TextView
    private lateinit var logoutButton: Button
    private lateinit var watchedContainer: LinearLayout
    private lateinit var wantContainer: LinearLayout
    private lateinit var ownContainer: LinearLayout
    private lateinit var ridContainer: LinearLayout
    private lateinit var navHome: Button
    private lateinit var navProfile: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        emailTextView = findViewById(R.id.emailTextView)
        logoutButton = findViewById(R.id.logoutButton)
        watchedContainer = findViewById(R.id.watchedContainer)
        wantContainer = findViewById(R.id.wantContainer)
        ownContainer = findViewById(R.id.ownContainer)
        ridContainer = findViewById(R.id.ridContainer)
        navHome = findViewById(R.id.navHome)
        navProfile = findViewById(R.id.navProfile)

        navHome.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
        }

        navProfile.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }

        val user = auth.currentUser
        if (user == null) {
            finish()
            return
        }

        emailTextView.text = "Email : ${user.email}"

        logoutButton.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        loadUserFilms(user.uid)
    }

    private fun loadUserFilms(userId: String) {
        watchedContainer.removeAllViews()
        wantContainer.removeAllViews()
        ownContainer.removeAllViews()
        ridContainer.removeAllViews()

        db.collection("userFilms")
            .document(userId)
            .collection("films")
            .get()
            .addOnSuccessListener { films ->
                for (filmDoc in films) {
                    val watched = filmDoc.getBoolean("watched") ?: false
                    val want = filmDoc.getBoolean("wantToWatch") ?: false
                    val own = filmDoc.getBoolean("ownPhysical") ?: false
                    val rid = filmDoc.getBoolean("wantToGetRid") ?: false

                    val filmId = filmDoc.id

                    db.collection("films")
                        .document(filmId)
                        .get()
                        .addOnSuccessListener { movieDoc ->
                            val title = movieDoc.getString("title") ?: "Titre inconnu"
                            val releaseDate = movieDoc.getString("releaseDate") ?: "Date inconnue"

                            val filmText = "$title ($releaseDate)"

                            if (watched) {
                                addFilmToSection(watchedContainer, filmText, filmId)
                            }

                            if (want) {
                                addFilmToSection(wantContainer, filmText, filmId)
                            }

                            if (own) {
                                addFilmToSection(ownContainer, filmText, filmId)
                            }

                            if (rid) {
                                addFilmToSection(ridContainer, filmText, filmId)
                            }
                        }
                }
            }
    }

    private fun addFilmToSection(container: LinearLayout, filmText: String, filmId: String) {
        val textView = TextView(this)
        textView.text = filmText
        textView.textSize = 16f
        textView.setTextColor(getColor(R.color.text_main))
        textView.setBackgroundColor(getColor(R.color.card_white))
        textView.setPadding(24, 20, 24, 20)
        textView.elevation = 2f

        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        params.setMargins(0, 0, 0, 14)
        textView.layoutParams = params

        textView.setOnClickListener {
            val intent = Intent(this, FilmDetailActivity::class.java)
            intent.putExtra("filmId", filmId)
            startActivity(intent)
        }

        container.addView(textView)
    }
}