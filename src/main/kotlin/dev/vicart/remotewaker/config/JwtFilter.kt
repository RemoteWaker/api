package dev.vicart.remotewaker.config

import dev.vicart.remotewaker.entities.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class JwtFilter : OncePerRequestFilter() {

    @Autowired
    private lateinit var jwtUtil: JwtUtil

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val header = request.getHeader("Authorization")
        if(header == null || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response)
            return
        }

        val token = header.split(" ")[1].trim()
        if(!jwtUtil.validateAccessToken(token)) {
            filterChain.doFilter(request, response)
            return
        }
        setAuthenticationContext(token, request)
        filterChain.doFilter(request, response)
    }

    private fun setAuthenticationContext(token: String, request: HttpServletRequest) {
        val userDetails = getUserDetails(token)
        val auth = UsernamePasswordAuthenticationToken(userDetails, null, null)
        auth.details = WebAuthenticationDetailsSource().buildDetails(request)
        SecurityContextHolder.getContext().authentication = auth
    }

    private fun getUserDetails(token: String) : UserDetails {
        val userDetails = User()
        val jwtSubjects = jwtUtil.getSubject(token).split(",")
        userDetails.id = jwtSubjects[0].toLong()
        userDetails.usern = jwtSubjects[1]
        return userDetails
    }
}