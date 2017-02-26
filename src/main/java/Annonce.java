import com.mongodb.BasicDBObject;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Ragonda on 20/01/2017.
 */
public class Annonce {

    private Map<String, String> proprietaire;
    private String villeDepart;
    private String villeArrivee;
    private int nbPlaces;
    private int prix;
    private List<String> passagers;
    private Date dateDepart;

    public Annonce(BasicDBObject basicDBObject){
        this.proprietaire = (Map) basicDBObject.get("proprietaire");
        this.villeDepart = basicDBObject.getString("villeDepart");
        this.villeArrivee = basicDBObject.getString("villeArrivee");
        this.prix = basicDBObject.getInt("prix");
        this.passagers = (List) basicDBObject.get("passager");
        this.dateDepart = basicDBObject.getDate("dateDepart");
        this.nbPlaces = basicDBObject.getInt("nbPlaces");
    }

    public Annonce(Map<String, String> proprietaire, String villeDepart, String villeArrivee, int nbPlaces, int prix, Date dateDepart){
        this.proprietaire = proprietaire;
        this.dateDepart = dateDepart;
        this.villeDepart = villeDepart;
        this.nbPlaces = nbPlaces;
        this.villeArrivee = villeArrivee;
        this.prix = prix;
        this.passagers = null;
    }

    public Map<String, String> getProprietaire() {
        return proprietaire;
    }

    public void setProprietaire(Map<String, String> proprietaire) {
        this.proprietaire = proprietaire;
    }

    public String getVilleDepart() {
        return villeDepart;
    }

    public void setVilleDepart(String villeDepart) {
        this.villeDepart = villeDepart;
    }

    public String getVilleArrivee() {
        return villeArrivee;
    }

    public void setVilleArrivee(String villeArrivee) {
        this.villeArrivee = villeArrivee;
    }

    public int getPrix() {
        return prix;
    }

    public void setPrix(int prix) {
        this.prix = prix;
    }

    public List<String> getPassagers() {
        return passagers;
    }

    public void setPassagers(List<String> passagers) {
        this.passagers = passagers;
    }

    public Date getDateDepart() {
        return dateDepart;
    }

    public void setDateDepart(Date dateDepart) {
        this.dateDepart = dateDepart;
    }

    public int getNbPlaces() {
        return nbPlaces;
    }

    public void setNbPlaces(int nbPlaces) {
        this.nbPlaces = nbPlaces;
    }
}
