package org.example.Person.Entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "contact")
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "contact_type")
    private String contactType;

    @Column(name = "contact_value")
    private String contactValue;

    @LazyCollection(LazyCollectionOption.FALSE)
    @ManyToOne(fetch=FetchType.LAZY,
            cascade= {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name="person_id")
    private Person person;

    public Contact(String contactType) {
        this.contactType = contactType;
    }

    public Contact(String contactType, String contactValue) {
        this.contactType = contactType;
        this.contactValue = contactValue;
    }

    @Override
    public String toString() {
        return "(id=" + this.id + ", type=" + this.getContactType() + ", value=" + this.getContactValue() + ")";
    }
}
