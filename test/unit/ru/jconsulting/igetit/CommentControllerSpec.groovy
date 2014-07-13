package ru.jconsulting.igetit

import grails.plugin.springsecurity.SpringSecurityService
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification

@TestFor(CommentController)
@Mock([Buy, Person, Price, Comment])
class CommentControllerSpec extends Specification {

    Person user
    Buy buy

    def setup() {
        Person.metaClass.encodePassword { -> }
        user = new Person(username: 'user', password: 'pwd').save(flush: true)
        def p = new Price(value: new BigDecimal(1), currency: Currency.getInstance('USD'))
        buy = new Buy(name: 'buy', owner: user, price: p, created: new Date()).save(flush: true)
        def buy2 = new Buy(name: 'buy2', owner: user, price: p, created: new Date()).save(flush: true)
        assert Buy.count() == 2
        new Comment(created: new Date(), text: 'comment1', author: user, buy: buy).save(flush: true)
        new Comment(created: new Date(), text: 'comment2', author: user, buy: buy2).save(flush: true)
        assert Comment.count() == 2
        def springSecurityServiceMock = mockFor(SpringSecurityService)
        springSecurityServiceMock.demand.getCurrentUser { -> user }
        controller.springSecurityService = springSecurityServiceMock.createMock()
    }

    void "test list all buys' comments"() {
        when:
        controller.index()
        then:
        response.json.size() == 2
    }

    void "test list current buy's comments"() {
        given:
        params.buyId = buy.id
        when:
        controller.index()
        then:
        response.json.size() == 1
        response.json.text == ['comment1']
    }

    void "test current user add comment to the buy"() {
        given:
        params.buyId = buy.id
        params.text = 'new comment'
        when:
        controller.save()
        then:
        response.json.text == 'new comment'
        response.json.author.id == user.id
        response.json.buy.id == buy.id
        Comment.count() == 3
    }

    void "test add comment to an undefined buy"() {
        given:
        params.text = 'new comment'
        when:
        controller.save()
        then:
        response.status == 422
        response.json.errors.size() == 3
        Comment.count() == 2
    }
}
