package transfi.web.app.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import transfi.web.app.entity.User;
import transfi.web.app.service.JwtService;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private  final UserDetailsService userDetailsService;
    @Override
    protected void doFilterInternal(
           @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

                String jwtToken = request.getHeader("Authorization");
                if(jwtToken == null || !jwtService.isTokenValid(jwtToken.substring(7))){
                    filterChain.doFilter(request, response);
                    return;
                }
                jwtToken=jwtToken.startsWith("Bearer ") ? jwtToken.substring(7) : jwtToken;

                String username = jwtService.extractUsername(jwtToken);
        User user = (User) userDetailsService.loadUserByUsername(username);
        var context= SecurityContextHolder.getContext();
        if(user != null && context.getAuthentication() == null){
            var authenticationToken= new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            authenticationToken.setDetails(request);
            context.setAuthentication(authenticationToken);
        }
                filterChain.doFilter(request, response);

                  }
}
