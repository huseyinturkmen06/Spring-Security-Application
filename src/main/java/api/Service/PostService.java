package api.Service;


import api.requests.PostCreateRequest;
import api.requests.PostUpdateRequest;
import api.Entities.Post;
import api.Entities.User;
import api.Repositories.PostRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    private PostRepository postRepository;
    private UserService userService;

    public PostService(PostRepository postRepository,UserService userService) {
        this.postRepository = postRepository;
        this.userService=userService;
    }



    //getAllPostsByID
    public List<Post> getAllPosts(Optional<Long> userId){
        if(userId.isPresent()){
            return postRepository.findByUserId(userId.get());
        }
        return postRepository.findAll();
    }


    public List<Post> getAll(){
        return postRepository.findAll();
    }



    public Post getOnePostById(Long postId){
        return postRepository.findById(postId).orElse(null);
        //bu hazır geldiği için optional geldi
        //tekrardan kendimize göre jpa içinde yazmış olsaydık
        //bu sefer orElse e gerek kalmazdı
    }


    //koca bir nesnenin tamamını vermek yerine
    public Post createOnePost(PostCreateRequest newPostRequest){

        User user=userService.getOneUserById(newPostRequest.getUserId());
        System.out.println("kullanıcı geldi: "+user.getUserName());
        if(user==null){
            System.out.println("böyle bir kullanıcı yok");
            return null;
        }
        //direkt olarak req body ile user objesi istemek yerine post dto objesi istiyoruz
        Post toSave=new Post();
        toSave.setId(newPostRequest.getId());
        toSave.setText(newPostRequest.getText());
        toSave.setTitle(newPostRequest.getTitle());
        toSave.setUser(user);  //db den gelen objeyi koyduk


        return postRepository.save(toSave);

        //PostCreateRequest class ından gelen objenin özelliklerini
        //db den gelen user objesine setliyoruz
        //Post objesi olan to save i onun özelliklerine eşitliyoruz


        //yeni bir yöntem
        //pst objesi sadece post ile ilgili 3 alan döner bize
        //ama biz 4 tane attribute verdik girerken?*
    }

    public void deleteOnePostById(Long postId){
        postRepository.deleteById(postId);
        //burda bir dto nesnesi felan göndermemize gerek yok çünkü
        //zaten postId normal bir Long kolon
        //başka tabloyla felan alakası yok
    }

    public Post updateOnePostById(Long userId, PostUpdateRequest updatePost){
        //postta sadece text alanının uğdate edilmesini istiyoruz
        //bu yüzden yalnızca o alana göre yazacağız
        Optional<Post> post=postRepository.findById(userId);
        if(post.isPresent()){
            Post toUpdate= post.get();
            toUpdate.setText(updatePost.getText());
            toUpdate.setTitle(updatePost.getTitle());
            postRepository.save(toUpdate);

            //burda hangi class dan bir nesne alırsak
            //onun tamamını json olarak vermek zorundayız
            //bu yüzden de yalnızca 2 tane attribute tutan bir PostUpdateRequest DTO su oluşturduk
        }
        return null;
    }

}
