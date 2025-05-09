package network

import android.util.Log
import cache.DataStoreManager
import models.CheckVersionRequest
import models.MultipleTranslationRequest
import models.TranslationFile
import models.UserLanguage
import providers.currentLocale
import providers.toLocaleInfo
import retrofit2.HttpException

internal class ApiFunctions(
    private val apiClient: ApiClient
) {

    suspend fun checkVersion(apiKey: String, lastVersion: Int) {
        println("check version baslar")
        try {
            println("respones baslar")
            val response = apiClient.checkVersion(
                apiKey,
                request = CheckVersionRequest(lastVersion)
            )
            println("respones yapildi")
            if (response.isSuccessful) {
                println("respones successful")
                Log.e(
                    "checkVersion",
                    "StatusCode : ${response.body()?.statusCode}\nMessage : ${response.body()?.message}"
                )
                if (response.body()?.data?.needsUpdate == true) {
                    DataStoreManager.saveLastVersion(response.body()?.data?.latestVersion!!)
                    setSupportedLanguages(apiKey)
                }
            } else {
                println("respones fails")
                Log.e("Err_CheckVersion", "${HttpException(response).localizedMessage}")
            }
        } catch (e: Exception) {
            Log.e("checkVersion_api", "${e.localizedMessage}")
        }
        println("check version biter")
    }

    suspend fun setSupportedLanguages(apiKey: String) {
        try {
            val response = apiClient.fetchSupportedLanguages(apiKey)
            if (response.isSuccessful) {
                Log.e(
                    "checkVersion",
                    "StatusCode : ${response.body()?.statusCode}\nMessage : ${response.body()?.message}"
                )
                DataStoreManager.saveSupportedLanguages(response.body()?.data!!)
                DataStoreManager.getCurrentLanguage()?.let{
                }?:run{
                    val systemLanguage: UserLanguage =
                        VerblazeBase.appContext.currentLocale().toLocaleInfo()
                    val currentLanguage: UserLanguage =
                        response.body()?.data?.supportedLanguages?.find { it.code == systemLanguage.code }
                            ?: response.body()?.data?.baseLanguage!!
                    DataStoreManager.saveCurrentLanguage(currentLanguage)
                }

                setMultipleTranslations(
                    apiKey,
                    response.body()?.data?.supportedLanguages?.map { it.code }!!,
                    DataStoreManager.getCurrentLanguage()!!
                )
            } else {
                Log.e("Err_supportedLanguages", "${HttpException(response).localizedMessage}")
            }
        } catch (e: Exception) {
            Log.e("setSupportedLanguages","${e.localizedMessage}")
        }
    }

    suspend fun setMultipleTranslations(apiKey: String, codeList: List<String>,currentLanguage:UserLanguage) {
        try {
            val response = apiClient.fetchMultipleTranslations(
                apiKey,
                request = MultipleTranslationRequest(codeList)
            )
            if (response.isSuccessful) {
                Log.e(
                    "checkVersion",
                    "StatusCode : ${response.body()?.statusCode}\nMessage : ${response.body()?.message}"
                )
                DataStoreManager.saveWholeTranslations(response.body()?.data!!)
                val currentTranslations :List<TranslationFile> = response.body()?.data?.translations?.get(currentLanguage.code)!!
                DataStoreManager.saveCurrentTranslations(currentTranslations)
            } else {
                Log.e("Err_multipleTranslations", "${HttpException(response).localizedMessage}")
            }
        } catch (e: Exception) {
            Log.e("setMultipleTranslations","${e.localizedMessage}")
        }
    }
}