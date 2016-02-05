package reports

import provider.DataProvider
import utils.CandleUtil
import utils._

import java.text.DecimalFormat
import java.time.DayOfWeek

/**
 * Created by SMufazzalov on 04.02.2016.
 */
class FriLast2HoursVolatile {

    def static YEAR_2015_CANDLES_ASCII_EURUSD_M1 = '../2015/candles/HISTDATA_COM_ASCII_EURUSD_M1'
    def static DIR_2015_CANDLES = new File(CandleUtil.getResource(YEAR_2015_CANDLES_ASCII_EURUSD_M1).path)

    static void main(String[] args) {
        def df = new DecimalFormat("#")
        df.setMaximumFractionDigits(0);

        def candles = new ArrayList()
        _.getCsvFiles(DIR_2015_CANDLES).forEach {
            candles.addAll CandleUtil.getCandlesFromFile(it)
        }
        println "Всего свечей ${candles.size()}"
        def fds = _.dateCandle(candles, DayOfWeek.FRIDAY)
        println """пятниц найдено ${fds.keySet().size()}"""
        fds.values().each {
            it ->
                def start = getStartTime(it)
                println start.time
                println it.last().time

                def diff = (getMax(it) - start.open) / 0.00001
                println """${getMax(it)}  ${df.format(diff)}"""
                println start.open
                diff = (start.open - getMin(it)) / 0.00001
                println """${getMin(it)}  ${df.format(diff)}"""
        }
    }

    static getStartTime(candles) {
        def twoHbefore = candles.last().time.minusHours(2)
        candles.find { it -> twoHbefore < it.time }
    }

    static getMax(candles) {
        def twoHbefore = candles.last().time.minusHours(2)
        candles.findAll { it -> twoHbefore < it.time }.max { it.high }.high
    }

    static getMin(candles) {
        def twoHbefore = candles.last().time.minusHours(2)
        candles.findAll { it -> twoHbefore < it.time }.min { it.low }.low
    }
}
