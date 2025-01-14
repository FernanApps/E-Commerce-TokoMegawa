package com.tinieblas.tokomegawa.data.local;

import android.content.Context;

import com.tinieblas.tokomegawa.data.local.base.SharedRepo;
import com.tinieblas.tokomegawa.domain.repository.UserLocalRepository;

public class UserLocalRepositoryImp extends SharedRepo implements UserLocalRepository {


    private static final String NAME = "dataUser";
    private String KEY_USER_ID = "userUid";
    private String KEY_NOMBRE = "name";
    private String KEY_APELLIDO = "apellido";

/**    private final static String NAME = "MiUbicacion";

*/
    public UserLocalRepositoryImp(Context context) {
        super(context, NAME);
    }

    @Override
    public String getUser() {
        return null;
    }

    @Override
    public String getCurrentUserId() {
        String UID = getString(KEY_USER_ID);
        return UID;
    }
    @Override
    public void saveInfoUser(String name, String apellido) {
        putString(KEY_NOMBRE, name);
        putString(KEY_APELLIDO, apellido);
    }


}
