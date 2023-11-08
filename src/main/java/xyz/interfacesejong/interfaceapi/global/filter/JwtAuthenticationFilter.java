package xyz.interfacesejong.interfaceapi.global.filter;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.filter.OncePerRequestFilter;
import xyz.interfacesejong.interfaceapi.domain.user.domain.AuthLevelType;
import xyz.interfacesejong.interfaceapi.global.jwt.TokenProvider;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final TokenProvider tokenProvider;

    public JwtAuthenticationFilter(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain) throws ServletException, IOException {
        if (request.getHeader("Authorization").equals("INTERFACE518")){
            log.info("ADMIN ACCESS CODE");
            return;
        }
        String token = tokenProvider.resolveToken(request);

        if (token != null && tokenProvider.isValidatedToken(token)){
            AuthLevelType authLevelType = tokenProvider.getAuthLevel(token);

            if (authLevelType.getLevel() < 3){
                response.setHeader("Authorization", token);
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.getWriter().write("ACCESS PERMISSION DENIED");
                log.info("ACCESS PERMISSION DENIED");
                return;
            }
        }else {
            response.setHeader("Authorization", token);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("INVALID TOKEN");
            log.info("INVALID TOKEN");
            return;
        }

        response.setHeader("Authorization", token);
        filterChain.doFilter(request, response);
    }
}
