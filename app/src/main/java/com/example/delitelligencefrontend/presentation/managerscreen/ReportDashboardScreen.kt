/*https://www.youtube.com/watch?v=YONO28H75gY&ab_channel=CodingInformer
How to use MPAndroidCharts in Android applications!
 */

/*https://chatgpt.com
prompt: 'Build a screen that will use the MPAndroiChart to make a pie chart for the getSalesByDate function in the view model
to get the List of sales and fill the pie chart with the data.'
*/

package com.example.delitelligencefrontend.presentation.managerscreen

import android.app.DatePickerDialog
import android.icu.text.NumberFormat
import android.icu.util.Calendar
import android.os.Build
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.delitelligencefrontend.model.DailySaleData
import com.example.delitelligencefrontend.presentation.viewmodel.AnalysisViewModel
import com.example.delitelligencefrontend.presentation.viewmodel.ManageProductViewModel
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.LegendEntry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportDashboardScreen(
    viewModel: AnalysisViewModel = hiltViewModel(),
    navController: NavController
) {
    val totalSales by viewModel.totalSales
    val salesToday by viewModel.totalSalesDay
    val salesData by viewModel.salesInGivenPeriod

    LaunchedEffect(Unit) {
        // Get today's date and format it
        val today = LocalDate.now()
        val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val todayFormatted = today.format(dateFormatter)

        // Use formatted date string in function calls
        viewModel.loadTotalSales()
        viewModel.loadTotalSalesDay(todayFormatted)
        viewModel.getSalesInGivenPeriod(todayFormatted, "")
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Report Dashboard") })
        },
        content = { padding ->
            Row(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
            ) {
                SideButtonsPanel(modifier = Modifier.weight(1f))
                MainDashboardContent(viewModel, totalSales, salesToday, salesData, modifier = Modifier.weight(4f))
            }
        }
    )
}

@Composable
fun SideButtonsPanel(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxHeight().padding(8.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = { /* Handle button action */ }) {
            Text("Sales")
        }
    }
}

@Composable
fun MainDashboardContent(
    viewModel: AnalysisViewModel,
    totalSales: Double,
    salesToday: Double,
    salesData: List<DailySaleData>,
    modifier: Modifier = Modifier
) {
    val currencyFormatter = NumberFormat.getCurrencyInstance(Locale.GERMANY)

    var startDate by remember { mutableStateOf("") }
    var endDate by remember { mutableStateOf("") }
    var isDayOnly by remember { mutableStateOf(false) }

    fun updateData() {
        if (isDayOnly) {
            if (startDate.isNotEmpty()) {
                viewModel.getSalesInGivenPeriod(startDate, "")
            }
        } else {
            if (startDate.isNotEmpty() && endDate.isNotEmpty()) {
                viewModel.getSalesInGivenPeriod(startDate, endDate)
            }
        }
    }

    Row(
        modifier = modifier
            .fillMaxHeight()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        // "Total Sales" section with border and increased size
        Column(
            modifier = Modifier
                .fillMaxHeight(0.5f) // Increase size vertically
                .border(2.dp, Color.Blue)
                .padding(16.dp)
                .weight(1f)
        ) {
            Text(text = "Total Sales:", style = MaterialTheme.typography.titleMedium)
            Text(
                text = currencyFormatter.format(totalSales),
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Sales Today:", style = MaterialTheme.typography.titleSmall)
            Text(
                text = currencyFormatter.format(salesToday),
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            )
        }

        // Spacer to push the Pie Chart further to the right
        Spacer(modifier = Modifier.width(16.dp))

        // Row for Pie Chart and Controls
        Row(
            modifier = Modifier
                .border(2.dp, Color.Gray)
                .height(280.dp)
                .padding(start = 16.dp, end = 16.dp)
                .weight(2f)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(300.dp)
            ) {
                PieChartView(
                    salesData = salesData,
                    modifier = Modifier.size(280.dp)
                )
            }

            // Controls next to the pie chart
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.Start
            ) {
                DatePickerInput("Start Date", startDate) { date ->
                    startDate = date
                    updateData()
                }
                if (!isDayOnly) {
                    DatePickerInput("End Date", endDate) { date ->
                        endDate = date
                        updateData()
                    }
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = isDayOnly,
                        onCheckedChange = { checked ->
                            isDayOnly = checked
                            endDate = if (checked) "" else endDate
                            updateData()
                        }
                    )
                    Text("Day Only")
                }
            }
        }
    }
}

@Composable
fun DatePickerInput(label: String, selectedDate: String, onDateSelected: (String) -> Unit) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    val datePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, y: Int, m: Int, d: Int ->
            val formattedDate = String.format("%d-%02d-%02d", y, m + 1, d)
            onDateSelected(formattedDate)
        },
        year, month, day
    )

    Button(onClick = { datePickerDialog.show() }) {
        Text(text = if (selectedDate.isNotEmpty()) selectedDate else label)
    }
}

@Composable
fun PieChartView(salesData: List<DailySaleData>, modifier: Modifier = Modifier) {
    val currencyFormatter = NumberFormat.getCurrencyInstance(Locale.GERMANY)

    if (salesData.isEmpty()) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No Sales Data Available")
        }
        return
    }

    // Create PieEntry objects with saleAmount as label
    val entries = salesData.map {
        PieEntry(it.saleAmount.toFloat(), currencyFormatter.format(it.saleAmount))
    }

    // Create a list of labels for the legend
    val legendLabels = salesData.map {
        "${it.saleCategoryTime}: %.1f%%".format(it.salesPercentage)
    }

    // Create the chart and set the necessary configuration
    AndroidView(
        factory = { context ->
            PieChart(context).apply {
                layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 300)
                description.isEnabled = false
                isRotationEnabled = true
                isHighlightPerTapEnabled = true

                // Customize legend to show desired text and colors
                legend.verticalAlignment = Legend.LegendVerticalAlignment.TOP
                legend.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
                legend.orientation = Legend.LegendOrientation.VERTICAL
                legend.setDrawInside(false)
                legend.isWordWrapEnabled = true
            }
        },
        modifier = modifier.size(280.dp),
        update = { chart ->
            val dataSet = PieDataSet(entries, "").apply {
                colors = ColorTemplate.MATERIAL_COLORS.toList()
                valueTextSize = 12f // Adjusted text size for pie slice labels
            }

            chart.data = PieData(dataSet)
            chart.setUsePercentValues(false) // Display actual values not percentages

            // Manually set legend entries to display category time and percentage
            val legendEntries = legendLabels.mapIndexed { index, label ->
                LegendEntry().apply {
                    this.label = label
                    formColor = dataSet.colors[index]
                }
            }
            chart.legend.setCustom(legendEntries)

            chart.invalidate()
            chart.animateY(1000)
        }
    )
}

// Custom formatter for percentages on the legend if needed elsewhere
class PercentFormatter : ValueFormatter() {
    override fun getFormattedValue(value: Float): String {
        return String.format("%.1f%%", value)
    }
}