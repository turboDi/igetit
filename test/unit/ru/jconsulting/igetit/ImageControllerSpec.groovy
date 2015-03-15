package ru.jconsulting.igetit

import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 *
 *
 * @author Дмитрий Борисов
 * @created 18.10.14 21:33
 */
@TestFor(ImageController)
@Mock([Buy, Person, Price, Image])
class ImageControllerSpec extends Specification {

    Person user
    Buy buy

    def setup() {
        Person.metaClass.encodePassword { -> }
        user = new Person(username: 'user@ww.ww', fullName: 'FIO', password: 'pwd').save(flush: true, failOnError: true)
        def p = new Price(value: new BigDecimal(1), currency: Currency.getInstance('USD'))
        buy = new Buy(name: 'buy', owner: user, price: p, images: [new Image(filename: '1', folderId: '1'), new Image(filename: '2', folderId: '2')])
                .save(flush: true, failOnError: true)
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
        Image.count() == 3
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
}
