package com.vant.tracker.ui.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.vant.tracker.data.local.MediaItemEntity
import com.vant.tracker.domain.model.MediaType
import com.vant.tracker.domain.model.Status
import com.vant.tracker.domain.model.displayName

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onItemClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    val state by viewModel.uiState.collectAsState()
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Movies", "Series")

    Column(modifier = modifier.fillMaxSize()) {
        TabRow(selectedTabIndex = selectedTab) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = { Text(title) },
                )
            }
        }

        val statusCounts = if (selectedTab == 0) state.movieStatusCounts else state.tvStatusCounts
        val watchingItems = if (selectedTab == 0) state.watchingMovies else state.watchingTV

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            item {
                Text(
                    text = "Summary",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp),
                )
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(statusCounts) { sc ->
                        StatusSummaryCard(sc)
                    }
                }
            }

            item {
                Text(
                    text = "Continue Watching",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp),
                )
                AnimatedVisibility(
                    visible = watchingItems.isEmpty(),
                    enter = fadeIn(), exit = fadeOut(),
                ) {
                    Text(
                        text = "Nothing in progress. Add something to watch!",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
            items(watchingItems, key = { it.id }) { item ->
                WatchingCard(item = item, onClick = { onItemClick(item.id) })
            }
        }
    }
}

@Composable
private fun StatusSummaryCard(sc: StatusCount) {
    val color = when (sc.status) {
        Status.PLANNED   -> MaterialTheme.colorScheme.primaryContainer
        Status.WATCHING  -> MaterialTheme.colorScheme.secondaryContainer
        Status.COMPLETED -> MaterialTheme.colorScheme.tertiaryContainer
        Status.DROPPED   -> MaterialTheme.colorScheme.errorContainer
    }
    val onColor = when (sc.status) {
        Status.PLANNED   -> MaterialTheme.colorScheme.onPrimaryContainer
        Status.WATCHING  -> MaterialTheme.colorScheme.onSecondaryContainer
        Status.COMPLETED -> MaterialTheme.colorScheme.onTertiaryContainer
        Status.DROPPED   -> MaterialTheme.colorScheme.onErrorContainer
    }
    Card(
        colors = CardDefaults.cardColors(containerColor = color),
        modifier = Modifier.width(100.dp),
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = sc.count.toString(),
                style = MaterialTheme.typography.headlineSmall,
                color = onColor,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = sc.status.displayName(),
                style = MaterialTheme.typography.labelSmall,
                color = onColor,
            )
        }
    }
}

@Composable
private fun WatchingCard(item: MediaItemEntity, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                )
                if (item.year != null) {
                    Text(
                        text = item.year.toString(),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                if (item.type == MediaType.TV && item.lastWatchedSeason != null) {
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = "S${item.lastWatchedSeason} E${item.lastWatchedEpisode ?: 0}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary,
                    )
                }
            }
            item.personalRating?.let { rating ->
                Surface(
                    shape = MaterialTheme.shapes.small,
                    color = MaterialTheme.colorScheme.primaryContainer,
                ) {
                    Text(
                        text = "★ $rating",
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                    )
                }
            }
        }
    }
}
