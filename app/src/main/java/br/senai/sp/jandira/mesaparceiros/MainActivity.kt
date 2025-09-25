package br.senai.sp.jandira.mesaparceiros

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import br.senai.sp.jandira.mesaparceiros.screens.CadastroEmpresa
import br.senai.sp.jandira.mesaparceiros.screens.LoginScreen
import br.senai.sp.jandira.mesaparceiros.screens.SplashScreen
import br.senai.sp.jandira.mesaparceiros.ui.theme.MesaParceirosTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MesaParceirosTheme {
                val navegacao = rememberNavController()
                NavHost(
                    navController = navegacao,
                    startDestination = "splash"
                ){
                    composable(route = "login"){ LoginScreen(navegacao)}
                    composable(route = "cadastro"){ CadastroEmpresa(navegacao)}
                    composable(route = "splash"){ SplashScreen(navegacao)}
                }
            }
        }
    }
}
