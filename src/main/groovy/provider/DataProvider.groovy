package provider

import candle.Candle
import provider.parser.Fx4uParser

/**
 * Created by SMufazzalov on 30.12.2015.
 */
class DataProvider {

    def static FX4U_EU_H4 = '../fx4y/eurusd/EURUSD60.csv'
    def static FX4U_EU_H4_CANDLES = new File(DataProvider.getResource(FX4U_EU_H4).path)

    static List<Candle> candlesFx4uEUh4() {
        def res = []
        FX4U_EU_H4_CANDLES.eachLine {
            res << new Candle(it, new Fx4uParser())
        }
        res.toSorted(Candle.COMPARATOR)
    }

    public static void main(String[] args) {
        println candlesFx4uEUh4().size()
    }
}
