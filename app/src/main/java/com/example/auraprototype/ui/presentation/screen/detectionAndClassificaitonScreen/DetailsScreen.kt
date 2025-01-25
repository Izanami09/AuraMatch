package com.example.auraprototype.ui.presentation.screen.detectionAndClassificaitonScreen

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.auraprototype.R
import com.google.gson.annotations.SerializedName

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(
    item : GridItem,
    modifier: Modifier = Modifier.fillMaxSize()
){
    val name = item.beardStyle ?: "No name"
    val scrollBehavior : TopAppBarScrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    Scaffold(
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            MediumTopAppBar(
                title = { Text(
                                text = name,
                                fontFamily = FontFamily.Monospace
                            )
                        },
                navigationIcon = {
                    IconButton(onClick = { /*TODO*/ }) {
                        androidx.compose.material3.Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Back Arrow")
                    }
                },
                scrollBehavior = scrollBehavior
            )
        },
        containerColor = Color.Black,
        contentColor = Color.White
    ) {
        LazyColumn(
            modifier = Modifier
                .padding(it)
                .padding(16.dp)
        ) {
            item{
                Card(
                    shape = CardDefaults.elevatedShape,
                    elevation = CardDefaults.cardElevation(4.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .padding(2.dp)

                ) {
                    AsyncImage(
                        model = item.image,
                        contentDescription = "Image",
                        contentScale = ContentScale.Fit,
                        placeholder = painterResource(id = R.drawable.baseline_downloading),
                        modifier = Modifier.fillMaxSize()
                    )
                }

            }
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = name,
                    style = MaterialTheme.typography.titleLarge,
                    fontStyle = FontStyle.Normal,
                    fontFamily = FontFamily.Monospace
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
            item{
                Text(
                    text = item.description ?: "No description found",
                    maxLines = 20,
                    overflow = TextOverflow.Clip,
                    fontFamily = FontFamily.Serif
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DetailScreenPreview(){
    DetailsScreen(
        GridItem()
    )
}

data class GridItem(
@SerializedName("_id") val id: String? = "temp_id",
@SerializedName("faceshape") val faceShape: String? = "Round",
@SerializedName("beardstyle") val beardStyle: String? = "Full Beard",
@SerializedName("image") val image: String? = "https://auramatch.onrender.com/spiky-mens-hairstyle-texture-yire63k7sbxp4si9.png",
@SerializedName("description") val description: String? = "A detailed description of the face. jasdkfjaskfjaljsfljalsjfkajsfljfkaljfajslfalskdfaksjfjalsjfakjsldjfioausfjalsjfaljlajlfjanncjasjflkjasufajfajlfjkaljfjalfjlajlfajlfjalfjkajflajsdlfjaljfalfn,ncafoiuaghjmafjshejaljfadfjalfjlaflakdflaksdfklaskThe sun began to set over the horizon, casting a warm, golden glow across the sky. Birds flew in graceful formations, their songs echoing through the peaceful air. Below, the river flowed gently, its surface reflecting the colors of the fading light. A soft breeze rustled the leaves of the trees, creating a soothing melody. In this quiet moment, there was a sense of calm, as if the world itself was taking a deep breath. Time seemed to slow, and for a brief moment, everything felt perfectly in place, a beautiful balance of nature and serenity.A detailed description of the face. jasdkfjaskfjaljsfljalsjfkajsfljfkaljfajslfalskdfaksjfjalsjfakjsldjfioausfjalsjfaljlajlfjanncjasjflkjasufajfajlfjkaljfjalfjlajlfajlfjalfjkajflajsdlfjaljfalfn,ncafoiuaghjmafjshejaljfadfjalfjlaflakdflaksdfklaskThe sun began to set over the horizon, casting a warm, golden glow across the sky. Birds flew in graceful formations, their songs echoing through the peaceful air. Below, the river flowed gently, its surface reflecting the colors of the fading light. A soft breeze rustled the leaves of the trees, creating a soothing melody. In this quiet moment, there was a sense of calm, as if the world itself was taking a deep breath. Time seemed to slow, and for a brief moment, everything felt perfectly in place, a beautiful balance of nature and serenity.,",
@SerializedName("__v") val version: Int? = 1,
@SerializedName("gender") val gender: String? = "Male"
)


