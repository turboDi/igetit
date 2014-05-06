import ru.jconsulting.igetit.Brand
import ru.jconsulting.igetit.Buy
import ru.jconsulting.igetit.Category as Category
import ru.jconsulting.igetit.Comment
import ru.jconsulting.igetit.Image
import ru.jconsulting.igetit.Price
import ru.jconsulting.igetit.auth.Role
import ru.jconsulting.igetit.auth.User
import ru.jconsulting.igetit.auth.UserRole

import java.text.SimpleDateFormat

class BootStrap {

    def customMarshallerRegistrar

    def init = { servletContext ->
        if (Brand.count() == 0) createData()
        customMarshallerRegistrar.register()
    }
    def destroy = {
    }

    private static void createData() {
        def apple = new Brand(name: 'Apple').save(flush: true)
        def smak = new Brand(name: 'Smakhleb').save(flush: true)

        def electronics = new Category(name: 'Electronics').save(flush: true)
        def food = new Category(name: 'Food').save(flush: true)

        def admin = new Role(authority: 'ROLE_USER').save(flush: true)
        def turbodi = new User(username: 'turbo_di', password: '1qazxsw2').save(flush: true)
        def potapovdd = new User(username: 'potapovdd', password: '1qazxsw2').save(flush: true)

        UserRole.create turbodi, admin, true
        UserRole.create potapovdd, admin, true

        def hostedImgLink = "https://googledrive.com/host/0B9Cfh7FJdsBUMWRob1hQVnUwZGc/"

        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy")

        new Buy(name: 'iPad air 32 gb WI-FI', brand: apple, category: electronics, owner: turbodi,
                price: new Price(value: new BigDecimal(499.99), currency: Currency.getInstance('USD')),
                image: new Image(filename: 'ipad.jpg', path: hostedImgLink + 'ipad.jpg'),
                created: sdf.parse("12.12.2013")).save(failOnError: true)
        def iphone = new Buy(name: 'iPhone 5', brand: apple, category: electronics, owner: turbodi,
                price: new Price(value: new BigDecimal(799.99), currency: Currency.getInstance('USD')),
                image: new Image(filename: 'iphone.jpg', path: hostedImgLink + 'iphone.jpg'),
                created: sdf.parse("12.01.2014")).save(failOnError: true)
        def cookie = new Buy(name: 'Lime cookie', brand: smak, category: food, owner: turbodi,
                price: new Price(value: new BigDecimal(56.99), currency: Currency.getInstance('RUB')),
                image: new Image(filename: 'Limonnaya_big.jpg', path: hostedImgLink + 'Limonnaya_big.jpg'),
                created: sdf.parse("12.12.2013"), description: 'Eat more of these soft French rolls, but drink tea.')
                .save(failOnError: true)
        new Buy(name: 'iPod', brand: apple, category: electronics, owner: potapovdd,
                price: new Price(value: new BigDecimal(49.99), currency: Currency.getInstance('USD')),
                image: new Image(filename: 'ipod.jpg', path: hostedImgLink + 'ipod.jpg'),
                created: new Date()).save(failOnError: true)
        new Buy(name: 'light idea bread', brand: smak, category: food, owner: potapovdd,
                price: new Price(value: new BigDecimal(29.70), currency: Currency.getInstance('RUB')),
                image: new Image(filename: 'Idea_light1.jpg', path: hostedImgLink + 'Idea_light1.jpg'),
                created: new Date()).save(failOnError: true)

        cookie.addToComments(new Comment(created: sdf.parse("13.12.2013"), author: potapovdd,
        text: "As for me i don't like such harmful for my health things.\nI like more healthy food, how do you keep such a great shape while eating this junk?"))
        cookie.addToComments(new Comment(created: sdf.parse("14.12.2013"), author: turbodi,
        text: "Eat more of these soft French rolls, but drink tea."))

        cookie.rate(potapovdd, -1)
        cookie.rate(turbodi, 5)
        iphone.rate(potapovdd, 1)

        potapovdd.addToFollowed(turbodi)
    }
}
