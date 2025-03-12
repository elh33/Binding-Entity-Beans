# OneToOne Relationship Example with JPA

## Overview
This project demonstrates the implementation of bidirectional relationships in JPA using a Student-Address model. It showcases how to create a relationship where both database tables have foreign keys referencing each other.

## Project Structure
- *Models*: Student and Address entities with bidirectional one-to-one relationship
- *DAO Layer*: Interfaces and implementations for data access operations
- *Persistence Configuration*: JPA/EclipseLink configuration for MySQL database

## Key Features
- Bidirectional one-to-one relationship with foreign keys in both tables
- Complete DAO pattern implementation with CRUD operations
- Transaction management
- Relationship testing and verification

## Database Configuration
The project uses MySQL with the following connection details:
- Database: entity1
- Username: root
- Password: (0000)

## Entity Relationship
- *Student* has a reference to *Address* via address_id foreign key
- *Address* has a reference to *Student* via student_id foreign key

## Technical Implementation
The relationship is configured using JPA annotations:
java
// In Student.java
@OneToOne
@JoinColumn(name = "address_id")
private Address address;

// In Address.java
@OneToOne
@JoinColumn(name = "student_id")
private Student student;


## Running the Application
1. Ensure MySQL is running with database entity2 created
2. Run the MainClass.java which demonstrates:
   - Creating Student and Address entities
   - Establishing bidirectional relationships
   - Testing navigation from both sides of the relationship
   - Updating entities and verifying changes propagate correctly

## Dependencies
- Jakarta Persistence API
- EclipseLink (JPA implementation)
- MySQL Connector/J

## Notes
This implementation creates foreign keys in both tables, making it a true bidirectional database relationship. This differs from standard JPA bidirectional mapping where typically only one side owns the relationship.
