package grupa8;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import java.util.List;
import java.io.IOException;

import javafx.stage.Modality;
import javafx.stage.Stage;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;


public class UrediDogadjajController {
    @FXML
    private VBox resetka;

    private static final int ITEMS_PER_PAGE = 6;

    EntityManager em = EntityManagerFactoryInstance.getInstance().getEntityManagerFactory().createEntityManager();
    
    @FXML
    private PrimaryController primaryController;

    public static Integer urediID;

    public void setPrimaryController(PrimaryController primaryController) {
        this.primaryController = primaryController;
    }

    @FXML
    private void urediDogadjaj(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("urediInformacije.fxml"));
            Parent registracijaRoot = fxmlLoader.load();
            UrediInformacijeController urediController = fxmlLoader.getController();
            urediController.setPrimaryController(this);
            Stage stage = new Stage();
            stage.setTitle("Uredite događaj");
            stage.setScene(new Scene(registracijaRoot));
            stage.setResizable(false);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(((Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow()));
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void dodajListuDogadjaja(List<Dogadjaj> listaDogadjaja) {
        resetka.getChildren().clear();
        resetka.setSpacing(10);
        for(Dogadjaj d : listaDogadjaja) {
            if(d.getOdobreno()) {
                Integer id = d.getDogadjajId();
                String naziv = d.getNaziv();
                Button btn = new Button(naziv);
                resetka.getChildren().addAll(btn);
                btn.setStyle("-fx-background-color: linear-gradient(from 0% 0% to 100% 100%, #6A6FB3, #80CDD7); -fx-pref-width: 379px; -fx-pref-height: 50px; -fx-margin: 20 0 20 0; -fx-text-fill: white; -fx-font-size: 16px; -fx-background-radius: 15;");
                btn.setOnAction((ActionEvent event) -> { urediID = id; urediDogadjaj(event); });
            }
        }

    }

    @FXML
    private void pronadjiDogadjaje() {
        String imeKorisnik = primaryController.getImeKorisnika();
        EntityManager em = EntityManagerFactoryInstance.getInstance().getEntityManagerFactory().createEntityManager();

        String jpq = "SELECT k.korisnikId FROM Korisnik k WHERE k.korisnickoIme = :imeKorisnik";
        TypedQuery<Integer> query = em.createQuery(jpq, Integer.class);
        query.setParameter("imeKorisnik", imeKorisnik);
        Integer idOrganizatora = query.getSingleResult();

        String jp = "SELECT o FROM Organizator o WHERE o.organizatorId = :idOrganizator";
        TypedQuery<Organizator> query2 = em.createQuery(jp, Organizator.class);
        query2.setParameter("idOrganizator", idOrganizatora);
        Organizator organizator = query2.getSingleResult();

        try {
            String jpql = "SELECT d FROM Dogadjaj d WHERE d.organizator = :organizator";
            TypedQuery<Dogadjaj> query1 = em.createQuery(jpql, Dogadjaj.class);
            query1.setParameter("organizator", organizator);
        
            // Lista svih dogadjaja datog organizatora
            List<Dogadjaj> dogadjaji = query1.getResultList();

            dodajListuDogadjaja(dogadjaji);
        } finally {
            em.close();
        }
    }

    @FXML
    public void initialize(){
      Platform.runLater(()-> {
        if(primaryController != null)
        pronadjiDogadjaje();
        else System.out.println("null sam");
    
      });
    }

}