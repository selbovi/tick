package ticks

import groovy.io.FileType
import utils.TickUtil

import java.time.Duration
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

/**
 * Created by SMufazzalov on 25.12.2015.
 */
class Tick {
    def moscowZone = ZoneId.of("Europe/Moscow")
    def estZone = ZoneId.of(ZoneId.SHORT_IDS.get("EST"))
    def serverZone = ZoneId.of("GMT+1")

    ZonedDateTime time
    double bid
    double ask
    int volume

    Tick(String line){
        def arr = line.tokenize(',')

        def ldt = LocalDateTime.parse(arr[0], DateTimeFormatter.ofPattern("uuuuMMdd HHmmssSSS"))
        time = ZonedDateTime.of(ldt, estZone)
        bid = Double.parseDouble(arr[1])
        ask = Double.parseDouble(arr[2])
        volume = Double.parseDouble(arr[3])
    }

    def static void main(String[] args) {
        TickUtil.findGaps(TickUtil.dirTest)
    }

    def server() {
        """${ time.withZoneSameInstant(serverZone).toLocalDateTime()} in Server"""
    }

    def moscow() {
        """${ time.withZoneSameInstant(moscowZone).toLocalDateTime()} in MSC"""
    }

    def est() {
        """${time.toLocalDateTime()} in EST"""
    }
}
