package com.example.lab4pdm.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import com.example.lab4pdm.model.Task
import com.example.lab4pdm.InitDatabase

class GeneralViewModel : ViewModel() {

    private val taskDao = InitDatabase.database.taskDao()

    val tasks: StateFlow<List<Task>> = taskDao.getAllTasks()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun addTask(task: Task) {
        viewModelScope.launch {
            taskDao.insertTask(task)
        }
    }
}