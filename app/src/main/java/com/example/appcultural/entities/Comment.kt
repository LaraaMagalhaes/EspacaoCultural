package com.example.appcultural.entities

data class Comment (
    val id: Int,
    val content: String,
    val likes: Int,
    val user: User
)