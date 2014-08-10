migrationsDir = "$basedir/grails-app/migrations".toString()

eventCompileStart = {
    projectCompiler.srcDirectories << migrationsDir
    copyResources "$resourcesDirPath/migrations"
}

eventCreateWarStart = { warName, stagingDir ->
    copyResources "$stagingDir/WEB-INF/classes/migrations"
}

private copyResources(destination) {
    ant.copy(todir: destination, preservelastmodified: true) {
        fileset(dir: migrationsDir)
    }
}
