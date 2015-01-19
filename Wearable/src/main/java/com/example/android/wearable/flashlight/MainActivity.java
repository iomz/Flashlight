/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * Created by ichiro, wataru, iomz on 1/19/15.
 */

package com.example.android.wearable.flashlight;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Let there be light.
 */
public class MainActivity extends Activity 
        implements SensorEventListener {

    private ViewPager mViewPager;
    private SensorManager mSensorManager;
    private final float GAIN = 0.9f;
    private int x, y, z;
    private PartyLightFragment partyFragment;
    private MotionLightFragment motionFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);
        mViewPager = (ViewPager) findViewById(R.id.pager);
        final LightFragmentAdapter adapter = new LightFragmentAdapter(getFragmentManager());
        adapter.addFragment(new WhiteLightFragment());
        partyFragment = new PartyLightFragment();
        adapter.addFragment(partyFragment);
        motionFragment = new MotionLightFragment();
        adapter.addFragment(motionFragment);
        mViewPager.setAdapter(adapter);
        mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                if (position == 1) {
                    partyFragment.startCycling();
                } else {
                    partyFragment.stopCycling();
                }
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
    }

    static class LightFragmentAdapter extends FragmentPagerAdapter {
        private ArrayList<Fragment> mFragments;

        public LightFragmentAdapter(FragmentManager fm) {
            super(fm);
            mFragments = new ArrayList<Fragment>();
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        public void addFragment(Fragment fragment) {
            mFragments.add(fragment);
            // Update the pager when adding a fragment.
            notifyDataSetChanged();
        }
    }

    @Override
    public void onSensorChanged(SensorEvent e) {
        if (e.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            x = (int)((x * GAIN + e.values[0] * (1 - GAIN)))%255;
            y = (int)((y * GAIN + e.values[1] * (1 - GAIN)))%255;
            z = (int)((z * GAIN + e.values[2] * (1 - GAIN)))%255;
            motionFragment.updateColor(x, y, z);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        ;
    }

    public static class WhiteLightFragment extends Fragment {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            return inflater.inflate(R.layout.white_light, container, false);
        }
    }

    public static class PartyLightFragment extends Fragment {

        private PartyLightView mView;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            mView = (PartyLightView) inflater.inflate(R.layout.party_light, container, false);
            return mView;
        }

        public void startCycling() {
            mView.startCycling();
        }

        public void stopCycling() {
            mView.stopCycling();
        }
    }

    public static class MotionLightFragment extends Fragment {

        private MotionLightView mView;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            mView = (MotionLightView) inflater.inflate(R.layout.motion_light, container, false);
            return mView;
        }

        public void updateColor(int x, int y, int z) {
            mView.updateColor(x, y, z);
        }
    }

}
