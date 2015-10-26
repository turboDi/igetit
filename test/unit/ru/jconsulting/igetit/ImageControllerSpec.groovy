package ru.jconsulting.igetit

import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import org.springframework.security.access.AccessDeniedException
import spock.lang.Specification

/**
 *
 *
 * @author Dmitriy Borisov
 * @created 18.10.14 21:33
 */
@TestFor(ImageController)
@Mock([Buy, Person, Price, Image])
class ImageControllerSpec extends Specification {

    Person user
    Buy buy, buy2

    def setup() {
        Person.metaClass.encodePassword { -> }
        Person.metaClass.accountService = [ isPasswordValid: { p, e -> true } ]
        user = new Person(username: 'user@ww.ww', fullName: 'FIO', password: 'pwd').save(flush: true, failOnError: true)
        Person user2 = new Person(username: 'user2@ww.ww', fullName: 'FIO', password: 'pwd').save(flush: true, failOnError: true)
        def p = new Price(value: new BigDecimal(1), currency: Currency.getInstance('USD'))
        buy = new Buy(name: 'buy', owner: user, price: p, images: [new Image(filename: '1', folderId: '1'), new Image(filename: '2', folderId: '2')])
                .save(flush: true, failOnError: true)
        buy2 = new Buy(name: 'buy2', owner: user2, price: p, images: [new Image(filename: 'a', folderId: 'a'), new Image(filename: 'b', folderId: 'b')])
                .save(flush: true, failOnError: true)
        controller.metaClass.getAuthenticatedUser = { -> user }
        controller.params.format = 'json'
    }

    void "test undefined folder save" () {
        given:
        params.filename = '123'
        when:
        controller.save()
        then:
        response.status == 422
    }

    void "test add to buy" () {
        given:
        params.buyId = buy.id
        params.filename = '3'
        params.folderId = '3'
        when:
        controller.save()
        then:
        Image.count() == 5
        buy.images.size() == 3
        buy.images[2].filename == '3'
        buy.images[2].folderId == '3'
        response.status == 201
        response.json.filename == '3'
        response.json.folderId == '3'
    }

    void "test undefined delete" () {
        when:
        controller.delete()
        then:
        response.status == 404
    }

    void "test delete" () {
        given:
        params.buyId = buy.id
        params.id = buy.images[0].id
        when:
        controller.delete()
        then:
        response.status == 204
        buy.images.size() == 1
        buy.images[0].filename == '2'
        buy.images[0].folderId == '2'
    }

    void "test add image to another's buy"() {
        given:
        params.buyId = buy2.id
        params.filename = '5'
        params.folderId = '5'
        when:
        controller.save()
        then:
        thrown(AccessDeniedException)
    }

    void "test delete another's image"() {
        given:
        params.buyId = buy2.id
        params.id = buy2.images[0].id
        when:
        controller.delete()
        then:
        thrown(AccessDeniedException)
    }
}
