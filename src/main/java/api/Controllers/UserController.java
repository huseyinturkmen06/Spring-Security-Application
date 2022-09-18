package api.Controllers;


import api.Entities.User;
import api.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {


    UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/getall")
    public List<User> getAllUsers(){
        return userService.getAllUsers();
    }

    @PostMapping("/saveUser")
    public void saveOneUser(@RequestBody User user){
        userService.saveOneUser(user);
    }
//
    @GetMapping("/{userId}")
    public User getOneUser(@PathVariable Long userId){
        //custom exception ekle
        return userService.getOneUserById(userId);
    }


    //defoult olan findById bize optional<user> döner


    //biz örneğin id ile birisini sıfırdan eklenmesini istemyiz
    //bu yüzden sadeec var olanlar üstünde ilem yapılmasının isteriz
    //bu yüzden de post değil de put mapping yaparız

    @PutMapping("/updateByUserId/{userId}")
    public void updateByUserId(@PathVariable Long userId,@RequestBody User newUser){
       userService.updateByUserId(userId,newUser);
    }

    @DeleteMapping("/deleteByUserId/{userId}")
    public void deleteByUserId(@PathVariable Long userId){
        userService.deleteByUserId(userId);
    }




}
