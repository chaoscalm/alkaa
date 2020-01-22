package com.escodro.task.presentation.detail.alarm

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.escodro.domain.usecase.alarm.CancelAlarm
import com.escodro.domain.usecase.alarm.ScheduleAlarm
import com.escodro.task.presentation.detail.TaskDetailProvider
import java.util.Calendar
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * [ViewModel] responsible to provide information to Task Alarm layout.
 */
internal class TaskAlarmViewModel(
    taskProvider: TaskDetailProvider,
    private val scheduleAlarmUseCase: ScheduleAlarm,
    private val cancelAlarmUseCase: CancelAlarm
) : ViewModel() {

    val taskData = taskProvider.taskData

    val chipVisibility = MediatorLiveData<Boolean>()

    init {
        chipVisibility.addSource(taskData) { chipVisibility.value = it.dueDate != null }
    }

    /**
     * Sets an alarm to the task.
     *
     * @param alarm the date and time to ring the calendar
     */
    fun setAlarm(alarm: Calendar) {
        Timber.d("setAlarm()")

        viewModelScope.launch {
            taskData.value?.let { task ->
                scheduleAlarmUseCase(task.id, alarm)
            }
        }
    }

    fun setRepeating(alarmInterval: AlarmInterval) {
        Timber.d("setRepeating - ${alarmInterval.name}")

        val updatedTask = if (alarmInterval == AlarmInterval.NEVER) {
            taskData.value?.copy(isRepeating = false, alarmInterval = null)
        } else {
            taskData.value?.copy(isRepeating = true, alarmInterval = alarmInterval)
        }

        updatedTask?.let { taskProvider.updateTask(it, viewModelScope) }
    }

    /**
     * Removes the task alarm.
     */
    fun removeAlarm() {
        Timber.d("removeAlarm()")

        viewModelScope.launch {
            taskData.value?.let { task ->
                cancelAlarmUseCase(task.id)
            }
        }
    }
}
