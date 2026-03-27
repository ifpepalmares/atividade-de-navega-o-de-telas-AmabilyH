package com.example.composenavigationapp

import android.os.Bundle
import androidx.compose.ui.unit.sp
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppNavigation()
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "telaA") {

        composable("telaA") {
            TelaA(navController)
        }

        composable("telaB") {
            TelaB(navController)
        }

        composable("telaC/{resultado}") {
            val resultado = it.arguments?.getString("resultado") ?: ""
            TelaC(navController, resultado)
        }
    }
}

@Composable
fun TelaA(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text("Gostaria de participar de uma pesquisa?",
            fontSize = 24.sp
        )


        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            navController.navigate("telaB")
        }) {
            Text("Ir para pesquisa",
                fontSize = 22.sp,
                color = Color.Yellow
            )
        }
    }
}

@Composable
fun TelaB(navController: NavController) {

    val perguntas = listOf(
        "Você toma decisões rápido?",
        "Você se considera um bom líder?",
        "Você acha que é assertivo?",
        "Você gosta de desafios?",
        "Você se adapta facilmente?",
        "Você é comunicativo?",
        "Você gosta de trabalhar em equipe?",
        "Você se considera organizado?",
        "Você assume responsabilidades?",
        "Você gosta de aprender coisas novas?"
    )

    // Lista de respostas (null = não respondida)
    val respostas = remember {
        mutableStateListOf<Boolean?>().apply {
            repeat(perguntas.size) { add(null) }
        }
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {

        Text(
            "Responda a pesquisa:",
            fontSize = 20.sp,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Cabeçalho da "tabela"
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(
                "Pergunta",
                modifier = Modifier.weight(2f),
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                "Sim",
                modifier = Modifier.weight(1f),
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                "Não",
                modifier = Modifier.weight(1f),
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Perguntas
        perguntas.forEachIndexed { index, pergunta ->

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = pergunta,
                    modifier = Modifier.weight(2f),
                    fontSize = 18.sp
                )

                Checkbox(
                    checked = respostas[index] == true,
                    onCheckedChange = { respostas[index] = true },
                    modifier = Modifier.weight(1f)
                )

                Checkbox(
                    checked = respostas[index] == false,
                    onCheckedChange = { respostas[index] = false },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                val sim = respostas.count { it == true }
                val nao = respostas.count { it == false }

                val resultado = when {
                    sim > nao -> "intensa"
                    nao > sim -> "passiva"
                    else -> "razoavel"
                }

                navController.navigate("telaC/$resultado")
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Finalizar e ver resultado")
        }
    }
}
@Composable
fun TelaC(navController: NavController, resultado: String) {

    val texto = when (resultado) {
        "intensa" -> "Você é uma pessoa intensa"
        "passiva" -> "Você é uma pessoa passiva"
        else -> "Você é uma pessoa razoável"
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(texto)

        Spacer(modifier = Modifier.height(20.dp))

        Button(onClick = {
            navController.navigate("telaA")
        }) {
            Text("Voltar para início",
                fontSize = 22.sp)
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(onClick = {
            navController.popBackStack("telaB", false)
        }) {
            Text("Voltar para perguntas",
                fontSize = 22.sp)
        }
    }
}
@Preview(showBackground = true)
@Composable
fun TelaAPreview() {
    val navController = rememberNavController()
    TelaA(navController)
}