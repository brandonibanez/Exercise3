package org.example.Person;

import org.example.Person.Entity.Contact;
import org.example.Person.Entity.Person;
import org.example.Person.Entity.Role;
import org.hibernate.sql.Update;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class App {

    final static private String GWA = "listByGWA";
    final static private String DATE_HIRED = "listByDateHired";
    final static private String LAST_NAME = "listByLastname";

    public static void main(String[] args) throws Exception {
        var controller = new Controller();
        var queries = new DBQueries();

        System.out.println(queries.testMethod());
//        //Create Person
//        queries.createPerson();
//        queries.listPerson(LAST_NAME);
//
//        //Update Person
//        queries.updatePerson();
//        queries.listPerson(LAST_NAME);
//
//        //Add Person Contact
//        queries.createPersonContact();
//        queries.displayPersonContacts();
//
//        //Update Person Contact
//        queries.updatePersonContact();
//        queries.displayPersonContacts();
//
//        //Delete Person Contact
//        queries.deletePersonContact();
//        queries.displayPersonContacts();
//
//        //Create a role
//        queries.createRole();
//        queries.displayRoles();
//
//        //Add Person Role
//        queries.createPersonRole();
//        queries.displayPersonRoles();
//
//        //Update a role
//        queries.updateRole();
//        queries.displayPersonRoles();
//
//        //Delete a role
//        queries.deleteRole();
//        queries.displayRoles();
//        queries.displayPersonRoles();
//
//        //Add Person Role
//        queries.createPersonRole();
//        queries.displayPersonRoles();
//
//        //Delete Person Role
//        queries.deletePersonRole();
//        queries.displayPersonRoles();
//        queries.displayRoles();

    }
}
