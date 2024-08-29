package grupa8;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import jakarta.persistence.Column;

@Entity
@Table(name = "lokacije")
public class Lokacija {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lokacija_id")
    private Integer lokacijaId;

    @Column(name = "naziv", nullable = false)
    private String naziv;

    @Column(name = "adresa", nullable = false)
    private String adresa;

    @Column(name = "grad", nullable = false)
    private String grad;

    @Column(name = "kapacitet", nullable = false)
    private Integer kapacitet;

    @Column(name = "slika_url")
    private String slikaUrl;

    // Getters and Setters

    public Integer getLokacijaId() {
        return lokacijaId;
    }

    public void setLokacijaId(Integer lokacijaId) {
        this.lokacijaId = lokacijaId;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public String getAdresa() {
        return adresa;
    }

    public void setAdresa(String adresa) {
        this.adresa = adresa;
    }

    public String getGrad() {
        return grad;
    }

    public void setGrad(String grad) {
        this.grad = grad;
    }

    public Integer getKapacitet() {
        return kapacitet;
    }

    public void setKapacitet(Integer kapacitet) {
        this.kapacitet = kapacitet;
    }

    public String getSlikaUrl() {
        return slikaUrl;
    }

    public void setSlikaUrl(String slikaUrl) {
        this.slikaUrl = slikaUrl;
    }
}
