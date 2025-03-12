import Dao.StudentDao;
import Dao.AddressDao;
import daoImpl.StudentDaoImpl;
import daoImpl.AddressDaoImpl;
import models.Student;
import models.Address;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.lang.reflect.Field;

public class MainClass {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("myJpaUnit");
        EntityManager entityManager = emf.createEntityManager();

        StudentDaoImpl studentDao = new StudentDaoImpl();
        AddressDaoImpl addressDao = new AddressDaoImpl();

        try {
            setEntityManager(studentDao, entityManager);
            setEntityManager(addressDao, entityManager);
        } catch (Exception e) {
            System.err.println("Failed to inject EntityManager: " + e.getMessage());
            e.printStackTrace();
        }

        try {
            // Begin transaction
            entityManager.getTransaction().begin();

            // Create both entities first
            Address address = new Address();
            address.setStreet("123 Main St");
            address.setCity("Boston");
            address.setPostalCode("02115");
            // Save address first to get ID
            addressDao.save(address);

            Student student = new Student();
            student.setNom("Doe");
            student.setPrenom("John");
            // Save student first to get ID
            studentDao.save(student);

            // Now establish the bidirectional relationship
            student.setAddress(address);
            address.setStudent(student);

            // Update both entities with the relationship
            studentDao.update(student);
            addressDao.update(address);

            entityManager.getTransaction().commit();

            // Start new transaction for testing
            entityManager.getTransaction().begin();

            System.out.println("--- Testing Student -> Address Relationship ---");
            Student foundStudent = studentDao.findById(student.getId());
            System.out.println("Student: " + foundStudent.getPrenom() + " " + foundStudent.getNom());
            if (foundStudent.getAddress() != null) {
                System.out.println("Address: " + foundStudent.getAddress().getStreet() + ", " +
                        foundStudent.getAddress().getCity());
            } else {
                System.out.println("No address found for student");
            }

            System.out.println("\n--- Testing Address -> Student Relationship ---");
            Address foundAddress = addressDao.findById(address.getId());
            System.out.println("Address: " + foundAddress.getStreet() + ", " + foundAddress.getCity());
            if (foundAddress.getStudent() != null) {
                System.out.println("Student: " + foundAddress.getStudent().getPrenom() + " " +
                        foundAddress.getStudent().getNom());
            } else {
                System.out.println("No student found for address");
            }

            // Test updating
            System.out.println("\n--- Testing Update ---");
            foundStudent.setPrenom("Jane");
            studentDao.update(foundStudent);

            // Check if update is reflected when accessed from the address side
            Address addressAfterUpdate = addressDao.findById(address.getId());
            System.out.println("Student name after update: " +
                    addressAfterUpdate.getStudent().getPrenom() + " " +
                    addressAfterUpdate.getStudent().getNom());

            entityManager.getTransaction().commit();

        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            entityManager.close();
            emf.close();
        }
    }

    private static void setEntityManager(Object dao, EntityManager em) throws Exception {
        Field field = dao.getClass().getDeclaredField("entityManager");
        field.setAccessible(true);
        field.set(dao, em);
    }
}