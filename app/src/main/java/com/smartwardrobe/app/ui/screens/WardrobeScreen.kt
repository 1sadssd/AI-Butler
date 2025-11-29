package com.smartwardrobe.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.smartwardrobe.app.data.ClothingItem
import com.smartwardrobe.app.data.WardrobeRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WardrobeScreen() {
    val context = LocalContext.current
    var expandedType by remember { mutableStateOf(false) }
    var selectedType by remember { mutableStateOf("") }

    var expandedColor by remember { mutableStateOf(false) }
    var selectedColor by remember { mutableStateOf("") }

    // Extended color map
    val colorMap = mapOf(
        "Red" to Color.Red,
        "Blue" to Color.Blue,
        "Green" to Color.Green,
        "Black" to Color.Black,
        "White" to Color.White,
        "Yellow" to Color.Yellow,
        "Cyan" to Color.Cyan,
        "Magenta" to Color.Magenta,
        "Gray" to Color.Gray,
        "Orange" to Color(0xFFFFA500)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // ----- Clothing type dropdown -----
        ExposedDropdownMenuBox(
            expanded = expandedType,
            onExpandedChange = { expandedType = !expandedType },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = selectedType,
                onValueChange = {},
                label = { Text("Clothing Type") },
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedType) },
                modifier = Modifier.menuAnchor()
            )
            DropdownMenu(
                expanded = expandedType,
                onDismissRequest = { expandedType = false },
                modifier = Modifier.fillMaxWidth()
            ) {
                WardrobeRepository.clothingTypes.forEach { type ->
                    DropdownMenuItem(
                        text = { Text(type) },
                        onClick = {
                            selectedType = type
                            expandedType = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // ----- Color picker dropdown -----
        ExposedDropdownMenuBox(
            expanded = expandedColor,
            onExpandedChange = { expandedColor = !expandedColor },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = selectedColor,
                onValueChange = {},
                label = { Text("Color") },
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedColor) },
                modifier = Modifier.menuAnchor()
            )
            DropdownMenu(
                expanded = expandedColor,
                onDismissRequest = { expandedColor = false },
                modifier = Modifier.fillMaxWidth()
            ) {
                colorMap.forEach { (name, color) ->
                    DropdownMenuItem(
                        text = { Text(name) },
                        onClick = {
                            selectedColor = name
                            expandedColor = false
                        },
                        leadingIcon = {
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .background(color)
                            )
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // ----- Add clothing button -----
        Button(
            onClick = {
                if (selectedType.isNotBlank() && selectedColor.isNotBlank()) {
                    val newItem = ClothingItem(type = selectedType, color = selectedColor)
                    WardrobeRepository.addItem(newItem, context)
                    selectedType = ""
                    selectedColor = ""
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add Clothing Item")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ----- Clothing list -----
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(WardrobeRepository.items) { item ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(item.type) // название одежды слева

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(20.dp)
                                    .clip(CircleShape)
                                    .background(colorMap[item.color] ?: Color.Gray)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Button(onClick = { WardrobeRepository.removeItem(item, context) }) {
                                Text("Delete")
                            }
                        }
                    }
                }
            }
        }

    }
}
