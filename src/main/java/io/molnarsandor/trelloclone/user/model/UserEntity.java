package io.molnarsandor.trelloclone.user.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.molnarsandor.trelloclone.project.model.ProjectEntity;
import io.molnarsandor.trelloclone.util.EntitySuperClass;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@SuppressFBWarnings({"EI_EXPOSE_REP"})
@Table(name = "user")
@Entity
@Setter
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
public class UserEntity extends EntitySuperClass {

    @Column(unique = true, nullable = false)
    private String email;
    @Column(nullable = false)
    private String fullName;
    @Column(nullable = false)
    private String password;

    @JsonIgnore
    private String activation;
    @JsonIgnore
    private Boolean enabled;

    @Transient
    private String confirmPassword;

    @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "user", orphanRemoval = true)
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    private List<ProjectEntity> projects = new ArrayList<>();
}
