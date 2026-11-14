
package com.example.nasaapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.nasaapp.navigation.AppNavGraph
import com.example.nasaapp.ui.theme.NASAAPPTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            NASAAPPTheme {
                AppNavGraph()
            }
        }
    }
}
