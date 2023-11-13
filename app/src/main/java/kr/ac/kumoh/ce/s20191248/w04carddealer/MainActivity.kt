package kr.ac.kumoh.ce.s20191248.w04carddealer

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kr.ac.kumoh.ce.s20191248.w04carddealer.databinding.ActivityMainBinding
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    private lateinit var main: ActivityMainBinding
    private lateinit var model: CardDealerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        main = ActivityMainBinding.inflate(layoutInflater)
        setContentView(main.root)

        model = ViewModelProvider(this)[CardDealerViewModel::class.java]
        model.cards.observe(this, Observer {
            val res = IntArray(5)
            for (i in it.indices) {
                res[i] = resources.getIdentifier(
                    getCardName(it[i]),
                    "drawable",
                    packageName
                )
            }

            main.card1.setImageResource(res[0])
            main.card2.setImageResource(res[1])
            main.card3.setImageResource(res[2])
            main.card4.setImageResource(res[3])
            main.card5.setImageResource(res[4])

            val shapecount = IntArray(4)
            val numbercount = IntArray(13)

            main.grade.text = getHandRank(it, shapecount, numbercount)

            if(it[0] == -1){
                main.grade.text = "Good Luck"
            }
        })

        main.btnShuffle.setOnClickListener {
            model.shuffle()
        }
    }

    private fun getCardName(c: Int): String {
        var shape = when (c / 13) {
            0 -> "spades"
            1 -> "diamonds"
            2 -> "hearts"
            3 -> "clubs"
            else -> "error"
        }

        val number = when (c % 13) {
            0 -> "ace"
            in 1..9 -> (c % 13 + 1).toString()
            10 -> {
                shape = shape.plus("2")
                "jack"
            }
            11 -> {
                shape = shape.plus("2")
                "queen"
            }
            12 -> {
                shape = shape.plus("2")
                "king"
            }
            else -> "error"
        }

        if (c == -1){
            return "c_red_joker"
        }

        return "c_${number}_of_${shape}"
    }

    private fun getHandRank(cards: IntArray, shapecount: IntArray, numbercount: IntArray): String {
        var grade = ""
        var flush = false
        var pair = 0
        var triple = 0
        var straight = 0
        var fourcard = false
        var backstraight = false
        var mountain = false

        for (c in cards) {
            when (c / 13) {
                0 -> shapecount[0]++
                1 -> shapecount[1]++
                2 -> shapecount[2]++
                3 -> shapecount[3]++
            }
            when (c % 13) {
                0 -> numbercount[0]++
                in 1..9 -> numbercount[c % 13]++
                10 -> numbercount[10]++
                11 -> numbercount[11]++
                12 -> numbercount[12]++
            }
        }

        for (i in 0 until 4) {
            if (shapecount[i] == 5)
                flush = true
        }

        for (i in 0 until 13) {
            when {
                numbercount[i] == 0 -> straight = 0
                numbercount[i] == 1 -> straight++
                numbercount[i] == 2 -> {
                    pair++
                    straight++
                }
                numbercount[i] == 3 -> triple++
                numbercount[i] == 4 -> fourcard = true
            }
        }
        if(numbercount[0] == 1 && numbercount[12] == 1 && numbercount[11] == 1 && numbercount[10] == 1 && numbercount[9] == 1){
            mountain = true
        }
        if(numbercount[0] == 1 && numbercount[1] == 1 && numbercount[2] == 1 && numbercount[3] == 1 && numbercount[4] == 1){
            backstraight = true
        }

        if (mountain && flush)
            grade = "로열 스트레이트 플러시"
        else if (backstraight && flush)
            grade = "백 스트레이트 플러시"
        else if (flush && straight == 5)
            grade = "스트레이트 플러시"
        else if (fourcard)
            grade = "포 카드"
        else if (pair == 1 && triple == 1)
            grade = "풀하우스"
        else if (flush)
            grade = "플러시"
        else if (mountain) {
            grade = "마운틴"
        } else if (backstraight) {
            grade = "백 스트레이트"
        } else if (straight == 5)
            grade = "스트레이트"
        else if (pair == 0 && triple == 1)
            grade = "트리플"
        else if (pair == 2 && triple == 0 )
            grade = "투 페어"
        else if (pair == 1 && triple == 0)
            grade = "원 페어"
        else if (pair == 0 && triple == 0)
            grade = "탑"

        return grade
    }
}
