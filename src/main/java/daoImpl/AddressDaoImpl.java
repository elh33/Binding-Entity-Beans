package daoImpl;

import Dao.AddressDao;
import models.Address;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;

public class AddressDaoImpl implements AddressDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void save(Address address) {
        entityManager.persist(address);
    }

    @Override
    public Address findById(int id) {
        return entityManager.find(Address.class, id);
    }

    @Override
    public List<Address> findAll() {
        return entityManager.createQuery("from Address", Address.class).getResultList();
    }

    @Override
    public void update(Address address) {
        entityManager.merge(address);
    }

    @Override
    public void delete(Address address) {
        entityManager.remove(entityManager.contains(address) ? address : entityManager.merge(address));
    }
}