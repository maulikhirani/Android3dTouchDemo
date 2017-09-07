package maulik.android3dtouchdemo.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import maulik.android3dtouchdemo.R;
import maulik.android3dtouchdemo.databinding.ListItemImageBinding;
import maulik.android3dtouchdemo.model.Animal;
import maulik.android3dtouchdemo.viewmodel.AnimalListActivityViewModel;
import maulik.android3dtouchdemo.viewmodel.AnimalListViewModel;

public class CustomRecyclerAdapter extends RecyclerView.Adapter<CustomRecyclerAdapter.ViewHolder> {

    private Dialog dialog;
    private ArrayList<Animal> mAnimalsList;
    private Context mContext;
    private AnimalListActivityViewModel mAnimalListActivityViewModel;

    public CustomRecyclerAdapter(AnimalListActivityViewModel mAnimalListActivityViewModel,ArrayList<Animal> mAnimalsList, Context mContext) {
        this.mAnimalListActivityViewModel = mAnimalListActivityViewModel;
        this.mAnimalsList = mAnimalsList;
        this.mContext = mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ListItemImageBinding listItemImageBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.list_item_image, parent, false);
        return new ViewHolder(mContext, listItemImageBinding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ListItemImageBinding listItemImageBinding = holder.listItemImageBinding;
        listItemImageBinding.setAvm(new AnimalListViewModel(mContext, mAnimalsList.get(position)));
    }

    @Override
    public int getItemCount() {
        return mAnimalsList.size();
    }

    public void hideQuickView() {
        if (dialog != null) dialog.dismiss();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ListItemImageBinding listItemImageBinding;

        ViewHolder(final Context mContext, ListItemImageBinding listItemImageBinding) {
            super(listItemImageBinding.listItemImage);
            this.listItemImageBinding = listItemImageBinding;
            listItemImageBinding.listItemImage.setOnTouchListener(new View.OnTouchListener() {
                final Handler handler = new Handler();
                Runnable mLongPressed = new Runnable() {
                    public void run() {
                        View dialog = LayoutInflater.from(mContext).inflate(R.layout.layout_dialog, null);

                        CustomRecyclerAdapter.this.dialog = new Dialog(mContext);
                        ImageView imageView = dialog.findViewById(R.id.img);
                        TextView textView = dialog.findViewById(R.id.text_img_name);
                        CustomRecyclerAdapter.this.dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        CustomRecyclerAdapter.this.dialog.getWindow().setBackgroundDrawable(
                                new ColorDrawable(Color.TRANSPARENT));
                        CustomRecyclerAdapter.this.dialog.setContentView(dialog);
                        CustomRecyclerAdapter.this.dialog.getWindow().setWindowAnimations(R.style.MyAnimation_Window);
                        CustomRecyclerAdapter.this.dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialogInterface) {
                                mAnimalListActivityViewModel.removeBlur();
                            }
                        });

                        textView.setText(mAnimalsList.get(getAdapterPosition()).getName());
                        Picasso.with(mContext).load(mAnimalsList.get(getAdapterPosition()).getUrl()).into(imageView);

                        mAnimalListActivityViewModel.blurBackground();

                        CustomRecyclerAdapter.this.dialog.show();
                    }
                };

                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                        handler.removeCallbacks(mLongPressed);
                        Log.d("action", "up");
                        mAnimalListActivityViewModel.removeBlur();
                        hideQuickView();
                        return false;
                    } else if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {

                        handler.postDelayed(mLongPressed, 50);

                        return true;
                    };
                    handler.removeCallbacks(mLongPressed);
                    return false;
                }
            });
        }
    }
}
