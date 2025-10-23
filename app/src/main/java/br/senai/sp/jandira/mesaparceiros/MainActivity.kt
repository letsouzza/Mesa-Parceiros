package br.senai.sp.jandira.mesaparceiros

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import br.senai.sp.jandira.mesaparceiros.screens.AtualizacaoSenha
import br.senai.sp.jandira.mesaparceiros.screens.CadastroAlimentoPrimeiro
import br.senai.sp.jandira.mesaparceiros.screens.CadastroAlimentoSegundo
import br.senai.sp.jandira.mesaparceiros.screens.CadastroEmpresa
import br.senai.sp.jandira.mesaparceiros.screens.CodigoSenha
import br.senai.sp.jandira.mesaparceiros.screens.LoginScreen
import br.senai.sp.jandira.mesaparceiros.screens.RecuperacaoSenha
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
                    startDestination = "cadastroAlimento1"
                ){
                    composable(route = "login"){ LoginScreen(navegacao)}
                    composable(route = "cadastro"){ CadastroEmpresa(navegacao)}
                    composable(route = "splash"){ SplashScreen(navegacao)}
                    composable(route = "recuperacao"){ RecuperacaoSenha(navegacao) }
                    composable(route = "codigo"){ CodigoSenha(navegacao) }
                    composable(route = "atualizarSenha"){ AtualizacaoSenha(navegacao) }
                    composable(
                        route = "cadastroAlimento1?fromSecond={fromSecond}",
                        arguments = listOf(
                            navArgument("fromSecond") {
                                type = NavType.BoolType
                                defaultValue = false
                            }
                        )
                    ) { backStackEntry ->
                        val fromSecond = backStackEntry.arguments?.getBoolean("fromSecond") ?: false
                        CadastroAlimentoPrimeiro(navegacao, fromSecond)
                    }
                    composable(route = "cadastroAlimento2"){ CadastroAlimentoSegundo(navegacao) }
                }
            }
        }
    }
}
