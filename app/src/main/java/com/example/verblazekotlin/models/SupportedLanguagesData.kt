package com.example.verblazekotlin.models

import kotlinx.serialization.Serializable

@Serializable
internal data class SupportedLanguagesData(
    val baseLanguage: mylanguage,
    val supportedLanguages: List<mylanguage>
)