package api.security;
//burda tokenlar oluşacak (her login olunduğunda)
//ve bu token lar login olan kişiye dönecek

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${questapp.app.secret}")
    private String APP_SECRET;   //app e özel key  (token ın kendisi)
    @Value("${questapp.expires.in}")
    private Long EXPIRES_IN;      //saniye cinsinden kaç saniyede bir tokenlar ekspire oluyor

    public String generateJwtToken(Authentication auth){
        JwtUserDetails userDetails=(JwtUserDetails) auth.getPrincipal();
        //getPrincipal == Authenticate edilecek user
        Date expireDate = new Date( new Date().getTime() + EXPIRES_IN);
        //sonlanma zamanı= şimdiki zaman + geçerlilik süresi
        //user, token ve token geçerlilik süresi elimize
        //şimdi bunların hepsini kullanarak bir key oluşturalım (Jwt token)

        return Jwts.builder().setSubject( Long.toString( userDetails.getId() ))
                .setIssuedAt(new Date()).setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS512 ,APP_SECRET).compact();
        //token oluştu
        //userId ile oluşturduk ama userName ile de oluşturabilirdik
    }

    // burdaki tokendan ters metod ile tekrardan userId yi elde edebiliyoruz
    //token = APP_SECRET + userId
    //userId= token-APP_SECRET    /// tersine işlem yaparken
    Long getUserIdFromJwt(String token) {
        Claims claims = Jwts.parser().setSigningKey(APP_SECRET).parseClaimsJws(token).getBody();
        return Long.parseLong(claims.getSubject());   //string olan değeri tekrar long yaptık (şifre String)
    }


    //validation for token
    //geçerli bir token mi diye bak
    boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(APP_SECRET).parseClaimsJws(token);
            //APP_SECRET i pars edebiliyosak geçerli bir token demektir, yoksa zaten pars edemezdi ve hata verirdi
            //böylece doğru token olduğunun anlarız
            return !isTokenExpired(token);  //token sonlanmadıysa hala geçerli demektir ve true döner
            //yoksa tüm hatalarda false döner
        } catch (SignatureException e) {
            return false;
        } catch (MalformedJwtException e) {
            return false;
        } catch (ExpiredJwtException e) {
            return false;
        } catch (UnsupportedJwtException e) {
            return false;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    private boolean isTokenExpired(String token) {
        Date expiration = Jwts.parser().setSigningKey(APP_SECRET).parseClaimsJws(token).getBody().getExpiration();
        return expiration.before(new Date());
        //eğer exxpiration date bugünün tarihinden önce ise tarihi geçmemiştir ve token günceldir,
        //yani true döner
    }



}
