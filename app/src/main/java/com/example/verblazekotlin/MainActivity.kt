package com.example.verblazekotlin

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.verblazekotlin.cache.exampleViewModel
import com.example.verblazekotlin.pages.TestPage1
import com.example.verblazekotlin.pages.TestPage2
import com.example.verblazekotlin.ui.theme.VerblazeKotlinTheme
import kotlinx.coroutines.runBlocking

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VerblazeKotlinTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding),
                        this
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier, context: Context) {
    /////////////////////////////////////////////
    //It is important to set provider and start configure before setting others
    val provider = VerblazeBase.FunctionProvider
    provider.configure(context,"vb-api-48c42a46472aef65")
    ////////////////////////////////////////////
    val examplevm = exampleViewModel(provider)
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "TestPage1"
    ) {
        composable(route = "TestPage1") {
            TestPage1(navController,examplevm,provider)
        }
        composable(route = "TestPage2") { TestPage2(navController) }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    VerblazeKotlinTheme {
        //Greeting("Android")
    }
}