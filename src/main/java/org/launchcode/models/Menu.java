package org.launchcode.models;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;


@Entity
public class Menu {



    @Id
    @GeneratedValue
    private int id;



    @NotNull
    @Size(min=3, max=15)
    private String name;



    @ManyToMany

    List<Cheese> cheeses = new ArrayList<>();



    public int getId() {

        return id;

    }



    @NotNull

    public String getName() {

        return name;

    }



    public void setName(@NotNull String name) {

        this.name = name;

    }



    public List<Cheese> getCheeses() {

        return cheeses;

    }



    public void addMenuItem(Cheese cheese) {

        this.cheeses.add(cheese);

    }



    public Menu(@NotNull String name) {

        this.name = name;

    }



    public Menu() {

    }

}