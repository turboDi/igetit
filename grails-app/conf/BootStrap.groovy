class BootStrap {

    def grailsApplication
    def eventProducer
    def personEmailListener
    def emailService

    def init = { servletContext ->
        grailsApplication.mainContext.addApplicationListener eventProducer
        personEmailListener.emailService = emailService
    }
}
