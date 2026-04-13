package com.neomfi.microlend.presentation.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.CloudSync
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.work.WorkInfo

@Composable
fun SyncStatusCard(
    syncState: WorkInfo.State?,
    onSyncClick: () -> Unit
) {
    val (containerColor, contentColor, icon, title, message) = when (syncState) {
        WorkInfo.State.RUNNING, WorkInfo.State.ENQUEUED -> listOf(
            MaterialTheme.colorScheme.primaryContainer,
            MaterialTheme.colorScheme.onPrimaryContainer,
            Icons.Default.CloudSync,
            "Syncing to Cloud...",
            "Please wait while we upload your data."
        )
        WorkInfo.State.SUCCEEDED -> listOf(
            MaterialTheme.colorScheme.tertiaryContainer,
            MaterialTheme.colorScheme.onTertiaryContainer,
            Icons.Default.CheckCircle,
            "All Data Synced",
            "Your leads and groups are safely in the cloud."
        )
        WorkInfo.State.FAILED -> listOf(
            MaterialTheme.colorScheme.errorContainer,
            MaterialTheme.colorScheme.onErrorContainer,
            Icons.Default.Warning,
            "Sync Failed",
            "Network error. Tap to try again."
        )
        else -> listOf(
            MaterialTheme.colorScheme.surfaceVariant,
            MaterialTheme.colorScheme.onSurfaceVariant,
            Icons.Default.CloudOff,
            "Offline Data Pending",
            "Tap to securely backup your local data."
        )
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = containerColor as Color,
            contentColor = contentColor as Color
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                if (syncState == WorkInfo.State.RUNNING) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(28.dp),
                        color = contentColor,
                        strokeWidth = 3.dp
                    )
                } else {
                    Icon(
                        imageVector = icon as androidx.compose.ui.graphics.vector.ImageVector,
                        contentDescription = "Sync Status",
                        modifier = Modifier.size(28.dp)
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column {
                    Text(text = title as String, style = MaterialTheme.typography.titleMedium)
                    Text(text = message as String, style = MaterialTheme.typography.bodySmall)
                }
            }

            if (syncState != WorkInfo.State.RUNNING && syncState != WorkInfo.State.ENQUEUED) {
                Button(
                    onClick = onSyncClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = contentColor,
                        contentColor = containerColor
                    )
                ) {
                    Text("Sync Now")
                }
            }
        }
    }
}