package generator;

import org.apache.commons.lang3.RandomStringUtils;
import pojo.User;

public class UserGenerator {

    private static final int NUMBER_OF_CHARACTERS_IN_EMAIL = 10;
    private static final int NUMBER_OF_CHARACTERS_IN_PASSWORD = 9;
    private static final int NUMBER_OF_CHARACTERS_IN_COMMENT = 8;
    private static final String EMAIL_DOMAIN = "@test.ru";


    public static User getValidUser() {
        return new User(generateEmail(), generatePassword(), generateFirstName());
    }

    public static User getUserWithoutEmail() {
        return new User(generatePassword(), generateFirstName());
    }


    public static String generateEmail() {
        return RandomStringUtils.randomAlphabetic(NUMBER_OF_CHARACTERS_IN_EMAIL).toLowerCase() + EMAIL_DOMAIN;
    }

    public static String generatePassword() {
        return RandomStringUtils.randomAlphabetic(NUMBER_OF_CHARACTERS_IN_PASSWORD).toLowerCase();
    }

    public static String generateFirstName() {
        return RandomStringUtils.randomAlphabetic(NUMBER_OF_CHARACTERS_IN_COMMENT).toLowerCase();
    }

}
