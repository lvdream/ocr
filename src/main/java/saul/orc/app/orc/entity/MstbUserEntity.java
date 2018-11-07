package saul.orc.app.orc.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@Data
@Entity
@Table(name = "mstb_user", schema = "xbkb_main", catalog = "")
public class MstbUserEntity {
    @Id@Column(name = "mu_id")
    private int muId;
    @Basic@Column(name = "mu_code")
    private String muCode;
    @Basic@Column(name = "mu_createtime")
    private String muCreatetime;
    @Basic@Column(name = "mu_updatetime")
    private String muUpdatetime;

}
