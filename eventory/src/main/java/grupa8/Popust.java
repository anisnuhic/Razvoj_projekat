package grupa8;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import java.math.BigDecimal;

@Entity
@Table(name = "popusti")
public class Popust {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "popust_id")
    private Integer popustId;

    @ManyToOne
    @JoinColumn(name = "korisnik_id", nullable = false)
    private Korisnik korisnik;

    @Column(name = "broj_kupljenih_karti", nullable = false)
    private Integer brojKupljenihKarti = 0;

    @Column(name = "potroseni_iznos", nullable = false)
    private BigDecimal potroseniIznos = BigDecimal.ZERO;

    @Column(name = "ostvareni_popust", nullable = false)
    private BigDecimal ostvareniPopust = BigDecimal.ZERO;

    // Getters and Setters

    public Integer getPopustId() {
        return popustId;
    }

    public void setPopustId(Integer popustId) {
        this.popustId = popustId;
    }

    public Korisnik getKorisnik() {
        return korisnik;
    }

    public void setKorisnik(Korisnik korisnik) {
        this.korisnik = korisnik;
    }

    public Integer getBrojKupljenihKarti() {
        return brojKupljenihKarti;
    }

    public void setBrojKupljenihKarti(Integer brojKupljenihKarti) {
        this.brojKupljenihKarti = brojKupljenihKarti;
    }

    public BigDecimal getPotroseniIznos() {
        return potroseniIznos;
    }

    public void setPotroseniIznos(BigDecimal potroseniIznos) {
        this.potroseniIznos = potroseniIznos;
    }

    public BigDecimal getOstvareniPopust() {
        return ostvareniPopust;
    }

    public void setOstvareniPopust(BigDecimal ostvareniPopust) {
        this.ostvareniPopust = ostvareniPopust;
    }
}
