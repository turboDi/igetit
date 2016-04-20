package ru.jconsulting.igetit

import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import org.springframework.security.access.AccessDeniedException
import spock.lang.Specification

@TestFor(CommentController)
@Mock([Buy, Person, Price, Comment])
class CommentControllerSpec extends Specification {

    Comment comment
    Person user
    Buy buy

    def setup() {
        Person.metaClass.encodePassword { -> }
        user = new Person(username: 'user@ww.ww', fullName: 'FIO', password: 'pwd').save(flush: true, failOnError: true)
        Person user2 = new Person(username: 'user2@ww.ww', fullName: 'FIO', password: 'pwd').save(flush: true, failOnError: true)
        def p = new Price(value: new BigDecimal(1), currency: Currency.getInstance('USD'))
        buy = new Buy(name: 'buy', owner: user, price: p).save(flush: true, failOnError: true)
        def buy2 = new Buy(name: 'buy2', owner: user, price: p).save(flush: true, failOnError: true)
        assert Buy.count() == 2
        new Comment(text: 'comment1', author: user, buy: buy).save(flush: true, failOnError: true)
        new Comment(text: 'comment2', author: user, buy: buy2).save(flush: true, failOnError: true)
        comment = new Comment(text: 'comment3', author: user2, buy: buy2).save(flush: true, failOnError: true)
        assert Comment.count() == 3
        controller.metaClass.getAuthenticatedUser = { -> user }
        controller.params.format = 'json'
    }

    void "test list all buys' comments"() {
        when:
        controller.index()
        then:
        thrown(IllegalStateException)
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
        Comment.count() == 4
    }

    void "test add comment to an undefined buy"() {
        given:
        params.text = 'new comment'
        when:
        controller.save()
        then:
        response.status == 422
        response.json.errors.size() == 2
        Comment.count() == 3
    }

    void "test delete another's comment"() {
        given:
        params.id = comment.id
        when:
        controller.delete()
        then:
        thrown(AccessDeniedException)
    }

    void "test update another's comment"() {
        given:
        params.id = comment.id
        params.text = 'comment3 changed'
        when:
        controller.update()
        then:
        thrown(AccessDeniedException)
    }
}
