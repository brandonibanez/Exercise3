package org.example.Person.Entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "person")
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "lastname")
    private String lastName;

    @Column(name = "firstname")
    private String firstName;

    @Column(name = "middlename")
    private String middleName;

    @Column(name = "suffix")
    private String suffix;

    @Column(name = "title")
    private String title;

    @Column(name = "streetno")
    private Integer streetNo;

    @Column(name = "barangay")
    private String barangay;

    @Column(name = "city")
    private String city;

    @Column(name = "zipcode")
    private Integer zipcode;

    @Column(name = "bday")
    @Temporal(TemporalType.DATE)
    private Date bDay;

    @Column(name = "gwa")
    private Double gwa;

    @Column(name = "datehired")
    @Temporal(TemporalType.DATE)
    private Date dateHired;

    @Column(name = "employed")
    private Boolean is_Employed;

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy="person",
            fetch=FetchType.LAZY,
            cascade= {CascadeType.PERSIST, CascadeType.MERGE,
                    CascadeType.DETACH, CascadeType.REFRESH, CascadeType.REMOVE})
    private List<Contact> contacts;

    @LazyCollection(LazyCollectionOption.FALSE)
    @ManyToMany(fetch=FetchType.LAZY,
            cascade= {CascadeType.PERSIST, CascadeType.MERGE,
                    CascadeType.DETACH, CascadeType.REFRESH})
    @JoinTable(
            name="person_role",
            joinColumns=@JoinColumn(name="person_id"),
            inverseJoinColumns=@JoinColumn(name="role_id")
    )
    private List<Role> role;

    public Person(String lastName, String firstName, String middleName, String suffix, String title, int streetNo,
                  String barangay, String city, int zipcode, Date bDay, Double gwa, Date dateHired, Boolean is_Employed) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.middleName = middleName;
        this.suffix = suffix;
        this.title = title;
        this.streetNo = streetNo;
        this.barangay = barangay;
        this.city = city;
        this.zipcode = zipcode;
        this.bDay = bDay;
        this.gwa = gwa;
        this.dateHired = dateHired;
        this.is_Employed = is_Employed;
        this.contacts = null;
        this.role = null;
    }

    public void addContact(Contact tempContact) {
        if (contacts == null) {
            contacts = new ArrayList<>();
        }
        contacts.add(tempContact);
        tempContact.setPerson(this);
    }

}
