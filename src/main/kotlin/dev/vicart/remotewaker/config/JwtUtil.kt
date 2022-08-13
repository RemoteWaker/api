package dev.vicart.remotewaker.config

import dev.vicart.remotewaker.entities.User
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.lang.Exception
import java.util.*
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.SecretKeySpec

@Component
class JwtUtil {

    private val exp = (24*60*60*1000)*30L

    @Value("\${app.jwt.secret}")
    private lateinit var secretKey: String

    private val key: SecretKey
        get() = SecretKeySpec(secretKey.encodeToByteArray(), 0, secretKey.encodeToByteArray().size, "HMACSHA512")

    fun generateAccessToken(user: User) : String {
        return Jwts.builder().setSubject("${user.id},${user.usern}").setIssuer("RemoteWaker")
            .setIssuedAt(Date()).setExpiration(Date(System.currentTimeMillis()+exp))
            .signWith(key, SignatureAlgorithm.HS512).compact()
    }

    fun validateAccessToken(token: String) : Boolean {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token)
            return true
        }
        catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    fun getSubject(token: String) : String {
        return parseClaims(token).subject
    }

    private fun parseClaims(token: String) : Claims {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token)
            .body
    }
}