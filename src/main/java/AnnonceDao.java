import com.mongodb.DB;
import com.mongodb.DBCollection;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Ragonda on 20/01/2017.
 */
public class AnnonceDao {

    static List<Annonce> annonces = new LinkedList<>();
    private final DB db;
    private final DBCollection collection;

    public AnnonceDao(DB db){
        this.db = db;
        this.collection = db.getCollection("annonces");
    }
}
