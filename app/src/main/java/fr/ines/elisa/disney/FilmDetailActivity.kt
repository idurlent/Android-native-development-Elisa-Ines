package fr.ines.elisa.disney

import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class FilmDetailActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var userId: String
    private lateinit var userEmail: String
    private lateinit var filmId: String

    private lateinit var watched: CheckBox
    private lateinit var want: CheckBox
    private lateinit var own: CheckBox
    private lateinit var rid: CheckBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_film_detail)

        db = FirebaseFirestore.getInstance()

        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser == null) {
            finish()
            return
        }

        userId = currentUser.uid
        userEmail = currentUser.email ?: "Unknown email"

        filmId = intent.getStringExtra("filmId") ?: ""
        if (filmId.isEmpty()) {
            finish()
            return
        }

        val titleView = findViewById<TextView>(R.id.titleTextView)
        val dateView = findViewById<TextView>(R.id.dateTextView)
        val categoryView = findViewById<TextView>(R.id.categoryTextView)
        val universeView = findViewById<TextView>(R.id.universeTextView)

        watched = findViewById(R.id.checkboxWatched)
        want = findViewById(R.id.checkboxWant)
        own = findViewById(R.id.checkboxOwn)
        rid = findViewById(R.id.checkboxRid)

        val saveButton = findViewById<Button>(R.id.saveButton)
        val resetButton = findViewById<Button>(R.id.resetButton)

        saveButton.setOnClickListener {
            saveData()
        }

        resetButton.setOnClickListener {
            deleteFilmFromProfile()
        }

        loadFilmInfo(titleView, dateView, categoryView, universeView)
        loadData()
        loadOwners()
    }

    private fun loadFilmInfo(
        titleView: TextView,
        dateView: TextView,
        categoryView: TextView,
        universeView: TextView
    ) {
        db.collection("films")
            .document(filmId)
            .get()
            .addOnSuccessListener { doc ->
                if (doc.exists()) {
                    titleView.text = doc.getString("title") ?: "Unknown title"
                    dateView.text = "Date: ${doc.getString("releaseDate") ?: "Unknown"}"
                    categoryView.text = "Category: ${doc.getString("category") ?: "Unknown"}"
                    universeView.text = "Universe: ${doc.getString("universe") ?: "Unknown"}"
                }
            }
    }

    private fun saveData() {
        val rootUserData = hashMapOf(
            "email" to userEmail
        )

        db.collection("userFilms")
            .document(userId)
            .set(rootUserData, SetOptions.merge())

        val filmData = hashMapOf(
            "watched" to watched.isChecked,
            "wantToWatch" to want.isChecked,
            "ownPhysical" to own.isChecked,
            "wantToGetRid" to rid.isChecked
        )

        db.collection("userFilms")
            .document(userId)
            .collection("films")
            .document(filmId)
            .set(filmData)
            .addOnSuccessListener {
                Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show()
                loadOwners()
            }
    }

    private fun deleteFilmFromProfile() {
        db.collection("userFilms")
            .document(userId)
            .collection("films")
            .document(filmId)
            .delete()
            .addOnSuccessListener {
                watched.isChecked = false
                want.isChecked = false
                own.isChecked = false
                rid.isChecked = false
                loadOwners()
                Toast.makeText(this, "Removed from profile", Toast.LENGTH_SHORT).show()
            }
    }

    private fun loadData() {
        db.collection("userFilms")
            .document(userId)
            .collection("films")
            .document(filmId)
            .get()
            .addOnSuccessListener { doc ->
                if (doc.exists()) {
                    watched.isChecked = doc.getBoolean("watched") ?: false
                    want.isChecked = doc.getBoolean("wantToWatch") ?: false
                    own.isChecked = doc.getBoolean("ownPhysical") ?: false
                    rid.isChecked = doc.getBoolean("wantToGetRid") ?: false
                }
            }
    }

    private fun loadOwners() {
        val container = findViewById<LinearLayout>(R.id.ownersContainer)
        container.removeAllViews()

        db.collection("userFilms")
            .get()
            .addOnSuccessListener { users ->
                for (userDoc in users) {
                    db.collection("userFilms")
                        .document(userDoc.id)
                        .collection("films")
                        .document(filmId)
                        .get()
                        .addOnSuccessListener { filmDoc ->
                            if (filmDoc.exists()) {
                                val ownPhysical = filmDoc.getBoolean("ownPhysical") ?: false
                                val wantToGetRid = filmDoc.getBoolean("wantToGetRid") ?: false

                                if (ownPhysical && wantToGetRid) {
                                    val email = userDoc.getString("email") ?: userDoc.id

                                    val textView = TextView(this)
                                    textView.text = email
                                    textView.textSize = 16f
                                    textView.setPadding(0, 0, 0, 20)
                                    container.addView(textView)
                                }
                            }
                        }
                }

                container.postDelayed({
                    if (container.childCount == 0) {
                        val textView = TextView(this)
                        textView.text = "No user currently owns this film and wants to get rid of it."
                        container.addView(textView)
                    }
                }, 500)
            }
    }
}