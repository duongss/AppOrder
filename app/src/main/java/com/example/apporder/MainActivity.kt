package com.example.apporder

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
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
import com.example.apporder.ui.theme.color6
import com.example.apporder.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar

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
    val datePick = mutableStateOf(System.currentTimeMillis())

    @Composable
    private fun MainUi() {
        val viewModel: MainViewModel = viewModel()
        viewModel.initData()

        AppOrderTheme {
            Surface(
                modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
            ) {
                val paddingApp = 10.dp

                Column() {
                    Column(Modifier.padding(paddingApp)) {
                        Row(Modifier.fillMaxWidth())
                        {
                            Text(
                                text = getString(R.string.app_name),
                                style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold),
                                modifier = Modifier.weight(1f)
                            )
                            ComposeLottieAnimation(
                                modifier = Modifier
                                    .size(50.dp)
                                    .clickable {
                                        viewModel.resetData()
                                    }
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
                            val myDate by datePick
                            Text(
                                text = myDate.getDateTime(),
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable {
                                        showTimePicker(viewModel)
                                    },
                                textAlign = TextAlign.End
                            )
                        }
                    }

                    ConstraintLayout(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    ) {
                        val (divider, stageText, gridAbove) = createRefs()

                        Divider(
                            color = Color.Black,
                            thickness = 1.dp, // Độ dày của đường thẳng
                            modifier = Modifier
                                .constrainAs(divider) {
                                    top.linkTo(parent.top)
                                }
                                .fillMaxWidth()
                        )

                        Text(
                            text = "Sân Khấu",
                            style = TextStyle(fontSize = 24.sp, color = color6),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 10.dp)
                                .constrainAs(stageText) {
                                    top.linkTo(divider.bottom)
                                },
                            textAlign = TextAlign.Center
                        )

                        PhotoGrid(viewModel,
                            Modifier
                                .padding(top = 28.dp, bottom = 10.dp)
                                .constrainAs(gridAbove) {
                                    top.linkTo(stageText.bottom)
                                    bottom.linkTo(parent.bottom)
                                })

                    }

                }

            }
        }
    }

    @Composable
    fun PhotoGrid(viewModel: MainViewModel, modifier: Modifier) {
        val listLive by viewModel.listData.observeAsState()

        LazyVerticalGrid(
            modifier = modifier,
            columns = GridCells.Fixed(12),
            contentPadding = PaddingValues(top = 10.dp, bottom = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            items(listLive ?: arrayListOf(), key = { it.id }, span = { item ->
                val span = when (item.type) {
                    Order.TYPE_DOUBLE_CHAIR -> {
                        4
                    }

                    Order.TYPE_LONG_CHAIR -> {
                        12
                    }

                    Order.TYPE_DOUBLE_CHAIR_VIP -> {
                        4
                    }
                    else -> {
                        2
                    }
                }
                GridItemSpan(span)
            }) { data ->
                when (data.type) {
                    Order.TYPE_DOUBLE_CHAIR -> {
                        DoubleChair(data, viewModel)
                    }

                    Order.TYPE_DOUBLE_CHAIR_VIP -> {
                        DoubleChair(data, viewModel)
                    }

                    Order.TYPE_LONG_CHAIR -> {
                        LongChair(data, viewModel)
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
                                    .padding(vertical = 4.dp) // Khoảng cách đường thẳng với các thành phần xung quanh
                            )
                        }

                    }
                }

            }
        }

    }

    @Composable
    private fun LongChair(
        data: Order,
        viewModel: MainViewModel
    ) {
        Column(Modifier.padding(10.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Row() {
                data.listLongTable.forEach {
                    Box(Modifier.weight(1f), contentAlignment = Alignment.Center) {
                        Image(
                            painter = painterResource(id = imageTable),
                            contentDescription = "State",
                            modifier = Modifier
                                .fillMaxSize()
                                .size(sizeTable)
                                .clickable {
                                    it.isSelected = !it.isSelected
                                    viewModel.updateOrder(data)
                                },
                            colorFilter = if (it.isSelected) ColorFilter.tint(Color.Black) else ColorFilter.tint(
                                Color.Gray
                            )
                        )
                        Text(
                            text = it.stt.toString(),
                            style = TextStyle(fontSize = 14.sp, color = Color.White),
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxSize()
                        )
                    }
                }
            }
            Card(
                colors = CardDefaults.cardColors(containerColor = if (data.listLongTable.all { it.isSelected }) color4 else color5)
            ) {
                val icon: Int = R.drawable.ic_chair_non_select
                Image(
                    painter = painterResource(id = icon),
                    alignment = Alignment.Center,
                    contentDescription = null, // Không cần mô tả hình ảnh
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                        .width(34.dp)
                        .height(30.dp) // Chiều cao của hình ảnh trong card
                )
            }
        }
    }

    @Composable
    private fun DoubleChair(
        data: Order,
        viewModel: MainViewModel
    ) {
        val fraction = 1f

        AnimatedVisibility(
            visible = data.isShow,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Table(data, viewModel)

                Row(
                    modifier = Modifier
                        .fillMaxWidth(fraction)
                        .padding(
                            top = 0.dp,
                            bottom = 16.dp,
                            start = 3.dp,
                            end = 3.dp
                        ),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Card(
                        modifier = Modifier
                            .padding(2.dp)
                            .weight(1f),
                        colors = CardDefaults.cardColors(containerColor = if (data.isSelected) color4 else color5)
                    ) {
                        val icon: Int = if (data.isSelected) {
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
                            .weight(1f),
                        colors = CardDefaults.cardColors(containerColor = if (data.isSelected) color4 else color5)
                    ) {
                        val icon: Int = if (data.isSelected) {
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

    @Composable
    private fun Table(data: Order, viewModel: MainViewModel) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Image(
                painter = painterResource(id = imageTable),
                contentDescription = "State",
                modifier = Modifier
                    .fillMaxSize()
                    .size(sizeTable)
                    .clickable {
                        data.isSelected = !data.isSelected
                        viewModel.updateOrder(data)
                    },
                colorFilter = if (data.isSelected) ColorFilter.tint(Color.Black) else ColorFilter.tint(
                    Color.Gray
                )
            )
            Text(
                text = data.stt.toString(),
                style = TextStyle(fontSize = 14.sp, color = Color.White),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxSize()
            )
        }
    }

    @Composable
    fun ComposeLottieAnimation(modifier: Modifier) {

        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.animation_lltfclah))

        LottieAnimation(
            modifier = modifier,
            composition = composition,
            iterations = LottieConstants.IterateForever,
            alignment = Alignment.CenterEnd,
        )
    }

    fun showTimePicker(viewModel: MainViewModel) {
        val calendar = Calendar.getInstance()
        val listener = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            calendar[Calendar.DAY_OF_MONTH] = dayOfMonth
            calendar[Calendar.MONTH] = month
            calendar[Calendar.YEAR] = year
            viewModel.initData(calendar.timeInMillis)
            datePick.value = calendar.timeInMillis
        }

        DatePickerDialog(
            this,
            listener,
            calendar[Calendar.YEAR],
            calendar[Calendar.MONTH],
            calendar[Calendar.DAY_OF_MONTH]
        ).show()
    }
}