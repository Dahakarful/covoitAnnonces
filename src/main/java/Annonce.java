import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;

import java.util.List;

/**
 * Created by Ragonda on 20/01/2017.
 */
public class Annonce {

    private Utilisateur proprietaire;
    private String villeDepart;
    private String villeArrivee;
    private int nbPlaces;
    private int prix;
    private List<Utilisateur> utilisateurs;
    private String dateDepart;

    public Annonce(BasicDBObject basicDBObject){
        BasicDBList proprietaire = (BasicDBList) basicDBObject.get("proprietaire");
        System.out.println(proprietaire.toString());
        String email = "";
        String nom = "";
        String prenom = "";
        String id = "";
        BasicDBObject[] proprietaireArr = proprietaire.toArray(new BasicDBObject[0]);
        for(BasicDBObject dbObj : proprietaireArr) {
            if(dbObj.get("email") != null){
                email = dbObj.get("email").toString();
            }
            if(dbObj.get("nom") != null){
                nom = dbObj.get("nom").toString();
            }
            if(dbObj.get("prenom") != null){
                prenom = dbObj.get("prenom").toString();
            }
            if(dbObj.get("id") != null){
                id = dbObj.get("id").toString();
            }
        }
        Utilisateur utilisateur = new Utilisateur(id, prenom, nom, email);
        this.proprietaire = utilisateur;
        this.villeDepart = basicDBObject.getString("villeDepart");
        this.villeArrivee = basicDBObject.getString("villeArrivee");
        this.prix = basicDBObject.getInt("prix");
        this.utilisateurs = (List) basicDBObject.get("passager");
        this.dateDepart = basicDBObject.getString("dateDepart");
        this.nbPlaces = basicDBObject.getInt("nbPlaces");
    }

    public Annonce(Utilisateur proprietaire, String villeDepart, String villeArrivee, int nbPlaces, int prix, String dateDepart){
        this.proprietaire = proprietaire;
        this.dateDepart = dateDepart;
        this.villeDepart = villeDepart;
        this.nbPlaces = nbPlaces;
        this.villeArrivee = villeArrivee;
        this.prix = prix;
        this.utilisateurs = null;
    }

    public Utilisateur getProprietaire() {
        return proprietaire;
    }

    public void setProprietaire(Utilisateur proprietaire) {
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

    public List<Utilisateur> getPassager() {
        return utilisateurs;
    }

    public void setPassager(List<Utilisateur> utilisateurs) {
        this.utilisateurs = utilisateurs;
    }

    public String getDateDepart() {
        return dateDepart;
    }

    public void setDateDepart(String dateDepart) {
        this.dateDepart = dateDepart;
    }

    public int getNbPlaces() {
        return nbPlaces;
    }

    public void setNbPlaces(int nbPlaces) {
        this.nbPlaces = nbPlaces;
    }
}
