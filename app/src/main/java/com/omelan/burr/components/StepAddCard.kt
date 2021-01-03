package com.omelan.burr.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.omelan.burr.model.Step
import com.omelan.burr.model.StepType
import com.omelan.burr.ui.BurrTheme
import com.omelan.burr.ui.full
import com.omelan.burr.ui.shapes
import kotlin.random.Random

@ExperimentalLayout
@Composable
fun StepAddCard(save: (Step) -> Unit) {
    val (pickedType, setPickedType) = remember { mutableStateOf<StepType?>(null) }
    val (stepName, setStepName) = remember { mutableStateOf<String>("") }
    val (stepTime, setStepTime) = remember { mutableStateOf<Int>(0) }
    val (stepValue, setStepValue) = remember { mutableStateOf<Int>(0) }
    BurrTheme {
        Card(
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(15.dp).animateContentSize()) {
                Box {
                    FlowRow(mainAxisSpacing = 5.dp, crossAxisSpacing = 5.dp) {
                        StepType.values().forEach { stepType ->
                            Button(onClick = { setPickedType(stepType) }, shape = shapes.full) {
                                Text(
                                    text = if (pickedType == stepType) {
                                        "✓ ${stepType.name}"
                                    } else {
                                        stepType.name
                                    },
                                    modifier = Modifier.animateContentSize(),
                                )
                            }
                        }
                    }
                }
                if (pickedType != null) {
                    OutlinedTextField(
                        label = { Text(text = "Name") },
                        value = stepName,
                        onValueChange = { setStepName(it) })
                    OutlinedTextField(
                        label = { Text(text = "Time") },
                        value = stepTime.toString(),
                        onValueChange = { setStepTime(it.toMillisValue()) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                    if (listOf<StepType>(
                            StepType.WATER,
                            StepType.ADD_COFFEE,
                            StepType.OTHER
                        ).contains(pickedType)
                    ) {
                        OutlinedTextField(
                            label = { Text(text = "Value") },
                            value = stepValue.toString(),
                            onValueChange = { setStepValue(it.safeToInt()) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )
                    }
                    Button(
                        onClick = {
                            save(
                                Step(
                                    id = Random.nextInt(),
                                    name = stepName,
                                    time = stepTime,
                                    type = pickedType,
                                    value = if (stepValue != 0) stepValue else null
                                )
                            )
                        },
                        modifier = Modifier.padding(vertical = 15.dp)
                    ) {
                        Text(text = "Save and add next")
                    }
                }
            }
        }
    }
}

private fun String.toMillisValue(): Int {
    return this.safeToInt() * 1000
}

private fun String.safeToInt(): Int {
    return when {
        this.isEmpty() -> 0
        else -> this.toInt()
    }
}

@ExperimentalLayout
@Composable
@Preview
fun StepAddCardPreview() {
    StepAddCard(save = {})
}