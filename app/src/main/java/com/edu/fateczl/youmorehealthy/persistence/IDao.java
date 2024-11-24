package com.edu.fateczl.youmorehealthy.persistence;

import com.edu.fateczl.youmorehealthy.persistence.exceptions.DatabaseException;
import com.edu.fateczl.youmorehealthy.persistence.exceptions.ResourceNotFoundException;

import java.util.List;

public interface IDao<T> {

    int insert(T t) throws DatabaseException;
    void update(T t) throws DatabaseException;
    void delete(int id) throws DatabaseException;
    T findOne(int id) throws DatabaseException, ResourceNotFoundException;
    List<T> findAll() throws DatabaseException;

}
