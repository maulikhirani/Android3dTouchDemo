package maulik.android3dtouchdemo.view;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import maulik.android3dtouchdemo.R;
import maulik.android3dtouchdemo.databinding.ActivityAnimalListBinding;
import maulik.android3dtouchdemo.viewmodel.AnimalListActivityViewModel;

public class AnimalListActivity extends AppCompatActivity {


    private ActivityAnimalListBinding mActivityAnimalListBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityAnimalListBinding = DataBindingUtil.setContentView(this, R.layout.activity_animal_list);
        AnimalListActivityViewModel viewModel = new AnimalListActivityViewModel(this, mActivityAnimalListBinding);
    }

}
