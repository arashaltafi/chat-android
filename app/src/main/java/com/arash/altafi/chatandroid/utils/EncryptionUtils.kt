package com.arash.altafi.chatandroid.utils

import java.nio.charset.StandardCharsets
import javax.crypto.Cipher
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.SecretKeySpec
import android.util.Base64
import kotlin.random.Random

class EncryptionUtils() {

    private val ivSize = 12 // 12 bytes IV for AES-GCM
    private val tagSize = 128 // 128-bit authentication tag for AES-GCM

    // Encrypt the given value using AES/GCM/NoPadding
    fun encrypt(value: String): String {
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        val secretKey = getSecretKey()

        // Generate a random IV
        val iv = ByteArray(ivSize)
        Random.nextBytes(iv)

        // Initialize cipher with the IV and secret key
        val gcmParameterSpec = GCMParameterSpec(tagSize, iv)
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, gcmParameterSpec)

        // Encrypt the input value
        val encryptedBytes = cipher.doFinal(value.toByteArray(StandardCharsets.UTF_8))

        // Combine IV and encrypted data
        val ivAndEncrypted = iv + encryptedBytes

        // Return Base64 encoded IV + encrypted value
        return Base64.encodeToString(ivAndEncrypted, Base64.DEFAULT)
    }

    // Decrypt the given value using AES/GCM/NoPadding
    fun decrypt(encryptedValue: String): String {
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        val secretKey = getSecretKey()

        // Decode the Base64-encoded input
        val decodedValue = Base64.decode(encryptedValue, Base64.DEFAULT)

        // Extract the IV and encrypted data
        val iv = decodedValue.copyOfRange(0, ivSize)
        val encryptedBytes = decodedValue.copyOfRange(ivSize, decodedValue.size)

        // Initialize cipher with the IV and secret key for decryption
        val gcmParameterSpec = GCMParameterSpec(tagSize, iv)
        cipher.init(Cipher.DECRYPT_MODE, secretKey, gcmParameterSpec)

        // Decrypt the data and return the result as a string
        return String(cipher.doFinal(encryptedBytes), StandardCharsets.UTF_8)
    }

    // Secret key generation. You can improve this by securely generating the key
    private fun getSecretKey(): SecretKeySpec {
        val keyString = "my_secret_key_1234" // Ensure the key is exactly 16 bytes for AES-128
        val keyBytes = keyString.toByteArray(StandardCharsets.UTF_8)
        val keyBytes16 = keyBytes.copyOf(16) // Trim or pad the key to 16 bytes
        return SecretKeySpec(keyBytes16, "AES")
    }
}
