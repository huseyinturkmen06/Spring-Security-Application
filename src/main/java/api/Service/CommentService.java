package api.Service;


import api.requests.CommentCreateRequest;
import api.requests.CommentUpdateRequest;
import api.Entities.Comment;
import api.Entities.Post;
import api.Entities.User;
import api.Repositories.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;

@Service
public class CommentService {

    private CommentRepository commentRepository;
    private UserService userService;
    private PostService postService;

    @Autowired
    public CommentService(CommentRepository commentRepository, UserService userService, PostService postService) {
        this.commentRepository = commentRepository;
        this.userService = userService;
        this.postService = postService;
    }

    public List<Comment> getAllCommentsWithParam(Optional<Long> userId,
                                                 Optional<Long> postId){
    if(userId.isPresent()&& postId.isPresent()){
        return commentRepository.findByUserIdAndPostId(userId.get(),postId.get());
    } else if (userId.isPresent()) {
        return commentRepository.findByUserId(userId.get());
    }
    else if(postId.isPresent()){
        return commentRepository.findByPostId(postId.get());
    }
    else{
        return commentRepository.findAll();
    }
    }


    public Comment getOneCommentById( Long commentId){
        return commentRepository.findById(commentId).orElse(null);
        //findById her zaman optional
    }

    public Comment createOneComment(@RequestBody CommentCreateRequest request){
        User user = userService.getOneUserById(request.getUserId());
        Post post = postService.getOnePostById(request.getPostId());
        if (user!=null && post!=null){
            Comment toSave=new Comment();
            toSave.setId(request.getId());
            toSave.setText(request.getText());
            toSave.setPost(post);
            toSave.setUser(user);

            return commentRepository.save(toSave);
            //save edilen obje döner
        }
        else {return null;}

    }


    public Comment updateOneCommentById(Long commentId, CommentUpdateRequest updateComment){
        Optional<Comment> comment=commentRepository.findById(commentId);  //db den gelen comment
        //or else yaparsak comment, yapmazsak optional döner
        if(comment.isPresent()){
            Comment toUpdate=comment.get();
            toUpdate.setText(updateComment.getText());
            return commentRepository.save(toUpdate);
        }
        else{
            System.out.println("böyle birisi yok");
            return null;
        }
    }
//----------

    public void deleteOneCommentById(Long commentId){
        Optional<Comment> comment=commentRepository.findById(commentId);
        if(comment.isPresent()){
            commentRepository.deleteById(commentId);
        }
    }







}
