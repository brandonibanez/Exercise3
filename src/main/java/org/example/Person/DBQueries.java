package org.example.Person;

import org.example.Person.Entity.Contact;
import org.example.Person.Entity.Customer;
import org.example.Person.Entity.Person;
import org.example.Person.Entity.Role;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import javax.transaction.Transactional;
import java.util.*;

public class DBQueries {

    final static private Controller controller = new Controller();
    final static private String GWA = "listByGWA";
    final static private String DATE_HIRED = "listByDateHired";
    final static private String LAST_NAME = "listByLastname";

    private Session createSession() {
        SessionFactory factory = new Configuration()
                .configure()
                .addAnnotatedClass(Person.class)
                .addAnnotatedClass(Contact.class)
                .addAnnotatedClass(Role.class)
                .buildSessionFactory();
        return factory.getCurrentSession();
    }

    public List<Customer> testMethod() {

        SessionFactory factory = new Configuration().configure().buildSessionFactory();

        Session session = factory.getCurrentSession();

        session.beginTransaction();

        List<Customer> customerList;

        customerList = session.createQuery("from Customer", Customer.class).getResultList();

        session.close();

        return customerList;
    }

    private List<Person> readFromDatabase(String criteria) {
        Session session = createSession();
        List<Person> personList;
        session.beginTransaction();
        if(!criteria.equals("gwa")) {
            try {
                personList = session.createQuery("from Person order by " + criteria)
                        .getResultList();
            } finally {
                session.close();
            }
        }
        else {
            try {
                personList = session.createQuery("from Person")
                        .getResultList();
            } finally {
                session.close();
            }
        }
        return personList;
    }

    private List<Person> getListFromDatabase(String criteria) {
        List<Person> personList = new ArrayList<>();
        switch(criteria) {
            case GWA:
                personList = readFromDatabase("gwa");
                personList = controller.sortedByGWA(personList);
                break;
            case DATE_HIRED:
                personList = readFromDatabase("datehired");
                break;
            case LAST_NAME:
                personList = readFromDatabase("lastname");
                break;
        }
        return personList;
    }

    public void listPerson(String criteria) {
        var personList = getListFromDatabase(criteria);
        System.out.println("'" + criteria + "'");
        personList.forEach(System.out::println);
    }

    public void createPerson() {
        try (Session session = createSession()) {
            session.beginTransaction();
            session.save(controller.createPersonObject());
            session.getTransaction().commit();
            System.out.println("Create success!");
            session.close();
        }
    }

    public void createRole() {
        try (Session session = createSession()) {
            session.beginTransaction();
            session.save(controller.createRoleObject());
            session.getTransaction().commit();
            System.out.println("Create success!");
            session.close();
        } catch (Exception e) {
            System.out.println("Role is already present in the database!");
        }
    }

    public void createPersonContact() {
        try (Session session = createSession()) {
            Scanner sc = new Scanner(System.in);
            List<String> contactTypes = new ArrayList<>();
            contactTypes.add("Email");
            contactTypes.add("Landline");
            contactTypes.add("MobileNumber");
            String choice ="";
            boolean loop = true;
            session.beginTransaction();
            Person tempPerson = session.get(Person.class, controller.returnObjectIdIfExist(session,"Person"));
            System.out.println("Contact types " + contactTypes);
            while(loop) {
                System.out.print("Please enter the type of contact you want to add: ");
                choice = sc.nextLine();
                for(String types : contactTypes)
                    if(types.equalsIgnoreCase(choice))
                        loop = false;
                if(loop)
                    System.out.println("Invalid input!");
            }
            var contact = controller.createContactObject(choice.toLowerCase());
            tempPerson.addContact(contact);
            session.save(contact);
            System.out.println("Create success!");
            session.close();
        }
    }

    public void createPersonRole() {
        try (Session session = createSession()) {
            session.beginTransaction();
            var tempRole = session.createQuery("from Role").getResultList();
            System.out.println("Roles " + tempRole);
            var role = session.get(Role.class, controller.returnObjectIdIfExist(session, "Role"));
            var tempPerson = session.get(Person.class, controller.returnObjectIdIfExist(session,"Person"));
            role.addPerson(tempPerson);
            session.save(tempPerson);
            session.getTransaction().commit();
            System.out.println("Create success!");
            session.close();
        } catch (Exception e) {
            System.out.println("Role is already present on that person!");
        }
    }

    public void displayPersonRoles() {
        try (Session session = createSession()) {
            session.beginTransaction();
            Person tempPerson = session.get(Person.class, controller.returnObjectIdIfExist(session,"Person"));
            List<Role> role = tempPerson.getRole();
            try {
                if (role.get(0) != null)
                    System.out.println("Roles: " + role);
            } catch (Exception e) {
                System.out.println("No roles available!");
            }
            session.close();
        }
    }

