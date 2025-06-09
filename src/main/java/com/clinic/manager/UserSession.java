package com.clinic.manager;

import com.clinic.models.User;

/**
 * Kelas Singleton untuk mengelola sesi pengguna yang sedang login.
 * Ini adalah pengganti dari konsep "session" pada aplikasi web.
 */
public final class UserSession {

    private static UserSession instance;

    private User loggedInUser;

    // Konstruktor privat untuk mencegah instansiasi dari luar
    private UserSession() {}

    /**
     * Mendapatkan satu-satunya instance dari UserSession.
     * @return instance UserSession.
     */
    public static UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

    /**
     * Memulai sesi dengan menyimpan data pengguna yang berhasil login.
     * @param user Objek pengguna yang telah diautentikasi.
     */
    public void startSession(User user) {
        this.loggedInUser = user;
    }

    /**
     * Mengakhiri sesi (logout) dengan menghapus data pengguna.
     */
    public void endSession() {
        this.loggedInUser = null;
    }

    /**
     * Mendapatkan objek pengguna yang sedang login.
     * @return Objek User jika ada sesi, atau null jika tidak ada.
     */
    public User getCurrentUser() {
        return loggedInUser;
    }

    /**
     * Memeriksa apakah ada pengguna yang sedang login.
     * @return true jika ada sesi aktif, false jika tidak.
     */
    public boolean isLoggedIn() {
        return this.loggedInUser != null;
    }
}