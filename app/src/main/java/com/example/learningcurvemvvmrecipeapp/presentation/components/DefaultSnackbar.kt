package com.example.learningcurvemvvmrecipeapp.presentation.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@ExperimentalMaterialApi
@Composable
fun DefaultSnackbar(
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit
) {
    SnackbarHost(
        modifier = modifier,
        hostState = snackbarHostState,
        snackbar = { snackbarData ->
            Snackbar(
                modifier = Modifier.padding(16.dp),
                text = {
                    Text(
                        text = snackbarData.message,
                        style = MaterialTheme.typography.body2,
                        color = Color.White,
                    )
                },
                action = {
                    snackbarData.actionLabel?.let { actonLabel ->
                        TextButton(
                            onClick = { onDismiss() }
                        ) {
                            Text(
                                text = actonLabel,
                                style = MaterialTheme.typography.body2,
                                color = Color.White,
                            )
                        }
                    }
                }
            )
        }
    )
}