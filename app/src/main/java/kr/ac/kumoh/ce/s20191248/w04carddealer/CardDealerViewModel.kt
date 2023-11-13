package kr.ac.kumoh.ce.s20191248.w04carddealer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlin.random.Random

class CardDealerViewModel : ViewModel() {
    private var _cards = MutableLiveData(IntArray(5) {-1})
    val cards : LiveData<IntArray>
        get() = _cards

    fun shuffle() {
        var num = 0
        val newCards = IntArray(5) {-1}

        for(i in newCards.indices){
            do {
                num = Random.nextInt(52)
            }while (num in newCards)
            newCards[i] = num
        }
        newCards.sort()
        _cards.value = newCards     //옵저버가 보고 데이터가 수정된 것을 확인할수 있게 해줌
    }

}