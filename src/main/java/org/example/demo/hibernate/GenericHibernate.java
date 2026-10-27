package org.example.demo.hibernate;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.example.demo.classes.Alignment;
import org.example.demo.classes.MagicalCreature;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class GenericHibernate {

    protected EntityManager entityManager;

    public GenericHibernate(EntityManagerFactory entityManagerFactory) {
        this.entityManager = entityManagerFactory.createEntityManager();
    }

    public void close(){
        if(entityManager.isOpen()){
            entityManager.close();
        }
    }

    public <T> void create(T object){
        var transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.persist(object);
            transaction.commit();
        } catch (RuntimeException e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
    }

    public <T> void update(T object){
        var transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.merge(object);
            transaction.commit();
        } catch (RuntimeException e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
    }

    public <T> void delete(T object){
        var transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.remove(object);
            transaction.commit();
        } catch (RuntimeException e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
    }

    /*public <T> List<T> getAll(Class<T> entityClass) {
        List<T> list = new ArrayList<>();
        try {
            CriteriaQuery criteriaQuery = entityManager.getCriteriaBuilder().createQuery();
            criteriaQuery.select(criteriaQuery.from(entityClass));
            Query q = entityManager.createQuery(criteriaQuery);
            list = q.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }*/

    public <T> List<T> getAll(Class<T> entityClass) {
        CriteriaQuery<T> query = entityManager.getCriteriaBuilder().createQuery(entityClass);
        query.select(query.from(entityClass));
        return entityManager.createQuery(query).getResultList();
    }

    public List<MagicalCreature> findCreatures(
            String founderName,
            String creatureName,
            Alignment alignment,
            LocalDate foundAfter
    ) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<MagicalCreature> query = builder.createQuery(MagicalCreature.class);
        Root<MagicalCreature> creature = query.from(MagicalCreature.class);

        List<Predicate> filters = new ArrayList<>();
        if (founderName != null && !founderName.isBlank()) {
            String founderPattern = "%" + founderName.trim().toLowerCase() + "%";
            filters.add(builder.or(
                    builder.like(builder.lower(creature.get("founderName")), founderPattern)
            ));
        }
        if (creatureName != null && !creatureName.isBlank()) {
            filters.add(builder.like(
                    builder.lower(creature.get("name")),
                    "%" + creatureName.trim().toLowerCase() + "%"
            ));
        }
        if (alignment != null) {
            filters.add(builder.equal(creature.get("alignment"), alignment));
        }
        if (foundAfter != null) {
            filters.add(builder.greaterThan(creature.get("dateFound"), foundAfter));
        }

        query.select(creature).where(filters.toArray(new Predicate[0]));

        return entityManager.createQuery(query).getResultList();
    }

    /*public List<MagicalCreature> getByCriteria(String wizard, String creatureTitle, CreatureType creatureType, LocalDate date) {
        List<MagicalCreature> list = new ArrayList<>();
        try {
            entityManager = entityManagerFactory.createEntityManager();
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<MagicalCreature> query = cb.createQuery(MagicalCreature.class);
            Root<MagicalCreature> root = query.from(MagicalCreature.class);

            query.select(root).where(cb.and(cb.like(root.get("wizard"), "%" + wizard + "%"),
                    cb.like(root.get("title"), "%" + creatureTitle + "%"),
                    cb.equal(root.get("creatureType"), creatureType),
                    cb.greaterThan(root.get("dateFound"), date)));
            Query q = entityManager.createQuery(query);

            list = q.getResultList();
        } catch (Exception e) {
            //cia noriu alert
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
        return list;
    }*/

    public <T> T getEntityById(Class<T> entityClass, int id) {
        T result = null;
        try {
            entityManager.getTransaction().begin();
            result = entityManager.find(entityClass, id);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
