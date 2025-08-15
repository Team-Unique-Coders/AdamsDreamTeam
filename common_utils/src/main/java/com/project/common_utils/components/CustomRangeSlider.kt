import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.graphics.Color

@Composable
fun ThreeValueSlider(
    modifier: Modifier = Modifier,
    valueRange: ClosedFloatingPointRange<Float> = 0f..100f,
    startValue: Float = 0f,
    midValue: Float = 50f,
    endValue: Float = 100f,
    onValueChange: (Float) -> Unit = {}
) {
    // Slider values will snap to start, mid, or end

    var sliderPosition by remember { mutableStateOf(startValue) }

    Column(modifier = modifier.padding(16.dp)) {
        Slider(
            value = sliderPosition,
            onValueChange = onValueChange,
            colors = SliderDefaults.colors(
                thumbColor = MaterialTheme.colorScheme.secondary,
                activeTrackColor = MaterialTheme.colorScheme.secondary,
                inactiveTrackColor = MaterialTheme.colorScheme.secondaryContainer,
            ),
            steps = 3,
            valueRange = valueRange
        )

        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Start: ${startValue.toInt()}")
            Text("Mid: ${midValue.toInt()}")
            Text("End: ${endValue.toInt()}")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ThreeValueSliderPreview() {
    ThreeValueSlider()
}
