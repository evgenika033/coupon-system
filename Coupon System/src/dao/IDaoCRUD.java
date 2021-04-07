package dao;

import java.sql.SQLException;
import java.util.List;

import exception.DBException;
import exception.MisMatchObjectException;
import exception.ThreadException;

public interface IDaoCRUD<T> {

	void add(T addObject) throws DBException, ThreadException, MisMatchObjectException;

	void update(T updateObject) throws DBException, ThreadException, MisMatchObjectException;

	void delete(int id) throws DBException, ThreadException;

	// return one object
	T get(int id) throws DBException, ThreadException, SQLException, MisMatchObjectException;

	// return collection
	List<T> get() throws DBException, ThreadException, SQLException;

}
