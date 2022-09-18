package api.requests;

import lombok.Data;

@Data
public class CommentUpdateRequest {

    //!!!!!COMMENT ICIN SADECE TEXT DEGİTİRMEK İSTERİZ

    String text;
}
