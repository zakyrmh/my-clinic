package com.clinic.utils;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;

public class SecurityUtil {
    private static final Argon2 ARGON2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id);
    private static final int ITERATIONS = 3;
    private static final int MEMORY = 65536;
    private static final int PARALLELISM = 1;

    public static String hashPassword(String password) {
        return ARGON2.hash(ITERATIONS, MEMORY, PARALLELISM, password.toCharArray());
    }

    public static boolean verifyPassword(String hash, String password) {
        return ARGON2.verify(hash, password.toCharArray());
    }
}