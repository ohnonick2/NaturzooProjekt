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

    @Column(nullable = false)
    private int weight;


    public Rolle() {}

    public Rolle(String name) {
        this.name = name;

        if (name.equalsIgnoreCase("Superadmin")) {
            this.weight = 0;
        } else if (name.equalsIgnoreCase("Admin")) {
            this.weight = 1;
        } else if (name.equalsIgnoreCase("Pfleger")) {
            this.weight = 2;
        }
    }

    public Rolle(String name, int weight) {
        this.name = name;
        this.weight = weight;
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

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
    public void setName(String name) {
        this.name = name;
    }

}
