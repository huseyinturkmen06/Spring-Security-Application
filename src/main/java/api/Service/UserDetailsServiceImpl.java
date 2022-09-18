package api.Service;

import api.Entities.User;
import api.Repositories.UserRepository;
import api.security.JwtUserDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    //constructor injection

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user=userRepository.findByUserName(username);
        return JwtUserDetails.create(user);  //userDetail tipinde bir user döner
    }

    public UserDetails loadUserById(Long id){
        User user=userRepository.findById(id).get();
        return JwtUserDetails.create(user);
        //düz findByid optional döner
        //ama ek alan --- normal user döner
    }


    //login olunca jwt token onlara dönecek ve bu tokenı kullanacaklar
}
