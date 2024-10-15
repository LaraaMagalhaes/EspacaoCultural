package com.example.appcultural.views

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.appcultural.R
import com.example.appcultural.adapters.ArtListAdapter
import com.example.appcultural.data.MockArtRepository
import com.example.appcultural.databinding.ActivityAlbumDetailBinding
import com.example.appcultural.entities.Album
import com.google.android.material.appbar.MaterialToolbar

class AlbumDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAlbumDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAlbumDetailBinding.inflate(layoutInflater)
        setContentView(binding.main)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val repository = MockArtRepository()
        val viewManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        viewManager.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS
        binding.recycleView.layoutManager = viewManager
        binding.recycleView.adapter = ArtListAdapter(this, repository.list())

        val albumName = intent.getStringExtra("albumName")
        binding.topAppBar.title = albumName

        setSupportActionBar(binding.topAppBar);
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)
    }

    // Função para mostrar o popup de edição de nome do álbum
    private fun showEditAlbumNamePopup(album: Album, albumTitle: MaterialToolbar) {
        val dialogBuilder = AlertDialog.Builder(this)
        val dialogView = LayoutInflater.from(this).inflate(R.layout.activity_dialog_add_album, null)
        dialogBuilder.setView(dialogView)

        val albumNameInput = dialogView.findViewById<EditText>(R.id.edit_album_name)
        albumNameInput.setText(album.name) // Preencher com o nome atual do álbum

        dialogBuilder.setTitle("Editar Nome do Álbum")
        dialogBuilder.setPositiveButton("Salvar") { dialog, _ ->
            val newName = albumNameInput.text.toString()
            if (newName.isNotEmpty()) {
                // Atualizar o nome do álbum diretamente no objeto
                album.name = newName
                albumTitle.title = newName
            }
            dialog.dismiss()
        }

        dialogBuilder.setNegativeButton("Cancelar") { dialog, _ ->
            dialog.dismiss()
        }

        val dialog = dialogBuilder.create()
        dialog.show()
    }

    // Função para compartilhar o álbum
    private fun shareAlbum(album: Album) {
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, "Confira o álbum: ${album.name}")
            type = "text/plain"
        }
        startActivity(Intent.createChooser(shareIntent, "Compartilhar álbum via"))
    }

    // Função para buscar o álbum por ID
    private fun getAlbumById(albumId: Int): Album? {
        return AlbumsListActivity.albumList.find { it.id == albumId }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.home -> {
                finish()
                true
            }
            R.id.edit -> {
                val album = getAlbumById(intent.getIntExtra("albumId", -1)) ?: return true
                showEditAlbumNamePopup(album, binding.topAppBar)
                true
            }

            R.id.share -> {
                val album = getAlbumById(intent.getIntExtra("albumId", -1)) ?: return true
                shareAlbum(album)
                true
            }

            else -> {
                finish()
                true
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.header_detail, menu)
        return true
    }
}