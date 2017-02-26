import com.mongodb.*;
import org.bson.types.ObjectId;

import javax.swing.text.Document;
import java.util.*;

/**
 * Created by Ragonda on 20/01/2017.
 */
public class AnnonceDao {

    private final DB db;
    private final DBCollection collection;
    private final DBCollection utilisateurs;
    static List<Annonce> annonces = new LinkedList<>();

    public AnnonceDao(DB db){
        this.db = db;
        this.collection = db.getCollection("annonces");
        this.utilisateurs = db.getCollection("utilisateurs");
    }

    public void ajouterAnnonce(Annonce annonce){
        System.out.println(annonce);
        System.out.println("Proprietaire " + annonce.getProprietaire() + " villeDepart " + annonce.getVilleDepart()
        + " villeArrivee " + annonce.getVilleArrivee() + " nbPlaces " + annonce.getNbPlaces()
        + " prix " + annonce.getPrix() + " dateDepart " + annonce.getDateDepart() + " passagers " + annonce.getPassager());
        Map<String,String> map = new HashMap<String,String>();
        map.put("email", annonce.getProprietaire().toString());
        DBObject doc = new BasicDBObject("proprietaire", map)
                .append("villeDepart", annonce.getVilleDepart())
                .append("villeArrivee", annonce.getVilleArrivee())
                .append("nbPlaces", annonce.getNbPlaces())
                .append("prix", annonce.getPrix())
                .append("dateDepart", annonce.getDateDepart())
                .append("passagers", null);
        collection.insert(doc);
        annonces.add(annonce);
    }

    public void supprimerAnnonce(String idUtilisateur, String idAnnonce){
        DBObject doc = new BasicDBObject();
        collection.remove(doc);
    }

    public List<Annonce> listAnnonces(){
        List<Annonce> annonces = new ArrayList<>();
        DBCursor cursor = collection.find();
        while(cursor.hasNext()){
            DBObject object = cursor.next();
            BasicDBObject annonceOb = (BasicDBObject) object;
            Annonce annonce = new Annonce(annonceOb);

            //Rechercher le proprietaire de l'annonce
            BasicDBObject query = new BasicDBObject();
            String email = annonce.getProprietaire().get("email").trim().replace("}", "").substring(7);
            query.put("email", email);
            DBObject proprietaire = utilisateurs.findOne(query);
            BasicDBObject utilisateurOb = (BasicDBObject) proprietaire;
            System.out.println(utilisateurOb);
            Map<String, String> map = annonce.getProprietaire();
            map.put("email", email);
            map.put("_id", utilisateurOb.getString("_id"));
            map.put("nom", utilisateurOb.getString("nom"));
            map.put("prenom", utilisateurOb.getString("prenom"));
            annonce.setProprietaire(map);
            annonces.add(annonce);
        }
        return annonces;
    }

    public String reserver(String reservation, String proprietaire, String villeDepart, String villeArrivee, Date dateDepart){
        String result = "reservationPleine";

        System.out.println(reservation + " " + proprietaire + " " + villeDepart + " " + villeArrivee + " " + dateDepart);

        BasicDBObject query = new BasicDBObject();
        query.put("email", proprietaire);
        query.put("villeDepart", villeDepart);
        query.put("villeArrivee", villeArrivee);
        
        query.put("dateDepart", dateDepart);
        DBObject annonceOb = collection.findOne(query);
        BasicDBObject annonceBasic = (BasicDBObject) annonceOb;
        Annonce annonce = new Annonce(annonceBasic);

        if(annonce.getNbPlaces() > 0 && annonce.getNbPlaces() != 0) {
            // Rechercher l'utilisateur qui veut reserver
            BasicDBObject query2 = new BasicDBObject();
            query2.put("email", reservation);
            DBObject reserv = utilisateurs.findOne(query2);
            BasicDBObject reservBasic = (BasicDBObject) reserv;
            // Creer l'utilisateur en tant que passager
            Passager passager = new Passager(reservBasic.get("_id").toString(),
                    reservBasic.get("nom").toString(),
                    reservBasic.get("prenom").toString());
            // Recuperer la liste des passager de l'annonce
            List<Passager> listPassager = annonce.getPassager();
            // Ajouter le nouveau passager à la liste des passagers
            listPassager.add(passager);
            // Definir la nouvelle liste de passager dans l'annonce
            annonce.setPassager(listPassager);
            annonce.setNbPlaces(annonce.getNbPlaces() - 1);

            DBObject newAnnonceObject = annonceOb;
            newAnnonceObject.put("passagers", annonce.getPassager());
            newAnnonceObject.put("nbPlaces", annonce.getNbPlaces());

            collection.update(annonceOb, newAnnonceObject);
            result = "reserve";
        }
        return result;
    }
}
