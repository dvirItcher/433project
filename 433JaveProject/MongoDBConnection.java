import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

public class MongoDBConnection {
    private static final String CONNECTION_STRING = "mongodb+srv://ditcher216:isP4JFnMH1UWaKcL@cluster0.r9lhy4p.mongodb.net/";
    public static MongoClient connect() {
        return MongoClients.create(CONNECTION_STRING);
    }
}
