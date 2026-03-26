package com.vant.tracker.ui.library

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.SwipeToDismissBoxValue.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.vant.tracker.data.local.MediaItemEntity
import com.vant.tracker.domain.model.MediaType
import com.vant.tracker.domain.model.Status
import com.vant.tracker.domain.model.displayName

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibraryScreen(
    viewModel: LibraryViewModel,
    onItemClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    val state by viewModel.uiState.collectAsState()
    var showFilters by remember { mutableStateOf(false) }

    Column(modifier = modifier.fillMaxSize()) {
        SearchBar(
            query = state.filter.query,
            onQueryChange = viewModel::setQuery,
            onFilterClick = { showFilters = !showFilters },
        )

        if (showFilters) {
            FilterChipsRow(
                filter = state.filter,
                onTypeChange = viewModel::setTypeFilter,
                onStatusChange = viewModel::setStatusFilter,
                onSortChange = viewModel::setSortBy,
            )
        }

        if (state.isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (state.items.isEmpty()) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        Icons.Default.MovieFilter,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Spacer(Modifier.height(16.dp))
                    Text("No items found", style = MaterialTheme.typography.titleMedium)
                    Text(
                        "Add something from the + tab",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(state.items, key = { it.id }) { item ->
                    SwipeableMediaCard(
                        item = item,
                        onClick = { onItemClick(item.id) },
                        onDelete = { viewModel.deleteItem(item) },
                        onComplete = { viewModel.markCompleted(item) },
                    )
                }
            }
        }
    }
}

@Composable
private fun SearchBar(query: String, onQueryChange: (String) -> Unit, onFilterClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        OutlinedTextField(
            value = query,
            onValueChange = onQueryChange,
            modifier = Modifier.weight(1f),
            placeholder = { Text("Search library…") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            trailingIcon = {
                if (query.isNotEmpty()) {
                    IconButton(onClick = { onQueryChange("") }) {
                        Icon(Icons.Default.Clear, contentDescription = "Clear")
                    }
                }
            },
            singleLine = true,
        )
        Spacer(Modifier.width(8.dp))
        IconButton(onClick = onFilterClick) {
            Icon(Icons.Default.FilterList, contentDescription = "Filter")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FilterChipsRow(
    filter: LibraryFilter,
    onTypeChange: (MediaType?) -> Unit,
    onStatusChange: (Status?) -> Unit,
    onSortChange: (String) -> Unit,
) {
    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FilterChip(
                selected = filter.type == null,
                onClick = { onTypeChange(null) },
                label = { Text("All") },
            )
            FilterChip(
                selected = filter.type == MediaType.MOVIE,
                onClick = { onTypeChange(MediaType.MOVIE) },
                label = { Text("Movies") },
            )
            FilterChip(
                selected = filter.type == MediaType.TV,
                onClick = { onTypeChange(MediaType.TV) },
                label = { Text("TV") },
            )
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(top = 4.dp),
        ) {
            FilterChip(
                selected = filter.status == null,
                onClick = { onStatusChange(null) },
                label = { Text("Any Status") },
            )
            Status.entries.forEach { s ->
                FilterChip(
                    selected = filter.status == s,
                    onClick = { onStatusChange(s) },
                    label = { Text(s.name.lowercase().replaceFirstChar { it.uppercase() }) },
                )
            }
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(top = 4.dp),
        ) {
            listOf("UPDATED" to "Recent", "TITLE" to "Title", "RATING" to "Rating").forEach { (key, label) ->
                FilterChip(
                    selected = filter.sortBy == key,
                    onClick = { onSortChange(key) },
                    label = { Text(label) },
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SwipeableMediaCard(
    item: MediaItemEntity,
    onClick: () -> Unit,
    onDelete: () -> Unit,
    onComplete: () -> Unit,
) {
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { value ->
            when (value) {
                StartToEnd -> { onComplete(); true }
                EndToStart -> { onDelete(); true }
                Settled    -> false
            }
        },
        positionalThreshold = { it * 0.4f },
    )

    SwipeToDismissBox(
        state = dismissState,
        backgroundContent = {
            val dir = dismissState.dismissDirection
            val color by animateColorAsState(
                when (dismissState.targetValue) {
                    StartToEnd -> MaterialTheme.colorScheme.secondaryContainer
                    EndToStart -> MaterialTheme.colorScheme.errorContainer
                    Settled    -> Color.Transparent
                },
                label = "swipe_color",
            )
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color)
                    .padding(horizontal = 20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = if (dir == EndToStart) Arrangement.End else Arrangement.Start,
            ) {
                if (dir == StartToEnd) {
                    Icon(Icons.Default.CheckCircle, contentDescription = "Complete", tint = MaterialTheme.colorScheme.onSecondaryContainer)
                    Spacer(Modifier.width(8.dp))
                    Text("Complete", color = MaterialTheme.colorScheme.onSecondaryContainer)
                } else if (dir == EndToStart) {
                    Text("Delete", color = MaterialTheme.colorScheme.onErrorContainer)
                    Spacer(Modifier.width(8.dp))
                    Icon(Icons.Default.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.onErrorContainer)
                }
            }
        },
    ) {
        MediaCard(item = item, onClick = onClick)
    }
}

@Composable
fun MediaCard(item: MediaItemEntity, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.Top,
        ) {
            Surface(
                shape = MaterialTheme.shapes.extraSmall,
                color = if (item.type == MediaType.MOVIE)
                    MaterialTheme.colorScheme.primaryContainer
                else
                    MaterialTheme.colorScheme.secondaryContainer,
                modifier = Modifier.padding(end = 12.dp),
            ) {
                Text(
                    text = if (item.type == MediaType.MOVIE) "M" else "TV",
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp),
                    style = MaterialTheme.typography.labelSmall,
                    color = if (item.type == MediaType.MOVIE)
                        MaterialTheme.colorScheme.onPrimaryContainer
                    else
                        MaterialTheme.colorScheme.onSecondaryContainer,
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    if (item.year != null) {
                        Text(
                            text = item.year.toString(),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                    StatusChip(item.status)
                }
                if (item.type == MediaType.TV && item.lastWatchedSeason != null && item.lastWatchedEpisode != null) {
                    Text(
                        text = "S${item.lastWatchedSeason}E${item.lastWatchedEpisode}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary,
                    )
                }
            }
            item.personalRating?.let { r ->
                Text(
                    text = "★$r",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.tertiary,
                )
            }
        }
    }
}

@Composable
fun StatusChip(status: Status, modifier: Modifier = Modifier) {
    val (containerColor, contentColor) = when (status) {
        Status.PLANNED   -> MaterialTheme.colorScheme.primaryContainer to MaterialTheme.colorScheme.onPrimaryContainer
        Status.WATCHING  -> MaterialTheme.colorScheme.secondaryContainer to MaterialTheme.colorScheme.onSecondaryContainer
        Status.COMPLETED -> MaterialTheme.colorScheme.tertiaryContainer to MaterialTheme.colorScheme.onTertiaryContainer
        Status.DROPPED   -> MaterialTheme.colorScheme.errorContainer to MaterialTheme.colorScheme.onErrorContainer
    }
    Surface(
        shape = MaterialTheme.shapes.extraSmall,
        color = containerColor,
        modifier = modifier,
    ) {
        Text(
            text = status.name.lowercase().replaceFirstChar { it.uppercase() },
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
            style = MaterialTheme.typography.labelSmall,
            color = contentColor,
        )
    }
}
