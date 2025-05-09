package com.example.verblazekotlin.cache

import VerblazeBase
import androidx.lifecycle.ViewModel
import com.example.verblazekotlin.models.SupportedLanguagesData
import com.example.verblazekotlin.models.mylanguage
import kotlinx.serialization.json.Json

class exampleViewModel(
    VerblazeProvider : VerblazeBase.FunctionProvider
): ViewModel() {
    private val serializer = mylanguage.serializer()
    var selectedLanguage : mylanguage = Json.decodeFromString(serializer,VerblazeProvider.getCurrentLanguage())
    val optionlist: List<mylanguage> =
        Json.decodeFromString<SupportedLanguagesData>(
            VerblazeProvider.getSupportedLanguages()
        ).supportedLanguages
}