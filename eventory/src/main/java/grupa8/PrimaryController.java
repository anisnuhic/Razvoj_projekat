package grupa8;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PrimaryController {
    @FXML
    private GridPane resetka;
    @FXML
    private HBox cijenaContainer;
    private FilterDefinition filterDefinition;
    @FXML
    private Button prijavaButton;
    @FXML
    private Button odjavaButton;
    @FXML
    private Button napraviButton;
    @FXML
    private Button registracijaButton;
    @FXML
    private Label imeKorisnika;
    @FXML
    private Label tipKorisnika;
    @FXML
    private Button urediProfil;
    @FXML 
    public void hideButton(){
        registracijaButton.setVisible(false);
        prijavaButton.setVisible(false);
        odjavaButton.setVisible(true);
        napraviButton.setVisible(true);
        imeKorisnika.setVisible(true);
        tipKorisnika.setVisible(true);
        urediProfil.setVisible(true);
    }
    @FXML
    private void odjavaAction(){
        registracijaButton.setVisible(true);
        prijavaButton.setVisible(true);
        odjavaButton.setVisible(false);
        napraviButton.setVisible(false);
        imeKorisnika.setVisible(false);
        tipKorisnika.setVisible(false);
        urediProfil.setVisible(false);
    }
    public void setKorisnickoIme(String korisnickoIme, String tip) {
        imeKorisnika.setText(korisnickoIme);
        tipKorisnika.setText(tip);
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
    public void initialize() {
        // Kreiraj listu za Dogadjaj objekte
        List<DogadjajStari> listaDogadjaja = new ArrayList<>();

        // Kreiraj 5 objekata Dogadjaj sa hardkodiranim vrijednostima
        listaDogadjaja.add(new DogadjajStari("Aleksandra Prijovic", "/grupa8/assets/slikeDogadjaja/aleksandra.png", LocalDate.of(2024, 8, 23))); // 15. avgust 2024
        listaDogadjaja.add(new DogadjajStari("Izložba umetnosti", "/grupa8/assets/slikeDogadjaja/boks_mec.png", LocalDate.of(2024, 8, 23))); // 20. septembar 2024
        listaDogadjaja.add(new DogadjajStari("Lepa Brena koncert", "/grupa8/assets/slikeDogadjaja/brena.png", LocalDate.of(2024, 8, 23))); // 10. oktobar 2024
        listaDogadjaja.add(new DogadjajStari("Pozorišna predstava", "/grupa8/assets/slikeDogadjaja/heni.png", LocalDate.of(2024, 8, 23))); // 5. novembar 2024
        listaDogadjaja.add(new DogadjajStari("Tehnička konferencija", "/grupa8/assets/slikeDogadjaja/folk_fest.png", LocalDate.of(2024, 8, 23))); // 25. decembar 2024
        resetka.getChildren().clear();
        int row = 0;
        int col = 0;
        // Ispis liste Dogadjaj objekata
        for (DogadjajStari d : listaDogadjaja) {
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

        EntityManagerFactoryInstance.init();
        this.filterDefinition = new FilterDefinition();
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
        //cijenaContainer.getChildren().clear();
        Button button = new Button(x + " -" + y + "  x");
        button.getStyleClass().add("filter-button");
        button.setOnAction(event -> cijenaContainer.getChildren().remove(button));
        cijenaContainer.getChildren().addAll(button);
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
//        EntityManagerFactory emf = Persistence.createEntityManagerFactory("eventoryPU");
//        EntityManager em = emf.createEntityManager();
//        Connection connection = em.unwrap(Connection.class);
//
//        CriteriaBuilder cb = em.getCriteriaBuilder();
//        CriteriaQuery<Dogadjaj> cq = cb.createQuery(Dogadjaj.class);
//        Root<Dogadjaj> root = cq.from(Dogadjaj.class);
//
//        List<Predicate> predicates = new ArrayList<>();
//
//        if (name != null) {
//            predicates.add(cb.equal(root.get("name"), name));
//        }
//
//        if (age != null) {
//            predicates.add(cb.equal(root.get("age"), age));
//        }
//
//        cq.where(predicates.toArray(new Predicate[0]));
//
//        return em.createQuery(cq).getResultList();
        System.out.println(filterDefinition);
    }

    public void resetFilters(){
        this.filterDefinition = new FilterDefinition();
    }
}
