import ru.jconsulting.igetit.Brand
import ru.jconsulting.igetit.Buy
import ru.jconsulting.igetit.Category as Category
import ru.jconsulting.igetit.Comment
import ru.jconsulting.igetit.Image
import ru.jconsulting.igetit.PersonFollower
import ru.jconsulting.igetit.Price
import ru.jconsulting.igetit.Person
import ru.jconsulting.igetit.auth.Role
import ru.jconsulting.igetit.auth.PersonRole

class BootStrap {

    def customMarshallerRegistrar

    def init = { servletContext ->
        if (Brand.count() == 0) createData()
        customMarshallerRegistrar.register()
    }

    private static void createData() {
        def apple = new Brand(name: 'Apple').save(flush: true)
        def smak = new Brand(name: 'Smakhleb').save(flush: true)

        def electronics = new Category(name: 'Electronics').save(flush: true)
        def food = new Category(name: 'Food').save(flush: true)

        def admin = new Role(authority: 'ROLE_USER').save(flush: true)
        def turbodi = new Person(username: 'turbo_di', email: 'ww@ww.ww', password: '1qazxsw2').save(flush: true, failOnError: true)
        def potapovdd = new Person(username: 'potapovdd', email: 'ww@ww1.ww', password: '1qazxsw2').save(flush: true, failOnError: true)

        PersonRole.create turbodi, admin, true
        PersonRole.create potapovdd, admin, true

        def bootstrapFolderId = "0B1lcabZIpE_KY2JEaThKeXRpZUE"
        def bootstrapFileId = "undeletable"

        new Buy(name: 'iPad air 32 gb WI-FI', brand: apple, category: electronics, owner: turbodi,
                price: new Price(value: new BigDecimal(499.99), currency: Currency.getInstance('USD')),
                image: new Image(filename: 'ipad.jpg', folderId: bootstrapFolderId, fileId: bootstrapFileId)).save(failOnError: true)
        def iphone = new Buy(name: 'iPhone 5', brand: apple, category: electronics, owner: turbodi,
                price: new Price(value: new BigDecimal(799.99), currency: Currency.getInstance('USD')),
                image: new Image(filename: 'iphone.jpg', folderId: bootstrapFolderId, fileId: bootstrapFileId)).save(failOnError: true)
        def cookie = new Buy(name: 'Lime cookie', brand: smak, category: food, owner: turbodi,
                price: new Price(value: new BigDecimal(56.99), currency: Currency.getInstance('RUB')),
                image: new Image(filename: 'Limonnaya_big.jpg', folderId: bootstrapFolderId, fileId: bootstrapFileId))
                .save(failOnError: true)
        new Buy(name: 'iPod', brand: apple, category: electronics, owner: potapovdd,
                price: new Price(value: new BigDecimal(49.99), currency: Currency.getInstance('USD')),
                image: new Image(filename: 'ipod.jpg', folderId: bootstrapFolderId, fileId: bootstrapFileId)).save(failOnError: true)
        new Buy(name: 'light idea bread', brand: smak, category: food, owner: potapovdd,
                price: new Price(value: new BigDecimal(29.70), currency: Currency.getInstance('RUB')),
                image: new Image(filename: 'Idea_light1.jpg', folderId: bootstrapFolderId, fileId: bootstrapFileId)).save(failOnError: true)

        cookie.addToComments(new Comment(author: potapovdd,
        text: "As for me i don't like such harmful for my health things.\nI like more healthy food, how do you keep such a great shape while eating this junk?"))
        cookie.addToComments(new Comment(author: turbodi,
        text: "Eat more of these soft French rolls, but drink tea."))

        iphone.like(potapovdd)

        PersonFollower.create turbodi, potapovdd, true
    }
}
