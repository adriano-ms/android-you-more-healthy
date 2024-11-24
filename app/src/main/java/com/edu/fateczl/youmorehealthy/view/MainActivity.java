package com.edu.fateczl.youmorehealthy.view;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.edu.fateczl.youmorehealthy.R;
import com.edu.fateczl.youmorehealthy.model.TaskType;
import com.edu.fateczl.youmorehealthy.view.fragments.ManageDataFragment;
import com.edu.fateczl.youmorehealthy.view.fragments.ManageTasksFragment;
import com.edu.fateczl.youmorehealthy.view.fragments.NewTaskFragment;
import com.edu.fateczl.youmorehealthy.view.fragments.NextTasksFragment;
import com.edu.fateczl.youmorehealthy.view.fragments.factories.FragmentFactory;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        BottomNavigationView bottomMenu = findViewById(R.id.bottomMenu);
        bottomMenu.setOnItemSelectedListener(this::chooseBottomMenuItem);
        loadFragment(chooseFragmentToLoad());
        requestNotificationPermission();
    }

    private void requestNotificationPermission() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission("android.permission.POST_NOTIFICATIONS") != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{"android.permission.POST_NOTIFICATIONS"}, 0);
            }
        }
    }

    private boolean chooseBottomMenuItem(MenuItem item){
        if(item.getItemId() == R.id.miManageData)
            loadFragment(new ManageDataFragment());
        else if(item.getItemId() == R.id.miManageTasks)
            loadFragment(new ManageTasksFragment());
        else
            loadFragment(new NextTasksFragment());
        return true;
    }

    public void loadFragment(Fragment f){
        ViewUtils.changeFragment(getSupportFragmentManager(), R.id.taskFragment, f, false);
    }

    public Fragment chooseFragmentToLoad(){
        Intent i = getIntent();
        if(i.hasExtra("task_type") && i.hasExtra("task_id")){
            TaskType taskType = TaskType.valueOf(i.getStringExtra("task_type"));
            switch (taskType) {
                case MEDIC:
                    return FragmentFactory.createMedicAppointmentDoneFragment(i.getIntExtra("task_id", 0));
                case SPORT:
                    return FragmentFactory.createTrainingFragment(i.getIntExtra("task_id", 0));
            }
        }
        return new NextTasksFragment();
    }
}