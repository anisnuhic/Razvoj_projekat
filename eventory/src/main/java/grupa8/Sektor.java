package grupa8;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;

@Entity
@Table(name = "sektori")
public class Sektor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sektor_id")
    private Integer sektorId;

    @ManyToOne
    @JoinColumn(name = "lokacija_id", nullable = false)
    private Lokacija lokacija;

    @Column(name = "naziv_sektora", nullable = false)
    private String nazivSektora;

    @Column(name = "kapacitet", nullable = false)
    private Integer kapacitet;

    // Getters and Setters

    public Integer getSektorId() {
        return sektorId;
    }

    public void setSektorId(Integer sektorId) {
        this.sektorId = sektorId;
    }

    public Lokacija getLokacija() {
        return lokacija;
    }

    public void setLokacija(Lokacija lokacija) {
        this.lokacija = lokacija;
    }

    public String getNazivSektora() {
        return nazivSektora;
    }

    public void setNazivSektora(String nazivSektora) {
        this.nazivSektora = nazivSektora;
    }

    public Integer getKapacitet() {
        return kapacitet;
    }

    public void setKapacitet(Integer kapacitet) {
        this.kapacitet = kapacitet;
    }
}
