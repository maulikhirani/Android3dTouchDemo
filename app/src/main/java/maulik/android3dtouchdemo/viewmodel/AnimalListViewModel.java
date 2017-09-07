package maulik.android3dtouchdemo.viewmodel;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import maulik.android3dtouchdemo.BR;
import maulik.android3dtouchdemo.model.Animal;

/**
 * Created by ln-202 on 30/8/17.
 */

public class AnimalListViewModel extends BaseObservable {

    private Context mContext;
    private Animal mAnimal;

    public AnimalListViewModel(Context mContext, Animal mAnimal) {
        this.mContext = mContext;
        this.mAnimal = mAnimal;
    }

    @BindingAdapter({"url"})
    public static void loadImage(ImageView imageView, String url) {
        Picasso.with(imageView.getContext()).load(url).into(imageView);
    }

    @Bindable
    public String getName() {
        return mAnimal.getName();
    }

    public void setName(String name) {
        mAnimal.setName(name);
        notifyPropertyChanged(BR.name);
    }

    @Bindable
    public String getDescription() {
        return mAnimal.getDescription();
    }

    public void setDescription(String description) {
        mAnimal.setDescription(description);
        notifyPropertyChanged(BR.description);
    }

    @Bindable
    public String getUrl() {
        return mAnimal.getUrl();
    }

    public void setUrl(String url) {
        mAnimal.setUrl(url);
        notifyPropertyChanged(BR.url);
    }


}
