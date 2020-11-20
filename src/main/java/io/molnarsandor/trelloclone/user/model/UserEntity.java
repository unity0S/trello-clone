package io.molnarsandor.trelloclone.user.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.molnarsandor.trelloclone.project.model.ProjectEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SuppressFBWarnings({"EI_EXPOSE_REP"})
@Table(name = "user")
@Entity
@Getter
@Setter
@NoArgsConstructor
@Data
@ApiModel
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Email(message = "Needs to be a valid email")
    @NotBlank(message = "Email is required")
    @Column(unique = true)
    @ApiModelProperty(value = "Existing email where the activation link will be sent", required = true)
    private String email;
    @NotBlank(message = "Please enter your full name")
    @Size(min = 5, max = 50, message = "Please use 5 to 50 characters")
    @Pattern(regexp = "^([A-Za-záéúőóüö.]+\\s?){5,}$", message = "Special characters not allowed!")
    @ApiModelProperty(value = "Full name of the User", required = true)
    private String fullName;
    @NotBlank(message = "Password field is required")
    @Size(min = 8, max = 100, message = "Please use 8 to 30 characters")
    @ApiModelProperty(value = "Password", required = true)
    private String password;

    @JsonIgnore
    private String activation;
    @JsonIgnore
    private Boolean enabled;

    @Transient
    private String confirmPassword;

    @JsonFormat(pattern = "yyyy-mm-dd")
    @Column(updatable = false)
    private Date createdAt;
    @JsonFormat(pattern = "yyyy-mm-dd")
    private Date updatedAt;

    @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER, mappedBy = "user", orphanRemoval = true)
    @ApiModelProperty(hidden = true)
    private List<ProjectEntity> projects = new ArrayList<>();

    @PrePersist
    protected void onCreate() { this.createdAt = new Date(); }

    @PreUpdate
    protected void onUpdate() { this.updatedAt = new Date(); }
}
