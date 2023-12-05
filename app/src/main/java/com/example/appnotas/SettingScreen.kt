package com.example.appnotas

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.appnotas.ViewModel.NotasViewModel

data class themeItem(
    val name: String,
    val color: Color,
    val action: () -> Unit
)

data class colorItem(
    val color: Color,
    val action: () -> Unit
)

data class idiomaItem(
    val image: Painter,
    val action: () -> Unit
)

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AjusteScreen(multiViewModel: NotasViewModel) {

    val theme_items = listOf(
        themeItem(
            name = "Claro",
            color = Color.White,
            action = { multiViewModel.changeTheme(false) }
        ),
        themeItem(
            name = "Oscuro",
            color = Color.Black,
            action = { multiViewModel.changeTheme(true) }
        )
    )

    val color_items1 = listOf(
        colorItem(
            color = Color.Blue,
            action = { }
        ),
        colorItem(
            color = Color.Cyan,
            action = { }
        ),
        colorItem(
            color = Color.Green,
            action = { }
        )
    )

    val color_items2 = listOf(
        colorItem(
            color = Color.Magenta,
            action = { }
        ),
        colorItem(
            color = Color.Red,
            action = { }
        ),
        colorItem(
            color = Color.Yellow,
            action = { }
        )
    )

    val color_items3 = listOf(
        colorItem(
            color = Color.Gray,
            action = { }
        ),
        colorItem(
            color = Color.LightGray,
            action = { }
        )
    )

    val idioma_items = listOf(
        idiomaItem(
            image = painterResource(id = R.drawable.ic_launcher_background),
            action = { }
        ),
        idiomaItem(
            image = painterResource(id = R.drawable.ic_launcher_background),
            action = { }
        )
    )


    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
    ) {
        TopBar()

        TemasBody(theme_items)

        Spacer(modifier = Modifier.size(32.dp))

        ColorsBody(color_items1,color_items2,color_items3)

        Spacer(modifier = Modifier.size(32.dp))

        IdiomasBody(idioma_items)

        Spacer(modifier = Modifier.size(32.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar() {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = stringResource(id = R.string.settings_title)
            )
        }
    )
}

@Composable
fun IdiomasBody(idioma_items: List<idiomaItem>) {
    Text(
        text = stringResource(id = R.string.settings_language_title),
        style = MaterialTheme.typography.bodyLarge,
        modifier = Modifier
            .padding(16.dp)
    )

    Row(
        modifier = Modifier
            .padding(horizontal = 16.dp)
    ) {
        idioma_items.forEach(){ item ->
            Card {
                Image(
                    painter = item.image,
                    contentDescription = null,
                    modifier = Modifier
                        .size(100.dp)
                        .padding(8.dp)
                        .clip(MaterialTheme.shapes.medium)
                )
            }
            Spacer(modifier = Modifier.size(16.dp))
        }
    }
}

@Composable
fun ColorsBody(
    color_items1: List<colorItem>,
    color_items2: List<colorItem>,
    color_items3: List<colorItem>
) {
    Text(
        text = stringResource(id = R.string.settings_color_title),
        style = MaterialTheme.typography.bodyLarge,
        modifier = Modifier
            .padding(16.dp)
    )

    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp)
    ) {/*
        Row(
            modifier = Modifier
                .padding(vertical = 8.dp)
        ) {
            color_items1.forEach(){ item ->
                BoxOption(item)
                Spacer(modifier = Modifier.size(16.dp))
            }
        }
        Row(
            modifier = Modifier
                .padding(vertical = 8.dp)
        ) {
            color_items2.forEach(){ item ->
                BoxOption(item)
                Spacer(modifier = Modifier.size(16.dp))
            }
        }
        Row {
            color_items3.forEach(){ item ->
                BoxOption(item)
                Spacer(modifier = Modifier.size(16.dp))
            }
        }*/
    }
}

@Composable
fun TemasBody(
    theme_items: List<themeItem>
) {
    Text(
        text = stringResource(id = R.string.settings_theme_title),
        style = MaterialTheme.typography.bodyLarge,
        modifier = Modifier
            .padding(16.dp)
    )

    Row(
        modifier = Modifier
            .padding(horizontal = 16.dp)
    ) {
        theme_items.forEach(){ item ->
            BoxOption(item = item)
            Spacer(modifier = Modifier.size(16.dp))
        }
    }
}

@Composable
fun BoxOption(
    item: themeItem
) {
    Card(
        Modifier.clickable { item.action() }
    ) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .padding(8.dp)
                .background(item.color, MaterialTheme.shapes.medium)
        )
    }
}