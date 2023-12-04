package com.example.appnotas

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

@Composable
fun MultiDetailsScreen(title: String, date: String, desc: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth(0.4f)
            .padding(PaddingValues(16.dp))
    ) {
        LazyColumn {
            item {
                DetailsScreenHeader(title,date)
                DetailsScreenBody(desc)
                DetailsScreenButtons()
            }
        }
    }
}

@Composable
fun DetailsScreenButtons() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(PaddingValues(8.dp)),
        horizontalArrangement = Arrangement.Center,
    ) {
        Button(
            onClick = { /*TODO*/ }
        ) {
            Text(
                text = stringResource(id = R.string.edit_button)
            )
        }

        Spacer(modifier = Modifier.size(8.dp))

        Button(
            onClick = { /*TODO*/ },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error,
                contentColor = MaterialTheme.colorScheme.onError
            )
        ) {
            Text(
                text = stringResource(id = R.string.delete_button)
            )
        }
    }
}

@Composable
fun DetailsScreenBody(desc: String) {
    Text(
        text = desc,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.outline,
        modifier = Modifier
            .padding(vertical = 8.dp, horizontal = 16.dp)
    )
}

@Composable
private fun DetailsScreenHeader(title: String, date: String) {
    Row(
        modifier = Modifier
            .padding(PaddingValues(16.dp))
    ) {
        Icon(
            imageVector = Icons.Filled.Edit,
            contentDescription = null,
            modifier = Modifier.size(
                24.dp
            )
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(
                    horizontal = 8.dp
                ),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium
            )
            Text(
                text = date,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.outline
            )
        }
    }
}