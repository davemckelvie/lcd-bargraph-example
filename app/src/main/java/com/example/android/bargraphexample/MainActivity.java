package com.example.android.bargraphexample;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;

import nz.geek.android.things.drivers.lcd.I2cSerialCharLcd;

public class MainActivity extends AppCompatActivity {

  private static final int LCD_WIDTH = 16;
  private static final int LCD_HEIGHT = 4;
  private static final int BARGRAPH_MAX = LCD_WIDTH * 5;
  private I2cSerialCharLcd lcd;
  private AnimatorSet animatorSet;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    createLcd();
    initBarGraph();
    initAnimator();
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    if (lcd != null) {
      lcd.disconnect();
    }
    animatorSet.cancel();
  }

  private void initAnimator() {
    final int DURATION = 700;
    ObjectAnimator line1Animator = ObjectAnimator.ofInt(lcd, new BarGraph(1), 0, BARGRAPH_MAX);
    line1Animator.setInterpolator(new BounceInterpolator());
    line1Animator.setRepeatMode(ValueAnimator.RESTART);
    line1Animator.setRepeatCount(ValueAnimator.INFINITE);
    line1Animator.setDuration(DURATION);

    ObjectAnimator line2Animator = ObjectAnimator.ofInt(lcd, new BarGraph(2), 0, BARGRAPH_MAX);
    line2Animator.setInterpolator(new LinearInterpolator());
    line2Animator.setRepeatMode(ValueAnimator.REVERSE);
    line2Animator.setRepeatCount(ValueAnimator.INFINITE);
    line2Animator.setDuration(DURATION);

    ObjectAnimator line3Animator = ObjectAnimator.ofInt(lcd, new BarGraph(3), 0, BARGRAPH_MAX);
    line3Animator.setInterpolator(new DecelerateInterpolator());
    line3Animator.setRepeatMode(ValueAnimator.REVERSE);
    line3Animator.setRepeatCount(ValueAnimator.INFINITE);
    line3Animator.setDuration(DURATION);


    ObjectAnimator line4Animator = ObjectAnimator.ofInt(lcd, new BarGraph(4), 0, BARGRAPH_MAX);
    line4Animator.setInterpolator(new AccelerateInterpolator());
    line4Animator.setRepeatMode(ValueAnimator.REVERSE);
    line4Animator.setRepeatCount(ValueAnimator.INFINITE);
    line4Animator.setDuration(DURATION);
    animatorSet = new AnimatorSet();
    animatorSet.play(line1Animator).with(line2Animator).with(line3Animator).with(line4Animator);
    animatorSet.start();
  }

  private void initBarGraph() {
    lcd.setCgRam(0x00, new byte[]{0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00});
    lcd.setCgRam(0x08, new byte[]{0x10, 0x10, 0x10, 0x10, 0x10, 0x10, 0x10, 0x00});
    lcd.setCgRam(0x10, new byte[]{0x18, 0x18, 0x18, 0x18, 0x18, 0x18, 0x18, 0x00});
    lcd.setCgRam(0x18, new byte[]{0x1C, 0x1C, 0x1C, 0x1C, 0x1C, 0x1C, 0x1C, 0x00});
    lcd.setCgRam(0x20, new byte[]{0x1E, 0x1E, 0x1E, 0x1E, 0x1E, 0x1E, 0x1E, 0x00});
    lcd.setCgRam(0x28, new byte[]{0x1F, 0x1F, 0x1F, 0x1F, 0x1F, 0x1F, 0x1F, 0x00});
  }

  private void createLcd() {
    I2cSerialCharLcd.I2cSerialCharLcdBuilder builder = I2cSerialCharLcd.builder(LCD_WIDTH, LCD_HEIGHT);
    builder.rs(0).rw(1).e(2).bl(3).data(4, 5, 6, 7).address(7);
    lcd = builder.build();
    lcd.connect();
    lcd.enableBackLight(true);
  }
}
