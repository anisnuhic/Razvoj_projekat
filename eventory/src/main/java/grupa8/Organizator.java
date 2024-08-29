package grupa8;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.OneToOne;
import jakarta.persistence.JoinColumn;

@Entity
@Table(name = "organizatori")
public class Organizator {

    @Id
    @Column(name = "organizator_id")
    private Integer organizatorId;

    @Column(name = "naziv_organizacije", nullable = false)
    private String nazivOrganizacije;

    @Column(name = "kontakt_osoba", nullable = false)
    private String kontaktOsoba;

    @Column(name = "telefon")
    private String telefon;

    @Column(name = "adresa")
    private String adresa;

    @OneToOne
    @JoinColumn(name = "organizator_id", referencedColumnName = "korisnik_id", insertable = false, updatable = false)
    private Korisnik korisnik;

    // Getters and Setters

    public Integer getOrganizatorId() {
        return organizatorId;
    }

    public void setOrganizatorId(Integer organizatorId) {
        this.organizatorId = organizatorId;
    }

    public String getNazivOrganizacije() {
        return nazivOrganizacije;
    }

    public void setNazivOrganizacije(String nazivOrganizacije) {
        this.nazivOrganizacije = nazivOrganizacije;
    }

    public String getKontaktOsoba() {
        return kontaktOsoba;
    }

    public void setKontaktOsoba(String kontaktOsoba) {
        this.kontaktOsoba = kontaktOsoba;
    }

    public String getTelefon() {
        return telefon;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    public String getAdresa() {
        return adresa;
    }

    public void setAdresa(String adresa) {
        this.adresa = adresa;
    }

    public Korisnik getKorisnik() {
        return korisnik;
    }

    public void setKorisnik(Korisnik korisnik) {
        this.korisnik = korisnik;
    }
}
