package com.example.appcultural.data

import com.example.appcultural.entities.Album
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await
import com.example.appcultural.entities.Art

class FirebaseAlbumsRepository {
    private val db = FirebaseFirestore.getInstance()
    private val collection = db.collection("albums")

    suspend fun fetchAll(): List<Album> {
        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        val result = db.collection("albums")
            .document(userId)
            .collection("userAlbums")
            .get().await()

        val albums = result.documents.mapNotNull {
            val album = it.toObject(Album::class.java)
            album?.id = it.id
            album
        }

        // Atualizar o campo `imageUrl` com a capa do álbum (primeira arte)
        for (album in albums) {
            if (album.artIds.isNotEmpty()) {
                val firstArtId = album.artIds.first() // Pega o primeiro ID de arte
                val artDocument = db.collection("arts").document(firstArtId).get().await()
                val art = artDocument.toObject(Art::class.java)
                if (art != null) {
                    album.imageUrl = art.imageUrl // Define a URL da primeira arte como capa
                }
            }
        }

        return albums
    }

    suspend fun delete(albumId: String) {
        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        db.collection("albums")
            .document(userId) // Documento do usuário
            .collection("userAlbums") // Subcoleção de álbuns
            .document(albumId) // Documento do álbum
            .delete()
            .await()
    }


    suspend fun findById(albumId: String): Album? {
        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        val document = db.collection("albums")
            .document(userId) // Documento do usuário
            .collection("userAlbums") // Subcoleção de álbuns
            .document(albumId) // Documento do álbum
            .get().await()

        val album = document.toObject(Album::class.java)
        album?.id = document.id
        return album
    }


    suspend fun create(name: String): Album {
        val userId = FirebaseAuth.getInstance().currentUser!!.uid // Pega o UID do usuário autenticado
        val album = Album(name = name)
        val result = db.collection("albums")
            .document(userId) // Documento do usuário
            .collection("userAlbums") // Subcoleção para álbuns
            .add(album).await()

        album.id = result.id
        return album
    }


    suspend fun appendArt(albumId: String, artId: String) {
        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        db.collection("albums")
            .document(userId) // Documento do usuário
            .collection("userAlbums") // Subcoleção de álbuns
            .document(albumId) // Documento do álbum
            .update("artIds", com.google.firebase.firestore.FieldValue.arrayUnion(artId))
            .await()
    }





    suspend fun update(albumId: String, updatedAlbum: Album) {
        collection.document(albumId).set(updatedAlbum).await()
    }
}
