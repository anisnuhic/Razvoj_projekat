package grupa8;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class EntityManagerFactoryInstance {

    private static EntityManagerFactoryInstance single_instance = null;

    private static EntityManagerFactory entityManagerFactory;

    private EntityManagerFactoryInstance() {
        entityManagerFactory = Persistence.createEntityManagerFactory("eventoryPU");
    }

    public static synchronized EntityManagerFactoryInstance getInstance() {
        if (single_instance == null) {
            single_instance = new EntityManagerFactoryInstance();
        }
        return single_instance;
    }

    public static synchronized void init() {
        single_instance = new EntityManagerFactoryInstance();
    }

    public synchronized EntityManagerFactory getEntityManagerFactory() {
        return entityManagerFactory;
    }
}
