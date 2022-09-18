package api.Controllers;


import api.requests.PostCreateRequest;
import api.requests.PostUpdateRequest;
import api.Entities.Post;
import api.Service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/posts")
public class PostController {


    private PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }



    //http://localhost:8080/posts
    //http://localhost:8080/posts?userId=2
    //getAllPostsByUserId
    @GetMapping
    public List<Post> getAllPosts(@RequestParam Optional<Long> userId){
        System.out.println("general worked");
        return postService.getAllPosts(userId);
    }



    //path deki değişkneleri parçalayıp alıyo
   // http://localhost:8080/posts/1
    //path variable ve request param farkı budur
    @GetMapping("/{postId}")
    public Post getOnePost(@PathVariable Long postId){
        return postService.getOnePostById(postId);
    }
    //dierkt path deki değşkenleri / dan sonra gelen değişkeni alıyor



    //ESKİ TİP METOD
//    @PostMapping("/createOnePost")
//    public void createOnePost(@RequestBody Post newPost){
//        postService.createOnePost(newPost);
//        //aslında direkt böyl yazmak yanlıştır,
//        //bu user var mı diye kontrol etmeliyiz, yoksa null hatası vs alabiliriz
//        //burada aslında userId kolonu için userdan komple bir de usr objesi istemiş oluyoruz
//
//        //ama bu işlem çok sağlıklı değildir, bize sadece bir id alanı gelmeli
//    }


    //createOnePost
    @PostMapping
    public Post createOnePost(@RequestBody PostCreateRequest newPostCreateRequest){
        System.out.println("dsadas");
        return postService.createOnePost(newPostCreateRequest);

        //PostCreateRequest nesnesi ile çalıştığımız için
        //gönderdiğimiz nesnede yalnızca userId kolonu olması yeterli oldu
        //ayrıca kocaman bir nesne göndermemize gerek kalmadı
    }


    //aynı sayfada aynı get, post vs istek türünde ve aynı isimde veya
    //ikisi de isimsiz olan birden fazla metod olamaz


    @PutMapping("/{postId}")
    public Post updateOnePostById(@PathVariable Long postId, @RequestBody PostUpdateRequest updatePost){
        return postService.updateOnePostById(postId,updatePost);
        //eskiden olsa post u update ederken
        //userId post class ındaki user nesnesi için
        //post body si alırken,
        //komple bir user body si almamız gerekirdi
        //ama şimdi yalnız vir PostCreateRequest nesnesi almamız yeterli

    }



    //path variable verirken  mutlaka { } içinde yaz yoksa hata verir
    @DeleteMapping("/{postId}")
    public void deleteOnePost(@PathVariable Long postId){
        postService.deleteOnePostById(postId);
    }



}
