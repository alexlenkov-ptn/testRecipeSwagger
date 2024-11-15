package org.example.model

import kotlinx.serialization.Serializable

@Serializable
data class Word(
    val word : String,
    val translate: String,
    val progress: Int,
)