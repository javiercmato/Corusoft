package com.corusoft.ticketmanager

import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.db.williamchart.view.DonutChartView
import com.db.williamchart.view.HorizontalBarChartView
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


class Landing : AppCompatActivity() {
    companion object {
        private val donutSet = listOf(
            20F, 80F, 100F
        )
        private val horizontalBarSet = listOf(
            "FOOD" to 85.47F, "SHOPPING" to 62.91F, "ENTERTAINMENT" to 25.93F
        )
        private const val animationDuration = 1500L
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing)

        datosCorrutina()

        val donutChartView: DonutChartView = findViewById(R.id.donutChart)
        val barChartView: HorizontalBarChartView = findViewById(R.id.barChartHorizontal)

        donutChartView.donutColors = intArrayOf(
            Color.parseColor("#7bde3a"),
            Color.parseColor("#5ea62e"),
            Color.parseColor("#3d6b1e")
        )

        donutChartView.animation.duration = animationDuration
        donutChartView.animate(donutSet)

        barChartView.animation.duration = animationDuration
        barChartView.animate(horizontalBarSet)
    }

    private fun datosCorrutina() = runBlocking {
        coroutineScope {
            launch {
                delay(1000L)
                println("Solicitando datos históricos a backend...")
            }
        }
        println("Datos históricos recibidos")
        Toast.makeText(applicationContext, "Datos históricos recibidos", Toast.LENGTH_SHORT).show()
    }

}
