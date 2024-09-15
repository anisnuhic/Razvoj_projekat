package grupa8;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "korisnici")
public class Korisnik {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "korisnik_id")
    private int korisnikId;

    @Column(name = "ime", nullable = false, length = 100)
    private String ime;

    @Column(name = "prezime", nullable = false, length = 100)
    private String prezime;

    @Column(name = "korisnicko_ime", nullable = false, length = 100)
    private String korisnickoIme;

    @Column(name = "email", nullable = false, length = 100, unique = true)
    private String email;

    @Column(name = "lozinka", nullable = false, length = 255)
    private String lozinka;

    @Column(name = "datum_registracije", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date datumRegistracije;

    @Enumerated(EnumType.STRING)
    @Column(name = "tip_korisnika", nullable = false)
    private TipKorisnika tipKorisnika;

    @Column(name = "novcanik")
    private BigDecimal novcanik;
    // Getteri i setteri

    public int getKorisnikId() {
        return korisnikId;
    }

    public void setKorisnikId(int korisnikId) {
        this.korisnikId = korisnikId;
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getPrezime() {
        return prezime;
    }

    public void setPrezime(String prezime) {
        this.prezime = prezime;
    }

    public String getKorisnickoIme() {
        return korisnickoIme;
    }

    public void setKorisnickoIme(String korisnickoIme) {
        this.korisnickoIme = korisnickoIme;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLozinka() {
        return lozinka;
    }

    public void setLozinka(String lozinka) {
        this.lozinka = lozinka;
    }

    public Date getDatumRegistracije() {
        return datumRegistracije;
    }

    public void setDatumRegistracije(Date datumRegistracije) {
        this.datumRegistracije = datumRegistracije;
    }

    public TipKorisnika getTipKorisnika() {
        return tipKorisnika;
    }

    public void setTipKorisnika(TipKorisnika tipKorisnika) {
        this.tipKorisnika = tipKorisnika;
    }

    public BigDecimal getNovcanik(){
        return novcanik;
    }

    public void setNovcanik(BigDecimal novcanik){
        this.novcanik = novcanik;
    }

    // Enum za tip korisnika
    public enum TipKorisnika {
        REGULAR,
        ORGANIZATOR,
        ADMIN
    }
}

