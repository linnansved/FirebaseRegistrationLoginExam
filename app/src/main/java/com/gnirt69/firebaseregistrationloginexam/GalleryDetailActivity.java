package com.gnirt69.firebaseregistrationloginexam;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
        import android.support.v4.app.Fragment;
        import android.support.v4.app.FragmentManager;
        import android.support.v4.app.FragmentPagerAdapter;
        import android.support.v4.view.ViewPager;
        import android.support.v7.app.AppCompatActivity;
        import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
        import android.view.Menu;
        import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
        import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

        import com.bumptech.glide.Glide;

import java.io.IOException;
import java.util.ArrayList;

public class GalleryDetailActivity extends AppCompatActivity {


    private SectionsPagerAdapter mSectionsPagerAdapter;

    public ArrayList<GalleryImageModel> data = new ArrayList<>();
    int pos;

    Toolbar toolbar;
    MediaPlayer mPlayer;
    public String mAudioName;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gallery_detail);

        toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        data = getIntent().getParcelableArrayListExtra("data");
        pos = getIntent().getIntExtra("pos", 0);
        setTitle(data.get(pos).getName());

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), data);
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setPageTransformer(true, new DepthPageTransformer());
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setCurrentItem(pos);

        mViewPager.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                playSound(data.get(pos).getAudio());
                Log.d("spelar","ljud");
            }
        });

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                //noinspection ConstantConditions
                setTitle(data.get(position).getName());
                //playSound(data.get(position).getAudio());


            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }

        });

       //LÄGGER TILL SÅ DET GÅR ATT KLICKA PÅ BILDEN
        mViewPager.setOnTouchListener(new View.OnTouchListener() {
            private boolean moved;

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    moved = false;
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                    moved = true;
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    if (!moved) {
                        view.performClick();
                    }
                }
                return false;
            }
        });
    }

    public void playSound(String audioUrl){
        mAudioName = audioUrl;
        Log.d("AudioLaddas", mAudioName);
        mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(mAudioName);
            Log.d("play1", String.valueOf(mPlayer));
            mPlayer.prepare();
            Log.d("play2", String.valueOf(mPlayer));
            mPlayer.start();
            Log.d("play3", String.valueOf(mPlayer));
        } catch (IOException e) {
            Log.e("Går", "inte");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        public ArrayList<GalleryImageModel> data = new ArrayList<>();

        public SectionsPagerAdapter(FragmentManager fm, ArrayList<GalleryImageModel> data) {
            super(fm);
            this.data = data;
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position, data.get(position).getName(), data.get(position).getUrl());
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return data.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return data.get(position).getName();
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */

        String name, url, audio;
        int pos;
        private static final String ARG_SECTION_NUMBER = "section_number";
        private static final String ARG_IMG_TITLE = "image_title";
        private static final String ARG_IMG_URL = "image_url";
        //private static final String ARG_IMG_AUD = "audio_url";


        @Override
        public void setArguments(Bundle args) {
            super.setArguments(args);
            this.pos = args.getInt(ARG_SECTION_NUMBER);
            this.name = args.getString(ARG_IMG_TITLE);
            this.url = args.getString(ARG_IMG_URL);
          //  this.audio = args.getString(ARG_IMG_AUD);
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber, String name, String url) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            args.putString(ARG_IMG_TITLE, name);
            args.putString(ARG_IMG_URL, url);
          //  args.putString(ARG_IMG_AUD, audio);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public void onStart() {
            super.onStart();

        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.gallery_fragment_detail, container, false);

            final ImageView imageView = (ImageView) rootView.findViewById(R.id.detail_image);

            Glide.with(getActivity()).load(url).thumbnail(0.1f).into(imageView);

            return rootView;
        }


    }
}
