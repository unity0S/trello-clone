package io.molnarsandor.pmtool.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Collaborator {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Email
    @NotBlank
    @Column(updatable = false)
    private String email;
    @Column(updatable = false)
    private String projectIdentifier;
    @Column(updatable = false, unique = true)
    private String collaboratorSequence;

    @ManyToOne
    @JoinColumn(name = "project_id", updatable = false, nullable = false)
    @JsonBackReference
    private Project project;
}
