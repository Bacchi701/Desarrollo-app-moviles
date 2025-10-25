@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
package com.example.confeccionesbrenda.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.VisualTransformation

/**
 * TextField reutilizable con mensaje de error.
 * Recibe un Modifier para poder usar fillMaxWidth() desde la pantalla.
 */
@Composable
fun ValidatedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    leadingIcon: @Composable (() -> Unit)? = null,
    errorText: String? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None
) {
    Column {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            leadingIcon = leadingIcon,
            visualTransformation = visualTransformation,
            isError = !errorText.isNullOrEmpty(),
            singleLine = true,
            modifier = modifier,
            colors = TextFieldDefaults.colors()
        )
        // por si existe un error.
        if (!errorText.isNullOrEmpty()) {
            Text(text = errorText, color = Color.Red)
        }
    }
}

