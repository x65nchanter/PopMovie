package com.example.popmovie.ui.collection

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.example.popmovie.ui.theme.Typography
import com.example.popmovie.domain.Movie
import com.example.popmovie.domain.Poster


@ExperimentalAnimationApi
@Composable
fun MoviesList(movies: List<Movie>, listState: LazyListState) {
    LazyColumn(state = listState) {
        items(movies) { movie ->
            MovieItem(movie = movie)
        }
    }
}

@ExperimentalAnimationApi
@Composable
fun MovieItem(movie: Movie) {
    var expanded by remember { mutableStateOf(false) }
    Column(
        Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded },
        ) {
            Column {
                Image(
                    painter = rememberImagePainter(
                        if (movie.poster is Poster.ExternalSized) {
                            (movie.poster as Poster.ExternalSized).url.toString()
                        } else {
                            null
                        }
                    ),
                    contentDescription = null,
                    modifier = Modifier
                        .height(200.dp)
                        .fillMaxWidth(),
                    contentScale = ContentScale.FillWidth
                )

                AnimatedVisibility(expanded) {
                    Divider()
                    Text(
                        modifier = Modifier.padding(16.dp),
                        text = movie.overview
                    )
                }
            }
        }
        Column(Modifier.padding(top = 16.dp)) {
            Text(text = movie.title, fontWeight = FontWeight.W900)
            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                Text(
                    modifier = Modifier.padding(top = 8.dp),
                    text = movie.genres.joinToString(", ") { it.name },
                    style = Typography.overline
                )
            }
        }
    }
}