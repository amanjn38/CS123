package in.dete.oops;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

public class IntroViewPagerAdapter extends PagerAdapter {

    Context context;

    public IntroViewPagerAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layoutScreen = inflater.inflate(R.layout.layout_screen, null);
        ImageView imgSlide = layoutScreen.findViewById(R.id.ivOn);


        switch (position) {
            case 0:
                imgSlide.setImageResource(R.drawable.ic_screen1);
                break;
            case 1:
                imgSlide.setImageResource(R.drawable.ic_screen2);
                break;
            case 2:
                imgSlide.setImageResource(R.drawable.ic_screen3);
                break;
        }
        container.addView(layoutScreen);
        return layoutScreen;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);

    }
}
