package com.edu.fateczl.youmorehealthy.persistence;

import com.edu.fateczl.youmorehealthy.model.SportRoutine;
import com.edu.fateczl.youmorehealthy.model.Training;
import com.edu.fateczl.youmorehealthy.persistence.exceptions.DatabaseException;
import com.edu.fateczl.youmorehealthy.persistence.exceptions.ResourceNotFoundException;

import java.util.List;

public interface ISportRoutineDao extends IDao<SportRoutine>{

    int insertTraining(int sportRoutineId, Training training) throws DatabaseException, ResourceNotFoundException;
    void deleteTraining(int id);
    void updateTraining(Training training);

}
