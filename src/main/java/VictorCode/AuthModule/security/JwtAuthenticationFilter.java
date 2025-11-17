package VictorCode.AuthModule.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider, UserDetailsService userDetailsService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userDetailsService = userDetailsService;
    }



    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String token = null;
        String username = null;

        // Pega token do cookie "token"
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("token".equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
        }

        // Se não veio no cookie, tenta pegar do header Authorization
        if (token == null) {
            String header = request.getHeader("Authorization");
            if (header != null && header.startsWith("Bearer ")) {
                token = header.substring(7);
            }
        }

        if (token != null) {
            try {
                username = jwtTokenProvider.getUsernameFromToken(token);

                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                    if (jwtTokenProvider.validateToken(token)) {
                        UsernamePasswordAuthenticationToken authToken =
                                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                }
            } catch (io.jsonwebtoken.ExpiredJwtException ex) {
                // Token expirado: limpa contexto e cookie
                SecurityContextHolder.clearContext();
                if (request.getCookies() != null) {
                    for (Cookie cookie : request.getCookies()) {
                        if ("token".equals(cookie.getName())) {
                            cookie.setMaxAge(0);
                            cookie.setValue("");
                            cookie.setPath("/"); // importante para remover corretamente
                            response.addCookie(cookie);
                        }
                    }
                }
            } catch (Exception ex) {
                // Outros erros (usuário não encontrado, token inválido, etc.)
                SecurityContextHolder.clearContext();
            }
        }

        filterChain.doFilter(request, response);
    }

}

/*
📘 Explicação:

Lê o header "Authorization".

Extrai o token e o nome de usuário.

Carrega o usuário com UserDetailsService.

Se o token for válido → autentica no contexto do Spring.
*/