package org.example.model
import kotlinx.serialization.Serializable

@Serializable
data class Ingredient(
    val quantity: String,
    val unitOfMeasure: String,
    val description: String,
)