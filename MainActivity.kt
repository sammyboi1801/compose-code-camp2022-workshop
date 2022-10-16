package com.example.composecampproject_startercode

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.composecampproject_startercode.ui.theme.ComposeCampProjectStarterCodeTheme
import java.text.NumberFormat

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeCampProjectStarterCodeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.SpaceBetween,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Heading()
                        val bill = billTipSplit()
                        FinalAmount(bill)
                    }
                }
            }
        }
    }
}

@Composable
fun Heading() {

    // A box composable is used to stack different elements.

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Color(0xFFCEF5E6),
                RoundedCornerShape(bottomStart = 25.dp, bottomEnd = 25.dp)
            ),
    ) {
        Text(
            text = "Tip Calculator",
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(20.dp)
                .align(Alignment.Center),
            color = Color(0xFF08502F)
        )
    }
}

@Composable
fun LabelText(label: String) {
    Text(
        fontSize = 18.sp,
        text = label,
        color = Color.Gray,
        fontWeight = FontWeight.SemiBold,
    )
}

@Composable
fun billTipSplit(): Double {
    var billInput by remember { mutableStateOf("") }
    var tipPercent by remember { mutableStateOf(0.0) }
    var splitValue by remember { mutableStateOf(0) }
    val bill = billInput.toDoubleOrNull() ?: 0.0

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .padding(start = 60.dp, end = 60.dp)
    ) {

        // TEXT INPUT FUNCTION CALLED
        LabelText(label = "Enter total bill")
        BillInputField(value = billInput, onValueChange = { billInput = it })

        // Add Spacer to space out the elements:
        Spacer(modifier = Modifier.height(48.dp))

        // CHOOSE TIP BUTTONS CALLED
        LabelText(label = "Choose tip")
        Spacer(modifier = Modifier.height(16.dp))

        fun onTipChange(tip: Double): Double {
            if (tipPercent != tip) {
                tipPercent = tip
            } else {
                tipPercent = 0.0
            }
            return tipPercent
        }

        TipButton(buttonText = "10%", tipPercent, value = 10.0) { onTipChange(10.0) }
    }

    // SPLIT BUTTONS CALLED
    LabelText(label = "Split")
    SplitBetweenPersons(
        splitValue = splitValue,
        onSplitIncrease = { splitValue++ },
        onSplitDecrease = { splitValue-- }
    )
    return calculateTip(bill, tipPercent, splitValue)
}

@Composable
fun BillInputField(
    value: String,
    onValueChange: (String) -> Unit
) {
    Column {
        TextField(
            value = value,
            onValueChange = onValueChange,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.Transparent,
                focusedIndicatorColor = Color(0xFF08502F),
            ),
            textStyle = TextStyle(
                fontSize = 36.sp,
                color = Color(0xFF0D8A53)
            ),
        )
    }
}

@Composable
fun TipButton(buttonText: String, tipPercent: Double, value: Double, onTipChange: () -> Unit) {

    Button(
        colors = ButtonDefaults.buttonColors(
            backgroundColor = if (tipPercent == value) Color(0xFF0D8A53) else Color.White
        ),
        onClick = { onTipChange() },
        shape = RoundedCornerShape(50),
        elevation = ButtonDefaults.elevation(
            defaultElevation = 6.dp,
        ),
        modifier = Modifier
            .width(75.dp)
            .height(50.dp)
    ) {
        Text(
            text = buttonText,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = if (tipPercent == value) Color.White else Color(0xFF0D8A53)
        )
    }
}

@Composable
fun SplitBetweenPersons(
    splitValue: Int,
    onSplitIncrease: () -> Unit,
    onSplitDecrease: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = {
                if (splitValue != 0) {
                    onSplitDecrease()
                }
            }
        ) {
            Icon(
                painterResource(id = R.drawable.ic_baseline_remove_24),
                contentDescription = null,
                tint = Color(0xFF0D8A53)
            )
        }
        Text(
            text = splitValue.toString(),
            fontWeight = FontWeight.Bold,
            color = Color(0xFF0D8A53),
            fontSize = 36.sp
        )
        IconButton(
            onClick = { onSplitIncrease() }
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null,
                tint = Color(0xFF0D8A53)
            )
        }
    }
}

@Composable
fun FinalAmount(final_bill: Double = 0.0) {

    val finalBillInCurrency = NumberFormat.getCurrencyInstance().format(final_bill)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(start = 30.dp, end = 30.dp)
            .fillMaxWidth()
            .background(Color(0xFFCEF5E6), RoundedCornerShape(topStart = 25.dp, topEnd = 25.dp))
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Total per person",
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                color = Color.Gray,
                modifier = Modifier
                    .padding(top = 24.dp)
            )
            Text(
                text = finalBillInCurrency,
                fontWeight = FontWeight.Bold,
                fontSize = 48.sp,
                color = Color(0xFF0D8A53)
            )
        }
    }
}

fun calculateTip(bill: Double = 0.0, tipPercent: Double, split: Int): Double {
    val tip = tipPercent / 100 * bill
    val finalBill = bill + tip
    if (split != 0) {
        return finalBill / split
    } else {
        return finalBill
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ComposeCampProjectStarterCodeTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Heading()
            val bill = billTipSplit()
            FinalAmount(bill)
        }
    }
}
