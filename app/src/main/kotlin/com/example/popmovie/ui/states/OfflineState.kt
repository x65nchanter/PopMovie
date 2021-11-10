package com.example.popmovie.ui.states

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LinkOff
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.popmovie.R


@Composable
fun OfflineState(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.fillMaxHeight(0.4f))
        Icon(
            Icons.Filled.LinkOff,
            contentDescription = stringResource(id = R.string.is_offline)
        )
        Text(
            text = stringResource(id = R.string.is_offline),
            maxLines = 1,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.h6
        )
    }
}
