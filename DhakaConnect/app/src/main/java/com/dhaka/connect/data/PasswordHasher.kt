package com.dhaka.connect.data

import java.security.MessageDigest

/** Minimal local hashing for the demo/offline auth flow — not intended for production use. */
object PasswordHasher {
    fun hash(rawPassword: String): String {
        val digest = MessageDigest.getInstance("SHA-256").digest(rawPassword.toByteArray(Charsets.UTF_8))
        return digest.joinToString(separator = "") { byte -> "%02x".format(byte) }
    }
}
