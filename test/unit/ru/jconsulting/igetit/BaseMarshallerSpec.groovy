package ru.jconsulting.igetit

import ru.jconsulting.igetit.marshallers.BaseMarshaller
import spock.lang.Specification

import java.text.SimpleDateFormat

class BaseMarshallerSpec extends Specification {

    BaseMarshaller marshaller
    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy")

    def setup() {
        marshaller = [:] as BaseMarshaller
    }

    void "test marshallDate" () {
        when:
        def now = new Date()
        def yearAgo = now - 365
        def monthAgo = now - 31
        def yesterday = now - 1
        then:
        marshaller.marshallDate(yearAgo) == yearAgo.format("dd MMM yyyy")
        marshaller.marshallDate(monthAgo) == monthAgo.format("dd MMM 'at' hh:mm")
        marshaller.marshallDate(yesterday) == yesterday.format("dd MMM 'at' hh:mm")
        marshaller.marshallDate(now) == "just now"
    }
}
