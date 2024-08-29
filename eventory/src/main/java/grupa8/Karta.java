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
import java.time.LocalDate;

@Entity
@Table(name = "karte")
public class Karta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "karta_id")
    private Integer kartaId;

    @ManyToOne
    @JoinColumn(name = "dogadjaj_id", nullable = false)
    private Dogadjaj dogadjaj;

    @ManyToOne
    @JoinColumn(name = "sektor_id", nullable = false)
    private Sektor sektor;

    @Column(name = "cijena", nullable = false)
    private BigDecimal cijena;

    @Column(name = "mjesto")
    private String mjesto;

    @Column(name = "datum_pocetka_prodaje")
    private LocalDate datumPocetkaProdaje;

    @Column(name = "datum_zavrsetka_prodaje")
    private LocalDate datumZavrsetkaProdaje;

    @Column(name = "maksimalan_broj_karti_po_korisniku")
    private Integer maksimalanBrojKartiPoKorisniku;

    @Column(name = "uslov_otkazivanja")
    private String uslovOtkazivanja;

    // Getters and Setters

    public Integer getKartaId() {
        return kartaId;
    }

    public void setKartaId(Integer kartaId) {
        this.kartaId = kartaId;
    }

    public Dogadjaj getDogadjaj() {
        return dogadjaj;
    }

    public void setDogadjaj(Dogadjaj dogadjaj) {
        this.dogadjaj = dogadjaj;
    }

    public Sektor getSektor() {
        return sektor;
    }

    public void setSektor(Sektor sektor) {
        this.sektor = sektor;
    }

    public BigDecimal getCijena() {
        return cijena;
    }

    public void setCijena(BigDecimal cijena) {
        this.cijena = cijena;
    }

    public String getMjesto() {
        return mjesto;
    }

    public void setMjesto(String mjesto) {
        this.mjesto = mjesto;
    }

    public LocalDate getDatumPocetkaProdaje() {
        return datumPocetkaProdaje;
    }

    public void setDatumPocetkaProdaje(LocalDate datumPocetkaProdaje) {
        this.datumPocetkaProdaje = datumPocetkaProdaje;
    }

    public LocalDate getDatumZavrsetkaProdaje() {
        return datumZavrsetkaProdaje;
    }

    public void setDatumZavrsetkaProdaje(LocalDate datumZavrsetkaProdaje) {
        this.datumZavrsetkaProdaje = datumZavrsetkaProdaje;
    }

    public Integer getMaksimalanBrojKartiPoKorisniku() {
        return maksimalanBrojKartiPoKorisniku;
    }

    public void setMaksimalanBrojKartiPoKorisniku(Integer maksimalanBrojKartiPoKorisniku) {
        this.maksimalanBrojKartiPoKorisniku = maksimalanBrojKartiPoKorisniku;
    }

    public String getUslovOtkazivanja() {
        return uslovOtkazivanja;
    }

    public void setUslovOtkazivanja(String uslovOtkazivanja) {
        this.uslovOtkazivanja = uslovOtkazivanja;
    }
}
