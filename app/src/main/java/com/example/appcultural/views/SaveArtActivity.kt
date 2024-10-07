package com.example.appcultural.views

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.appcultural.R
import com.google.android.material.textfield.MaterialAutoCompleteTextView

class SaveArtActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_save_art)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val genders = listOf(
            "Impressionismo",
            "Expressionismo",
            "Cubismo",
            "Surrealismo",
            "Barroco",
            "Rococó",
            "Renascimento",
            "Neoclassicismo",
            "Romantismo",
            "Realismo",
            "Futurismo",
            "Dadaísmo",
            "Pop Art",
            "Minimalismo",
            "Arte Abstrata"
        )
        val genderTextView = findViewById<MaterialAutoCompleteTextView>(R.id.art_genders_field)
        val genderAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, genders)
        genderTextView.setAdapter(genderAdapter)

        val artists = listOf(
            "Leonardo da Vinci",
            "Vincent van Gogh",
            "Pablo Picasso",
            "Claude Monet",
            "Michelangelo",
            "Rembrandt",
            "Salvador Dalí",
            "Frida Kahlo",
            "Jackson Pollock",
            "Georgia O'Keeffe",
            "Henri Matisse",
            "Edvard Munch",
            "Paul Cézanne",
            "Gustav Klimt",
            "Andy Warhol"
        )
        val artistTextView = findViewById<MaterialAutoCompleteTextView>(R.id.art_artist_field)
        val artistAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, artists)
        artistTextView.setAdapter(artistAdapter)
    }
}