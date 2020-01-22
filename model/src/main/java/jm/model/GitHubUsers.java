package jm.model;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
@Entity
public class GitHubUsers {
    @Id
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "login")
    @EqualsAndHashCode.Include
    private String login;

    @Column(name = "password")
    private String password;

    public GitHubUsers(String login, String password) {
        this.login = login;
        this.password = password;
    }
}
