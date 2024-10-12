package com.example.appcultural.entities

import java.util.Date

data class Art(
    public val id: Long,
    public val name: String,
    public val publishDate: Date,
    public val description: String,
    public val author: String,
    public val isActive: Boolean,
    public val imageUrl: String,
    public val genders: List<String>,
    public val location: ArtLocation
)
