package utils

import groovy.io.FileType
import ticks.Tick
import candle.Candle

import java.time.DayOfWeek
import java.time.Duration

/**
 * Created by SMufazzalov on 28.12.2015.
 */
class TickUtil {

    def static YEAR_2015_TICKS = '../2015/ticks'
    def static TEST_TICK_DIR = '../test/tick'

    def static dir2015 = new File(Tick.getResource(YEAR_2015_TICKS).path)
    def static dirTestTick = new File(Tick.getResource(TEST_TICK_DIR).path)

    def static cmpTick = new Comparator<Tick>() {

        @Override
        int compare(Tick o1, Tick o2) {
            o1.time.compareTo(o2.time)
        }
    }

    static List<Tick> getTicksFor2015() {

        def csvFiles = _.getCsvFiles(dir2015)

        println "files found " + csvFiles.size()

        LinkedList ticks = []
        csvFiles.forEach { file ->
            def month = getTicksFromFile(file)
            ticks.addAll(month)
        }

        println "unsorted list for " + YEAR_2015_TICKS + " contains total " + ticks.size + "ticks"
        ticks.sort(cmpTick)

        ticks
    }

    static List<Tick> getTicksFromFile(File file) {
        LinkedList ticks = []
        println "parsing file - " + file.name
        file.eachLine { line ->
            ticks << new Tick(line)
        }
        println "file " + file.name + " contains " + ticks.size() + " ticks"
        ticks.toSorted(cmpTick)
    }

    static void findGaps(File dir) {
        Map<String, List<Tick>> sundays = new HashMap<>()
        Map<String, List<Tick>> fridays = new HashMap<>()
        _.getCsvFiles(dir).forEach {
            file ->
                def tcs = getTicksFromFile(file)
                tcs.forEach { t ->
                    if (t.time.dayOfWeek == DayOfWeek.SUNDAY) {
                        sundays.computeIfAbsent(t.time.toLocalDate().toString(), {
                            []
                        })
                        sundays.get(t.time.toLocalDate().toString()).add(t)

                    } else if (t.time.dayOfWeek == DayOfWeek.FRIDAY) {
                        fridays.computeIfAbsent(t.time.toLocalDate().toString(), {
                            []
                        })
                        fridays.get(t.time.toLocalDate().toString()).add(t)

                    }
                }
        }

        def sds = []
        sundays.forEach {
            stamp, list ->
                def firstTick = list.first()
                sds.add(firstTick)
        }
        def fds = []
        fridays.forEach {
            stamp, list ->
                def lastTick = list.last()
                fds.add(lastTick)
        }

        Map<Tick, Tick> kv = new HashMap<>()
        fds.forEach {
            f ->
                sds.forEach {
                    s ->
                        if (Math.abs(Duration.between(s.time, f.time).toDays()) == 2) {
                            kv.put(f, s)
                            return true
                        }
                }
        }

        kv.entrySet().forEach { e ->
            println(e.key.bid.toString() + " " + e.value.bid.toString() + " pts " + Math.abs(e.key.bid - e.value.bid) * 10_000)
            println "Закрытие в " + e.key.moscow()
            println "Открытие в " + e.value.moscow()
           /* println "Закрытие в " + e.key.est()
            println "Открытие в " + e.value.est()
            println "Закрытие в " + e.key.server()
            println "Открытие в " + e.value.server()*/
        }

        _print(kv.entrySet(), 10)
        _print(kv.entrySet(), 20)
        _print(kv.entrySet(), 30)
        _print(kv.entrySet(), 50)
    }

    def static _print(Set<Map.Entry<Tick, Tick>> set, int pts) {
        println """всего ${set.size()} из них > ${pts}pts ${
            def count = 0
            set.each {
                if (Math.abs(it.key.bid - it.value.bid)*10_000 >= pts){
                    count++
                }
            }
            count
        }"""
    }

    public static void main(String[] args) {
        findGaps(dir2015)
    }
}
