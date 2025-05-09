package models

import kotlinx.serialization.Serializable

@Serializable
internal data class SupportedLanguagesResponse(
    val data: SupportedLanguagesData,
    val message: String,
    val statusCode: Int
)

@Serializable
internal data class SupportedLanguagesData(
    val baseLanguage: UserLanguage,
    val supportedLanguages: List<UserLanguage>
)
@Serializable
internal data class UserLanguage(
    val code: String,
    val general: String,
    val local : String
)
