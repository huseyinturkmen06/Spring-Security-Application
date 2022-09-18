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
@Table(name="p_like")
public class Like {

    //mapping işlemleri

    @Id
    Long id;  //long????

    @ManyToOne(fetch = FetchType.LAZY) //post u çektiğimde ilgili userı getirmene gerek yok
    //birden çok comment, bir post a ait olabilir
    @JoinColumn(name="post_id",nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    //user silinnince postlaırn da sil
    @JsonIgnore  // bu alanla işimz yok
    Post post;

    @ManyToOne(fetch = FetchType.LAZY) //post u çektiğimde ilgili userı getirmene gerek yok
    @JoinColumn(name="user_id",nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    //user silinnince postlaırn da sil
    @JsonIgnore  // bu alanla işimz yok
    User user;




}
