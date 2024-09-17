

package grupa8;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.util.List;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

public class KarticaController {
    @FXML
    private ImageView slikaDogadjaja;
    @FXML
    private Label nazivDogadjaja, datumDogadjaja;
    private Dogadjaj dogadjaj;
    @FXML
    private PrimaryController primaryController;
    
    public static boolean dugmad = false;

    private DogadjajController dogadjajController;

    EntityManager em = EntityManagerFactoryInstance.getInstance().getEntityManagerFactory().createEntityManager();

    public Dogadjaj getDogadjaj() {
        return dogadjaj;
    }
    public void setPrimaryController(PrimaryController x) {
        this.primaryController = x;
    }

    public void setDogadjaj(Dogadjaj x) {
        nazivDogadjaja.setText(x.getNaziv());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        datumDogadjaja.setText(x.getDatumVrijeme().format(formatter));
        if (x.getSlikaUrl() != null) {
            Image slika = new Image(getClass().getResourceAsStream(x.getSlikaUrl()));
            slikaDogadjaja.setImage(slika);
        } else {
            slikaDogadjaja.setImage(null);
        }
        this.dogadjaj = x;
    }

    public void eventCardClicked(MouseEvent e) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("dogadjaj.fxml"));
            Parent prijavaRoot = loader.load();
            DogadjajController controller = loader.getController();
            String imeKorisnika = primaryController.getImeKorisnika();
            System.out.println(imeKorisnika);
            TypedQuery<Korisnik.TipKorisnika> korisnikIdQuery = em
                .createQuery("SELECT k.tipKorisnika FROM Korisnik k WHERE k.korisnickoIme = :korisnickoIme", Korisnik.TipKorisnika.class);
            korisnikIdQuery.setParameter("korisnickoIme", imeKorisnika);
            List<Korisnik.TipKorisnika> tipKorisnika = korisnikIdQuery.getResultList();
            System.out.println(tipKorisnika);
           
        
            controller.setDogadjaj(this.dogadjaj);
            controller.setKarticaController(this);
            controller.setPrimaryController(primaryController);
            
            Stage stage = new Stage();
            stage.setTitle("Događaj");
            stage.setScene(new Scene(prijavaRoot, 860, 650));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.setResizable(false);
            stage.initOwner(((Stage) ((javafx.scene.Node) e.getSource()).getScene().getWindow()));
            if(tipKorisnika.isEmpty() || tipKorisnika.get(0).equals(Korisnik.TipKorisnika.ADMIN) ){
                dugmad = true;
                System.out.println("dugmad su:" + dugmad); 
         } 
         else {
            dugmad = false;
            System.out.println("dugmad su: " + dugmad);
         }
            stage.showAndWait();
               
        } catch (IOException em) {
            em.printStackTrace();
        }
    }

    @FXML
    public void initialize() {
    }
}