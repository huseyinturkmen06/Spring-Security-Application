package api.Service;

import api.Entities.User;
import api.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class UserService {



    UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }


    public void saveOneUser( User user){
        userRepository.save(user);
    }


    public User getOneUserById(Long userId){
        //custom exception ekle
        return userRepository.findById(userId).orElse(null);
        //findById bize optional döndürür çünkü:
        //o id ye sahip olan kullanıcı gele de bilir gelmeye de bilir
        ///gelmezse de null dön diyoruz
        //null gelirse diye kontrol  db den veri gelmezse diye
        //find by bize optional döner
    }


    //defoult olan findById bize optional<user> döner


    //biz örneğin id ile birisini sıfırdan eklenmesini istemyiz
    //bu yüzden sadeec var olanlar üstünde ilem yapılmasının isteriz
    //bu yüzden de post değil de put mapping yaparız


    public void updateByUserId( Long userId, User newUser){
        Optional<User> user=userRepository.findById(userId);
        //path ile gelen user varsa
        if(user.isPresent()){
            User foundUser=user.get();  //direkt olarak o nesneyi aldık
            foundUser.setUserName(newUser.getUserName());
            foundUser.setPassword(newUser.getPassword());
            userRepository.save(newUser);

        }
        else{
            System.out.println("böyle bir kullanıcı yok");
        }
    }


    public void deleteByUserId( Long userId){
        userRepository.deleteById(userId);
    }

    public User getOneUserByName(String userName){
        return userRepository.findByUserName(userName);

    }

}
