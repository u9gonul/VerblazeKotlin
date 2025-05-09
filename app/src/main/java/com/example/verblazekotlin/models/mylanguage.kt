package com.example.verblazekotlin.models

import kotlinx.serialization.Serializable

@Serializable
data class mylanguage(
    val code: String,
    val general: String,
    val local : String
)
