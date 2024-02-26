package com.example.sorting

import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.example.sorting.databinding.ActivityMainBinding
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.FileNotFoundException
import java.io.OutputStreamWriter
import java.util.Scanner

class MainActivity : AppCompatActivity() {

    private lateinit var mainBinding: ActivityMainBinding
    private var numbers = mutableListOf<Int>()
    private var animation: Animation? = null

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        val view = mainBinding.root
        setContentView(view)

        animation = AnimationUtils.loadAnimation(this, R.anim.bounce_animation)

        try {
            val file = application.assets.open("retrieveData.txt")
            val scanner = Scanner(file)

            while (scanner.hasNextInt()) {
                val number = scanner.nextInt()
                numbers.add(number)
            }
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }

        mainBinding.buttonBubbleSort.setOnClickListener {
            mainBinding.ball.startAnimation(animation)
            GlobalScope.launch(Dispatchers.IO) {
                numbers = bubbleSort(numbers)
                mainBinding.textViewData.text = numbers.toString()
            }
        }

        mainBinding.buttonMergeSort.setOnClickListener {

        }

    }

    private fun bubbleSort(numbers: MutableList<Int>): MutableList<Int> {

        val n = numbers.size

        for (i in 0 until n - 1) {
            for (j in 0 until n - i - 1) {
                if (numbers[j] > numbers[j + 1]) {
                    // Swap numbers[j] and numbers[j + 1]
                    val temp = numbers[j]
                    numbers[j] = numbers[j + 1]
                    numbers[j + 1] = temp
                }
            }
        }
        writeDataToFile()
        return numbers
    }

    private fun writeDataToFile() {
        try {
            val fileOutput = openFileOutput("saveData.txt", MODE_PRIVATE)
            val outputWriter = OutputStreamWriter(fileOutput)
            outputWriter.write(numbers.toString())
            outputWriter.close()

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


}