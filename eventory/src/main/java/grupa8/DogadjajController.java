package grupa8;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.time.format.DateTimeFormatter;
import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

public class DogadjajController {

    @FXML
    private ImageView slikaDogadjaja, slikaLokacije;

    @FXML
    private Label nazivDogadjaja, datumDogadjaja, gradDogadjaja, mjestoDogadjaja, opisDogadjaja;

    @FXML
    private VBox sektori;
    @FXML
    private Button openNewViewButton, kupovinaButton, rezervacijaButton;

    @FXML
    private StackPane stek;
    @FXML
    private KarticaController karticaController;

    private PrimaryController primaryController;
    public void setPrimaryController(PrimaryController primaryController) {
        this.primaryController = primaryController;
    }

    public void setKarticaController(KarticaController x) {
        this.karticaController = x;
    }

    public void disableButtons() {
        kupovinaButton.setDisable(true);
        rezervacijaButton.setDisable(true);
    }
    EntityManager em = EntityManagerFactoryInstance.getInstance().getEntityManagerFactory().createEntityManager();
    List<Sektor> lista_sektora;
    public List<Sektor> getSektoriByLokacija(Lokacija lokacija) {
        String jpql = "SELECT s FROM Sektor s WHERE s.lokacija = :lokacija";
        TypedQuery<Sektor> query = em.createQuery(jpql, Sektor.class);
        query.setParameter("lokacija", lokacija);
        return query.getResultList();
    }
    public Karta getKartaBySektor(Sektor sektor) {
        String jpql = "SELECT k FROM Karta k WHERE k.sektor = :sektor";
        TypedQuery<Karta> query = em.createQuery(jpql, Karta.class);
        query.setParameter("sektor", sektor);
        return query.getSingleResult(); // Pretpostavimo da ima samo jedna karta po sektoru
    }

    public void setDogadjaj(Dogadjaj x) {
        Lokacija lokacija = x.getLokacija();
        if (lokacija != null) {
            mjestoDogadjaja.setText(lokacija.getNaziv());
            gradDogadjaja.setText(lokacija.getGrad());
        }
        else {
            mjestoDogadjaja.setText("N/A");
            gradDogadjaja.setText("N/A");
        }
        nazivDogadjaja.setText(x.getNaziv());
        opisDogadjaja.setText(x.getOpis());
        lista_sektora = getSektoriByLokacija(lokacija);
        System.out.println(lista_sektora);
        for(Sektor s : lista_sektora){
            Karta karta = getKartaBySektor(s);
            if(karta != null){
                Label sektorLabel = new Label (s.getNazivSektora() + ": "  + karta.getCijena() + "KM");
                sektorLabel.setStyle("-fx-text-fill: white; -fx-text-weight: bold");
                sektori.getChildren().add(sektorLabel);
            }
            else{
                Label sektorLabel = new Label (s.getNazivSektora() + " N/A");
                sektorLabel.setStyle("-fx-text-fill: white;");
                sektori.getChildren().add(sektorLabel);
            }
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy  HH:mm");
        datumDogadjaja.setText(x.getDatumVrijeme().format(formatter));
        if (x.getSlikaUrl() != null) {
            Image slika = new Image(getClass().getResourceAsStream(x.getSlikaUrl()));
            slikaDogadjaja.setImage(slika);
        }
          if(lokacija.getSlikaUrl() != null){
              Image slika2 = new Image (getClass().getResourceAsStream(lokacija.getSlikaUrl()));
              slikaLokacije.setImage(slika2);
          }
        Rectangle2D viewport = new Rectangle2D(0, 0, 700, 330);
        slikaDogadjaja.setViewport(viewport);
    }

    @FXML
    private void rezervacijaClicked(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("kupovina.fxml"));
            Parent parent = fxmlLoader.load();

            KupovinaController kupovinaController = fxmlLoader.getController();
            kupovinaController.setDogadjajController(this);
            if (this != null)System.out.println("razlicit sam");
            kupovinaController.stek = this.stek; // Postavi StackPane
            //kupovinaController.setAkcijskiButtonText("Kupi");
            kupovinaController.setAkcijskiButtonText("Rezerviši");
            stek.getChildren().add(parent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void kupovinaClicked(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("kupovina.fxml"));
            Parent parent = fxmlLoader.load();

            KupovinaController kupovinaController = fxmlLoader.getController();
            kupovinaController.setDogadjajController(this);

            kupovinaController.stek = this.stek; // Postavi StackPane
            kupovinaController.setAkcijskiButtonText("Kupi");
            // kupovinaController.setAkcijskiButtonText("Rezerviši");
            stek.getChildren().add(parent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize(){
        
    }
}
