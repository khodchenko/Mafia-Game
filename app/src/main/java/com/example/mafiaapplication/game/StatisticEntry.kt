package com.example.mafiaapplication.game

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class StatisticEntry(
    val timestamp: Long,
    val text: String,
    val type: Type = Type.SIMPLE
)

enum class Type {
    MAIN,
    SIMPLE
}

class StatisticManager(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("StatisticPrefs", Context.MODE_PRIVATE)

    fun addEntry(text: String, type: Type) {
        val timestamp = System.currentTimeMillis()
        val entry = "$timestamp,$text,${type.name}"
        val currentEntries = prefs.getStringSet("entries", mutableSetOf()) ?: mutableSetOf()
        currentEntries.add(entry)
        prefs.edit().putStringSet("entries", currentEntries).apply()
    }

    fun getAllEntries(): List<StatisticEntry> {
        val entries = prefs.getStringSet("entries", mutableSetOf()) ?: return emptyList()
        return entries.mapNotNull {
            val parts = it.split(",")
            if (parts.size < 3) return@mapNotNull null

            val timestamp = parts[0].toLongOrNull() ?: return@mapNotNull null
            val text = parts[1]
            val type = runCatching { Type.valueOf(parts[2]) }.getOrElse { Type.SIMPLE }

            StatisticEntry(timestamp, text, type)
        }.sortedBy { it.timestamp }
    }
}

@Composable
fun StatisticRow(entry: StatisticEntry) {
    Row(
        Modifier
            .fillMaxWidth()
            .wrapContentSize(Alignment.CenterStart)
    ) {
        Text(
            text = "[${formatTimestamp(entry.timestamp)}] ${entry.text}",
            fontSize = if (entry.type == Type.MAIN) 20.sp else 16.sp,
            fontWeight = if (entry.type == Type.MAIN) FontWeight.Bold else FontWeight.Normal,
            color = Color.White,
            textAlign = if (entry.type == Type.MAIN) TextAlign.Center else TextAlign.Start
        )
    }
}

fun formatTimestamp(timestamp: Long): String {
    val date = Date(timestamp)
    val formatter = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
    return formatter.format(date)
}

@Preview
@Composable
fun PreviewStatisticRow() {
    val sampleEntry = StatisticEntry(System.currentTimeMillis(), "SIMPLE TEXT", Type.MAIN)
    StatisticRow(entry = sampleEntry)
}