package api.Controllers;

import api.Entities.User;
import api.Service.UserService;
import api.requests.UserRequest;
import api.security.JwtTokenProvider;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {


    private AuthenticationManager authenticationManager;
    private JwtTokenProvider jwtTokenProvider;

    private PasswordEncoder passwordEncoder;
    private UserService userService;


    public AuthController(AuthenticationManager authenticationManager,
                          JwtTokenProvider jwtTokenProvider, PasswordEncoder passwordEncoder, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
    }

    @PostMapping("/login")
    public String Login(@RequestBody UserRequest loginRequest){
        UsernamePasswordAuthenticationToken authToken=
                new UsernamePasswordAuthenticationToken(loginRequest.getUserName(),loginRequest.getPassword());
        //jwt token oluştururken bir tane Authentication objesi gelir
        Authentication auth= authenticationManager.authenticate(authToken);

        SecurityContextHolder.getContext().setAuthentication(auth);
        String jwtToken=jwtTokenProvider.generateJwtToken(auth);

        //JwtTokenProvider auth isimli Authentication objesini alır ve
        //JwtTokenProvider içinde bu objenin bilgilerini kullanarak bir token oluşturur

        return "Bearer " + jwtToken;
        //login olunca kişiye özel ve login bilgilerimiz ile kişiye özel üretilen bir token döner
        //bu token ı kullanarak diğer sayfalara erişebiliriz
        //not: bearer'ı frontend de kullanmak için ekledim

    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserRequest registerrequest){
        if(userService.getOneUserByName(registerrequest.getUserName())!=null){
            return new ResponseEntity<>("Username already in use", HttpStatus.BAD_REQUEST);
        }
        else{
            User user=new User();
            user.setUserName(registerrequest.getUserName());
            user.setPassword(passwordEncoder.encode(registerrequest.getPassword()));
            userService.saveOneUser(user);
            return new ResponseEntity<>("User successfully registered",HttpStatus.CREATED);
        }
    }




}
