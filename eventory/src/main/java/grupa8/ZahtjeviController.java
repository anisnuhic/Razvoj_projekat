package grupa8;

//import org.hibernate.mapping.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import java.util.List;
import java.io.IOException;


public class ZahtjeviController {
    @FXML
    private GridPane resetka;

    private PrimaryController primaryController;
    public void setPrimaryController (PrimaryController controller){
        this.primaryController = controller;
    }
    public void initialize(){
       addDogadjajListToResetka(getfalseDogadjajList());
    }


    public void addDogadjajListToResetka(List<Dogadjaj> listaDogadjaja) {
        resetka.getChildren().clear();
        int row = 0;
        int col = 0;
        // Ispis liste Dogadjaj objekata
        for (Dogadjaj d : listaDogadjaja) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("kartica.fxml"));
                AnchorPane eventCard = loader.load();
                KarticaController controller = loader.getController();
                controller.setDogadjaj(d);
                controller.setPrimaryController(primaryController);
                Button actionButton = new Button("Dodaj");
                Button odbijButton = new Button("Odbij");
                odbijButton.setId("btn");
                odbijButton.setOnAction(event -> {
                    //ako ostane vremena i ovo napravit
                    //obrisiDogadjajIzBaze(d);
                    listaDogadjaja.remove(d);
                    addDogadjajListToResetka(listaDogadjaja);

                });
                actionButton.setId("btn");
                actionButton.setOnAction(event -> {
                    updateDogadjajStatus(d);
                    listaDogadjaja.remove(d);
                    addDogadjajListToResetka(listaDogadjaja);
                
            });
                eventCard.getChildren().add(actionButton);
                eventCard.getChildren().add(odbijButton);
                AnchorPane.setTopAnchor(odbijButton, 150.0);
                AnchorPane.setLeftAnchor(odbijButton, 250.0);
                AnchorPane.setTopAnchor(actionButton, 120.0); 
                AnchorPane.setLeftAnchor(actionButton, 250.0);
                resetka.add(eventCard, col, row);
                col++;
                if (col == 2) {
                    col = 0;
                    row++;
                }
                if (primaryController != null){
                    primaryController.initialize();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public List<Dogadjaj> getfalseDogadjajList() {
    EntityManager em = EntityManagerFactoryInstance.getInstance().getEntityManagerFactory().createEntityManager();
    List<Dogadjaj> listaDogadjaja = em.createQuery("SELECT d FROM Dogadjaj d WHERE d.odobreno = false ", Dogadjaj.class).getResultList();
    return listaDogadjaja;
}
 public void updateDogadjajStatus(Dogadjaj dogadjaj) {
        EntityManager em = EntityManagerFactoryInstance.getInstance().getEntityManagerFactory().createEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();
            dogadjaj.setOdobreno(true);
            em.merge(dogadjaj); 
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
        }
    }
    public void obrisiDogadjajIzBaze(Dogadjaj dogadjaj) {
        EntityManager entityManager = EntityManagerFactoryInstance.getInstance().getEntityManagerFactory().createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        
        try {
            transaction.begin();
            
            // Moraš spojiti entitet sa trenutnom sesijom ako nije vezan
            Dogadjaj dogadjajToDelete = entityManager.find(Dogadjaj.class, dogadjaj.getDogadjajId());
            
            if (dogadjajToDelete != null) {
                entityManager.remove(dogadjajToDelete);
            }
            
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            entityManager.close();  
        }
    }
    
}
