package grupa8;

import java.util.List;

import java.io.IOException;
import java.util.ArrayList;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

public class ListaKartiController {
    @FXML
    private VBox resetka;

    private PrimaryController primaryController;

     EntityManager em = EntityManagerFactoryInstance.getInstance().getEntityManagerFactory().createEntityManager();

    public void setPrimaryController(PrimaryController primaryController) {
        this.primaryController = primaryController;
    }

    //metod za pronalazak korisnika koji je prijavljen na osnovu imena
    private Korisnik nadjiKorisnika(String imeKorisnika){
        TypedQuery<Korisnik> query = em.createQuery("SELECT k from Korisnik k where k.korisnickoIme = :imeKorisnika", Korisnik.class);
        query.setParameter("imeKorisnika", imeKorisnika);
        return query.getSingleResult();
    }

    //metod za pronalazak svih razlicitih karti koje je taj korisnik kupio
    private List<Karta> razliciteKarteKupljene(Korisnik k){
        TypedQuery<Karta> query = em.createQuery("SELECT DISTINCT k.karta from Rezervacija k WHERE k.korisnik = :korisnik AND k.status = :status", Karta.class);
        query.setParameter("korisnik", k);
        query.setParameter("status", Rezervacija.Status.KUPLJENO);
        return query.getResultList();
    }
    //metod za pronalazak svih razlicitih karti koje je taj korisnik rezervisao
    private List<Karta> razliciteKarteRezervisane(Korisnik k){
        TypedQuery<Karta> query = em.createQuery("SELECT DISTINCT k.karta from Rezervacija k WHERE k.korisnik = :korisnik AND k.status = :status", Karta.class);
        query.setParameter("korisnik", k);
        query.setParameter("status", Rezervacija.Status.REZERVISANO);
        return query.getResultList();
    }
    
    //metod za izracunavanje broja karti koje su kupljene ili rezervisane 
    private List<Integer> izbrojiKarteKorisnika(List<Karta> listaKarti, Korisnik k){
        List<Integer> brojKartiRespektivno = new ArrayList<Integer>();
        for(Karta karta : listaKarti){
        TypedQuery<Long> query = em.createQuery("SELECT SUM(k.kolicina) FROM Rezervacija k WHERE k.korisnik = :korisnik AND k.karta =  :karta", Long.class);
        query.setParameter("korisnik", k);
        query.setParameter("karta", karta);
        int x = query.getSingleResult().intValue();
        brojKartiRespektivno.add(x);
    }
    return brojKartiRespektivno;
    }

    public void prikaziListuKarti(){
        String imeKorisnika = primaryController.getImeKorisnika();
        Korisnik korisnik = nadjiKorisnika(imeKorisnika);
        List<Karta> kupljeneKarteKorisnika = razliciteKarteKupljene(korisnik);
        List<Karta> rezervisaneKarteKorisnika = razliciteKarteRezervisane(korisnik);
        List<Integer> brojKupljenihKartirespektivno = izbrojiKarteKorisnika(kupljeneKarteKorisnika, korisnik);
        List<Integer> brojRezervisanihKartirespektivno = izbrojiKarteKorisnika(rezervisaneKarteKorisnika, korisnik);
        resetka.getChildren().clear();
        resetka.setSpacing(10.0);
        // Ispis liste Dogadjaj objekata
        for (int i = 0; i < brojKupljenihKartirespektivno.size(); i++) {
            Karta karta = kupljeneKarteKorisnika.get(i);
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("mojeKarte.fxml"));
                AnchorPane eventCard = loader.load();
                MojeKarteController controller = loader.getController();
                controller.inicijaliziraj(karta, korisnik);
                controller.postaviIzgledKarte(karta.getDogadjaj().getNaziv(), karta.getCijena().toString() + "KM", karta.getDogadjaj().getSlikaUrl(), karta.getSektor().getNazivSektora(), "KUPLJENO", brojKupljenihKartirespektivno.get(i));
                System.out.println("karte: " + karta  );
                System.out.println("Korisnik: " + korisnik);
                resetka.getChildren().add(eventCard);
            } catch (IOException e) {
                e.printStackTrace();
            }

            
        }
        for (int i = 0; i < brojRezervisanihKartirespektivno.size(); i++) {
            Karta karta = rezervisaneKarteKorisnika.get(i);
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("mojeKarte.fxml"));
                AnchorPane eventCard = loader.load();
                MojeKarteController controller = loader.getController();
                controller.postaviIzgledKarte(karta.getDogadjaj().getNaziv(), karta.getCijena().toString() + "KM", karta.getDogadjaj().getSlikaUrl(), karta.getSektor().getNazivSektora(), "REZERVISANO", brojRezervisanihKartirespektivno.get(i));
                resetka.getChildren().add(eventCard);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
    }

    

    @FXML
    public void initialize(){
      Platform.runLater(()-> {
        if(primaryController != null)
        prikaziListuKarti();
        else System.out.println("null sam");
    
      });
    }
}
