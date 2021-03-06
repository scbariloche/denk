package com.example.andenk.schichtln.helper

import org.spongycastle.util.encoders.Base64
import java.nio.charset.Charset
import java.security.NoSuchAlgorithmException
import java.security.spec.InvalidKeySpecException
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec

//import org.bouncycastle.util.encoders.Base64
/**
 * Created by andenk on 08.01.2018.
 */





    val algorithm = "pbkdf2_sha1"

    fun getEncodedHash(password: String, salt: String, iterations: Int): String {
        // Returns only the last part of whole encoded password
        var keyFactory: SecretKeyFactory? = null
        try {
            keyFactory = SecretKeyFactory.getInstance("PBKDF2withHmacSHA1")
        } catch (e: NoSuchAlgorithmException) {
            System.err.println("Could NOT retrieve $algorithm algorithm")
            System.err.println(e.stackTrace)
        }

        val keySpec = PBEKeySpec(password.toCharArray(), salt.toByteArray(Charset.forName("UTF-8")), iterations, 256)
        var secret: SecretKey? = null
        try {
            secret = keyFactory!!.generateSecret(keySpec)
        } catch (e: InvalidKeySpecException) {
            e.printStackTrace()
        }

        val rawHash = secret!!.encoded
        val hashBase64 = Base64.encode(rawHash)

        return String(hashBase64)
    }

    fun encode(password: String, salt: String, iterations: Int): String {
        // returns hashed password, along with algorithm, number of iterations and salt
        val hash = getEncodedHash(password, salt, iterations)
        return String.format("%s$%d$%s$%s", algorithm, iterations, salt, hash)
    }


    fun checkPassword(password: String, hashedPassword: String): Boolean {
        // hashedPassword consist of: ALGORITHM, ITERATIONS_NUMBER, SALT and
        // HASH; parts are joined with dollar character ("$")
        val parts = hashedPassword.split("\\$".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        if (parts.size != 4) {
            // wrong hash format
            return false
        }
        val iterations = Integer.parseInt(parts[1])
        val salt = parts[2]
        val hash = encode(password, salt, iterations)
        return hash.substring(0, 55) == hashedPassword.substring(0, 55)
    }




