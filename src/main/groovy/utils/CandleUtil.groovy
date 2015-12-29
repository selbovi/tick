package utils

import candle.Candle

import java.time.DayOfWeek
import java.time.Duration
import java.time.LocalDate

/**
 * Created by SMufazzalov on 29.12.2015.
 */
class CandleUtil {

    def static TEST_CANDLE_DIR = '../test/candle'
    def static DIR_TEST_CANDLES = new File(CandleUtil.getResource(TEST_CANDLE_DIR).path)
    def static YEAR_2015_CANDLES_ASCII_EURUSD_M1 = '../2015/candles/HISTDATA_COM_ASCII_EURUSD_M1'
    def static DIR_2015_CANDLES = new File(CandleUtil.getResource(YEAR_2015_CANDLES_ASCII_EURUSD_M1).path)

    def calcGaps(File dir) {
        def fridays = []
        def sundays = []
        _.getCsvFiles(dir).forEach {
            def candles = getCandlesFromFile(it)
            def sds = dateCandle(candles, DayOfWeek.SUNDAY)
            def fds = dateCandle(candles, DayOfWeek.FRIDAY)

            sds.entrySet().each {
                sundays << it.value.last()
            }
            fds.entrySet().each {
                fridays << it.value.last()
            }
        }
        Map<Candle, Candle> kv = new HashMap<>()
        sundays.each { s ->
            fridays.each { f ->
                if (Math.abs(Duration.between(s.time, f.time).toDays()) == 2) {
                    kv.put(f, s)
                    return true
                }
            }
        }

        kv.entrySet().forEach { e ->
            println """${e.key.close} ${e.value.open} pts ${Math.abs(e.key.close - e.value.open)*10_000}"""
        }

        println """выходных ${kv.entrySet().size()}"""
        _print(kv.entrySet(), 10)
        _print(kv.entrySet(), 20)
        _print(kv.entrySet(), 30)
        _print(kv.entrySet(), 40)
        _print(kv.entrySet(), 50)
        _print(kv.entrySet(), 100)

    }

    def _print(Set<Map.Entry<Candle, Candle>> set, int pts) {
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
    def static cmpCandle = new Comparator<Candle>() {
        @Override
        int compare(Candle o1, Candle o2) {
            o1.time.compareTo(o2.time)
        }
    }

    static List<Candle> getCandlesFromFile(File file) {
        LinkedList cadles = []
        println "parsing file - " + file.name
        file.eachLine { line ->
            cadles << new Candle(line)
        }
        println """file ${file.name} contains ${cadles.size()} candles"""
        cadles.toSorted(cmpCandle)
    }

    static Map<LocalDate, List<Candle>> dateCandle(List<Candle> candles, DayOfWeek... filter) {
        Map<LocalDate, List<Candle>> map = new HashMap<>()
        candles.each {
            if (filter.contains(it.time.dayOfWeek)) {
                map.computeIfAbsent(it.time.toLocalDate(), {
                    []
                })
                map.get(it.time.toLocalDate()).add(it)
            }
        }
        map
    }

    public static void main(String[] args) {
        (new CandleUtil()).calcGaps(DIR_2015_CANDLES)
    }
}
