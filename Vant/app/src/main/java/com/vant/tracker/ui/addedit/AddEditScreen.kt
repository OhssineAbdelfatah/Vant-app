package com.vant.tracker.ui.addedit

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.vant.tracker.domain.model.MediaType
import com.vant.tracker.domain.model.Status
import com.vant.tracker.domain.model.displayName

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditScreen(
    viewModel: AddEditViewModel,
    itemId: Long?,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    LaunchedEffect(itemId) {
        if (itemId != null && itemId != 0L) viewModel.loadItem(itemId)
    }

    val state by viewModel.state.collectAsState()

    LaunchedEffect(state.isSaved) {
        if (state.isSaved) onNavigateBack()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (state.id == 0L) "Add Media" else "Edit Media") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = viewModel::save) {
                        Icon(Icons.Default.Save, contentDescription = "Save")
                    }
                },
            )
        },
        modifier = modifier,
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            OutlinedTextField(
                value = state.title,
                onValueChange = viewModel::setTitle,
                label = { Text("Title *") },
                isError = state.titleError != null,
                supportingText = { state.titleError?.let { Text(it) } },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
            )

            Text("Type", style = MaterialTheme.typography.labelLarge)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                MediaType.entries.forEach { type ->
                    FilterChip(
                        selected = state.type == type,
                        onClick = { viewModel.setType(type) },
                        label = { Text(type.name) },
                    )
                }
            }

            OutlinedTextField(
                value = state.year,
                onValueChange = viewModel::setYear,
                label = { Text("Year") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
            )

            Text("Status", style = MaterialTheme.typography.labelLarge)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Status.entries.forEach { s ->
                    FilterChip(
                        selected = state.status == s,
                        onClick = { viewModel.setStatus(s) },
                        label = { Text(s.displayName()) },
                    )
                }
            }

            OutlinedTextField(
                value = state.personalRating,
                onValueChange = viewModel::setRating,
                label = { Text("Rating (1–10)") },
                isError = state.ratingError != null,
                supportingText = { state.ratingError?.let { Text(it) } },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
            )

            OutlinedTextField(
                value = state.notes,
                onValueChange = viewModel::setNotes,
                label = { Text("Notes") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
            )

            if (state.type == MediaType.TV) {
                HorizontalDivider()
                Text("TV Series Details", style = MaterialTheme.typography.titleSmall)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = state.seasons,
                        onValueChange = viewModel::setSeasons,
                        label = { Text("Total Seasons") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                    )
                    OutlinedTextField(
                        value = state.episodesTotal,
                        onValueChange = viewModel::setEpisodesTotal,
                        label = { Text("Total Episodes") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                    )
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = state.lastWatchedSeason,
                        onValueChange = viewModel::setLastWatchedSeason,
                        label = { Text("Last Season") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                    )
                    OutlinedTextField(
                        value = state.lastWatchedEpisode,
                        onValueChange = viewModel::setLastWatchedEpisode,
                        label = { Text("Last Episode") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                    )
                }
            }

            Button(
                onClick = viewModel::save,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(if (state.id == 0L) "Add to Library" else "Save Changes")
            }
        }
    }
}
