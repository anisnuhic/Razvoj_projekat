package grupa8;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class PrimaryController {
    @FXML
    private GridPane resetka;
    @FXML
    private HBox cijenaContainer;
    @FXML
    private Button registracijaButton,napraviButton, odjavaButton, prijavaButton, urediProfil, zahtjevi, uredi_lokacije;
    @FXML
    private Label tipKorisnika, imeKorisnika;
    @FXML
    private ImageView icon1;
    @FXML
    private Button muzikaButton, kulturaButton, sportButton, ostaloButton;
    @FXML
    private TextField searchText;
    

    private FilterDefinition filterDefinition;

    @FXML
    private void handleSearchAction() {
        System.out.println("Search action called");
        deselectCategoryButtons(kulturaButton, sportButton, ostaloButton, muzikaButton);
        cijenaContainer.getChildren().clear();
        filterDefinition.resetFilters();
        filterDefinition.setSearchText(searchText.getText());
        filter();
    }

    @FXML
    private void muzikaClicked(ActionEvent event) {
        selectCategoryButton(muzikaButton);
        deselectCategoryButtons(kulturaButton, sportButton, ostaloButton);
        handleCategoryChanged(CategoryEnum.MUZIKA);
    }

    @FXML
    private void kulturaClicked(ActionEvent event) {
        selectCategoryButton(kulturaButton);
        deselectCategoryButtons(sportButton, ostaloButton, muzikaButton);
        handleCategoryChanged(CategoryEnum.KULTURA);
    }

    @FXML
    private void sportClicked(ActionEvent event) {
        selectCategoryButton(sportButton);
        deselectCategoryButtons(kulturaButton, ostaloButton, muzikaButton);
        handleCategoryChanged(CategoryEnum.SPORT);
    }

    @FXML
    private void ostaloClicked(ActionEvent event) {
        selectCategoryButton(ostaloButton);
        deselectCategoryButtons(kulturaButton, sportButton, muzikaButton);
        handleCategoryChanged(CategoryEnum.OSTALO);
    }

    @FXML
    public void hideButton() {
        registracijaButton.setVisible(false);
        prijavaButton.setVisible(false);
        odjavaButton.setVisible(true);
        napraviButton.setVisible(true);
        imeKorisnika.setVisible(true);
        tipKorisnika.setVisible(true);
        urediProfil.setVisible(true);
        icon1.setVisible(true);
        resetFilters();
        muzikaButton.setStyle("-fx-background-color: #333333; -fx-text-fill: white;");
        sportButton.setStyle("-fx-background-color: #333333; -fx-text-fill: white;");
        ostaloButton.setStyle("-fx-background-color: #333333; -fx-text-fill: white;");
        kulturaButton.setStyle("-fx-background-color: #333333; -fx-text-fill: white;");
    }
    public void hideButtonAdmin() {
        registracijaButton.setVisible(false);
        prijavaButton.setVisible(false);
        odjavaButton.setVisible(true);
        imeKorisnika.setVisible(true);
        uredi_lokacije.setVisible(true);
        zahtjevi.setVisible(true);
        tipKorisnika.setVisible(true);
        urediProfil.setVisible(false);
        icon1.setVisible(true);
        resetFilters();
        muzikaButton.setStyle("-fx-background-color: #333333; -fx-text-fill: white;");
        sportButton.setStyle("-fx-background-color: #333333; -fx-text-fill: white;");
        ostaloButton.setStyle("-fx-background-color: #333333; -fx-text-fill: white;");
        kulturaButton.setStyle("-fx-background-color: #333333; -fx-text-fill: white;");
    }

    @FXML
    private void odjavaAction() {
        registracijaButton.setVisible(true);
        prijavaButton.setVisible(true);
        odjavaButton.setVisible(false);
        napraviButton.setVisible(false);
        imeKorisnika.setVisible(false);
        tipKorisnika.setVisible(false);
        urediProfil.setVisible(false);
        icon1.setVisible(false);
        resetFilters();
        muzikaButton.setStyle("-fx-background-color: #333333; -fx-text-fill: white;");
        sportButton.setStyle("-fx-background-color: #333333; -fx-text-fill: white;");
        ostaloButton.setStyle("-fx-background-color: #333333; -fx-text-fill: white;");
        kulturaButton.setStyle("-fx-background-color: #333333; -fx-text-fill: white;");

    }
    
    @FXML
    private void zahtjeviButton(ActionEvent event){}
    @FXML
    private void lokacijeButton(ActionEvent event){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("adminLokacija.fxml"));
            Parent registracijaRoot = fxmlLoader.load();
            AdminLokacijaController adminController = fxmlLoader.getController();
            adminController.setPrimaryController(this);
            Stage stage = new Stage();
            stage.setTitle("Uredi Lokacije");
            stage.setScene(new Scene(registracijaRoot));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(((Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow()));
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void handlePrijavaButtonAction(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("prijava.fxml"));
            Parent prijavaRoot = fxmlLoader.load();
            PrijavaController prijavaController = fxmlLoader.getController();
            prijavaController.setPrimaryController(this);
            Stage stage = new Stage();
            stage.setTitle("Prijava");
            stage.setScene(new Scene(prijavaRoot));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(((Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow()));
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleRegistracijaButtonAction(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("registracija.fxml"));
            Parent registracijaRoot = fxmlLoader.load();
            RegistracijaController registracijaController = fxmlLoader.getController();
            registracijaController.setPrimaryController(this);
            Stage stage = new Stage();
            stage.setTitle("Registracija");
            stage.setScene(new Scene(registracijaRoot));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(((Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow()));
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void noviDogadjajButtonAction(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("informacije.fxml"));
            Parent registracijaRoot = fxmlLoader.load();
            InformacijeController informacijaController = fxmlLoader.getController();
            informacijaController.setPrimaryController(this);
            Stage stage = new Stage();
            stage.setTitle("Napravi dogadjaj");
            stage.setScene(new Scene(registracijaRoot));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(((Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow()));
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize() {
        addDogadjajListToResetka(getInitDogadjajList());
        EntityManagerFactoryInstance.init();
        this.filterDefinition = FilterDefinition.getInstance();
    }

    @FXML
    void updatePrice(String x, String y) {
        if (x.isEmpty() && y.isEmpty()) {
            cijenaContainer.getChildren().removeIf(node -> node.getStyleClass().contains("price"));
        } else {
            Button button = new Button(x + " -" + y + " KM " + "  x");
            button.getStyleClass().add("filter-button");
            button.getStyleClass().add("price");
            button.setOnAction(event -> {
                cijenaContainer.getChildren().remove(button);
                filterDefinition.setPrice("", "");
                filter();
            });
            cijenaContainer.getChildren().removeIf(node -> node.getStyleClass().contains("price"));
            cijenaContainer.getChildren().add(button);
        }
        filterDefinition.setPrice(x, y);
        filter();
    }

    @FXML
    private void handleCijenaButtonAction(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("cijena.fxml"));
            Parent prijavaRoot = fxmlLoader.load();
            CijenaController kontroler = fxmlLoader.getController();
            kontroler.setPrimaryController(this);
            Stage stage = new Stage();
            stage.setTitle("Filter Cijena");
            stage.setScene(new Scene(prijavaRoot));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(((Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow()));
            stage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void updateDate(String x, String y) {
        if (x.isEmpty() && y.isEmpty()) {
            cijenaContainer.getChildren().removeIf(node -> node.getStyleClass().contains("date"));
        } else {
            Button button = new Button(x + " -" + y + "  x");
            button.getStyleClass().add("filter-button");
            button.getStyleClass().add("date");
            button.setOnAction(event -> {
                cijenaContainer.getChildren().remove(button);
                filterDefinition.setDate("", "");
                filter();
            });
            cijenaContainer.getChildren().removeIf(node -> node.getStyleClass().contains("date"));
            cijenaContainer.getChildren().add(button);
        }
        filterDefinition.setDate(x, y);
        filter();
    }

    @FXML
    private void handleDatumButtonAction(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("datum.fxml"));
            Parent prijavaRoot = fxmlLoader.load();
            DatumController kontroler = fxmlLoader.getController();
            kontroler.setPrimaryController(this);
            Stage stage = new Stage();
            stage.setTitle("Filter Datum");
            stage.setScene(new Scene(prijavaRoot));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(((Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow()));
            stage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleLokacijaButtonAction(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("lokacija.fxml"));
            Parent prijavaRoot = fxmlLoader.load();

            LokacijaController lokacijaController = fxmlLoader.getController();
            lokacijaController.setPrimaryController(this);

            Stage stage = new Stage();
            stage.setTitle("Filter Lokacija");
            stage.setScene(new Scene(prijavaRoot));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(((Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow()));
            stage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void updateLocations(List<String> locations) {
        cijenaContainer.getChildren().removeIf(node -> node.getStyleClass().contains("location"));
        for (String location : locations) {
            Button button = new Button(location + "  x");
            button.getStyleClass().add("filter-button");
            button.getStyleClass().add("location");
            button.setOnAction(event -> {
                cijenaContainer.getChildren().remove(button);
                filterDefinition.removeLocation(button.getText().substring(0, button.getText().length() - 3));
                filter();
            });
            cijenaContainer.getChildren().add(button);
        }
        filterDefinition.setLocationNames(locations);
        filter();
    }

    @FXML
    void filter() {
        addDogadjajListToResetka(FilterUtil.filterDogadjaj());
        System.out.println(filterDefinition);
    }

    public void setKorisnickoIme(String korisnickoIme, String tip) {
        imeKorisnika.setText(korisnickoIme);
        tipKorisnika.setText(tip);
    }

    private void handleCategoryChanged(CategoryEnum categoryEnum) {
        cijenaContainer.getChildren().clear();
        filterDefinition.resetFilters();
        filterDefinition.setCategory(categoryEnum.name());
        filter();
    }

    private void deselectCategoryButtons(Button... buttons) {
        for (Button button : buttons) {
            button.setStyle("-fx-background-color: #333333; -fx-text-fill: white;");
        }
    }

    private void selectCategoryButton(Button button) {
        button.setStyle("-fx-background-color: white; -fx-text-fill: black;");
    }

    private List<Dogadjaj> getInitDogadjajList() {
    // Kreiraj EntityManager
    EntityManager em = EntityManagerFactoryInstance.getInstance().getEntityManagerFactory().createEntityManager();

    // Kreiraj upit za dohvaćanje svih Dogadjaj objekata iz baze podataka
    List<Dogadjaj> listaDogadjaja = em.createQuery("SELECT d FROM Dogadjaj d", Dogadjaj.class).getResultList();

    // Vrati listu dogadjaja
    return listaDogadjaja;
}


    public void resetFilters() {
        cijenaContainer.getChildren().clear();
        this.filterDefinition.resetFilters();
        filter();
    }

    private void addDogadjajListToResetka(List<Dogadjaj> listaDogadjaja) {
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
                controller.setPrimaryController(this);
                resetka.add(eventCard, col, row);
                col++;
                if (col == 2) {
                    col = 0;
                    row++;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
