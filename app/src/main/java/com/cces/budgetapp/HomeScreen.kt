package com.cces.budgetapp

import android.app.LocaleManager
import android.content.Context
import android.os.Build
import android.os.LocaleList
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.core.os.LocaleListCompat
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.cces.budgetapp.data.model.ExpenseEntity
import com.cces.budgetapp.ui.theme.Zinc
import com.cces.budgetapp.viewmodel.HomeViewModel
import com.cces.budgetapp.widget.ExpenseTextView


@Composable
fun HomeScreen(navController: NavController) {
 /*   val model = ImageRequest
        .Builder(LocalContext.current)
        .data("https://drive.google.com/file/d/11cnAUaLJ3a6ggVbi-NLpmVU7raIVALHO/view?usp=sharing")
        .size(Size.ORIGINAL)
        .build()
    val imageState = rememberAsyncImagePainter(model = model).state */
    val viewModel = HomeViewModel.HomeViewModelFactory(LocalContext.current).create(HomeViewModel::class.java)
    val context = LocalContext.current
    Surface(modifier = Modifier.fillMaxSize()) {
        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            val (nameRow, list, card, topBar, add) = createRefs()
            Image(painter = painterResource(id = R.drawable.ic_topbar), contentDescription = null,
                modifier = Modifier.constrainAs(topBar) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                })
            /* Box(modifier = Modifier.constrainAs(topBar)
                 {
                     top.linkTo(parent.top)
                     start.linkTo(parent.start)
                     end.linkTo(parent.end)
                 }) {
                 if (imageState is AsyncImagePainter.State.Success) {
                     Image(painter = imageState.painter, contentDescription = null)
                 }
                 if (imageState is AsyncImagePainter.State.Loading) {
                     CircularProgressIndicator(
                         modifier = Modifier
                             .size(100.dp),
                         color = (MaterialTheme.colorScheme.primary)
                     )
                 }
                 if (imageState is AsyncImagePainter.State.Error) {
                     Icon(
                         imageVector = Icons.Rounded.ImageNotSupported,
                         contentDescription = null
                     )
                 }
            }*/
            Box(modifier = Modifier
                .fillMaxWidth()
                .padding(top = 64.dp, start = 16.dp, end = 16.dp)
                .constrainAs(nameRow) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }) {
                Column(modifier = Modifier.align(Alignment.CenterStart)) {
                    ExpenseTextView(text = stringResource(R.string.good_afternoon), fontSize = 16.sp, color = Color.White)
                    ExpenseTextView(
                        text = stringResource(R.string.user),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
                Spacer(modifier = Modifier.size(8.dp))
            }

            val state = viewModel.expenses.collectAsState(initial = emptyList())
            val expense = viewModel.getTotalExpense(state.value)
            val income = viewModel.getTotalIncome(state.value)
            val balance = viewModel.getBalance(state.value)
            CardItem(
                modifier = Modifier.constrainAs(card) {
                    top.linkTo(nameRow.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
                balance = balance, income = income, expense = expense
            )
            TransactionList(
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(list) {
                        top.linkTo(card.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                        height = Dimension.fillToConstraints
                    }, list = state.value,
                viewModel
            )

            Image(
                painter = painterResource(id = android.R.drawable.ic_menu_add),
                contentDescription = null,
                modifier = Modifier
                    .constrainAs(add) {
                        bottom.linkTo(parent.bottom)
                        end.linkTo(parent.end)
                    }
                    .size(48.dp)
                    .clip(CircleShape)
                    .clickable {
                        navController.navigate("/add")
                    }
            )
        }
    }
}


@Composable
fun CardItem(
    modifier: Modifier,
    balance: String, income: String, expense: String
) {
    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth()
            .height(200.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Zinc)
            .padding(16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            Column {
                ExpenseTextView(text = stringResource(R.string.total_balance), fontSize = 16.sp, color = Color.White)
                ExpenseTextView(
                    text = balance, fontSize = 20.sp, color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
           /* Image(
                painter = painterResource(id = R.drawable.dots_menu),
                contentDescription = null,
                modifier = Modifier.align(Alignment.CenterEnd)
            ) */
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            CardRowItem(
                modifier = Modifier
                    .align(Alignment.CenterStart),
                title = stringResource(R.string.income),
                amount = income,
                imaget = R.drawable.ic_income
            )
            Spacer(modifier = Modifier.size(8.dp))
            CardRowItem(
                modifier = Modifier
                    .align(Alignment.CenterEnd),
                title = stringResource(R.string.expense),
                amount = expense,
                imaget = R.drawable.ic_expense
            )
        }

    }
}


@Composable
fun TransactionList(modifier: Modifier, list: List<ExpenseEntity>, viewModel: HomeViewModel) {
    LazyColumn(modifier = modifier.padding(horizontal = 16.dp)) {
        item {
            Column {
                Box(modifier = modifier.fillMaxWidth()) {
                    ExpenseTextView(
                        text = stringResource(R.string.recent_transactions),
                        fontSize = 20.sp,
                    )
                    ExpenseTextView(
                        text = stringResource(R.string.see_all),
                        fontSize = 16.sp,
                        modifier = Modifier.align(Alignment.CenterEnd)
                    )
                }
            }
        }
        items(list) { item ->
            val icon = viewModel.getItemIcon(item)
            TransactionItem(
                title = item.title,
                amount = item.amount.toString(),
                icon = icon,
                date = item.date,
                color = if (item.type == stringResource(R.string.income)) Color.Green else Color.Red
            )
        }

    }
}

@Composable
fun TransactionItem(
    title: String,
    amount: String,
    icon: Any,
    date: String,
    color: Color
) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Row() {
            Image(
                painter = painterResource(id = icon as Int),
                contentDescription = null,
                modifier = Modifier.size(50.dp)
            )
            Spacer(modifier = Modifier.size(8.dp))
            Column {
                ExpenseTextView(text = title, fontSize = 16.sp)
                ExpenseTextView(text = date, fontSize = 12.sp)
            }
        }
        ExpenseTextView(
            text = amount,
            fontSize = 20.sp,
            modifier = Modifier.align(Alignment.CenterEnd),
            color = color
        )
    }
}

@Composable
fun CardRowItem(modifier: Modifier, title: String, amount: String, imaget: Int) {
    Column(modifier = modifier) {
        Row {

            Image(
                painter = painterResource(id = imaget),
                contentDescription = null,
            )
            Spacer(modifier = Modifier.size(8.dp))
            ExpenseTextView(text = title, fontSize = 16.sp, color = Color.White)
        }
        ExpenseTextView(text = amount, fontSize = 20.sp, color = Color.White)
    }
}

fun changeLocales(context: Context, localeString: String){
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            context.getSystemService(LocaleManager::class.java)
                .applicationLocales = LocaleList.forLanguageTags(localeString)
        }
    else {
        AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(localeString))
    }

}
@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(rememberNavController())
}


private const val LABEL_BACKGROUND_SHADOW_RADIUS_DP = 4f
private const val LABEL_BACKGROUND_SHADOW_DY_DP = 2f
private const val CLIPPING_FREE_SHADOW_RADIUS_MULTIPLIER = 1.4f