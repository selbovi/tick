package provider.parser;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by SMufazzalov on 30.12.2015.
 */
interface CandleParserInterface {
    ZonedDateTime getTime(String line);
    double getOpen(String line);
    double getHigh(String line);
    double getLow(String line);
    double getClose(String line);
    int getVolume(String line);
}
