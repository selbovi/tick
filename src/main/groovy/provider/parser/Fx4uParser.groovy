package provider.parser

import utils._

import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

/**
 * Created by SMufazzalov on 30.12.2015.
 *
 * 2015.07.13,07:00,1.11330,1.11491,1.11320,1.11424,2607
 */
class Fx4uParser implements CandleParserInterface{

    def static DELIMETR = ','

    @Override
    ZonedDateTime getTime(String line) {
        def arr = line.tokenize(DELIMETR)
        def ldt = LocalDateTime.parse("""${arr[0]},${arr[1]}""", _.FX4U_CANDLE_DATE_TIME_FORMAT)
        return ZonedDateTime.of(ldt, _.FX4U_ZONE)
    }

    @Override
    double getOpen(String line) {
        def arr = line.tokenize(DELIMETR)
        return Double.parseDouble(arr[2])
    }

    @Override
    double getHigh(String line) {
        def arr = line.tokenize(DELIMETR)
        return Double.parseDouble(arr[3])
    }

    @Override
    double getLow(String line) {
        def arr = line.tokenize(DELIMETR)
        return Double.parseDouble(arr[4])
    }

    @Override
    double getClose(String line) {
        def arr = line.tokenize(DELIMETR)
        return Double.parseDouble(arr[5])
    }

    @Override
    int getVolume(String line) {
        def arr = line.tokenize(DELIMETR)
        return Double.parseDouble(arr[6])
    }
}
