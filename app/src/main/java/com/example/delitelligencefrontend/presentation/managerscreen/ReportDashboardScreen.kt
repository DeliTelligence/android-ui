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
import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.delitelligencefrontend.model.DailySaleData
import com.example.delitelligencefrontend.model.GraphData
import com.example.delitelligencefrontend.model.HandMadeOrNotSalesData
import com.example.delitelligencefrontend.model.QuantitySalesData
import com.example.delitelligencefrontend.model.SalesTypeData
import com.example.delitelligencefrontend.presentation.Screen
import com.example.delitelligencefrontend.presentation.viewmodel.AnalysisViewModel
import com.example.delitelligencefrontend.presentation.viewmodel.ReportSendingStatus
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportDashboardScreen(
    navController: NavHostController,
    viewModel: AnalysisViewModel = hiltViewModel()
) {
    val totalSales by viewModel.totalSales
    val salesToday by viewModel.totalSalesDay
    val salesData by viewModel.salesInGivenPeriod
    val salesByQuantityData by viewModel.salesByQuantityData
    val salesByTypeData by viewModel.salesByTypeData
    val salesByHandMadeOrNotData by viewModel.salesByHandMadeOrNotData

    val employeeEmail = viewModel.getEmployeeEmail()


    LaunchedEffect(Unit) {
        val today = LocalDate.now()
        val dateFormatter = DateTimeFormatter.ofPattern("2025-02-18")
        val todayFormatted = today.format(dateFormatter)

        viewModel.loadTotalSales()
        viewModel.loadTotalSalesDay(todayFormatted)
        viewModel.getSalesInGivenPeriod(todayFormatted, "")
        viewModel.getQuantityOfSalesData(todayFormatted, "")
        viewModel.getSalesByTypeData(todayFormatted, "")
        viewModel.getSalesByHandMadeOrNot(todayFormatted, "")
        viewModel.getSalesPrediction()
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Report Dashboard") }) },
        content = { padding ->
            Row(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
            ) {
                SideButtonsPanel(modifier = Modifier.weight(1f), navController)
                MainDashboardContent(
                    viewModel = viewModel,
                    totalSales = totalSales,
                    salesToday = salesToday,
                    salesData = salesData,
                    salesByQuantityData = salesByQuantityData,
                    salesByTypeData = salesByTypeData,
                    salesByHandMadeOrNotData = salesByHandMadeOrNotData,
                    modifier = Modifier.weight(4f)
                )
            }
        }
    )
}

@Composable
fun SideButtonsPanel(modifier: Modifier = Modifier,
        navController: NavHostController) {
    Column(
        modifier = modifier
            .fillMaxHeight()
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = { /* Handle Sales button action */ }) {
            Text("Sales")
        }

        Button(onClick = {navController.navigate(Screen.InventoryDashboardScreen.route)
        }) {
            Text("Inventory")
        }
    }
}

