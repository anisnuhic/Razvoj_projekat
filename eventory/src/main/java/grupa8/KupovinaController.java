package grupa8;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class KupovinaController {

    @FXML
    StackPane stek;

    @FXML
    private Button akcijskiButton;

    @FXML
    private ImageView strelica1, strelica2;
   
    @FXML
    private Label brojKartiLabela, cijenaKartiLabela;

    @FXML
    private VBox vBox;

    private DogadjajController dogadjajController;

    private KarticaController karticaController;
    public void setDogadjajController(DogadjajController x) {
        this.dogadjajController = x;
    }
    public void setKarticaController(KarticaController x) {
        this.karticaController = x;
    }

    private int brojKarti = 0; 
    EntityManager em = EntityManagerFactoryInstance.getInstance().getEntityManagerFactory().createEntityManager();
    
    public int maxKarti(Dogadjaj x){
        TypedQuery<Integer> query = em.createQuery("SELECT k.maksimalanBrojKartiPoKorisniku from Karta k WHERE k.dogadjaj:= x", Integer.class);
        query.setParameter("x", x);
        return query.getSingleResult();
    }
    @FXML
    private void vratiSeNazad(ActionEvent event) {
        try {
            stek.getChildren().remove(stek.getChildren().size() - 1);  
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setAkcijskiButtonText(String text) {
        akcijskiButton.setText(text);
    }

    @FXML
    private void povecajBrojKarti(ActionEvent event) {
       if(((dogadjajController.getKartaBySektor(dogadjajController.lista_sektora.get(1),karticaController.getDogadjaj())).getMaksimalanBrojKartiPoKorisniku()> brojKarti))
        brojKarti++;
        else{
            //printat u labelu neki da je to max broj karti po korisniku
        }
        azurirajBrojKartiLabelu();
        azurirajUkupnuCijenu();
    }

    @FXML
    private void smanjiBrojKarti(ActionEvent event) {
        if (brojKarti > 0) {
            brojKarti--;
        }
        azurirajBrojKartiLabelu();
        azurirajUkupnuCijenu();
    }

    private void azurirajBrojKartiLabelu() {
        brojKartiLabela.setText(String.valueOf(brojKarti));
    }

    @FXML
    public void initialize() {
        Platform.runLater(() -> {
            vBox.getChildren().clear();
            strelica1.setMouseTransparent(true);
            strelica2.setMouseTransparent(true);
            if (dogadjajController == null) {
                System.out.println("DogadjajController nije postavljen");
                return;
            }
    
            for (Sektor s : dogadjajController.lista_sektora) {
                CheckBox check = new CheckBox();
                Karta karta = dogadjajController.getKartaBySektor(s,karticaController.getDogadjaj());
                System.out.println(karticaController.getDogadjaj());
                check.setText(s.getNazivSektora() + ": " + karta.getCijena() + "KM");
                check.setStyle("-fx-text-fill: white;");
                
                // Dodaj listener za izbor jednog checkbox-a
                check.selectedProperty().addListener((obs, wasSelected, isNowSelected) -> {
                    if (isNowSelected) {
                        // Poništi sve ostale checkboxove
                        for (var node : vBox.getChildren()) {
                            if (node instanceof CheckBox && node != check) {
                                ((CheckBox) node).setSelected(false);
                            }
                        }
                    }
                    azurirajUkupnuCijenu();
                });
    
                vBox.getChildren().add(check);
            }
            
        });
    }
    private void azurirajUkupnuCijenu() {
        // Pronađi odabrani sektor
        for (var node : vBox.getChildren()) {
            if (node instanceof CheckBox) {
                CheckBox checkBox = (CheckBox) node;
                if (checkBox.isSelected()) {
                    // Izdvoji cijenu iz naziva CheckBox-a (pretpostavljam da je format: "Naziv sektora: XXKM")
                    String text = checkBox.getText();
                    String[] parts = text.split(":");
                    if (parts.length > 1) {
                        String cijenaString = parts[1].trim().replace("KM", "");
                        try {
                            double cijenaKarte = Double.parseDouble(cijenaString);
                            // Izračunaj ukupnu cijenu
                            double ukupnaCijena = brojKarti * cijenaKarte;
                            // Prikaz ukupne cijene u labeli
                            cijenaKartiLabela.setText(String.format("%.2f KM", ukupnaCijena));
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }
    
}
