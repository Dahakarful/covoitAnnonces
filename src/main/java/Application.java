import com.google.gson.Gson;
import com.mongodb.*;
import com.mongodb.util.JSON;

import java.util.*;

import static spark.Spark.*;

/**
 * Created by Ragonda on 20/01/2017.
 */
public class Application {

    private static DB mongo() throws Exception{
        String host = System.getenv("MONGODB_ADDON_HOST");
        System.out.println(host);
        if(host == null){
            MongoClient mongoClient = new MongoClient("localhost");
            return mongoClient.getDB("covoiturage");
        }
        int port = 27017;
        String dbname = System.getenv("MONGODB_ADDON_DB");
        String username = System.getenv("MONGODB_ADDON_USER");
        String password = System.getenv("MONGODB_ADDON_PASSWORD");
        MongoClientOptions mongoClientOptions = MongoClientOptions.builder().build();
        MongoClient mongoClient = new MongoClient(new ServerAddress(host, port), mongoClientOptions);
        mongoClient.setWriteConcern(WriteConcern.SAFE);
        DB db = mongoClient.getDB(dbname);
        if(db.authenticate(username, password.toCharArray())){
            return db;
        }else{
            throw new RuntimeException("Not able to authenticate with MongoDB");
        }
    }

    public static void main(String args[]) throws Exception {
        port(8083);
        enableCORS("*", "*", "*");

        AnnonceDao annonceDao = new AnnonceDao(mongo());

        // AJOUTER ANNONCE ---------------------------------------------------
        post("/ajouterAnnonce", (req, res) -> {
            System.out.println(
                    req.queryParams("proprietaire").toString() + " " +
                    req.queryParams("villeDepart").toString() + " " +
                    req.queryParams("villeArrivee").toString() + " " +
                    req.queryParams("nbPlaces").toString() + " " +
                    req.queryParams("prix").toString() + " " +
                    req.queryParams("dateDepart").toString()
            );
            Map<String, String> map = new HashMap<String, String>();
            map.put("email", req.queryParams("proprietaire").toString());
            Annonce annonce = new Annonce(
                    map,
                    req.queryParams("villeDepart"),
                    req.queryParams("villeArrivee"),
                    Integer.parseInt(req.queryParams("nbPlaces")),
                    Integer.parseInt(req.queryParams("prix")),
                    Utils.stringToDate(req.queryParams("dateDepart")));
            try {
                annonceDao.ajouterAnnonce(annonce);
            }catch(Exception e){
                System.out.println(e);
            }
            res.status(201);
            return annonce;
        }, new JsonTransformer());
        // --------------------------------------------------------------------------

        // SUPPRIMER ANNONCE -------------------------------------------------
        post("/supprimerAnnonce", (req, res) -> {
            annonceDao.supprimerAnnonce(
                    req.queryParams("idUtilisateur"),
                    req.queryParams("idAnnonce"));
            res.status(201);
            return "supprimerAnnonce";
        }, new JsonTransformer());
        // -------------------------------------------------------------------

        // AVOIR TOUTES LES ANNONCES -----------------------------------------
        get("/listAnnonces", (req, res) -> {
            List<Annonce> list = new ArrayList<>();
            try {
                list = (List) annonceDao.listAnnonces();
            }catch(Exception e){
                System.out.println(e);
            }
            res.status(201);
            return list;
        }, new JsonTransformer());
        // -------------------------------------------------------------------

        // RESERVER UNE ANNONCE ----------------------------------------------
        post("/reserver", (req, res) -> {
            String result = "";
            try{
                result = annonceDao.reserver(
                        req.queryParams("reservation"),
                        req.queryParams("proprietaire"),
                        req.queryParams("villeDepart"),
                        req.queryParams("villeArrivee"),
                        Utils.stringToDate(req.queryParams("dateDepart"))
                );
            }catch(Exception e){
                System.out.println(e);
            }
            return result;
        }, new JsonTransformer());
    }

    // Enables CORS on requests. This method is an initialization method and should be called once.
    private static void enableCORS(final String origin, final String methods, final String headers) {

        options("/*", (request, response) -> {

            String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
            if (accessControlRequestHeaders != null) {
                response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
            }

            String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
            if (accessControlRequestMethod != null) {
                response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
            }

            return "OK";
        });

        before((request, response) -> {
            response.header("Access-Control-Allow-Origin", origin);
            response.header("Access-Control-Request-Method", methods);
            response.header("Access-Control-Allow-Headers", headers);
            // Note: this may or may not be necessary in your particular application
            response.type("application/json");
        });
    }
}
