import com.mongodb.*;

import java.text.ParseException;
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

    public void ajouterAnnonce(Annonce annonce) throws ParseException {
        BasicDBObject query = new BasicDBObject();
        Utilisateur utili = annonce.getProprietaire();
        String email = utili.getEmail();
        query.put("email", email);
        DBObject proprietaire = utilisateurs.findOne(query);
        BasicDBObject utilisateurOb = (BasicDBObject) proprietaire;

        BasicDBList dbl = new BasicDBList();
        dbl.add(new BasicDBObject("email", annonce.getProprietaire().getEmail()));
        dbl.add(new BasicDBObject("nom", utilisateurOb.get("nom")));
        dbl.add(new BasicDBObject("prenom", utilisateurOb.get("prenom")));
        dbl.add(new BasicDBObject("id", utilisateurOb.getObjectId("_id")));
        System.out.println("AjouterAnnocne" + utilisateurOb.toString());
        DBObject doc = new BasicDBObject("proprietaire", dbl)
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
            Utilisateur utili = annonce.getProprietaire();
            String email = utili.getEmail();
            query.put("email", email);
            DBObject proprietaire = utilisateurs.findOne(query);
            BasicDBObject utilisateurOb = (BasicDBObject) proprietaire;
//            System.out.println(utilisateurOb);
            utili.setEmail(email);
            utili.setId("_id");
            utili.setNom(utilisateurOb.getString("nom"));
            utili.setPrenom(utilisateurOb.getString("prenom"));
            annonce.setProprietaire(utili);
            annonces.add(annonce);
        }
        return annonces;
    }

    public String reserver(String reservation, String proprietaire, String villeDepart, String villeArrivee, String dateDepart){
        String result = "reservationPleine";

        System.out.println(reservation + " " + proprietaire + " " + villeDepart + " " + villeArrivee + " " + dateDepart);

        BasicDBObject query = new BasicDBObject();
//        query.put("email", proprietaire);
        query.put("villeDepart", villeDepart);
        query.put("villeArrivee", villeArrivee);
//        query.put("dateDepart", dateDepart);

        DBObject annonceOb = collection.findOne(query);
        BasicDBObject annonceBasic = (BasicDBObject) annonceOb;
        Annonce annonce = new Annonce(annonceBasic);
//        System.out.println(annonceOb);
        if(annonce.getNbPlaces() > 0 && annonce.getNbPlaces() != 0) {
            // Rechercher l'utilisateur qui veut reserver
            BasicDBObject query2 = new BasicDBObject();
            query2.put("email", reservation);
            DBObject reserv = utilisateurs.findOne(query2);
            System.out.println(reserv);
            BasicDBObject reservBasic = (BasicDBObject) reserv;
            // Creer l'utilisateur en tant que utilisateur
            Utilisateur utilisateur = new Utilisateur(reservBasic.getObjectId("_id").toString(),
                    reservBasic.get("nom").toString(),
                    reservBasic.get("prenom").toString(),
                    reservBasic.get("email").toString());
            // Recuperer la liste des utilisateur de l'annonce
            List<Utilisateur> listUtilisateur = annonce.getPassager();
            if(listUtilisateur == null){
                listUtilisateur = new ArrayList<>();
            }
            // Ajouter le nouveau utilisateur à la liste des passagers
            listUtilisateur.add(utilisateur);
            // Definir la nouvelle liste de utilisateur dans l'annonce
            annonce.setPassager(listUtilisateur);
            annonce.setNbPlaces(annonce.getNbPlaces() - 1);

            DBObject newAnnonceObject = annonceOb;
            System.out.println(newAnnonceObject);

            newAnnonceObject.put("passagers", annonce.getPassager());
            newAnnonceObject.put("nbPlaces", annonce.getNbPlaces());
            System.out.println(newAnnonceObject);

            collection.update(annonceOb, newAnnonceObject);
            System.out.println("5");
            result = "reserve";
        }
        return result;
    }
}
