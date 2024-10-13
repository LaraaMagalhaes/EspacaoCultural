package com.example.appcultural.data

import com.example.appcultural.entities.Comment
import com.example.appcultural.entities.User

class MockCommentRepository {
    private val data: ArrayList<Comment> = arrayListOf(
        Comment(1, "Comentario 1", 1, User(1, "Usuário 1")),
        Comment(2, "Comentario 2", 2, User(1, "Usuário 1")),
        Comment(3, "Comentario 3", 3, User(2, "Usuário 2")),
        Comment(4, "Comentario 4", 4, User(2, "Usuário 2")),
        Comment(5, "Comentario 5", 5, User(3, "Usuário 3")),
        Comment(6, "Comentario 6", 6, User(4, "Usuário 4")),
        Comment(7, "Comentario 7", 7, User(5, "Usuário 5")),
        Comment(8, "Comentario 8", 8, User(6, "Usuário 6"))
    )

    fun list(): ArrayList<Comment> {
        return data
    }
}