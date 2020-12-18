package com.example.android.architecture.blueprints.todoapp.data.source.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.example.android.architecture.blueprints.todoapp.data.Result
import com.example.android.architecture.blueprints.todoapp.data.Task
import com.example.android.architecture.blueprints.todoapp.data.succeeded
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by murodjon on 2020/12/18
 */
@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@MediumTest
class TasksLocalDataSourceTest{

    // Executes each task synchronously using Architecture Components
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var localDataSource: TasksLocalDataSource
    private lateinit var database: ToDoDatabase

    @Before
    fun setUp() {
        database = Room
                .inMemoryDatabaseBuilder(getApplicationContext(), ToDoDatabase::class.java)
                .allowMainThreadQueries()
                .build()
        localDataSource = TasksLocalDataSource(database.taskDao(), Dispatchers.Main)
    }

    @Test
    fun saveTask_retrieveTask() = runBlocking {
        // given a new task saved in the database
        val newTask = Task("Title 1", "Description 1", false)
        localDataSource.saveTask(newTask)

        // when the task retrieved by ID
        val result = localDataSource.getTask(newTask.id)

        // then - same task is returned
        assertThat(result.succeeded, `is`(true))
        result as Result.Success
        assertThat(result.data.title, `is`("Title 1"))
        assertThat(result.data.description, `is`("Description 1"))
        assertThat(result.data.isCompleted, `is`(false))
    }

    @Test
    fun completeTask_retrieveTaskIsComplete()= runBlocking{
        // given - an active task
        val activeTask = Task("Title 1", "Description 1", false)
        localDataSource.saveTask(activeTask)

        val result = localDataSource.getTask(activeTask.id)
        val loadedTask = (result as Result.Success).data

        // when - an active task is completed
        localDataSource.completeTask(loadedTask)

        val completedTask = localDataSource.getTask(loadedTask.id)
        // then - the task is completed
        completedTask as Result.Success
        assertThat(completedTask.succeeded, `is`(true))
        assertThat(completedTask.data.isCompleted, `is`(true))
        assertThat(completedTask.data.title, `is`("Title 1"))
        assertThat(completedTask.data.description, `is`("Description 1"))
    }

    @After
    fun tearDown() {
        database.close()
    }
}