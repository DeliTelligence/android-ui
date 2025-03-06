package com.example.delitelligencefrontend.presentation.managerscreen

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.delitelligencefrontend.presentation.Screen
import com.example.delitelligencefrontend.presentation.viewmodel.AnalysisViewModel
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.HorizontalBarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InventoryDashboardScreen(
    navController: NavHostController,
    viewModel: AnalysisViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.loadInventoryData()
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Inventory Dashboard") }) }
    ) { padding ->
        Row(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            SideButtonsPanelInventory(modifier = Modifier.width(130.dp), navController) // Increased width
            MainInventoryContent(
                viewModel = viewModel,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun SideButtonsPanelInventory(modifier: Modifier = Modifier, navController: NavHostController) {
    Column(
        modifier = modifier
            .fillMaxHeight()
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = { navController.navigate(Screen.ReportDashboardScreen.route) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Sales")
        }
        Button(
            onClick = { /* Already on Inventory screen */ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Inventory")
        }
    }
}

@Composable
fun MainInventoryContent(
    viewModel: AnalysisViewModel,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            // Left container for numerical data
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(8.dp)
                    .border(1.dp, Color.Gray)
            ) {
                InventoryDataDisplay(viewModel)
            }

            // Right side with pie chart and Send Report button
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(8.dp)
                    .border(1.dp, Color.Gray)
            ) {
                Column {
                    Text(
                        "Adjustment Data",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                    ) {
                        InventoryAdjustmentPieChart(viewModel)
                    }
                }

                Button(
                    onClick = { },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(8.dp)
                ) {
                    Text("Send Report")
                }
            }
        }

        // Bar chart across the bottom with vertical scroll
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .padding(start = 0.dp, top = 8.dp, end = 8.dp, bottom = 8.dp)
                .border(1.dp, Color.Gray)
                .verticalScroll(rememberScrollState())
        ) {
            InventoryLevelBarChart(viewModel)
        }
    }
}

@Composable
fun InventoryDataDisplay(viewModel: AnalysisViewModel) {
    val totalValue by viewModel.totalInventoryValue
    val totalWeight by viewModel.totalInventoryWeight
    val inventoryLevels by viewModel.inventoryLevels

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Total Items: ${inventoryLevels.size}")
        Text("Total Value: €%.2f".format(totalValue / 1000)) // Divided by 1000
        Text("Total Weight: %.2f kg".format(totalWeight / 1000)) // Divided by 1000
        Button(onClick = { viewModel.loadInventoryData() }) {
            Text("Refresh Data")
        }
    }
}

@Composable
fun InventoryAdjustmentPieChart(viewModel: AnalysisViewModel) {
    val adjustments by viewModel.inventoryAdjustments

    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { context ->
            PieChart(context).apply {
                description.isEnabled = false
                setUsePercentValues(true)
                setDrawEntryLabels(false)
                legend.isEnabled = true
                setEntryLabelTextSize(12f)
                setEntryLabelColor(Color.Black.toArgb())
                setCenterTextSize(16f)
            }
        },
        update = { chart ->
            val entries = adjustments.map { adjustment ->
                PieEntry(adjustment.adjustmentPercentage.toFloat(), adjustment.adjustmentType)
            }
            val dataSet = PieDataSet(entries, "").apply {
                colors = ColorTemplate.MATERIAL_COLORS.toList()
                valueTextSize = 14f
                valueTextColor = Color.Black.toArgb()
            }
            val pieData = PieData(dataSet)
            chart.data = pieData
            chart.invalidate()
        }
    )
}
@Composable
fun InventoryLevelBarChart(viewModel: AnalysisViewModel) {
    val levels by viewModel.inventoryLevels

    if (levels.isEmpty()) {
        Text("No inventory data available")
        return
    }

    AndroidView(
        modifier = Modifier
            .fillMaxWidth()
            .height((levels.size * 50 + 100).dp), // Adjust height based on number of items
        factory = { context ->
            HorizontalBarChart(context).apply {
                description.isEnabled = false
                setDrawValueAboveBar(true)
                setFitBars(true)
                xAxis.setDrawGridLines(false)
                axisLeft.setDrawGridLines(false)
                axisRight.setDrawGridLines(false)
                xAxis.position = XAxis.XAxisPosition.BOTTOM
                xAxis.granularity = 1f
                legend.isEnabled = true
                legend.textSize = 12f
                setPinchZoom(false)
                setScaleEnabled(false)
                isDoubleTapToZoomEnabled = false

                // Push Y-axis closer to the side panel
                extraLeftOffset = -15f

                // Ensure all labels are visible
                xAxis.setLabelCount(levels.size, true)
            }
        },
        update = { chart ->
            val entries = levels.mapIndexed { index, level ->
                BarEntry(index.toFloat(), (level.inventoryWeight / 1000).toFloat())
            }
            val dataSet = BarDataSet(entries, "Inventory Levels (kg)").apply {
                colors = ColorTemplate.MATERIAL_COLORS.toList()
                valueTextSize = 10f
                valueFormatter = object : ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        return String.format("%.2f", value)
                    }
                }
            }
            val barData = BarData(dataSet)
            barData.barWidth = 0.6f // Adjust this value to change bar width
            chart.data = barData

            // Set Y-axis (left) to show weight in kg
            chart.axisLeft.apply {
                axisMinimum = 0f
                valueFormatter = object : ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        return String.format("%.0f kg", value)
                    }
                }
            }

            // Disable right Y-axis
            chart.axisRight.isEnabled = false

            // Set X-axis to show product names
            chart.xAxis.apply {
                valueFormatter = IndexAxisValueFormatter(levels.map { it.productName })
                position = XAxis.XAxisPosition.BOTTOM
                granularity = 1f
                labelRotationAngle = 45f // Rotate labels for better readability
                setDrawAxisLine(true)
                setDrawGridLines(false)
                setAvoidFirstLastClipping(true)
                textSize = 8f // Adjust text size if needed
                labelCount = levels.size
            }

            // Adjust the visible range to show all bars
            chart.setVisibleXRangeMaximum(levels.size.toFloat())
            chart.moveViewToX(0f)

            chart.setFitBars(true)
            chart.animateY(1000)
            chart.invalidate()
        }
    )
}