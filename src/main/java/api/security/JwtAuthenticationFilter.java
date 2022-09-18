package api.security;

import api.Service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    UserDetailsServiceImpl userDetailsService;



//--------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        //bir request geldiğinde spring security bir sürü filtreleme yapar ama bir filre de biz ekliyoruz
        //JWTfiltresi (Authorize oldu mun diye)
        try{
            String jwtToken = extractJwtFromRqruest(request);
            //gelen istekten token' ı çeken metod

            if(StringUtils.hasText(jwtToken) && jwtTokenProvider.validateToken(jwtToken)) {
                //token geçerli ve dolu ise
                //içinden user ı alıyoruz

                Long id = jwtTokenProvider.getUserIdFromJwt(jwtToken);
                UserDetails user = userDetailsService.loadUserById(id);
                //user artık elimizde

                if(user != null) {
                    //eğer böyle bir user var ise
                    UsernamePasswordAuthenticationToken auth =
                            new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                    auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(auth);
                    //user için gerekli security bilgilerini tutar
                }
            }

        }
        catch (Exception e){
            return;
        }
        filterChain.doFilter(request, response);
        //gelen request ve response ile security filter zincirleri için filtreleme işlemlerini başlat

    }

//--------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------


    private String extractJwtFromRqruest(HttpServletRequest request) {
        String bearer=request.getHeader("Authorization");
        if(StringUtils.hasText(bearer)&&bearer.startsWith("Bearer ")){
            return bearer.substring("Bearer".length()+1);
            //"Bearer " taki 1 boşluğu atıp sadece JWT token kısmını dönüyoruz
        }
        else {
            return null;
        }
    }







}
