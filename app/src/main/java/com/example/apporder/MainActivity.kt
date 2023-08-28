package com.example.apporder

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.apporder.extension.getDateTime
import com.example.apporder.room.model.Order
import com.example.apporder.ui.theme.AppOrderTheme
import com.example.apporder.ui.theme.color4
import com.example.apporder.ui.theme.color5
import com.example.apporder.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity() : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainUi()
        }
    }

    val imageStage = R.drawable.stage
    val imageTable = R.drawable.ic_chair

    val sizeTable = 40.dp
    val sizeChair = 18.dp
    val paddingChair = 6.dp


    @Composable
    private fun MainUi() {
        val viewModel: MainViewModel = viewModel()
        viewModel.initData()

        AppOrderTheme {
            // A surface container using the 'background' color1 from the theme
            Surface(
                modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
            ) {
                val paddingApp = 10.dp

                Column() {
                    Column(Modifier.padding(paddingApp)) {
                        Row(Modifier.fillMaxWidth())
                        {
                            Text(
                                text = "Welcome!", style = TextStyle(fontSize = 24.sp)
                            )
                            ComposeLottieAnimation(
                                modifier = Modifier.fillMaxWidth() .size(50.dp)
                            )
                        }

                        val paddingTop = 26.dp
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .padding(top = paddingTop),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            val countNon = viewModel.numberNonOrder.observeAsState()
                            Text(
                                text = "Số ghế trống : ${countNon.value}",
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                text = System.currentTimeMillis().getDateTime(),
                                modifier = Modifier.weight(1f),
                                textAlign = TextAlign.End
                            )
                        }
                    }

                    Divider(
                        color = androidx.compose.ui.graphics.Color.Black,
                        thickness = 1.dp, // Độ dày của đường thẳng
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp) // Khoảng cách đường thẳng với các thành phần xung quanh
                    )

                    Image(
                        painter = painterResource(id = imageStage), // Tải hình ảnh từ tài nguyên
                        contentDescription = "State",
                        Modifier
                            .fillMaxWidth()
                            .size(80.dp),
                        alignment = Alignment.Center,
                    )


                    PhotoGrid(viewModel)

                }

            }
        }
    }

    @Composable
    fun PhotoGrid(viewModel: MainViewModel = viewModel()) {
        val listLive by viewModel.listData.observeAsState()

        LazyVerticalGrid(
            columns = GridCells.Fixed(6),
            contentPadding = PaddingValues(top = 32.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            items(listLive ?: arrayListOf(), key = { it.id }, span = { item ->
                val span = when (item.type) {
                    Order.TYPE_DOUBLE_CHAIR -> {
                        2
                    }

                    Order.TYPE_LONG_CHAIR -> {
                        3
                    }

                    else -> {
                        1
                    }
                }
                GridItemSpan(span)
            }) { data ->
                when (data.type) {
                    Order.TYPE_DOUBLE_CHAIR -> {
                        AnimatedVisibility(
                            visible = data.isShow,
                            enter = fadeIn(),
                            exit = fadeOut()
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Image(
                                    painter = painterResource(id = imageTable), // Tải hình ảnh từ tài nguyên
                                    contentDescription = "State",
                                    Modifier
                                        .fillMaxWidth()
                                        .size(sizeTable),
                                    alignment = Alignment.Center,
                                )

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(
                                            top = 0.dp,
                                            bottom = 20.dp,
                                            start = 6.dp,
                                            end = 6.dp
                                        ),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Card(
                                        modifier = Modifier
                                            .padding(2.dp)
                                            .weight(1f)
                                            .clickable {
                                                data.isSelectChart1 = !data.isSelectChart1
                                                data.date = System.currentTimeMillis()
                                                viewModel.updateOrder(data)
                                            },
                                        colors = CardDefaults.cardColors(containerColor = if (data.isSelectChart1) color4 else color5)
                                    ) {
                                        val icon: Int = if (data.isSelectChart1) {
                                            R.drawable.ic_chair_selected
                                        } else {
                                            R.drawable.ic_chair_non_select
                                        }

                                        Image(
                                            painter = painterResource(id = icon),
                                            alignment = Alignment.Center,
                                            contentDescription = null, // Không cần mô tả hình ảnh
                                            modifier = Modifier
                                                .padding(paddingChair)
                                                .fillMaxWidth()
                                                .size(sizeChair) // Chiều cao của hình ảnh trong card
                                        )
                                    }
                                    Card(
                                        modifier = Modifier
                                            .padding(2.dp)
                                            .weight(1f)
                                            .clickable {
                                                data.isSelectChart2 = !data.isSelectChart2
                                                viewModel.updateOrder(data)
                                            },
                                        colors = CardDefaults.cardColors(containerColor = if (data.isSelectChart2) color4 else color5)
                                    ) {
                                        val icon: Int = if (data.isSelectChart2) {
                                            R.drawable.ic_chair_selected
                                        } else {
                                            R.drawable.ic_chair_non_select
                                        }

                                        Image(
                                            painter = painterResource(id = icon),
                                            alignment = Alignment.Center,
                                            contentDescription = null, // Không cần mô tả hình ảnh
                                            modifier = Modifier
                                                .padding(paddingChair)
                                                .fillMaxWidth()
                                                .size(sizeChair) // Chiều cao của hình ảnh trong card
                                        )
                                    }
                                }
                            }
                        }

                    }

                    Order.TYPE_LONG_CHAIR -> {
                        Card(
                            modifier = Modifier
                                .padding(top = 20.dp)
                                .clickable {
                                    data.isSelectChartLong = !data.isSelectChartLong
                                    data.date = System.currentTimeMillis()
                                    viewModel.updateOrder(data)
                                },
                            colors = CardDefaults.cardColors(containerColor = if (data.isSelectChartLong) color4 else color5)
                        ) {
                            val icon: Int = if (data.isSelectChartLong) {
                                R.drawable.ic_chair_selected
                            } else {
                                R.drawable.ic_chair_non_select
                            }

                            Image(
                                painter = painterResource(id = icon),
                                alignment = Alignment.Center,
                                contentDescription = null, // Không cần mô tả hình ảnh
                                modifier = Modifier
                                    .padding(4.dp)
                                    .fillMaxWidth()
                                    .width(80.dp)
                                    .height(40.dp) // Chiều cao của hình ảnh trong card
                            )
                        }
                    }

                    else -> {
                        AnimatedVisibility(
                            visible = data.isShow,
                            enter = fadeIn(),
                            exit = fadeOut()
                        ) {
                            Divider(
                                color = androidx.compose.ui.graphics.Color.Black,
                                thickness = 2.dp, // Độ dày của đường thẳng
                                modifier = Modifier
                                    .width(80.dp)
                                    .padding(vertical = 8.dp) // Khoảng cách đường thẳng với các thành phần xung quanh
                            )
                        }

                    }
                }

            }
        }
    }

    @Composable
    fun ComposeLottieAnimation(modifier: Modifier) {

        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.animation_lltfclah))

        LottieAnimation(
            modifier = modifier,
            composition = composition,
            iterations = LottieConstants.IterateForever,
            alignment = Alignment.CenterEnd
        )
    }

}
