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

        albumId = intent.getStringExtra("albumId") ?: run {
            finish()
            return
        }
        binding.topAppBar.title = intent.getStringExtra("albumName") ?: "Detalhes do Álbum"
        binding.topAppBar.setNavigationOnClickListener {
            finish()
        }
        setupMenu()
        setupRecyclerView()
        loadArtsFromDatabase()
    }

    private fun confirmDelete() {
        AlertDialog.Builder(this)
            .setTitle("Excluir Álbum")
            .setMessage("Tem certeza de que deseja excluir este álbum? Esta ação não pode ser desfeita.")
            .setPositiveButton("Excluir") { _, _ ->
                deleteAlbumFromFirebase() // Chama a função para excluir o álbum
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun deleteAlbumFromFirebase() {
        val albumsRepository = FirebaseAlbumsRepository()

        lifecycleScope.launch {
            try {
                albumsRepository.delete(albumId) // Chama o repositório para excluir
                Toast.makeText(this@AlbumDetailActivity, "Álbum excluído com sucesso!", Toast.LENGTH_SHORT).show()
                finish() // Fecha a tela de detalhes do álbum
            } catch (e: Exception) {
                Log.e("AlbumDetailActivity", "Erro ao excluir o álbum", e)
                Toast.makeText(this@AlbumDetailActivity, "Erro ao excluir o álbum", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun setupMenu() {
        val menuId = resources.getIdentifier("menu_album_detail", "menu", packageName)
        if (menuId != 0) {
            binding.topAppBar.inflateMenu(menuId)
        } else {
            Log.e("MenuError", "Menu não encontrado")
        }

        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                getResourceId("edit", "id") -> {
                    editName()
                    true
                }
                getResourceId("share", "id") -> {
                    shareContent()
                    true
                }
                getResourceId("delete", "id") -> {
                    confirmDelete() // Chama a função de confirmação de exclusão
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
                // Busca o álbum pelo ID
                val album = albumsRepository.findById(albumId)
                if (album == null) {
                    Toast.makeText(this@AlbumDetailActivity, "Álbum não encontrado!", Toast.LENGTH_SHORT).show()
                    finish()
                    return@launch
                }

                // Carrega as artes associadas ao álbum
                val arts = if (album.artIds.isNotEmpty()) {
                    artsRepository.fetchByIds(album.artIds)
                } else {
                    emptyList()
                }

                // Atualiza a interface com as artes
                binding.recycleView.adapter = ArtListAdapter(this@AlbumDetailActivity, arts)
            } catch (e: Exception) {
                Log.e("AlbumDetailActivity", "Erro ao carregar as artes do álbum", e)
                Toast.makeText(this@AlbumDetailActivity, "Erro ao carregar as artes", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun editName() {
        val editText = EditText(this).apply {
            hint = "Novo nome do álbum"
        }

        AlertDialog.Builder(this)
            .setTitle("Editar Nome")
            .setView(editText)
            .setPositiveButton("Salvar") { _, _ ->
                val newName = editText.text.toString().trim()
                if (newName.isNotEmpty()) {
                    updateNameInFirebase(newName)
                } else {
                    Toast.makeText(this, "O nome não pode estar vazio!", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun updateNameInFirebase(newName: String) {
        val albumsRepository = FirebaseAlbumsRepository()

        lifecycleScope.launch {
            try {
                val album = albumsRepository.findById(albumId)
                if (album != null) {
                    album.name = newName
                    albumsRepository.update(albumId, album)
                    binding.topAppBar.title = newName
                    Toast.makeText(this@AlbumDetailActivity, "Nome alterado com sucesso!", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e("AlbumDetailActivity", "Erro ao atualizar o nome do álbum", e)
                Toast.makeText(this@AlbumDetailActivity, "Erro ao alterar o nome", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun shareContent() {
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
