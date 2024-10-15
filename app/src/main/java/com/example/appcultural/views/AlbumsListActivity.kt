package com.example.appcultural.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.appcultural.R
import com.example.appcultural.adapters.AlbumListAdapter
import com.example.appcultural.databinding.ActivityAlbumsListBinding
import com.example.appcultural.entities.Album

class AlbumsListActivity : Fragment() {
    private lateinit var binding: ActivityAlbumsListBinding
    private lateinit var albumAdapter: AlbumListAdapter

    companion object {
        val albumList = mutableListOf<Album>(
            Album(1, "Album padrão", "https://www.artic.edu/iiif/2/57babddc-d879-16e5-5fb6-ea83ff05f21a/full/600,/0/default.jpg")
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ActivityAlbumsListBinding.inflate(inflater, container, false)
        return binding.main
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        albumAdapter = AlbumListAdapter(requireContext(), albumList)
        binding.recycleView.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.recycleView.adapter = albumAdapter

        binding.addAlbumButton.setOnClickListener {
            showAddAlbumPopup()
        }
    }

    // Função para mostrar um popup de adicionar álbum
    private fun showAddAlbumPopup() {
        val dialogBuilder = AlertDialog.Builder(requireContext())
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.activity_dialog_add_album, null)
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