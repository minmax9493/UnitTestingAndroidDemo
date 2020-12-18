package com.example.android.architecture.blueprints.todoapp.statistics

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.android.architecture.blueprints.todoapp.MainCoroutineRule
import com.example.android.architecture.blueprints.todoapp.data.source.FakeTestRepository
import com.example.android.architecture.blueprints.todoapp.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.CoreMatchers.`is`
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created by murodjon on 2020/12/18
 */
@ExperimentalCoroutinesApi
class StatisticsViewModelTest{

    // executes each task synchronously using architecture components
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    // set the main coroutines dispatcher for unit testing
    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    // subject under test
    private lateinit var statisticsViewModel:StatisticsViewModel

    // use a fake repository to be injected into the view model
    private lateinit var taskRepository:FakeTestRepository

    @Before
    fun setUp() {
        // initialize the repository with no tasks
        taskRepository = FakeTestRepository()

        statisticsViewModel = StatisticsViewModel(taskRepository)
    }

    @Test
    fun loadTasks_loading(){
        // pause dispatcher so you can verify initial values
        mainCoroutineRule.pauseDispatcher()

        //load the task in the view model.
        statisticsViewModel.refresh()

        // then assert that the progress indicator is shown
        assertThat(statisticsViewModel.dataLoading.getOrAwaitValue(), `is`(true))

        // execute pending coroutines actions
        mainCoroutineRule.resumeDispatcher()

        // then assert that the progress indicator is hidden
        assertThat(statisticsViewModel.dataLoading.getOrAwaitValue(), `is`(false))
    }

    @Test
    fun loadStatisticsWhenTasksAreUnavailable_callErrorToDisplay(){
        // make the repository
        taskRepository.setReturnError(true)
        statisticsViewModel.refresh()

        //then empty and error are true(which triggers an error message to be shown)
        assertThat(statisticsViewModel.empty.getOrAwaitValue(), `is`(true))
        assertThat(statisticsViewModel.error.getOrAwaitValue(), `is`(true))
    }
}