package com.pragma.powerup.infrastructure.out.security.filter;

import com.pragma.powerup.infrastructure.out.security.adapter.JwtAdapter;
import com.pragma.powerup.infrastructure.out.security.model.AuthenticatedUser;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String BEARER_PREFIX = "Bearer ";
    private static final String ROL_CLAIM = "rol";
    private static final String ID_CLAIM = "id";
    private static final String ROLE_PREFIX = "ROLE_";

    private final JwtAdapter jwtAdapter;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authHeader != null && authHeader.startsWith(BEARER_PREFIX)) {
            String token = authHeader.substring(BEARER_PREFIX.length());

            if (jwtAdapter.esTokenValido(token)) {
                Claims claims = jwtAdapter.extraerClaims(token);
                String correo = claims.getSubject();
                String rol = claims.get(ROL_CLAIM, String.class);
                Number idNum = claims.get(ID_CLAIM, Number.class);
                Long id = idNum != null ? idNum.longValue() : null;

                if (rol != null && correo != null) {
                    String authority = rol.startsWith(ROLE_PREFIX) ? rol : ROLE_PREFIX + rol;
                    List<SimpleGrantedAuthority> authorities =
                            Collections.singletonList(new SimpleGrantedAuthority(authority));

                    AuthenticatedUser user = new AuthenticatedUser(id, correo, rol);
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(user, null, authorities);

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }

        filterChain.doFilter(request, response);
    }
}
