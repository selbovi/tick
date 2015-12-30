package utils

import groovy.io.FileType

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

}
