package maulik.android3dtouchdemo.viewmodel;

import android.content.Context;
import android.databinding.BaseObservable;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.TransitionManager;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

import maulik.android3dtouchdemo.databinding.ActivityAnimalListBinding;
import maulik.android3dtouchdemo.adapter.CustomRecyclerAdapter;
import maulik.android3dtouchdemo.model.Animal;

/**
 * Created by ln-202 on 30/8/17.
 */

public class AnimalListActivityViewModel extends BaseObservable {

    private Context mContext;
    private ActivityAnimalListBinding mBinding;
    private View mBlurBgFrame;

    public AnimalListActivityViewModel(Context mContext, final ActivityAnimalListBinding mBinding) {
        this.mContext = mContext;
        this.mBinding = mBinding;
        mBlurBgFrame = mBinding.frameContainer;
        mBinding.imgBg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mBinding.recyclerList.getVisibility()==View.GONE) {
                    mBinding.recyclerList.setVisibility(View.VISIBLE);
                }
            }
        });
        setUpRecyclerView();
        setDataInAdapter();
    }



    public void setUpRecyclerView() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 2);
        mBinding.recyclerList.setLayoutManager(gridLayoutManager);
    }

    public void setDataInAdapter() {
        ArrayList<Animal> animalArrayList = new ArrayList<>();
        animalArrayList.add(
                new Animal("Lion","Desc of Lion","https://i.pinimg.com/736x/07/29/97/072997211c5621934716a261d321034e--photos.jpg")
        );
        animalArrayList.add(
                new Animal("Tiger","Desc of Tiger","https://c402277.ssl.cf1.rackcdn.com/photos/1620/images/carousel_small/bengal-tiger-why-matter_7341043.jpg?1345548942")
        );
        animalArrayList.add(
                new Animal("Elephant","Desc of Elephant","https://img.purch.com/h/1000/aHR0cDovL3d3dy5saXZlc2NpZW5jZS5jb20vaW1hZ2VzL2kvMDAwLzAzNi85ODgvb3JpZ2luYWwvZWxlcGhhbnRzLmpwZw==")
        );
        animalArrayList.add(
                new Animal("Rabbit","Desc of Rabbit","https://www.thesun.co.uk/wp-content/uploads/2016/06/nintchdbpict000171898291-e1466207066744.jpg?w=960&strip=all")
        );
        animalArrayList.add(
                new Animal("Monkey","Desc of Monkey","http://assets.nydailynews.com/polopoly_fs/1.2753191.1471352717!/img/httpImage/image.jpg_gen/derivatives/article_750/483572672.jpg")
        );
        for(int i=0;i<4;i++)
        {
            for(int j=0;j<5;j++) {
                animalArrayList.add(animalArrayList.get(j));
            }
        }
        mBinding.recyclerList.setAdapter(new CustomRecyclerAdapter(this,animalArrayList,mContext));
    }

    public void blurBackground() {

        mBlurBgFrame.setDrawingCacheEnabled(true);
        mBlurBgFrame.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_LOW);
        mBlurBgFrame.buildDrawingCache();

        if (mBlurBgFrame.getDrawingCache() == null) return;


        Bitmap snapshot = Bitmap.createBitmap(mBlurBgFrame.getDrawingCache());
        mBlurBgFrame.setDrawingCacheEnabled(false);
        mBlurBgFrame.destroyDrawingCache();

        BitmapDrawable bitmapDrawable = new BitmapDrawable(mContext.getResources(), blurBitmap(snapshot));
        TransitionManager.beginDelayedTransition(mBinding.frameContainer);
        mBinding.imgBg.setImageDrawable(bitmapDrawable);
        mBinding.recyclerList.setVisibility(View.GONE);
        mBinding.recyclerList.requestDisallowInterceptTouchEvent(true);
        mBinding.recyclerList.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                if (e.getAction() == MotionEvent.ACTION_UP) {
                     CustomRecyclerAdapter customRecyclerAdapter = (CustomRecyclerAdapter) rv.getAdapter();
                    removeBlur();
                    customRecyclerAdapter.hideQuickView();
                    rv.requestDisallowInterceptTouchEvent(false);
                    return false;
                }
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });

    }


    public void removeBlur() {
        mBinding.recyclerList.setVisibility(View.VISIBLE);
    }


    public Bitmap blurBitmap(Bitmap bitmap) {

        //Let's create an empty bitmap with the same size of the bitmap we want to blur
        Bitmap outBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);

        //Instantiate a new Renderscript
        RenderScript rs = RenderScript.create(mContext);

        //Create an Intrinsic Blur Script using the Renderscript
        ScriptIntrinsicBlur blurScript = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));

        //Create the in/out Allocations with the Renderscript and the in/out bitmaps
        Allocation allIn = Allocation.createFromBitmap(rs, bitmap);
        Allocation allOut = Allocation.createFromBitmap(rs, outBitmap);

        //Set the radius of the blur
        blurScript.setRadius(25f);

        //Perform the Renderscript
        blurScript.setInput(allIn);
        blurScript.forEach(allOut);

        //Copy the final bitmap created by the out Allocation to the outBitmap
        allOut.copyTo(outBitmap);

        //recycle the original bitmap
//        bitmap.recycle();

        //After finishing everything, we destroy the Renderscript.
        rs.destroy();

        return outBitmap;
    }

}
