package candle

import provider.parser.CandleParserInterface
import utils.TickUtil
import utils._

import java.time.LocalDateTime
import java.time.ZonedDateTime

/**
 * Created by SMufazzalov on 29.12.2015.
 */
class Candle {

    ZonedDateTime time
    double open
    double high
    double low
    double close
    int volume

    @Deprecated
    Candle(String line) {
        def arr = line.tokenize(';')

        def ldt = LocalDateTime.parse(arr[0], _.CANDLE_DATE_TIME_FORMAT)
        time = ZonedDateTime.of(ldt, _.estZone)
        open = Double.parseDouble(arr[1])
        high = Double.parseDouble(arr[2])
        low = Double.parseDouble(arr[3])
        close = Double.parseDouble(arr[4])
        volume = Double.parseDouble(arr[5])
    }

    Candle(String line, CandleParserInterface parser) {
        assert line != null
        assert parser != null

        time = parser.getTime(line)
        open = parser.getOpen(line)
        high = parser.getHigh(line)
        low = parser.getLow(line)
        close = parser.getClose(line)
        volume = parser.getVolume(line)
    }

    def static COMPARATOR = new Comparator<Candle>() {
        @Override
        int compare(Candle o1, Candle o2) {
            o1.time.compareTo(o2.time)
        }
    }

    public static void main(String[] args) {
        TickUtil.getCsvFiles(TickUtil.DIR_TEST_CANDLES).forEach {
            TickUtil.getCandlesFromFile(it)
        }

    }
}