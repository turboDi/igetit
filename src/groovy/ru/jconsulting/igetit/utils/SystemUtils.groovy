package ru.jconsulting.igetit.utils

/**
 *
 *
 * @author Dmitriy Borisov
 * @created 12/8/2015
 */
class SystemUtils {

    public static String env(String name) {
        System.getProperty(name, System.getenv(name))
    }
}
