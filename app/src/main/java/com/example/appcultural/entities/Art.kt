package com.example.appcultural.entities

data class Art(
    public val id: Int,
    public val name: String,
    public val publishDate: Int,
    public val description: String,
    public val author: String,
    public val isActive: Boolean,
    public val imageUrl: String,
    public val genders: List<String>,
    public val location: ArtLocation
)
