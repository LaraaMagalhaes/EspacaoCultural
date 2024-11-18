package com.example.appcultural.views

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.example.appcultural.R
import com.example.appcultural.adapters.ArtListAdapter
import com.example.appcultural.data.FirebaseAlbumsRepository
import com.example.appcultural.data.FirebaseArtsRepository
import com.example.appcultural.data.MockAuthProvider
import com.example.appcultural.databinding.ActivityArtDetailBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

class ArtDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityArtDetailBinding
    private lateinit var db: FirebaseFirestore
    private var isLiked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityArtDetailBinding.inflate(layoutInflater)
        setContentView(binding.constraintLayoutMain)

        ViewCompat.setOnApplyWindowInsetsListener(binding.constraintLayoutMain) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        db = FirebaseFirestore.getInstance()
        val repository = FirebaseArtsRepository()
        val artId = intent.getStringExtra("id") ?: run {
            finish()
            return
        }

        setupArtDetails(repository, artId)
        setupRecyclerView(repository)
        setupToolbar()
        setupButtons(repository, artId)
    }

    private fun setupArtDetails(repository: FirebaseArtsRepository, artId: String) {
        lifecycleScope.launch {
            val art = repository.list().find { it.id == artId }
            if (art == null) {
                finish()
                return@launch
            }

            updateImage(art.imageUrl)
            binding.tvArtDescription.text = art.description.take(80)

            binding.btnLike.setOnClickListener {
                isLiked = !isLiked
                updateLikeButton()
                saveLikeStatus(art.id, isLiked)
            }

            loadLikeStatus(art.id)

            binding.btnComment.setOnClickListener {
                val intent = Intent(this@ArtDetailActivity, CommentsActivity::class.java)
                intent.putExtra("id", art.id)
                startActivity(intent)
            }
        }
    }

    private fun setupRecyclerView(repository: FirebaseArtsRepository) {
        val layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL).apply {
            gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS
        }
        binding.recyclerRecommended.layoutManager = layoutManager

        lifecycleScope.launch {
            val recommendedArts = repository.list()
            binding.recyclerRecommended.adapter = ArtListAdapter(this@ArtDetailActivity, recommendedArts)
        }
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbarTop)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun setupButtons(repository: FirebaseArtsRepository, artId: String) {
        val authProvider = MockAuthProvider(this)
        binding.fabEdit.visibility = if (authProvider.isAdmin) View.VISIBLE else View.GONE
        binding.fabEdit.setOnClickListener {
            startActivity(Intent(this, SaveArtActivity::class.java))
        }

        binding.btnLocation.setOnClickListener {
            startActivity(Intent(this, CurrentArtLocationActivity::class.java))
        }

        binding.fabFavorite.setOnClickListener {
            showSelectAlbumDialog(artId)
        }
    }

    private fun updateImage(imageUrl: String) {
        Glide.with(this).load(imageUrl).into(binding.imageArtDetail)
    }

    private fun updateLikeButton() {
        val icon = if (isLiked) {
            R.drawable.ic_favorite_filled
        } else {
            R.drawable.outline_favorite_24
        }
        binding.btnLike.setImageResource(icon)
    }

    private fun saveLikeStatus(artId: String, liked: Boolean) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val likeRef = db.collection("artworks").document(artId).collection("likes").document(userId)

        if (liked) {
            likeRef.set(mapOf("liked" to true))
        } else {
            likeRef.delete()
        }
    }

    private fun loadLikeStatus(artId: String) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val likeRef = db.collection("artworks").document(artId).collection("likes").document(userId)

        likeRef.get().addOnSuccessListener { document ->
            if (document != null && document.exists()) {
                isLiked = document.getBoolean("liked") ?: false
                updateLikeButton()
            }
        }
    }

    private fun showSelectAlbumDialog(artId: String) {
        val albumsRepository = FirebaseAlbumsRepository()

        lifecycleScope.launch {
            val albums = albumsRepository.listAlbums()
            val albumNames = albums.map { it.name }.toTypedArray()

            AlertDialog.Builder(this@ArtDetailActivity)
                .setTitle("Selecione um Álbum")
                .setItems(albumNames) { _, which ->
                    val selectedAlbum = albums[which]
                    addArtToAlbum(selectedAlbum.id, artId)
                }
                .setPositiveButton("Criar Novo Álbum") { _, _ ->
                    showCreateAlbumDialog(artId)
                }
                .setNegativeButton("Cancelar", null)
                .show()
        }
    }

    private fun showCreateAlbumDialog(artId: String) {
        val albumsRepository = FirebaseAlbumsRepository()
        val input = EditText(this)

        AlertDialog.Builder(this)
            .setTitle("Nome do Novo Álbum")
            .setView(input)
            .setPositiveButton("Criar") { _, _ ->
                val albumName = input.text.toString()
                if (albumName.isNotEmpty()) {
                    lifecycleScope.launch {
                        try {
                            val newAlbum = albumsRepository.createAlbum(albumName)
                            addArtToAlbum(newAlbum.id, artId)
                        } catch (e: Exception) {
                            Toast.makeText(this@ArtDetailActivity, "Erro ao criar álbum", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(this, "O nome do álbum não pode ser vazio", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun addArtToAlbum(albumId: String, artId: String) {
        lifecycleScope.launch {
            try {
                FirebaseAlbumsRepository().addArtToAlbum(albumId, artId)
                Toast.makeText(this@ArtDetailActivity, "Arte adicionada ao álbum", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(this@ArtDetailActivity, "Erro ao adicionar arte ao álbum", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        android.R.id.home -> {
            finish()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }
}
