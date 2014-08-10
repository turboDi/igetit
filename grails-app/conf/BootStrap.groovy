class BootStrap {

    def grailsApplication
    def customMarshallerRegistrar
    def eventProducer
    def personEmailListener
    def emailService

    def init = { servletContext ->
        customMarshallerRegistrar.register()

        grailsApplication.mainContext.addApplicationListener eventProducer
        personEmailListener.emailService = emailService
    }
}
