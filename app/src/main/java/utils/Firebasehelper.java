package utils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.CollectionReference;

public class FirebaseHelper {

    // Coleções Firestore
    public static final String COLLECTION_USUARIOS = "usuarios";
    public static final String COLLECTION_INSTITUICOES = "instituicoes";
    public static final String COLLECTION_SETORES = "setores";
    public static final String COLLECTION_EQUIPAMENTOS = "equipamentos";
    public static final String COLLECTION_MANUTENCOES = "manutencoes";
    public static final String COLLECTION_CONSUMOS = "consumos";
    public static final String COLLECTION_ALERTAS = "alertas";
    public static final String COLLECTION_ATIVIDADES_CAMPO = "atividades_campo";

    private static FirebaseHelper instance;
    private final FirebaseFirestore db;
    private final FirebaseAuth auth;

    private FirebaseHelper() {
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
    }

    public static synchronized FirebaseHelper getInstance() {
        if (instance == null) {
            instance = new FirebaseHelper();
        }
        return instance;
    }

    public FirebaseFirestore getDb() { return db; }
    public FirebaseAuth getAuth() { return auth; }

    public FirebaseUser getCurrentUser() {
        return auth.getCurrentUser();
    }

    public String getCurrentUserId() {
        FirebaseUser user = auth.getCurrentUser();
        return user != null ? user.getUid() : null;
    }

    public boolean isLoggedIn() {
        return auth.getCurrentUser() != null;
    }

    // Collection References
    public CollectionReference getUsuariosRef() {
        return db.collection(COLLECTION_USUARIOS);
    }

    public CollectionReference getInstituicoesRef() {
        return db.collection(COLLECTION_INSTITUICOES);
    }

    public CollectionReference getSetoresRef() {
        return db.collection(COLLECTION_SETORES);
    }

    public CollectionReference getEquipamentosRef() {
        return db.collection(COLLECTION_EQUIPAMENTOS);
    }

    public CollectionReference getManutencoesRef() {
        return db.collection(COLLECTION_MANUTENCOES);
    }

    public CollectionReference getConsumosRef() {
        return db.collection(COLLECTION_CONSUMOS);
    }

    public CollectionReference getAlertasRef() {
        return db.collection(COLLECTION_ALERTAS);
    }

    public CollectionReference getAtividadesCampoRef() {
        return db.collection(COLLECTION_ATIVIDADES_CAMPO);
    }
}
