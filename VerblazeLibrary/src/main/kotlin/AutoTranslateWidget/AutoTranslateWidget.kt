package AutoTranslateWidget

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.staticCompositionLocalOf
import managers.TranslationManager
import models.UserLanguage

internal val LocalCurrentLanguage = staticCompositionLocalOf<UserLanguage> {
    error("Language not provided")
}

@Composable
fun AutoTranslatedWidget(
    content: @Composable () -> Unit
) {
    val lang = TranslationManager.currentLanguage.collectAsState().value
    CompositionLocalProvider(LocalCurrentLanguage provides lang) {
        content()
    }
}
