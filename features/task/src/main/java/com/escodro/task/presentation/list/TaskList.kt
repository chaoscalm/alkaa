package com.escodro.task.presentation.list

import android.text.format.DateUtils
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.preferredHeight
import androidx.compose.foundation.layout.preferredWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Card
import androidx.compose.material.Checkbox
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.AmbientContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.escodro.task.model.Category
import com.escodro.task.model.Task
import com.escodro.task.model.TaskWithCategory
import com.escodro.theme.AlkaaTheme
import org.koin.androidx.compose.getViewModel
import java.util.Calendar

/**
 * Alkaa Task Section.
 *
 * @param modifier the decorator
 */
@Composable
fun TaskListSection(modifier: Modifier = Modifier) {
    TaskListLoader(modifier = modifier)
}

@Composable
private fun TaskListLoader(
    modifier: Modifier = Modifier,
    viewModel: TaskListViewModel = getViewModel()
) {
    viewModel.loadTasks()
    val viewState by viewModel.state.collectAsState()
    val taskList = viewState.items
    TaskListContent(
        taskList = taskList,
        modifier = modifier,
        onCheckedChanged = { item: TaskWithCategory ->
            viewModel.updateTaskStatus(item)
        }
    )
}

@Composable
private fun TaskListContent(
    taskList: List<TaskWithCategory>,
    modifier: Modifier = Modifier,
    onCheckedChanged: (TaskWithCategory) -> Unit
) {
    Surface(modifier = modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
        Column(modifier = Modifier.padding(start = 8.dp, end = 8.dp)) {
            LazyColumn {
                items(items = taskList, itemContent = { task ->
                    TaskItem(task = task, onItemClicked = { }, onCheckedChanged = onCheckedChanged)
                })
            }
        }
    }
}

/**
 * Alkaa Task Item.
 *
 * @param modifier the decorator
 * @param task the task item to be rendered
 * @param onItemClicked the action to be done when the item is clicked
 */
@Composable
fun TaskItem(
    modifier: Modifier = Modifier,
    task: TaskWithCategory,
    onItemClicked: (TaskWithCategory) -> Unit,
    onCheckedChanged: (TaskWithCategory) -> Unit
) {
    Card(
        elevation = 4.dp,
        modifier = modifier
            .fillMaxWidth()
            .padding(all = 8.dp)
            .preferredHeight(74.dp)
            .clickable { onItemClicked(task) }
    ) {
        Row {
            CardRibbon(color = task.category?.color)
            Checkbox(
                modifier = modifier.fillMaxHeight(),
                checked = task.task.completed,
                onCheckedChange = { onCheckedChanged(task) }
            )
            Spacer(Modifier.preferredWidth(8.dp))
            Column(modifier = modifier.fillMaxHeight(), verticalArrangement = Arrangement.Center) {
                Text(
                    text = task.task.title,
                    style = MaterialTheme.typography.body1,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
                RelativeDateText(calendar = task.task.dueDate)
            }
        }
    }
}

@Composable
private fun CardRibbon(color: Color?, modifier: Modifier = Modifier) {
    val ribbonColor = color ?: MaterialTheme.colors.background

    Spacer(
        modifier
            .preferredWidth(18.dp)
            .fillMaxHeight()
            .padding(end = 8.dp)
            .background(ribbonColor)
    )
}

@Composable
private fun RelativeDateText(calendar: Calendar?) {
    if (calendar == null) {
        return
    }

    val context = AmbientContext.current
    val time = calendar.time.time
    val stringTime = DateUtils.getRelativeDateTimeString(
        context, time, DateUtils.DAY_IN_MILLIS, DateUtils.DAY_IN_MILLIS, 0
    ).toString()

    Text(
        text = stringTime,
        style = MaterialTheme.typography.body2,
        overflow = TextOverflow.Ellipsis,
        maxLines = 1
    )
}

@Suppress("UndocumentedPublicFunction")
@Preview
@Composable
fun AlkaaBottomNavPreview() {
    val task1 = Task(title = "Buy milk", dueDate = null)
    val task2 = Task(title = "Call Mark", dueDate = Calendar.getInstance())
    val task3 = Task(title = "Watch Moonlight", dueDate = Calendar.getInstance())

    val category1 = Category(name = "Books", color = Color.Green)
    val category2 = Category(name = "Reminders", color = Color.Magenta)

    val taskList = listOf(
        TaskWithCategory(task = task1, category = category1),
        TaskWithCategory(task = task2, category = category2),
        TaskWithCategory(task = task3, category = null)
    )

    AlkaaTheme {
        TaskListContent(taskList = taskList, modifier = Modifier, onCheckedChanged = {})
    }
}