    public void displayPersonContacts() {
        try (Session session = createSession()) {
            session.beginTransaction();
            Person tempPerson = session.get(Person.class, controller.returnObjectIdIfExist(session,"Person"));
            List<Contact> contact = tempPerson.getContacts();
            try {
                if (contact.get(0) != null)
                    System.out.println("Contacts: " + contact);
            } catch (Exception e) {
                System.out.println("No contacts available!");
            }
            session.close();
        }
    }

    public void displayRoles() {
        try (Session session = createSession()) {
            session.beginTransaction();
            var tempRole = session.createQuery("from Role").getResultList();
            try {
                if (tempRole.get(0) != null)
                    System.out.println("Roles: " + tempRole);
                System.out.println("Success");
                session.close();
            } catch (Exception e) {
                System.out.println("No roles available!");
            }
            session.close();
        }
    }

    public void updatePerson() {
        try (Session session = createSession()) {
            session.beginTransaction();
            var tempPerson = session.get(Person.class, controller.returnObjectIdIfExist(session,"Person"));
            controller.updatePersonObject(tempPerson);
            session.getTransaction().commit();
            System.out.println("Update success!");
            session.close();
        }
    }

    public void updateRole() {
        try (Session session = createSession()) {
            session.beginTransaction();
            Scanner sc = new Scanner(System.in);
            String value  = "";
            var tempRole = session.get(Role.class, controller.returnObjectIdIfExist(session,"Role"));
            while(value.isEmpty()) {
                System.out.print("Enter new value: ");
                value = sc.nextLine();
                if(value.isEmpty())
                    System.out.println("Invalid input!");
            }
            tempRole.setRole(value);
            session.getTransaction().commit();
            System.out.println("Update success!");
            session.close();
        }
    }

    public void updatePersonContact() {
        try (Session session = createSession()) {
            Scanner sc = new Scanner(System.in);
            String value  = "";
            session.beginTransaction();
            var tempPerson = session.get(Person.class, controller.returnObjectIdIfExist(session,"Person"));
            System.out.println(tempPerson.getContacts());
            var tempContact = controller.selectPersonContactOrRole(tempPerson.getContacts());
            for(Contact contact : tempPerson.getContacts())
                if(contact.equals(tempContact)) {
                    while(value.isEmpty()) {
                        System.out.print("Enter new value: ");
                        value = sc.nextLine();
                        if(value.isEmpty())
                            System.out.println("Invalid input!");
                    }
                    contact.setContactValue(value);
                }
            session.getTransaction().commit();
            System.out.println("Update success!");
            session.close();
        }
    }

    public void deletePerson() {
        try (Session session = createSession()) {
            Scanner sc = new Scanner(System.in);
            String choice = "";
            session.beginTransaction();
            Person tempPerson = session.get(Person.class, controller.returnObjectIdIfExist(session,"Person"));
            while (choice.isEmpty()) {
                System.out.print("\nDo you want to proceed with the deletion? [Y] [N] Default is N : ");
                choice = sc.next();
                if (choice.equalsIgnoreCase("y")) {
                    session.delete(tempPerson);
                    session.getTransaction().commit();
                    System.out.println("Delete success!");
                    session.close();
                }
            }
        }
    }

    public void deleteRole() {
        try (Session session = createSession()) {
            session.beginTransaction();
            Scanner sc = new Scanner(System.in);
            String choice  = "";
            var roles = session.createQuery("from Role")
                    .getResultList();
            System.out.println("Roles " + roles);
            var tempRole = session.get(Role.class, controller.returnObjectIdIfExist(session,"Role"));

            while (choice.isEmpty()) {
                System.out.print("\nDo you want to proceed with the deletion? [Y] [N] Default is N : ");
                choice = sc.next();
                if (choice.equalsIgnoreCase("y")) {
                    session.delete(tempRole);
                    session.getTransaction().commit();
                    System.out.println("Delete success!");
                }
            }
            session.close();
        }
    }

    public void deletePersonContact() {
        try (Session session = createSession()) {
            session.beginTransaction();
            Person tempPerson = session.get(Person.class, controller.returnObjectIdIfExist(session,"Person"));
            try {
                if (tempPerson.getContacts().get(0) != null) {
                    var contact = controller.selectPersonContactOrRole(tempPerson.getContacts());
                    session.delete(contact);
                    session.getTransaction().commit();
                    System.out.println("Delete success!");
                    session.close();
                }
            } catch (Exception e) {
                System.out.println("No contacts available!");
            }
        }
    }

    public void deletePersonRole() {
        try (Session session = createSession()) {
            session.beginTransaction();
            Person tempPerson = session.get(Person.class, controller.returnObjectIdIfExist(session,"Person"));
            try {
                if (tempPerson.getRole().get(0) != null) {
                    tempPerson.getRole().remove(controller.selectPersonContactOrRole(tempPerson.getRole()));
                    session.save(tempPerson);
                    session.getTransaction().commit();
                    System.out.println("Delete success!");
                    session.close();
                }
            } catch (Exception e) {
                System.out.println("No contacts available!");
            }
        }
    }
}
