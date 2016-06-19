package ru.jconsulting.igetit.security

/**
 *
 *
 * @author Dmitriy Borisov
 * @created 11/1/2015
 */
class PasswordGenerator {

    static final String ALPHABET = (('A'..'Z') + ('0'..'9')).join('')

    def generate(int length) {
        new Random().with {
            (1..length).collect { ALPHABET[ nextInt( ALPHABET.length() ) ] }.join('')
        }
    }
}
