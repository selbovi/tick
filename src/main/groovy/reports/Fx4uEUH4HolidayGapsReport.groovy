package reports

import candle.Candle
import provider.DataProvider
import utils._

import java.time.DayOfWeek
import java.time.Duration

/**
 * Created by SMufazzalov on 30.12.2015.
 */
class Fx4uEUH4HolidayGapsReport {

    def static _print(Set<Map.Entry<Candle, Candle>> set, int pts) {
        println """всего ${set.size()} из них > ${pts}pts ${
            def count = 0
            set.each {
                if (Math.abs(it.key.close - it.value.open)*10_000 >= pts){
                    count++
                }
            }
            count
        }"""
    }

    public static void main(String[] args) {
        def candles = DataProvider.candlesFx4uEUh4()
        println "Всего свечей ${candles.size()}"
        def fds = _.dateCandle(candles, DayOfWeek.FRIDAY)
        println """пятниц найдено ${fds.keySet().size()}"""
        def mds = _.dateCandle(candles, DayOfWeek.MONDAY)
        println """понедельников найдено ${mds.keySet().size()}"""

        def fridays = []
        def mondays = []
        mds.entrySet().each {
            mondays << it.value.first()
        }
        fds.entrySet().each {
            fridays << it.value.last()
        }
        Map<Candle, Candle> kv = new HashMap<>()
        mondays.each { s ->
            fridays.each { f ->
                if (Math.abs(Duration.between(s.time, f.time).toDays()) == 2) {
                    kv.put(f, s)
                    return true
                }
            }
        }

        println """найдено выходных всего ${kv.size()}"""
        println ""

        kv.entrySet().each {
            println """${it.key.close} ${it.value.open} pts ${(Math.abs(it.key.close - it.value.open)*10_000).intValue()}"""
        }

        println ""
        def pts = [10, 20, 30, 40, 50, 100]
        pts.each {
            _print(kv.entrySet(), it)
        }
    }
}
