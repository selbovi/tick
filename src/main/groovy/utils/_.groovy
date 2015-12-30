package utils

import candle.Candle
import groovy.io.FileType

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class _ {
    def static moscowZone = ZoneId.of("Europe/Moscow")
    def static estZone = ZoneId.of(ZoneId.SHORT_IDS.get("EST"))
    def static serverZone = ZoneId.of("GMT+1")
    def static FX4U_ZONE = ZoneId.of("GMT+2")

    def static TICK_DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("uuuuMMdd HHmmssSSS")
    def static CANDLE_DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("uuuuMMdd HHmmss")
    def static FX4U_CANDLE_DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("uuuu.MM.dd,HH:mm")

    static List<File> getCsvFiles(File dir) {
        def csvFiles = []
        dir.eachFileRecurse(FileType.ANY) { file ->
            if (file.name.endsWith(".csv")) {
                println file.name
                csvFiles << file
            }
        }
        csvFiles
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
}
