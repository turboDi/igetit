import static ru.jconsulting.igetit.utils.SystemUtils.env

// locations to search for config files that get merged into the main config;
// config files can be ConfigSlurper scripts, Java properties files, or classes
// in the classpath in ConfigSlurper format

grails.config.locations = [ "file:${userHome}/.grails/${appName}-config.groovy" ]
//                             "classpath:${appName}-config.groovy",
//                             "file:${userHome}/.grails/${appName}-config.properties",
//                             "file:${userHome}/.grails/${appName}-config.groovy"]

// if (System.properties["${appName}.config.location"]) {
//    grails.config.locations << "file:" + System.properties["${appName}.config.location"]
// }

grails.project.groupId = appName // change this to alter the default package name and Maven publishing destination

// The ACCEPT header will not be used for content negotiation for user agents containing the following strings (defaults to the 4 major rendering engines)
grails.mime.disable.accept.header.userAgents = ['Gecko', 'WebKit', 'Presto', 'Trident']
grails.mime.types = [ // the first one is the default format
    all:           '*/*', // 'all' maps to '*' or the first available format in withFormat
    atom:          'application/atom+xml',
    css:           'text/css',
    csv:           'text/csv',
    form:          'application/x-www-form-urlencoded',
    html:          ['text/html','application/xhtml+xml'],
    js:            'text/javascript',
    json:          ['application/json', 'text/json'],
    multipartForm: 'multipart/form-data',
    rss:           'application/rss+xml',
    text:          'text/plain',
    hal:           ['application/hal+json','application/hal+xml'],
    xml:           ['text/xml', 'application/xml']
]

// URL Mapping Cache Max Size, defaults to 5000
//grails.urlmapping.cache.maxsize = 1000

// What URL patterns should be processed by the resources plugin
grails.resources.adhoc.patterns = ['/images/*', '/css/*', '/js/*', '/plugins/*']
grails.resources.adhoc.includes = ['/images/**', '/css/**', '/js/**', '/plugins/**']

// Legacy setting for codec used to encode data with ${}
grails.views.default.codec = "html"

// The default scope for controllers. May be prototype, session or singleton.
// If unspecified, controllers are prototype scoped.
grails.controllers.defaultScope = 'singleton'

// GSP settings
grails {
    views {
        gsp {
            encoding = 'UTF-8'
            htmlcodec = 'xml' // use xml escaping instead of HTML4 escaping
            codecs {
                expression = 'html' // escapes values inside ${}
                scriptlet = 'html' // escapes output from scriptlets in GSPs
                taglib = 'none' // escapes output from taglibs
                staticparts = 'none' // escapes output from static template parts
            }
        }
        // escapes all not-encoded output at final stage of outputting
        // filteringCodecForContentType.'text/html' = 'html'
    }
}


grails.converters.encoding = "UTF-8"
// scaffolding templates configuration
grails.scaffolding.templates.domainSuffix = 'Instance'

// Set to false to use the new Grails 1.2 JSONBuilder in the render method
grails.json.legacy.builder = false
// enabled native2ascii conversion of i18n properties files
grails.enable.native2ascii = true
// packages to include in Spring bean scanning
grails.spring.bean.packages = []
// whether to disable processing of multi part requests
grails.web.disable.multipart=false

// request parameters to mask when logging exceptions
grails.exceptionresolver.params.exclude = ['password']

// configure auto-caching of queries by default (if false you can cache individual queries with 'cache: true')
grails.hibernate.cache.queries = false

// configure passing transaction's read-only attribute to Hibernate session, queries and criterias
// set "singleSession = false" OSIV mode in hibernate configuration after enabling
grails.hibernate.pass.readonly = false
// configure passing read-only to OSIV session by default, requires "singleSession = false" OSIV mode
grails.hibernate.osiv.readonly = false

grails.gorm.default.mapping = {
    version false
}

environments {
    development {
        grails.logging.jul.usebridge = true
        grails.mail.disabled = true
    }
    production {
        grails.logging.jul.usebridge = false
        grails.serverURL = env("SERVER_URL")

        grails {
            mail {
                host = "smtp.sendgrid.net"
                username = env("MAILER_USERNAME")
                password = env("MAILER_PASSWORD")
            }
        }
        grails.mail.default.from = "MyChoice <no-reply@mychoiceapp.ru>"
    }
    test {
        grails.mail.disabled = true
    }
}

// log4j configuration
//noinspection GroovyUnusedAssignment
log4j = {

    error  'org.codehaus.groovy.grails.web.servlet',        // controllers
           'org.codehaus.groovy.grails.web.pages',          // GSP
           //'org.codehaus.groovy.grails.web.sitemesh',       // layouts
           'org.codehaus.groovy.grails.web.mapping.filter', // URL mapping
           'org.codehaus.groovy.grails.web.mapping',        // URL mapping
           'org.codehaus.groovy.grails.commons',            // core / classloading
           'org.codehaus.groovy.grails.plugins',            // plugins
           'org.codehaus.groovy.grails.orm.hibernate',      // hibernate integration
           'org.springframework',
           'org.hibernate',
           'net.sf.ehcache.hibernate'

    warn   'grails.app.services.grails.plugin.mail'

    debug  'grails.app.controllers.ru.jconsulting',
           'grails.app.services.ru.jconsulting',
           'ru.jconsulting'
    //debug  'org.hibernate.SQL'
    info 'org.flywaydb'
}

// Added by the Spring Security Core plugin:
grails.plugin.springsecurity.userLookup.userDomainClassName = 'ru.jconsulting.igetit.Person'
grails.plugin.springsecurity.userLookup.authorityJoinClassName = 'ru.jconsulting.igetit.auth.PersonRole'
grails.plugin.springsecurity.authority.className = 'ru.jconsulting.igetit.auth.Role'
grails.plugin.springsecurity.controllerAnnotations.staticRules = [
        '/like/save':                   ['ROLE_USER'],
        '/like/delete':                 ['ROLE_USER'],
        '/like/index':                  ['ROLE_USER'],
        '/category/save':               ['ROLE_ADMIN'],
        '/category/delete':             ['ROLE_ADMIN'],
        '/category/update':             ['ROLE_ADMIN'],
        '/shop/delete':                 ['ROLE_ADMIN'],
        '/shop/update':                 ['ROLE_ADMIN'],
        '/city/save':                   ['ROLE_ADMIN'],
        '/city/delete':                 ['ROLE_ADMIN'],
        '/city/update':                 ['ROLE_ADMIN']
]
grails.plugin.springsecurity.filterChain.chainMap = [
        '/**': 'JOINED_FILTERS,-exceptionTranslationFilter,-authenticationProcessingFilter,-securityContextPersistenceFilter,-rememberMeAuthenticationFilter'
]
grails.plugin.springsecurity.rest.token.storage.jwt.expiration = 1800
grails.plugin.springsecurity.rest.login.endpointUrl = '/login'

grails.plugin.likeable.liker.className = 'ru.jconsulting.igetit.Person'
grails.plugin.likeable.liker.evaluator = { delegate.getAuthenticatedUser() }
grails.plugin.likeable.permission.evaluator = { liker, likeable -> true }

site {
    url = 'http://mychoiceapp.ru'
    email = 'info@mychoiceapp.ru'
}