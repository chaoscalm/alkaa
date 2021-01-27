package com.escodro.alkaa.presentation.home

import androidx.compose.animation.animateAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomAppBar
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.savedinstancestate.savedInstanceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.escodro.alkaa.model.HomeSection
import com.escodro.task.presentation.list.TaskListSection
import com.escodro.theme.AlkaaTheme

/**
 * Alkaa Home screen.
 */
@Composable
fun Home() {
    val (currentSection, setCurrentSection) = savedInstanceState { HomeSection.Tasks }
    val navItems = HomeSection.values().toList()
    Scaffold(
        topBar = {
            AlkaaTopBar(currentSection = currentSection)
        },
        bodyContent = {
            AlkaaBodyContent(currentSection)
        },
        bottomBar = {
            AlkaaBottomNav(
                currentSection = currentSection,
                onSectionSelected = setCurrentSection,
                items = navItems
            )
        }
    )
}

@Composable
private fun AlkaaTopBar(currentSection: HomeSection) {
    TopAppBar(backgroundColor = MaterialTheme.colors.background, elevation = 0.dp) {
        Box(modifier = Modifier.fillMaxSize()) {
            Text(
                modifier = Modifier.align(Alignment.Center),
                style = MaterialTheme.typography.h5,
                text = stringResource(currentSection.title)
            )
        }
    }
}

@Composable
private fun AlkaaBodyContent(currentSection: HomeSection) {
    val modifier = Modifier.padding(bottom = 56.dp)
    when (currentSection) {
        HomeSection.Tasks -> TaskListSection(modifier = modifier)
        HomeSection.Search -> { /* TODO create new section */
        }
        HomeSection.Categories -> { /* TODO create new section */
        }
        HomeSection.Settings -> { /* TODO create new section */
        }
    }
}

@Composable
private fun AlkaaBottomNav(
    currentSection: HomeSection,
    onSectionSelected: (HomeSection) -> Unit,
    items: List<HomeSection>
) {
    BottomAppBar(backgroundColor = MaterialTheme.colors.background) {
        items.forEach { section ->
            val selected = section == currentSection
            val colorState: State<Color> = animateAsState(
                if (selected) {
                    MaterialTheme.colors.primary
                } else {
                    MaterialTheme.colors.onSecondary
                }
            )
            AlkaaBottomIcon(
                section = section,
                tint = colorState.value,
                onSectionSelected = onSectionSelected,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun AlkaaBottomIcon(
    section: HomeSection,
    tint: Color,
    onSectionSelected: (HomeSection) -> Unit,
    modifier: Modifier
) {
    val title = stringResource(section.title)
    IconButton(
        onClick = { onSectionSelected(section) },
        modifier = modifier.semantics { contentDescription = title }
    ) {
        Icon(imageVector = section.icon, tint = tint)
    }
}

@Suppress("UndocumentedPublicFunction")
@Preview(showBackground = true)
@Composable
fun AlkaaTopBarPreview() {
    AlkaaTheme {
        AlkaaTopBar(HomeSection.Tasks)
    }
}

@Suppress("UndocumentedPublicFunction")
@Preview(showBackground = true)
@Composable
fun AlkaaBottomNavPreview() {
    AlkaaTheme {
        AlkaaBottomNav(
            currentSection = HomeSection.Tasks,
            onSectionSelected = {},
            items = HomeSection.values().toList()
        )
    }
}
