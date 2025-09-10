package com.ecomm.Project.Model;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long addressID;

    @NotBlank
    @Size(min = 5, message = "Street name must be atleast 5 characters")
    private String Street;

    @NotBlank
    @Size(min = 5, message = "Building Name name must be atleast 5 characters")
    private String buildingName;

    @NotBlank
    @Size(min = 2, message = "City name must be atleast 5 characters")
    private String city;

    @NotBlank
    @Size(min = 2, message = "State name must be atleast 5 characters")
    private String state;

    @NotBlank
    @Size(min = 2, message = "Country name must be atleast 5 characters")
    private String country;

    @NotBlank
    @Size(min = 6, message = "Pincode must be atleast 5 characters")
    private String pincode;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Address(String street, String buildingName, String city, String state,
            String country, String pincode) {
        Street = street;
        this.buildingName = buildingName;
        this.city = city;
        this.state = state;
        this.country = country;
        this.pincode = pincode;
    }
}
