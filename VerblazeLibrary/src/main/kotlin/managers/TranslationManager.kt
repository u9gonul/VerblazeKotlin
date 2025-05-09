package managers

import android.util.Log
import cache.DataStoreManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.runBlocking
import models.MultipleTranslations
import models.TranslationFile
import models.UserLanguage
import java.lang.Exception

internal object TranslationManager {
    internal val currentLanguage: MutableStateFlow<UserLanguage> = runBlocking {
        MutableStateFlow(DataStoreManager.getCurrentLanguage()!!)
    }
    private val currentTranslations: MutableStateFlow<List<TranslationFile>> = runBlocking {
        println("runBlocking in the TranslationManager currentTranslations")
        MutableStateFlow<List<TranslationFile>>(DataStoreManager.getCurrentTranslations())
    }
    private val wholeTranslations: MutableStateFlow<MultipleTranslations?> =
        runBlocking { MutableStateFlow<MultipleTranslations?>(DataStoreManager.getWholeTranslations()) }

    suspend fun updateCurrentLanguageAndTranslations(newLanguage: String) {
        try {
            val currentLanguageWillSet: UserLanguage =
                DataStoreManager.getSupportedLanguages().supportedLanguages.find { it.code == newLanguage }!!
            DataStoreManager.saveCurrentLanguage(currentLanguageWillSet)
            DataStoreManager.saveCurrentTranslations(
                wholeTranslations.value?.translations?.get(
                    newLanguage
                )!!
            )

            currentLanguage.value = currentLanguageWillSet
            currentTranslations.value =
                wholeTranslations.value?.translations?.get(newLanguage)!!
        } catch (e: Exception) {
            Log.e("updateCurrentTranslations", "${e.localizedMessage}")
        }
        println("updating biter")
    }

    fun translate(text: String): String {
        require(currentTranslations.value.isNotEmpty()) { "Translations for ${runBlocking { DataStoreManager.getCurrentLanguage() }} was not found" }
        require(text.isNotEmpty()) { "Text $text,that you entered to translate, is empty" }
        val parts = text.split(".", limit = 2)
        require(parts[0].isNotEmpty() && parts[1].isNotEmpty() && parts.size == 2) { "Invalid translation key format. Use: file_key.translation_key" }
        val fileKey = parts[0]
        val valueKey = parts[1]
        val values: Map<String, String>? =
            currentTranslations.value.firstOrNull { it.file_key == fileKey }?.values
        return values?.get(valueKey) ?: text
    }

    fun translateWithLang(text: String, languageCode: String): String {
        require(text.isNotEmpty()) { "Text $text, that you entered to translate, is empty" }
        val selectedTranslations: List<TranslationFile>? =
            wholeTranslations.value?.translations?.get(languageCode)
        require(!selectedTranslations.isNullOrEmpty()) { "Tranlastions for $languageCode was not found" }
        val parts = text.split(".", limit = 2)
        require(parts[0].isNotEmpty() && parts[1].isNotEmpty() && parts.size == 2) { "Invalid translation key format. Use: file_key.translation_key" }
        val fileKey = parts[0]
        val valueKey = parts[1]
        val values: Map<String, String>? =
            selectedTranslations.firstOrNull { it.file_key == fileKey }?.values
        return values?.get(valueKey) ?: text
    }
}