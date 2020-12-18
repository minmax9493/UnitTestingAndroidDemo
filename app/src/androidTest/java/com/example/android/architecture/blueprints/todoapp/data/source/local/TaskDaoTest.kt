package com.example.android.architecture.blueprints.todoapp.data.source.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.android.architecture.blueprints.todoapp.data.Task
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by murodjon on 2020/12/18
 */
@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class TasksDaoTest{

    // Executes each task synchronously using architecture components
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: ToDoDatabase

    @Before
    fun setUp() {
        // using an in-memory database so that the information stored here disappears when the process is killed
        database = Room.inMemoryDatabaseBuilder(
                getApplicationContext(),
                ToDoDatabase::class.java
        ).build()
    }

    @After
    fun tearDown() {
        //clean up your database
        database.close()
    }

    @Test
    fun insertTaskAndGetById() = runBlockingTest {
        // given - insert a task
        val task = Task("title", "description")
        database.taskDao().insertTask(task)

        // when - get the task by id from the database
        val loaded = database.taskDao().getTaskById(task.id)

        // then - the loaded data contains the expected values
        assertThat<Task>(loaded as Task, notNullValue())
        assertThat(loaded.id, `is`(task.id))
        assertThat(loaded.title, `is`(task.title))
        assertThat(loaded.description, `is`(task.description))
        assertThat(loaded.isCompleted, `is`(task.isCompleted))
    }

    @Test
    fun updateTaskAndGetById() = runBlockingTest{
        // When - inserting a task
        val task = Task("Title 1", "Description 1")
        database.taskDao().insertTask(task)

        // When - the task is updated
        val updatedTask = Task("Title 2", "Description 2", true, task.id)
        database.taskDao().updateTask(updatedTask)

        // then - the loaded data contains the expected values
        val loaded = database.taskDao().getTaskById(task.id)
        assertThat(loaded?.id, `is`(task.id))
        assertThat(loaded?.title, `is`("Title 2"))
        assertThat(loaded?.description, `is`("Description 2"))
        assertThat(loaded?.isCompleted, `is`(true))
    }
}