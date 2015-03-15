import org.grails.datastore.mapping.core.Datastore
import ru.jconsulting.igetit.Brand
import ru.jconsulting.igetit.Buy
import ru.jconsulting.igetit.Category as Category
import ru.jconsulting.igetit.Comment
import ru.jconsulting.igetit.IGetItPersistenceEventListener
import ru.jconsulting.igetit.Image
import ru.jconsulting.igetit.PersonFollower
import ru.jconsulting.igetit.Price
import ru.jconsulting.igetit.Person
import ru.jconsulting.igetit.auth.Role
import ru.jconsulting.igetit.auth.PersonRole

class BootStrap {

    def grailsApplication
    def customMarshallerRegistrar

    def init = { servletContext ->
        if (Brand.count() == 0) createData()
        customMarshallerRegistrar.register()

        def ctx = grailsApplication.mainContext

        ctx.getBeansOfType(Datastore).values().each { Datastore d ->
            ctx.addApplicationListener new IGetItPersistenceEventListener(d)
        }
    }

    private static void createData() {
        def apple = new Brand(name: 'Apple').save(flush: true)
        def smak = new Brand(name: 'Smakhleb').save(flush: true)

        def electronics = new Category(name: 'Electronics').save(flush: true)
        def food = new Category(name: 'Food').save(flush: true)

        def admin = new Role(authority: 'ROLE_USER').save(flush: true)
        def turbodi = new Person(username: 'turbo_di@ww.ww', password: '1qazxsw2', fullName: 'Dmitriy Borisov').save(flush: true, failOnError: true)
        def potapovdd = new Person(username: 'potapovdd@ww.ww', password: '1qazxsw2', fullName: 'Potapov Denis').save(flush: true, failOnError: true)

        PersonRole.create turbodi, admin, true
        PersonRole.create potapovdd, admin, true

        new Buy(name: 'iPad air 32 gb WI-FI', brand: apple, category: electronics, owner: turbodi,
                price: new Price(value: new BigDecimal(499.99), currency: Currency.getInstance('USD')),
                images: [new Image(filename: 'ipad.jpg', folderId: '0B1lcabZIpE_KLXA3VTZEelRUcEE')]).save(failOnError: true)
        def iphone = new Buy(name: 'iPhone 5', brand: apple, category: electronics, owner: turbodi,
                price: new Price(value: new BigDecimal(799.99), currency: Currency.getInstance('USD')),
                images: [new Image(filename: 'iphone.jpg', folderId: '0B1lcabZIpE_KeHhwekpUVFNSeW8')]).save(failOnError: true)
        def cookie = new Buy(name: 'Lime cookie', brand: smak, category: food, owner: turbodi,
                price: new Price(value: new BigDecimal(56.99), currency: Currency.getInstance('RUB')),
                images: [new Image(filename: 'Limonnaya_big.jpg', folderId: '0B1lcabZIpE_Kb2VPYUVQcHRxd2s')])
                .save(failOnError: true)
        new Buy(name: 'iPod', brand: apple, category: electronics, owner: potapovdd,
                price: new Price(value: new BigDecimal(49.99), currency: Currency.getInstance('USD')),
                images: [new Image(filename: 'ipod.jpg', folderId: '0B1lcabZIpE_Kc29VckJ6bGU1WmM')]).save(failOnError: true)
        new Buy(name: 'light idea bread', brand: smak, category: food, owner: potapovdd,
                price: new Price(value: new BigDecimal(29.70), currency: Currency.getInstance('RUB')),
                images: [new Image(filename: 'Idea_light1.jpg', folderId: '0B1lcabZIpE_KTDlhaFhZVkpaV0E')]).save(failOnError: true)

        cookie.addToComments(new Comment(author: potapovdd,
        text: "As for me i don't like such harmful for my health things.\nI like more healthy food, how do you keep such a great shape while eating this junk?"))
        cookie.addToComments(new Comment(author: turbodi,
        text: "Eat more of these soft French rolls, but drink tea."))

        iphone.like(potapovdd)

        PersonFollower.create turbodi, potapovdd, true
    }
}
