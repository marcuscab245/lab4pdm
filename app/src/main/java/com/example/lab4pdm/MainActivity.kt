package com.example.lab4pdm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.lab4pdm.ui.theme.Lab4pdmTheme
import com.example.lab4pdm.model.Task
import com.example.lab4pdm.components.TaskCard
import com.example.lab4pdm.viewmodel.GeneralViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Lab4pdmTheme {
                MainNavigation()
            }
        }
    }
}

@Composable
fun MainNavigation() {
    val navController = rememberNavController()
    val viewModel: GeneralViewModel = viewModel()

    NavHost(navController = navController, startDestination = "task_screen") {
        composable("task_screen") {
            TaskScreen(viewModel = viewModel)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskScreen(viewModel: GeneralViewModel) {
    var showDialog by remember { mutableStateOf(false) }

    val taskList by viewModel.tasks.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Mis Tareas Pendientes") })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Añadir tarea")
            }
        }
    ) { paddingValues ->

        LazyColumn(modifier = Modifier.padding(paddingValues)) {
            items(taskList) { task ->
                TaskCard(task)
                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        if (showDialog) {
            CreateTask(
                onDismiss = { showDialog = false },
                onTaskCreated = { newTitle, newDescription ->
                    val newTask = Task(
                        title = newTitle,
                        description = newDescription
                    )
                    viewModel.addTask(newTask)
                    showDialog = false
                }
            )
        }
    }
}

@Composable
fun CreateTask(
    onDismiss: () -> Unit,
    onTaskCreated: (String, String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    Dialog(
        onDismissRequest = { onDismiss() },
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false
        ),
    ) {
        Column(
            modifier = Modifier
                .wrapContentSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Nueva Tarea",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 16.dp),
                color = MaterialTheme.colorScheme.onBackground
            )

            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Título") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Descripción") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Button(
                    onClick = { onDismiss() },
                    modifier = Modifier.padding(end = 8.dp)
                ) {
                    Text(text = "Cancelar")
                }

                Button(
                    onClick = {
                        if (title.isNotBlank()) {
                            onTaskCreated(title, description)
                        }
                    },
                    enabled = title.isNotBlank()
                ) {
                    Text("Guardar")
                }
            }
        }
    }
}