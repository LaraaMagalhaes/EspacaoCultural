package com.example.appcultural.views

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.appcultural.adapters.ArtListAdapter
import com.example.appcultural.data.FirebaseAlbumsRepository
import com.example.appcultural.data.FirebaseArtsRepository
import com.example.appcultural.databinding.ActivityAlbumDetailBinding
import kotlinx.coroutines.launch

class AlbumDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAlbumDetailBinding
    private lateinit var albumId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAlbumDetailBinding.inflate(layoutInflater)
        setContentView(binding.main)

        // Obter o ID do álbum da Intent
        albumId = intent.getStringExtra("albumId") ?: run {
            finish()
            return
        }

        // Configurar o título da AppBar
        binding.topAppBar.title = intent.getStringExtra("albumName") ?: "Detalhes do Álbum"
        binding.topAppBar.setNavigationOnClickListener {
            finish()
        }

        // Configurar o menu
        setupMenu()

        // Configurar o RecyclerView
        setupRecyclerView()

        // Carregar as artes do álbum
        loadArtsFromDatabase()
    }

    private fun setupMenu() {
        // Infla o menu dinamicamente
        val menuId = resources.getIdentifier("menu_album_detail", "menu", packageName)
        if (menuId != 0) {
            binding.topAppBar.inflateMenu(menuId)
        } else {
            Log.e("MenuError", "Menu não encontrado")
        }

        // Configurar listener para os itens do menu
        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                getResourceId("edit", "id") -> {
                    editAlbumName()
                    true
                }
                getResourceId("share", "id") -> {
                    shareAlbum()
                    true
                }
                else -> false
            }
        }
    }

    private fun setupRecyclerView() {
        val layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL).apply {
            gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS
        }
        binding.recycleView.layoutManager = layoutManager
    }

    private fun loadArtsFromDatabase() {
        val artsRepository = FirebaseArtsRepository()
        val albumsRepository = FirebaseAlbumsRepository()

        lifecycleScope.launch {
            try {
                val album = albumsRepository.findById(albumId)
                val arts = if (album != null && album.artIds.isNotEmpty()) {
                    artsRepository.getArtsByIds(album.artIds)
                } else {
                    emptyList()
                }
                binding.recycleView.adapter = ArtListAdapter(this@AlbumDetailActivity, arts)
            } catch (e: Exception) {
                Log.e("AlbumDetailActivity", "Erro ao carregar as artes do álbum", e)
                Toast.makeText(this@AlbumDetailActivity, "Erro ao carregar as artes", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun editAlbumName() {
        val editText = EditText(this).apply {
            hint = "Novo nome do álbum"
        }

        AlertDialog.Builder(this)
            .setTitle("Editar Nome")
            .setView(editText)
            .setPositiveButton("Salvar") { _, _ ->
                val newName = editText.text.toString().trim()
                if (newName.isNotEmpty()) {
                    updateAlbumNameInFirebase(newName)
                } else {
                    Toast.makeText(this, "O nome não pode estar vazio!", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun updateAlbumNameInFirebase(newName: String) {
        val albumsRepository = FirebaseAlbumsRepository()

        lifecycleScope.launch {
            try {
                val album = albumsRepository.findById(albumId)
                if (album != null) {
                    album.name = newName
                    albumsRepository.updateAlbum(albumId, album)
                    binding.topAppBar.title = newName
                    Toast.makeText(this@AlbumDetailActivity, "Nome alterado com sucesso!", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e("AlbumDetailActivity", "Erro ao atualizar o nome do álbum", e)
                Toast.makeText(this@AlbumDetailActivity, "Erro ao alterar o nome", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun shareAlbum() {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, "Confira este álbum!")
            putExtra(
                Intent.EXTRA_TEXT,
                "Dê uma olhada no álbum '${binding.topAppBar.title}' na minha app cultural!"
            )
        }
        startActivity(Intent.createChooser(shareIntent, "Compartilhar álbum via"))
    }

    private fun getResourceId(name: String, type: String): Int {
        return resources.getIdentifier(name, type, packageName)
    }
}
