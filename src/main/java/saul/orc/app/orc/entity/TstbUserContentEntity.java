package saul.orc.app.orc.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@Data
@Entity
@Table(name = "tstb_user_content", schema = "xbkb_main", catalog = "")
public class TstbUserContentEntity {
    @Id@Column(name = "tuc_ic")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int tucIc;
    @Basic@Column(name = "mu_id")
    private Integer muId;
    @Basic@Column(name = "tuc_content")
    private String tucContent;
    @Basic@Column(name = "tuc_createtime")
    private String tucCreatetime;
    @Basic@Column(name = "tuc_reply")
    private String tucReply;

}
