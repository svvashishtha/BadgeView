package com.example.badgeview

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    var rank = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val t = Timer()
        t.scheduleAtFixedRate(
            object : TimerTask() {

                override fun run() {
                    badge.setRank(rank.toString())
                    rank++
                    if (rank > 30)
                        t.cancel()
                }

            },
            0,
            1000
        )

    }
}