@Composable
fun MainDashboardContent(
    viewModel: AnalysisViewModel,
    totalSales: Double,
    salesToday: Double,
    salesData: List<DailySaleData>,
    salesByQuantityData: List<QuantitySalesData>,
    salesByTypeData: List<SalesTypeData>,
    salesByHandMadeOrNotData: List<HandMadeOrNotSalesData>,
    modifier: Modifier = Modifier
) {
    val currencyFormatter = NumberFormat.getCurrencyInstance(Locale.GERMANY)
    var selectedChartType by remember { mutableStateOf(ChartType.QUANTITY) }
    var startDate by remember { mutableStateOf("") }
    var endDate by remember { mutableStateOf("") }
    var isDayOnly by remember { mutableStateOf(false) }
    var validationError by remember { mutableStateOf<String?>(null) }

    val employeeEmail = viewModel.getEmployeeEmail()
    val reportStatus by viewModel.reportSendingStatus
    val salesPredictions by viewModel.salesPredictions

    fun updateData() {
        if (startDate.isNotEmpty()) {
            viewModel.getSalesInGivenPeriod(startDate, if (isDayOnly) "" else endDate)
            when (selectedChartType) {
                ChartType.QUANTITY -> viewModel.getQuantityOfSalesData(startDate, if (isDayOnly) "" else endDate)
                ChartType.TYPE -> viewModel.getSalesByTypeData(startDate, if (isDayOnly) "" else endDate)
                ChartType.HANDMADE -> viewModel.getSalesByHandMadeOrNot(startDate, if (isDayOnly) "" else endDate)
            }
        }
    }

    fun validateAndSendReport() {
        val formattedStartDate = viewModel.formatAndValidateDate(startDate)
        val formattedEndDate = viewModel.formatAndValidateDate(endDate)

        if (formattedStartDate == null || (!isDayOnly && endDate.isNotEmpty() && formattedEndDate == null)) {
            validationError = "Invalid date format. Please use YYYY-MM-DD."
            return
        }

        if (employeeEmail == null) {
            validationError = "Email not found for sending report."
            return
        }

        validationError = null

        (if (isDayOnly) "" else formattedEndDate)?.let {
            viewModel.sendSalesReport(
                to = employeeEmail,
                startDate = formattedStartDate,
                endDate = it
            )
        }
    }

    Column(
        modifier
            .fillMaxSize()
            .padding(8.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Left Column
            Column(
                modifier = Modifier
                    .weight(1.5f)
            ) {
                // Total Sales Container
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .border(2.dp, Color.Blue)
                        .padding(16.dp)
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

                Spacer(modifier = Modifier.height(16.dp))

                // Sales Prediction Line Graph
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .border(2.dp, Color.Green)
                        .padding(16.dp)
                ) {
                    Text("Sales Prediction", style = MaterialTheme.typography.titleMedium)
                    LineChartView(salesPredictions = salesPredictions)
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Right Column
            Column(modifier = Modifier.weight(2f)) {
                // Pie Chart Container
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .border(2.dp, Color.Gray)
                        .padding(16.dp)
                ) {
                    Box(modifier = Modifier.weight(2f)) {
                        PieChartView(salesData = salesData)
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.Top
                    ) {
                        DatePickerInput("Start Date", startDate) { date ->
                            startDate = date
                            updateData()
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        if (!isDayOnly) {
                            DatePickerInput("End Date", endDate) { date ->
                                endDate = date
                                updateData()
                            }
                            Spacer(modifier = Modifier.height(8.dp))
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
                        Spacer(modifier = Modifier.height(16.dp))

                        if (validationError != null) {
                            Text(
                                text = validationError ?: "",
                                color = Color.Red,
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }

                        Button(onClick = { validateAndSendReport() }) {
                            Text("Send Report")
                        }

                        when (reportStatus) {
                            is ReportSendingStatus.Success -> Text(
                                text = "Report Sent",
                                color = Color.Green,
                                style = MaterialTheme.typography.bodyMedium
                            )
                            is ReportSendingStatus.Error -> Text(
                                text = (reportStatus as ReportSendingStatus.Error).message,
                                color = Color.Red,
                                style = MaterialTheme.typography.bodyMedium
                            )
                            else -> {}
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Bar Chart Container
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .border(2.dp, Color.Gray)
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Button(onClick = {
                            selectedChartType = ChartType.QUANTITY
                            updateData()
                        }) {
                            Text("Quantity Analysis")
                        }
                        Button(onClick = {
                            selectedChartType = ChartType.TYPE
                            updateData()
                        }) {
                            Text("Sale Type Analysis")
                        }
                        Button(onClick = {
                            selectedChartType = ChartType.HANDMADE
                            updateData()
                        }) {
                            Text("Hand Made Analysis")
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Box(modifier = Modifier.fillMaxWidth().weight(1f)) {
                        when (selectedChartType) {
                            ChartType.QUANTITY -> BarChartView(salesData = BarDataType.Quantity(salesByQuantityData))
                            ChartType.TYPE -> BarChartView(salesData = BarDataType.Type(salesByTypeData))
                            ChartType.HANDMADE -> BarChartView(salesData = BarDataType.HandMade(salesByHandMadeOrNotData))
                        }
                    }
                }
            }
        }
    }
}
@Composable
fun LineChartView(salesPredictions: List<GraphData>, modifier: Modifier = Modifier) {
    AndroidView(
        factory = { context ->
            LineChart(context).apply {
                description.isEnabled = false
                setTouchEnabled(true)
                setDrawGridBackground(false)
                xAxis.setDrawGridLines(false)
                axisRight.isEnabled = false
                axisLeft.setDrawGridLines(false)
            }
        },
        modifier = modifier.fillMaxSize(),
        update = { chart ->
            val entries = salesPredictions.mapIndexed { index, data ->
                Entry(index.toFloat(), data.salesTotal.toFloat())
            }

            val dataSet = LineDataSet(entries, "Sales Prediction").apply {
                color = Color.Blue.toArgb()
                setCircleColor(Color.Blue.toArgb())
                lineWidth = 2f
                circleRadius = 4f
                setDrawCircleHole(false)
                valueTextSize = 10f
                setDrawFilled(true)
                fillColor = Color.Blue.copy(alpha = 0.3f).toArgb()
            }

            chart.xAxis.valueFormatter = object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    return if (value.toInt() in salesPredictions.indices) salesPredictions[value.toInt()].monthOfSales else ""
                }
            }

            chart.data = LineData(dataSet)
            chart.invalidate()
            chart.animateX(1000)
        }
    )
}
@Composable
fun BarChartView(salesData: BarDataType, modifier: Modifier = Modifier) {
    AndroidView(
        factory = { context ->
            BarChart(context).apply {
                description.isEnabled = false
                setFitBars(true)
            }
        },
        modifier = modifier.fillMaxSize(),
        update = { chart ->
            val (entries, labels, label) = when (salesData) {
                is BarDataType.Quantity -> {
                    val entries = salesData.data.mapIndexed { index, data -> BarEntry(index.toFloat(), data.salesData.toFloat()) }
                    val labels = salesData.data.map { data -> data.quantityRepresented.toString() }
                    Triple(entries, labels, "Quantity")
                }
                is BarDataType.Type -> {
                    val entries = salesData.data.mapIndexed { index, data -> BarEntry(index.toFloat(), data.saleAmountSaleType.toFloat()) }
                    val labels = salesData.data.map { data -> data.saleType.name }
                    Triple(entries, labels, "Sales Type")
                }
                is BarDataType.HandMade -> {
                    val entries = salesData.data.mapIndexed { index, data -> BarEntry(index.toFloat(), data.saleAmountHandMade.toFloat()) }
                    val labels = salesData.data.map { data -> data.handMadeDescription }
                    Triple(entries, labels, "Handmade Sales")
                }
            }

            val dataSet = BarDataSet(entries, label).apply {
                colors = ColorTemplate.MATERIAL_COLORS.toList()
            }

            chart.xAxis.valueFormatter = object : ValueFormatter() {
                override fun getAxisLabel(value: Float, axis: AxisBase?): String {
                    return if (value.toInt() in labels.indices) labels[value.toInt()] else value.toString()
                }
            }
            chart.xAxis.granularity = 1f  // Prevent skipping labels

            chart.data = BarData(dataSet)
            chart.invalidate()
            chart.animateY(1000)
        }
    )
}

@Composable
fun PieChartView(salesData: List<DailySaleData>, modifier: Modifier = Modifier) {
    if (salesData.isEmpty()) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No Sales Data Available")
        }
        return
    }

    val entries = salesData.map { PieEntry(it.saleAmount.toFloat(), "${it.saleCategoryTime}: ${NumberFormat.getCurrencyInstance(Locale.GERMANY).format(it.saleAmount)}") }

    AndroidView(
        factory = { context ->
            PieChart(context).apply {
                layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
                description.isEnabled = false
                isRotationEnabled = true
                isHighlightPerTapEnabled = true
                legend.isEnabled = true
                legend.textSize = 12f
                legend.formSize = 12f
                legend.formToTextSpace = 5f
                setDrawEntryLabels(false)  // Disable label drawing on slices
                setUsePercentValues(true) // Show percentage values
            }
        },
        modifier = modifier.fillMaxSize(),
        update = { chart ->
            val dataSet = PieDataSet(entries, "").apply {
                colors = ColorTemplate.MATERIAL_COLORS.toList()
                valueTextSize = 12f
                valueFormatter =
                    com.github.mikephil.charting.formatter.PercentFormatter(chart) // Use percentage formatter
            }

            chart.data = PieData(dataSet)
            chart.invalidate()
            chart.animateY(1000)
        }
    )
}

class PercentFormatter : ValueFormatter() {
    override fun getFormattedValue(value: Float): String {
        return String.format("%.1f%%", value)
    }
}

class CurrencyFormatter : ValueFormatter() {
    private val currencyFormatter = NumberFormat.getCurrencyInstance(Locale.GERMANY)
    override fun getFormattedValue(value: Float): String {
        return currencyFormatter.format(value.toDouble())
    }
}

sealed class BarDataType {
    data class Quantity(val data: List<QuantitySalesData>) : BarDataType()
    data class Type(val data: List<SalesTypeData>) : BarDataType()
    data class HandMade(val data: List<HandMadeOrNotSalesData>) : BarDataType()
}

@Composable
fun DatePickerInput(label: String, selectedDate: String, onDateSelected: (String) -> Unit) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    val datePickerDialog = DatePickerDialog(context, { _: DatePicker, y: Int, m: Int, d: Int ->
        val formattedDate = String.format("%d-%02d-%02d", y, m + 1, d)
        onDateSelected(formattedDate)
    }, year, month, day)

    Button(onClick = { datePickerDialog.show() }) {
        Text(text = if (selectedDate.isNotEmpty()) selectedDate else label)
    }
}

enum class ChartType {
    QUANTITY,
    TYPE,
    HANDMADE
}