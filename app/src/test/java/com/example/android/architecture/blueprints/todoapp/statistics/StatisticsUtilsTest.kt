package com.example.android.architecture.blueprints.todoapp.statistics

import com.example.android.architecture.blueprints.todoapp.data.Task
import org.hamcrest.Matchers.`is`
import org.junit.Assert.*
import org.junit.Test

/**
 * Created by murodjon on 2020/12/14
 */
class StatisticsUtilsTest{

    @Test
    fun getActiveAndCompletedStats_nonCompleted_returnHundredZero(){
        // create an active task
        val tasks = listOf<Task>(
                Task("Task 1", "Task 1 description", isCompleted = false)
        )
        // call your function
        val result = getActiveAndCompletedStats(tasks)

        // check the result
        assertEquals(result.completedTasksPercent, 0f)
        assertEquals(result.activeTasksPercent, 100f)

        // Using Hamcrest
        assertThat(result.completedTasksPercent, `is`(0f))
        assertThat(result.activeTasksPercent, `is`(100f))
    }

    @Test
    fun getActiveAndCompleted_noActive_returnZeroHundred(){
        // create completed tasks
        // given a task
        val tasks = listOf<Task>(
                Task("Task 2", "Task 2 description", isCompleted = true)
        )

        // when the list of tasks is computed
        val result = getActiveAndCompletedStats(tasks)

        // Then the percentage 0 and 100
        assertThat(result.activeTasksPercent, `is`(0f))
        assertThat(result.completedTasksPercent, `is`(100f))
    }

    @Test
    fun getActiveAndCompleted_both_returnFourtySixty(){
        // create 3 completed tasks and 2 active tasks
        // given 3 completed tasks and 2 active tasks
        val tasks = listOf<Task>(
                Task("Task 1", "Task 1 description", isCompleted = true),
                Task("Task 2", "Task 2 description", isCompleted = true),
                Task("Task 3", "Task 3 description", isCompleted = true),
                Task("Task 4", "Task 3 description", isCompleted = false),
                Task("Task 5", "Task 3 description", isCompleted = false))

        // when the list of tasks is computed
        val result = getActiveAndCompletedStats(tasks)

        // then the result is 40-60
        assertThat(result.completedTasksPercent, `is`(60f))
        assertThat(result.activeTasksPercent, `is`(40f))
    }

    @Test
    fun getActiveAndCompleted_empty_returnsZeros(){
        // when there are no tasks
        val result = getActiveAndCompletedStats(emptyList())

        // then both active and completed tasks are 0
        assertThat(result.activeTasksPercent, `is`(0f))
        assertThat(result.completedTasksPercent, `is`(0f))
    }

}