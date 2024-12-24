package com.arash.altafi.chatandroid.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.arash.altafi.chatandroid.ui.theme.CustomFont
import com.arash.altafi.chatandroid.viewmodel.ProfileViewModel

@Composable
fun ProfileScreen(navController: NavController) {
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    val profileViewModel: ProfileViewModel = hiltViewModel()
    val liveEditProfile by profileViewModel.liveEditProfile.collectAsState()
    val liveProfile by profileViewModel.liveProfile.observeAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp, 10.dp)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = liveProfile?.avatar,
            contentDescription = liveProfile?.name,
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .shadow(8.dp)
                .border(
                    1.dp,
                    Color.White,
                    CircleShape
                ),
            contentScale = ContentScale.Crop,
        )

        Row(
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Text(
                modifier = Modifier
                    .width(LocalConfiguration.current.screenWidthDp.dp * 0.5f),
                text = liveProfile?.name + " " + liveProfile?.family,
                fontFamily = CustomFont,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center,
                fontSize = 20.sp,
                color = Color.White
            )

            Text(
                modifier = Modifier
                    .width(LocalConfiguration.current.screenWidthDp.dp * 0.5f),
                text = liveProfile?.phone ?: "",
                fontFamily = CustomFont,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
                color = Color.White
            )
        }

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp),
            fontStyle = FontStyle.Normal,
            textAlign = TextAlign.Justify,
//            text = liveProfile?.bio ?: "",
            text = "لورم ایپسوم متن ساختگی با تولید سادگی نامفهوم از صنعت چاپ، و با استفاده از طراحان گرافیک است، چاپگرها و متون بلکه روزنامه و مجله در ستون و سطرآنچنان که لازم است، و برای شرایط فعلی تکنولوژی مورد نیاز، و کاربردهای متنوع با هدف بهبود ابزارهای کاربردی می باشد، کتابهای زیادی در شصت و سه درصد گذشته حال و آینده، شناخت فراوان جامعه و متخصصان را می طلبد، تا با نرم افزارها شناخت بیشتری را برای طراحان رایانه ای علی الخصوص طراحان خلاقی، و فرهنگ پیشرو در زبان فارسی ایجاد کرد، در این صورت می توان امید داشت که تمام و دشواری موجود در ارائه راهکارها، و شرایط سخت تایپ به پایان رسد و زمان مورد نیاز شامل حروفچینی دستاوردهای اصلی، و جوابگوی سوالات پیوسته اهل دنیای موجود طراحی اساسا مورد استفاده قرار گیرد.",
            fontFamily = CustomFont,
            fontSize = 12.sp,
            color = Color.White
        )
    }
}