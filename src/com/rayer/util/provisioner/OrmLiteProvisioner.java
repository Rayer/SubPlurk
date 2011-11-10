package com.rayer.util.provisioner;

import java.io.IOException;
import java.sql.SQLException;

import com.j256.ormlite.dao.Dao;

//目前只支援String based ID
public class OrmLiteProvisioner<Type, ID> implements ResourceProvisioner<Type, ID> {

	Dao<Type, ID> mTargetDao;
	
	OrmLiteProvisioner(Dao<Type, ID> targetDAO) {
		
	}
	
	@Override
	public Type getResource(ID identificator) throws IOException {
		Type obj = null;
		try {
			obj = mTargetDao.queryForId(identificator);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new IOException("Sqlite IO Error!");
		}
		
		return obj;
	}

	@Override
	public boolean setResource(ID identificator, Type targetResource) {
		try {
			mTargetDao.create(targetResource);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public boolean dereferenceResource(ID identificator) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean clearAllCachedResource() {
		// TODO Auto-generated method stub
		return false;
	}


}
