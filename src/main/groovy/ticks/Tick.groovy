package ticks

import utils.TickUtil
import utils._

import java.time.LocalDateTime
import java.time.ZonedDateTime

/**
 * Created by SMufazzalov on 25.12.2015.
 */
class Tick {

    ZonedDateTime time
    double bid
    double ask
    int volume

    Tick(String line) {
        def arr = line.tokenize(',')

        def ldt = LocalDateTime.parse(arr[0], _.TICK_DATE_TIME_FORMAT)
        time = ZonedDateTime.of(ldt, _.estZone)
        bid = Double.parseDouble(arr[1])
        ask = Double.parseDouble(arr[2])
        volume = Double.parseDouble(arr[3])
    }

    def static void main(String[] args) {
        TickUtil.findGaps(TickUtil.dirTest)
    }

    def server() {
        """${time.withZoneSameInstant(_.serverZone).toLocalDateTime()} in Server"""
    }

    def moscow() {
        """${time.withZoneSameInstant(_.moscowZone).toLocalDateTime()} in MSC"""
    }

    def est() {
        """${time.toLocalDateTime()} in EST"""
    }
}
