/**
 * Created by Ragonda on 26/02/2017.
 */
public class Passager{

    private String nom;
    private String prenom;
    private String id;

    public Passager(String id, String prenom, String nom){
        this.id = id;
        this.prenom = prenom;
        this.nom = nom;
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
}
