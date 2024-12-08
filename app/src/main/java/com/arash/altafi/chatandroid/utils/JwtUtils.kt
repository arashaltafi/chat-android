package com.arash.altafi.chatandroid.utils

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import java.util.*
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec
import kotlin.text.Charsets.UTF_8

object JwtUtils {
    // Secret key for signing (HS256 algorithm)
    private const val SECRET_KEY_STRING = "dsfj9nf978h34ehf8734fh89w8fh8w4fh8whfh3f9h34f"
    private val secretKey: SecretKey = SecretKeySpec(
        SECRET_KEY_STRING.toByteArray(UTF_8),
        SignatureAlgorithm.HS256.jcaName
    )

    // 1. Generate JWT Token
    fun generateToken(claims: Map<String, Any>, expireDays: Int): String {
        val expirationDate = Date(System.currentTimeMillis() + expireDays * 24 * 60 * 60 * 1000L)

        return Jwts.builder()
            .setClaims(claims)
            .setExpiration(expirationDate)
            .signWith(secretKey)
            .compact()
    }

    // 2. Verify JWT Token
    fun verifyToken(token: String): Boolean {
        return try {
            Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
            true
        } catch (_: Exception) {
            false
        }
    }

    // 3. Decode JWT Token
    fun decodeToken(token: String): Map<String, Any>? {
        return try {
            val claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .body

            claims as Map<String, Any>
        } catch (_: Exception) {
            null
        }
    }

    // Decode without verifying signature
    fun decodeTokenWithoutVerification(token: String): Map<String, Any>? {
        return try {
            val claims = Jwts.parserBuilder()
                .build()
                .parseClaimsJws(token)
                .body

            claims as Map<String, Any>
        } catch (_: Exception) {
            null
        }
    }
}
