import java.io.Serializable;

/**
 * Created by Ragonda on 26/02/2017.
 */
public class Utilisateur implements Serializable{

    private String nom;
    private String prenom;
    private String id;
    private String email;

    public Utilisateur(String id, String prenom, String nom, String email){
        this.id = id;
        this.prenom = prenom;
        this.nom = nom;
        this.email = email;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
