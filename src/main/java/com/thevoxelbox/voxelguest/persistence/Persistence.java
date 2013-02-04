package com.thevoxelbox.voxelguest.persistence;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.support.DatabaseConnection;
import com.j256.ormlite.table.TableUtils;
import com.thevoxelbox.voxelguest.modules.asshat.ban.BannedPlayer;

import java.io.File;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Monofraps
 * @author MikeMatrix
 */
public class Persistence
{
	private static Persistence instance = new Persistence();
	private ConnectionSource connectionSource;
	private Map<Class, Dao> daoCache = new HashMap<>();

	private Persistence()
	{
	}

	public static Persistence getInstance()
	{
		return instance;
	}

	public void initialize(File dbFile) throws SQLException
	{
		dbFile.mkdirs();
		connectionSource = new JdbcConnectionSource("jdbc:sqlite:" + new File(dbFile, "persistence2.db").getPath());
	}

	public void shutdown() throws SQLException
	{
		connectionSource.close();
	}

	public <E> Dao<E, ?> getDao(Class<E> clazz) throws SQLException
	{
		if(!daoCache.containsKey(clazz)) {
			TableUtils.createTableIfNotExists(connectionSource, clazz);
			daoCache.put(clazz, DaoManager.createDao(Persistence.getInstance().getConnectionSource(), clazz));
		}
		return daoCache.get(clazz);
	}

	public <V extends Object, ID extends Object> void save(V object)
	{
		try
		{
			Dao<V, ID> objectDao = (Dao<V, ID>) getDao(object.getClass());
			DatabaseConnection connection = objectDao.startThreadConnection();
			objectDao.createOrUpdate(object);

			if (!objectDao.isAutoCommit(connection))
			{
				objectDao.commit(connection);
			}
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
	}

	public <V extends Object, ID extends Object> void saveAll(List<Object> objects)
	{
		if (objects.size() == 0)
		{
			return;
		}

		DatabaseConnection connection = null;
		try
		{
			Dao<V, ID> objectDao = (Dao<V, ID>) getDao(objects.get(0).getClass());
			connection = objectDao.startThreadConnection();

			for (Object object : objects)
			{
				objectDao.createOrUpdate((V) object);
			}

			if (!objectDao.isAutoCommit(connection))
			{
				objectDao.commit(connection);
			}
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
	}

	public <V extends Object, ID extends Object> List<V> loadAll(Class<?> clazz)
	{
		DatabaseConnection connection = null;
		List<V> objects = null;
		try
		{
			Dao<V, ID> objectDao = (Dao<V, ID>) getDao(clazz);
			connection = objectDao.startThreadConnection();

			objects = objectDao.queryForAll();

			if (!objectDao.isAutoCommit(connection))
			{
				objectDao.commit(connection);
			}

		} catch (SQLException e)
		{
			e.printStackTrace();
		}

		return objects;
	}

	public <V extends Object, ID extends Object> List<V> loadAll(Class<?> clazz, Map<String, Object> restrictions)
	{
		List<V> objects = null;
		try
		{
			Dao<V, ID> objectDao = (Dao<V, ID>) getDao(clazz);
			objects = objectDao.queryForFieldValues(restrictions);
		} catch (SQLException e)
		{
			e.printStackTrace();
		}

		return objects;
	}

	public <V extends Object, ID extends Object> void delete(final Object object)
	{
		DatabaseConnection connection = null;
		try
		{
			Dao<V, ID> objectDao = (Dao<V, ID>) getDao(object.getClass());
			connection = objectDao.startThreadConnection();

			objectDao.delete((V) object);

			if (!objectDao.isAutoCommit(connection))
			{
				objectDao.commit(connection);
			}
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
	}

	public ConnectionSource getConnectionSource()
	{
		return connectionSource;
	}
}
