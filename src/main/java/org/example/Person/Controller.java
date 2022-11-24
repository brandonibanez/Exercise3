package org.example.Person;

import org.example.Person.Entity.Contact;
import org.example.Person.Entity.Person;
import org.example.Person.Entity.Role;
import org.hibernate.Session;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class Controller {

//    private static final Map<String,String> inputMapDetails = new HashMap<>();
    private final List<Field> personFieldList = Arrays.stream(Person.class.getDeclaredFields()).filter(a -> a.getType() != List.class && a.getType() != int.class).collect(Collectors.toList());
    private final List<Field> contactFieldList = Arrays.stream(Contact.class.getDeclaredFields()).filter(a -> a.getName().equals("contactValue")).collect(Collectors.toList());
    private final List<Field> roleFieldList = Arrays.stream(Role.class.getDeclaredFields()).filter(a -> a.getName().equals("role")).collect(Collectors.toList());
    private final List<String> personFieldNamesWithoutIdAndList = personFieldList.stream().map(a -> a.getName().toLowerCase()).collect(Collectors.toList());

//    private void loadMapChoices(String criteria) {
//        if(criteria.equals("Person"))
//            for(Field field : personFieldList)
//                inputMapDetails.put(field.getName().toLowerCase(), "Please enter " + field.getName() + ": ");
//        if(criteria.equals("Contact"))
//            for(Field field : contactFieldList)
//                inputMapDetails.put(field.getName().toLowerCase(), "Please enter " + field.getName() + ": ");
//        if(criteria.equals("Role"))
//            for(Field field : roleFieldList)
//                inputMapDetails.put(field.getName().toLowerCase(), "Please enter " + field.getName() + ": ");
//    }

    public Person createPersonObject() {
        return inputValidator(new Person(), personFieldList);
    }

    public Contact createContactObject(String contactType) {
        return inputValidator(new Contact(contactType), contactFieldList);
    }

    public Role createRoleObject() {
        return inputValidator(new Role(), roleFieldList);
    }

    public Person updatePersonObject(Person personObject) {
        Scanner sc = new Scanner(System.in);
        Set<String> choices = new HashSet<>();
        System.out.print("\nCategories = " + personFieldNamesWithoutIdAndList + "\nPlease choose the categories you want to edit, type EXIT to stop, type ALL or select one or multiple categories\n\n");
        while(personFieldNamesWithoutIdAndList.size() != choices.size()){
            if(choices.size() > 0)
                System.out.println("Categories selected = " + choices + "\n");
            System.out.print("Input: ");
            String categories = sc.next();
            if(categories.equalsIgnoreCase("exit"))
                break;
            if(categories.equalsIgnoreCase("all")){
                choices.addAll(personFieldNamesWithoutIdAndList);
                System.out.println("Categories selected = " + choices);
                break;
            }
            if(personFieldNamesWithoutIdAndList.contains(categories.toLowerCase()))
                choices.add(categories.toLowerCase());
            else
                System.out.println("Invalid input!");
            if(choices.size() == personFieldNamesWithoutIdAndList.size())
                System.out.println("Categories selected = " + choices);
        }
        List<Field> fieldList = new ArrayList<>();
        for(Field fields : personFieldList){
            fields.trySetAccessible();
            if(choices.contains(fields.getName().toLowerCase()))
                fieldList.add(fields);
        }
        return inputValidator(personObject, fieldList);
    }

    public int returnObjectIdIfExist(Session session, String entity) {
        Scanner sc =  new Scanner(System.in);
        Integer id = null;
        while(Objects.isNull(id)) {
            System.out.print("Enter " + entity.toLowerCase() + " id number: ");
            id = sc.hasNextInt() ? sc.nextInt() : null;
            if(Objects.isNull(id)) {
                System.out.println("Invalid Number!\n");
                sc.nextLine();
            }
            else {
                if(entity.equals("Person"))
                    if(Objects.isNull(session.get(Person.class,id))) {
                        System.out.println("ID does not exist!\n");
                        id = null;
                    }
                if(entity.equals("Role"))
                    if(Objects.isNull(session.get(Role.class,id))) {
                        System.out.println("ID does not exist!\n");
                        id = null;
                    }
            }
        }
        return id;
    }

//    public Contact selectPersonContact(List<Contact> contacts) {
//        Scanner sc =  new Scanner(System.in);
//        Integer id = null;
//        Contact tempContact = new Contact();
//        while(Objects.isNull(id)) {
//            System.out.print("Enter an id number: ");
//            id = sc.hasNextInt() ? sc.nextInt() : null;
//            if(Objects.isNull(id)) {
//                System.out.println("Invalid Number!\n");
//                sc.nextLine();
//            }
//            else {
//                for(Contact contact : contacts) {
//                    if(contact.getId() == id) {
//                        tempContact = contact;
//                        break;
//                    }
//                }
//                if(Objects.isNull(tempContact.getContactValue())) {
//                    System.out.println("ID does not exist!\n");
//                    id = null;
//                }
//            }
//        }
//        return tempContact;
//    }

    public <T> T selectPersonContactOrRole(List<T> object){
        Scanner sc =  new Scanner(System.in);
        Integer id = null;
        Contact tempContact=null;
        Role tempRole=null;
        List<Contact> tempContactList=null;
        List<Role> tempRoleList=null;
        T returnObject = null;
        if(object.get(0).getClass().equals(Contact.class)) {
            tempContactList = (List<Contact>) object;
            System.out.println("Contacts " + tempContactList);
            tempContact = new Contact();
        }
        if(object.get(0).getClass().equals(Role.class)) {
            tempRoleList = (List<Role>) object;
            System.out.println("Roles " + tempRoleList);
            tempRole = new Role();
        }
        while(Objects.isNull(id)) {
            System.out.print("Enter an id number: ");
            id = sc.hasNextInt() ? sc.nextInt() : null;
            if(Objects.isNull(id)) {
                System.out.println("Invalid Number!\n");
                sc.nextLine();
            }
            else {
                if(Objects.nonNull(tempContact)) {
                    for(Contact contact : tempContactList) {
                        if(contact.getId() == id) {
                            returnObject = (T) contact;
                            break;
                        }
                    }
                }
                if(Objects.nonNull(tempRole)){
                    for(Role role : tempRoleList) {
                        if(role.getId() == id) {
                            returnObject = (T) role;
                            break;
                        }
                    }
                }
                if(Objects.isNull(returnObject)) {
                    System.out.println("ID does not exist!\n");
                    id = null;
                }
            }
        }
        return returnObject;
    }

//    private <T> T inputValidator(T Object, List<Field> fieldList, String entity) {
//        loadMapChoices(entity);
//        if(Object.getClass() == Person.class)
//            System.out.println("Input 'null' if you have no input | Follow MM/dd/yyyy when providing for birthday and date hired");
//        else
//            System.out.println("Input 'null' if you have no input");
//        Scanner sc = new Scanner(System.in);
//        String validator = "exit";
//        do {
//            for (Field field : fieldList) {
//                field.trySetAccessible();
//                System.out.print(inputMapDetails.get(field.getName().toLowerCase()));
//                String choice = sc.nextLine();
//                try {
//                    if(choice.equalsIgnoreCase("null"))
//                        field.set(Object,null);
//                    else if(choice.isEmpty() || !fieldValidator(field,choice)) {
//                        validator = "iterate";
//                        break;
//                    }
//                    else
//                        field.set(Object, field.getType() == Date.class
//                                ? parseDate(choice)
//                                : field.getType().getConstructor(String.class).newInstance(choice));
//                } catch (Exception e) {
//                    validator = "iterate";
//                    break;
//                }
//                validator = "exit";
//            }
//            if(validator.equals("iterate"))
//                System.out.println("Make sure that you have correct inputs!\n");
//        } while (!validator.equals("exit"));
//        return Object;
//    }

    private <T> T inputValidator(T Object, List<Field> fieldList) {
        if(Object.getClass() == Person.class)
            System.out.println("Input 'null' if you have no input | Follow MM/dd/yyyy when providing for birthday and date hired");
        else
            System.out.println("Input 'null' if you have no input");
        Scanner sc = new Scanner(System.in);
        String validator = "exit";
        do {
            for (Field field : fieldList) {
                field.trySetAccessible();
                System.out.print("Please enter " + field.getName().toLowerCase() + ": ");
                String choice = sc.nextLine();
                try {
                    if(choice.equalsIgnoreCase("null"))
                        field.set(Object,null);
                    else if(choice.isEmpty() || !fieldValidator(field,choice)) {
                        validator = "iterate";
                        break;
                    }
                    else
                        field.set(Object, field.getType() == Date.class
                                ? parseDate(choice)
                                : field.getType().getConstructor(String.class).newInstance(choice));
                } catch (Exception e) {
                    validator = "iterate";
                    break;
                }
                validator = "exit";
            }
            if(validator.equals("iterate"))
                System.out.println("Make sure that you have correct inputs!\n");
        } while (!validator.equals("exit"));
        return Object;
    }

    private boolean fieldValidator(Field field, String value) {
        if(field.getType() == Date.class)
            return validateJavaDate(value);
        return true;
    }

    public List<Person> sortedByGWA(List<Person> person) {
        return person.stream().sorted((o1, o2) -> {
                    if (o1.getGwa().equals(o2.getGwa()))
                        return 0;
                    return (o1.getGwa() < o2.getGwa()) ? 1 : -1;
                }
        ).collect(Collectors.toList());
    }

    private Date parseDate(String dateStr) throws ParseException {
        final DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        Date utilDate = formatter.parse(dateStr);
        return new java.sql.Date(utilDate.getTime());
    }

    private boolean validateJavaDate(String strDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        sdf.setLenient(false);
        try {
            sdf.parse(strDate);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }
}