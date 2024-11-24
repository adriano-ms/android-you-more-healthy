package com.edu.fateczl.youmorehealthy.view.fragments.factories;

import androidx.fragment.app.Fragment;

import com.edu.fateczl.youmorehealthy.model.Doctor;
import com.edu.fateczl.youmorehealthy.model.Medicine;
import com.edu.fateczl.youmorehealthy.model.SportRoutine;
import com.edu.fateczl.youmorehealthy.model.Task;
import com.edu.fateczl.youmorehealthy.model.TaskType;
import com.edu.fateczl.youmorehealthy.model.Training;
import com.edu.fateczl.youmorehealthy.view.fragments.DoctorFragment;
import com.edu.fateczl.youmorehealthy.view.fragments.EventFragment;
import com.edu.fateczl.youmorehealthy.view.fragments.IEntityFragment;
import com.edu.fateczl.youmorehealthy.view.fragments.MedicAppointmentDoneFragment;
import com.edu.fateczl.youmorehealthy.view.fragments.MedicAppointmentFragment;
import com.edu.fateczl.youmorehealthy.view.fragments.MedicineConsumeFragment;
import com.edu.fateczl.youmorehealthy.view.fragments.MedicineFragment;
import com.edu.fateczl.youmorehealthy.view.fragments.RoutineFragment;
import com.edu.fateczl.youmorehealthy.view.fragments.SportRoutineFragment;
import com.edu.fateczl.youmorehealthy.view.fragments.TrainingFragment;
import com.edu.fateczl.youmorehealthy.view.fragments.TrainingListFragment;

public class FragmentFactory {

    public static Fragment createTaskFragment(TaskType type){
        switch (type){
            case ROUTINE:
                return new RoutineFragment();
            case MEDIC:
                return new MedicAppointmentFragment();
            case MEDICINE:
                return new MedicineConsumeFragment();
            case SPORT:
                return new SportRoutineFragment();
            default:
                return new EventFragment();
        }
    }

    @SuppressWarnings("unchecked")
    public static Fragment createTaskFragment(Task task){
        Fragment f = createTaskFragment(task.getType());
        ((IEntityFragment<Task>)f).setEntity(task);
        return f;
    }

    public static Fragment createMedicAppointmentDoneFragment(int id){
        return new MedicAppointmentDoneFragment(id);
    }

    public static Fragment createTrainingsFragment(SportRoutine sportRoutine){
        return new TrainingListFragment(sportRoutine);
    }

    public static Fragment createTrainingsFragment(int sportRoutineId){
        return new TrainingListFragment(sportRoutineId);
    }

    public static Fragment createTrainingFragment(SportRoutine sportRoutine, Training training){
        return new TrainingFragment(sportRoutine, training);
    }

    public static Fragment createTrainingFragment(SportRoutine sportRoutine){
        return new TrainingFragment(sportRoutine);
    }


    public static Fragment createTrainingFragment(int sportRoutineId){
        return new TrainingFragment(sportRoutineId);
    }



    public static Fragment createDoctorFragment(Doctor doctor){
        if(doctor == null)
            return createDoctorFragment();
        DoctorFragment f = new DoctorFragment();
        f.setEntity(doctor);
        return f;
    }

    public static Fragment createDoctorFragment(){
        return new DoctorFragment();
    }

    public static Fragment createMedicineFragment(Medicine medicine){
        if(medicine == null)
            return createMedicineFragment();
        MedicineFragment f = new MedicineFragment();
        f.setEntity(medicine);
        return f;
    }

    public static Fragment createMedicineFragment(){
        return new MedicineFragment();
    }
}
