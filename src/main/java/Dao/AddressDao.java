package Dao;

import models.Address;
import java.util.List;

public interface AddressDao {
    void save(Address address);
    Address findById(int id);
    List<Address> findAll();
    void update(Address address);
    void delete(Address address);
}