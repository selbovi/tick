package utils

import groovy.io.FileType
import ticks.Tick

import java.time.DayOfWeek
import java.time.Duration

/**
 * Created by SMufazzalov on 28.12.2015.
 */
class TickUtil {

    def static YEAR_2015_TICKS = '../2015/ticks'
    def static YEAR_2015_CANDLES_ASCII_EURUSD_M1 = '../2015/candles/HISTDATA_COM_ASCII_EURUSD_M1'
    def static TEST_DIR = '../test'
    def static dir2015 = new File(Tick.getResource(YEAR_2015_TICKS).path)
    def static dirTest = new File(Tick.getResource(TEST_DIR).path)

    def static cmp = new Comparator<Tick>() {

        @Override
        int compare(Tick o1, Tick o2) {
            return o1.time.compareTo(o2.time)
        }
    }

    static List<Tick> getTicksFor2015() {

        def csvFiles = getCsvFiles(dir2015)

        println "files found " + csvFiles.size()

        LinkedList ticks = []
        csvFiles.forEach { file ->
            def month = getTicksFromFile(file)
            ticks.addAll(month)
        }

        println "unsorted list for " + YEAR_2015_TICKS + " contains total " + ticks.size + "ticks"
        ticks.sort(cmp)

        ticks
    }

    static List<Tick> getTicksFromFile(File file) {
        LinkedList ticks = []
        println "parsing file - " + file.name
        file.eachLine { line ->
            ticks << new Tick(line)
        }
        println "file " + file.name + " contains " + ticks.size() + " ticks"
        ticks.toSorted(cmp)
    }

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

    static void findGaps(File dir) {
        Map<String, List<Tick>> sundays = new HashMap<>()
        Map<String, List<Tick>> fridays = new HashMap<>()
        getCsvFiles(dir).forEach {
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

        def sds  = []
        sundays.forEach {
            stamp, list ->
                def firstTick = list.first()
                sds.add(firstTick)
        }
        def fds  = []
        fridays.forEach {
            stamp, list ->
                def lastTick = list.last()
                fds.add(lastTick)
        }

        Map <Tick, Tick> kv = new HashMap<>()
        fds.forEach {
            f ->
                sds.forEach {
                    s ->
                        if (Math.abs(Duration.between(s.time, f.time).toDays()) == 2) {
                            kv.put(f,s)
                            return true
                        }
                }
        }

        kv.entrySet().forEach { e->
            println (e.key.bid.toString() + " " + e.value.bid.toString() + " pts " + Math.abs(e.key.bid - e.value.bid)*10_000)
            println "Закрытие в " + e.key.moscow()
            println "Открытие в " + e.value.moscow()
            println "Закрытие в " + e.key.est()
            println "Открытие в " + e.value.est()
            println "Закрытие в " + e.key.server()
            println "Открытие в " + e.value.server()
        }
    }
}
