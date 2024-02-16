package com.example.week5

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import com.example.week5.ui.theme.Week5Theme
import androidx.compose.ui.Alignment
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.TextFieldDefaults

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Week5Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CalorieScreen()
                }
            }
        }
    }
}

@Composable
fun Calculation(male:Boolean,weight:Int,intensity:Float,setResult:(Int)-> Unit) {
    // Calculate calories based on inputs and set the result
    Button(
        onClick = {
            if(male){
                setResult(((879+10.2*weight)*intensity).toInt())
            }
            else
            {
                setResult(((795+7.48*weight)*intensity).toInt())
            }
        },
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text="CALCULATE")
    }
}

@Composable
fun CalorieScreen() {
    // Define mutable variables to store user inputs and calculated results
    var weightInput by remember { mutableStateOf("") }
    var weight = weightInput.toIntOrNull() ?:0
    var male by remember { mutableStateOf(true) }
    var intensity by remember { mutableStateOf(1.3f) }
    var result by remember { mutableStateOf(0) }

    Column(
        modifier = Modifier.padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ){
        // Display heading
        Heading(title = stringResource(R.string.calories))
        // Input field for weight
        WeightField(weightInput = weightInput, onValueChange = {weightInput = it})
        // Radio buttons for gender selection
        GenderChoices(male, setGenderMale = { male = it})
        // Dropdown menu for intensity selection
        IntensityList(onClick ={intensity = it})
        // Display calculated result
        Text(
            text = result.toString(),
            color = if (male) Color.Blue else Color.Red,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(vertical = 8.dp))
        // Button to calculate calories
        Calculation(male=male,weight=weight,intensity=intensity,setResult={result = it})

    }
}

@Composable
fun Heading(title:String) {
    // Display heading text
    Text(
        text = title,
        fontSize = 24.sp,
        textAlign = TextAlign.Center,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, bottom = 16.dp)
    )
}

@Composable
fun WeightField(weightInput:String, onValueChange:(String) -> Unit) {
    // Input field for weight
    OutlinedTextField(
        value = weightInput,
        onValueChange = onValueChange,
        label = { Text(text = "Enter weight") },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        modifier = Modifier
            .fillMaxWidth()
    )
}

@Composable
fun GenderChoices(male:Boolean,setGenderMale:(Boolean) -> Unit) {
    // Radio buttons for gender selection
    Column(modifier = Modifier.selectableGroup()) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            RadioButton(
                selected = male,
                onClick = { setGenderMale(true) },
                colors = RadioButtonDefaults.colors(selectedColor = Color.Blue)
            )
            Text(text = "Male")
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            RadioButton(
                selected = !male,
                onClick = { setGenderMale(false) },
                colors = RadioButtonDefaults.colors(selectedColor = Color.Red)
            )
            Text(text = "Female")
        }
    }
}

@Composable
fun IntensityList(onClick: (Float) -> Unit) {
    // Dropdown menu for intensity selection
    var expanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf("Light") }
    var textFieldSize by remember { mutableStateOf(Size.Zero) }
    val items = listOf("Light", "Usual", "Moderate", "Hard", "Very hard")
    val icon = if (expanded)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown

    Column {
        OutlinedTextField(
            readOnly = true,
            value = selectedText,
            onValueChange = { selectedText = it },
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { coordinates ->
                    textFieldSize = coordinates.size.toSize()
                },
            label = { Text("Select intensity") },
            trailingIcon = {
                Icon(
                    icon,
                    "contentDescription",
                    Modifier.clickable { expanded = !expanded }
                )
            },
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.width(with(LocalDensity.current) { textFieldSize.width.toDp() })
        ) {
            items.forEach { label ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = label,
                            color = Color.DarkGray // Change the color here
                        )
                    },
                    onClick = {
                        selectedText = label
                        val intensity: Float = when (label) {
                            "Light" -> 1.3f
                            "Usual" -> 1.5f
                            "Moderate" -> 1.7f
                            "Hard" -> 2f
                            "Very hard" -> 2.2f
                            else -> 0.0f
                        }
                        onClick(intensity)
                        expanded = false
                    }
                )
            }
        }
        Text(
            text = "Your calories",
            color = Color(0xFF512DA8),
            fontSize = 16.sp,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}
