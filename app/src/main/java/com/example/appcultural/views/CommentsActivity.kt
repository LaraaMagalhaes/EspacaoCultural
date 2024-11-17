package com.example.appcultural.views

import CommentAdapter
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appcultural.R
import com.example.appcultural.databinding.ActivityCommentsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.android.material.floatingactionbutton.FloatingActionButton

class CommentsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCommentsBinding
    private val firestore = FirebaseFirestore.getInstance()

    private lateinit var adapter: CommentAdapter
    private val comments = mutableListOf<Comment>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCommentsBinding.inflate(layoutInflater)
        setContentView(binding.main)

        // Configuração do RecyclerView
        binding.recycleView.layoutManager = LinearLayoutManager(this)

        // Criando o adaptador
        adapter = CommentAdapter(comments) { comment ->
            incrementLikeCount(comment)
        }

        binding.recycleView.adapter = adapter

        setSupportActionBar(binding.topAppBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Tratamento do botão de envio de comentário
        binding.sendCommentFab.setOnClickListener {
            val newComment = binding.commentInputText.text.toString()

            if (newComment.isNotBlank()) {
                val user = FirebaseAuth.getInstance().currentUser
                if (user == null) {
                    Toast.makeText(this, "Você precisa estar logado para comentar", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                val username = user.displayName ?: "Usuário Desconhecido"
                val userId = user.uid

                // Adiciona o comentário ao Firestore
                addCommentToFirestore(userId, username, newComment)

                // Limpa o campo de entrada
                binding.commentInputText.text.clear()
            } else {
                Toast.makeText(this, "O comentário não pode estar vazio", Toast.LENGTH_SHORT).show()
            }
        }

        // Carregar os comentários ao iniciar a activity
        loadComments()
    }

    private fun loadComments() {
        firestore.collection("comments")
            .addSnapshotListener { result, exception ->
                if (exception != null) {
                    Toast.makeText(this, "Erro ao carregar comentários: ${exception.message}", Toast.LENGTH_SHORT).show()
                    return@addSnapshotListener
                }

                comments.clear()
                result?.forEach { document ->
                    val comment = document.toObject(Comment::class.java)
                    comments.add(comment.copy(id = document.id))
                }
                adapter.notifyDataSetChanged()
            }
    }


    private fun addCommentToFirestore(userId: String, username: String, commentText: String) {
        val comment = hashMapOf(
            "userId" to userId,
            "username" to username,
            "commentText" to commentText,
            "likes" to 0 // Inicia o comentário com 0 curtidas
        )

        firestore.collection("comments")
            .add(comment)
            .addOnSuccessListener { documentReference ->
                val commentWithId = Comment(
                    userId = userId,
                    username = username,
                    commentText = commentText,
                    likes = 0,
                    id = documentReference.id // Atribui o ID gerado
                )
                comments.add(commentWithId)
                adapter.notifyDataSetChanged()
                Toast.makeText(this, "Comentário adicionado!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Erro ao adicionar comentário: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun incrementLikeCount(comment: Comment) {
        val commentRef = firestore.collection("comments").document(comment.id)
        val newLikeCount = if (comment.likedByUser) {
            comment.likes - 1 // Descurtir
        } else {
            comment.likes + 1 // Curtir
        }

        firestore.runTransaction { transaction ->
            val snapshot = transaction.get(commentRef)
            if (snapshot.exists()) {
                transaction.update(commentRef, "likes", newLikeCount)
            } else {
                throw FirebaseFirestoreException("Comentário não encontrado", FirebaseFirestoreException.Code.NOT_FOUND)
            }
        }.addOnSuccessListener {
            // Atualizar a lista local de comentários após sucesso na transação
            val updatedComment = comment.copy(
                likes = newLikeCount,
                likedByUser = !comment.likedByUser // Alterar o estado de curtido para descurtido, ou vice-versa
            )
            val index = comments.indexOfFirst { it.id == comment.id }
            if (index != -1) {
                comments[index] = updatedComment
                adapter.notifyItemChanged(index)
            }
        }.addOnFailureListener { e ->
            Toast.makeText(this, "Erro ao atualizar curtida: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    // Modelo de dados de um comentário
    data class Comment(
        val userId: String = "",
        val username: String = "",
        val commentText: String = "",
        val likes: Int = 0,
        val id: String = "",
        val likedByUser: Boolean = false // Indica se o usuário curtiu ou não
    )


    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        android.R.id.home -> {
            finish()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }
}
