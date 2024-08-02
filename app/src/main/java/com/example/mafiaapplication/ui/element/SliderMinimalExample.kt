package com.example.mafiaapplication.ui.element

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun SliderMinimalExample(
    onSliderValueChanged: (Float) -> Unit,
    currentValue: Float,
    steps: Int,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f
) {
    Column {
        Slider(
            value = currentValue,
            onValueChange = {
                onSliderValueChanged(it)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            colors = SliderDefaults.colors(
                thumbColor = Color.White,
                activeTrackColor = Color.White,
                inactiveTrackColor = Color.Gray
            ),
            steps = steps - 1,
            valueRange = valueRange
        )
    }
}