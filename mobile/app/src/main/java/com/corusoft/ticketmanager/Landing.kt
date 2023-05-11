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
            20f, 80f, 100f
        )
        private val horizontalBarSet = listOf(
            "PORRO" to 5F, "FUSCE" to 6.4F, "EGET" to 3F
        )
        private const val animationDuration = 1000L
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing)

        datosCorrutina()

        val donutChartView: DonutChartView = findViewById(R.id.donutChart)
        val barChartView: HorizontalBarChartView = findViewById(R.id.barChartHorizontal)

        donutChartView.donutColors = intArrayOf(
            Color.parseColor("#FFFFFF"),
            Color.parseColor("#9EFFFFFF"),
            Color.parseColor("#8DFFFFFF")
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
