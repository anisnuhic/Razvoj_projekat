package grupa8;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "dogadjaji")
public class Dogadjaj {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dogadjaj_id")
    private Integer dogadjajId;

    @ManyToOne
    @JoinColumn(name = "organizator_id", nullable = false)
    private Organizator organizator;

    @Column(name = "naziv", nullable = false)
    private String naziv;

    @Column(name = "opis")
    private String opis;

    @ManyToOne
    @JoinColumn(name = "lokacija_id", nullable = false)
    private Lokacija lokacija;

    @Column(name = "datum_vrijeme", nullable = false)
    private LocalDateTime datumVrijeme;

    @Column(name = "vrsta_dogadjaja")
    private String vrstaDogadjaja;

    @Column(name = "podvrsta_dogadjaja")
    private String podvrstaDogadjaja;

    @Column(name = "slika_url")
    private String slikaUrl;

    @Column(name = "dodatne_informacije")
    private String dodatneInformacije;

    @Column(name = "odobreno")
    private Boolean odobreno = false;

    @OneToMany
    private List<Karta> karte;

    // Getters and Setters

    public Dogadjaj() {
        super();
    }

    public Dogadjaj(String x, String y, LocalDate z) {
        naziv = x;
        slikaUrl = y;
        datumVrijeme = z.atStartOfDay();
    }

    public Integer getDogadjajId() {
        return dogadjajId;
    }

    public void setDogadjajId(Integer dogadjajId) {
        this.dogadjajId = dogadjajId;
    }

    public Organizator getOrganizator() {
        return organizator;
    }

    public void setOrganizator(Organizator organizator) {
        this.organizator = organizator;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public Lokacija getLokacija() {
        return lokacija;
    }

    public void setLokacija(Lokacija lokacija) {
        this.lokacija = lokacija;
    }

    public LocalDateTime getDatumVrijeme() {
        return datumVrijeme;
    }

    public void setDatumVrijeme(LocalDateTime datumVrijeme) {
        this.datumVrijeme = datumVrijeme;
    }

    public String getVrstaDogadjaja() {
        return vrstaDogadjaja;
    }

    public void setVrstaDogadjaja(String vrstaDogadjaja) {
        this.vrstaDogadjaja = vrstaDogadjaja;
    }

    public String getPodvrstaDogadjaja() {
        return podvrstaDogadjaja;
    }

    public void setPodvrstaDogadjaja(String podvrstaDogadjaja) {
        this.podvrstaDogadjaja = podvrstaDogadjaja;
    }

    public String getSlikaUrl() {
        return slikaUrl;
    }

    public void setSlikaUrl(String slikaUrl) {
        this.slikaUrl = slikaUrl;
    }

    public String getDodatneInformacije() {
        return dodatneInformacije;
    }

    public void setDodatneInformacije(String dodatneInformacije) {
        this.dodatneInformacije = dodatneInformacije;
    }

    public Boolean getOdobreno() {
        return odobreno;
    }

    public void setOdobreno(Boolean odobreno) {
        this.odobreno = odobreno;
    }

    public List<Karta> getKarte() {
        return karte;
    }

    public void setKarte(List<Karta> karte) {
        this.karte = karte;
    }
}
