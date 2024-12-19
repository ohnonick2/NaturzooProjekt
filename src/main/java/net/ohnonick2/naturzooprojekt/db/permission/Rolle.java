package net.ohnonick2.naturzooprojekt.db.permission;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "rolle")
public class Rolle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    public String name;


    public Rolle() {}

    public Rolle(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
