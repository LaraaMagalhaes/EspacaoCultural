package com.example.appcultural.views

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.example.appcultural.R
import com.example.appcultural.adapters.AlbumListAdapter
import com.example.appcultural.databinding.ActivityAlbumsListBinding
import com.example.appcultural.entities.Album

class AlbumsListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAlbumsListBinding
    private lateinit var albumAdapter: AlbumListAdapter

    companion object {
        val albumList = mutableListOf<Album>(
            Album(1, "Album padrão", "https://www.artic.edu/iiif/2/57babddc-d879-16e5-5fb6-ea83ff05f21a/full/600,/0/default.jpg")
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAlbumsListBinding.inflate(layoutInflater)
        setContentView(binding.main)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        albumAdapter = AlbumListAdapter(this, albumList)
        binding.recycleView.layoutManager = GridLayoutManager(this, 2)
        binding.recycleView.adapter = albumAdapter

        binding.addAlbumButton.setOnClickListener {
            showAddAlbumPopup()
        }
    }

    // Função para mostrar um popup de adicionar álbum
    private fun showAddAlbumPopup() {
        val dialogBuilder = AlertDialog.Builder(this)
        val dialogView = LayoutInflater.from(this).inflate(R.layout.activity_dialog_add_album, null)
        dialogBuilder.setView(dialogView)

        val albumNameInput = dialogView.findViewById<EditText>(R.id.edit_album_name)

        dialogBuilder.setTitle("Adicionar Novo Álbum")
        dialogBuilder.setPositiveButton("Adicionar") { dialog, _ ->
            val albumName = albumNameInput.text.toString()
            if (albumName.isNotEmpty()) {
                val newAlbum = Album(
                    id = albumList.size + 1,
                    name = albumName,
                    imageUrl = "https://www.artic.edu/iiif/2/57babddc-d879-16e5-5fb6-ea83ff05f21a/full/600,/0/default.jpg", // Use uma imagem padrão para o álbum
                )
                albumList.add(newAlbum)
                albumAdapter.notifyItemInserted(albumList.size - 1)
            }
            dialog.dismiss()
        }

        dialogBuilder.setNegativeButton("Cancelar") { dialog, _ ->
            dialog.dismiss()
        }

        val dialog = dialogBuilder.create()
        dialog.show()
    }
}