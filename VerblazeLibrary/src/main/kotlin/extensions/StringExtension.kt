package extensions


import managers.TranslationManager

val String.vbt: String
    get() {
        return TranslationManager.translate(this)
    }

fun String.vbtWithLang(languagecode: String): String {
    return TranslationManager.translateWithLang(this, languagecode)
}
