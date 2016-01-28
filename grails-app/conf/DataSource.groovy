import ru.jconsulting.igetit.postgres.TableNameSequencePostgresDialect

import static ru.jconsulting.igetit.utils.SystemUtils.env

dataSource {
    pooled = true
    jmxExport = true
    driverClassName = "org.postgresql.Driver"
}
hibernate {
    cache.use_second_level_cache = true
    cache.use_query_cache = false
    cache.region.factory_class = 'net.sf.ehcache.hibernate.EhCacheRegionFactory' // Hibernate 3
//    cache.region.factory_class = 'org.hibernate.cache.ehcache.EhCacheRegionFactory' // Hibernate 4
    singleSession = true // configure OSIV singleSession mode
}

// environment specific settings
environments {
    development {
        dataSource {
            url = "jdbc:postgresql://localhost:5432/igetit"
            username = "igetit"
            password = "1qazxsw2"
            dialect = TableNameSequencePostgresDialect
        }
    }
    test {
        dataSource {
            dbCreate = "update"
            driverClassName = "org.h2.Driver"
            username = "sa"
            password = ""
            url = "jdbc:h2:mem:testDb;MVCC=TRUE;LOCK_TIMEOUT=10000;DB_CLOSE_ON_EXIT=FALSE"
        }
    }
    production {
        dataSource {
            url = "jdbc:postgresql://${env('RDS_HOSTNAME')}:5432/${env('RDS_DB_NAME')}"
            username = env('RDS_USERNAME')
            password = env('RDS_PASSWORD')
            dialect = TableNameSequencePostgresDialect
        }
    }
}
