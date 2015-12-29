import spock.lang.Specification

/**
 * Created by salavat on 26.07.2015.
 */
class FirstSpec extends Specification {

    def "lets test it" () {
        expect:
        a == false

        where:
        a = false
    }
}
