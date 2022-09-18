package api.Entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;


import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data       //getter ve setter ları hazır almak için
@Table(name="post")
public class Post {

    @Id
    Long id;

    @ManyToOne(fetch = FetchType.LAZY) //post u çektiğimde ilgili userı getirmene gerek yok
    @JoinColumn(name="user_id",nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    //kolon üzerinden yorum yap
    //user silinnince postlaırn da sil
    @JsonIgnore  // bu alanla işimz yok
    User user;

    String title;
    @Lob  //??
    @Column(columnDefinition = "text")  //hybernamete mysql de stirng i text yapsın diye, yoksa varchar 255 alır
    String text;

}
