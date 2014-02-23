package com.example.mysoundsample;

import android.os.Bundle;
import android.R.integer;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class MainActivity extends Activity {
	Thread t;
	int sr = 44100;
	boolean isRunning = true;
	SeekBar fSlider;
	double sliderval;
	double m_fr = 440.f;
	
	public void buttonMethod(View button){
		int[] frs2 = new int[]{
			0,2,3,5,7,8,10
		};
		double[] frs = new double[]{ // 12ループ
				110, // ラ  0
				116.54, // ラ# 1
				123.47, // シ 2
				130.81, // ド [3]
				138.59, // ド# 4
				146.83, // レ 5
				155.56, // レ# 6
				164.81, // ミ 7
				174.61, // ファ 8
				184.99, // ファ# 9
				195.99, // ソ 10
				207.65, // ソ# 11
				220, // ラ [12]
				233.08, // ラ# 13
				246.94, // シ 14
				261.62, // ド  [15]
				277.18, // ド#
				293.66, // レ
				311.12, // レ#
				329.62, // ミ
				349.22, // ファ
				369.99, // ファ#
				391.99, // ソ
				415.3, // ソ#
				440, // ラ
				466.16, // ラ#
				493.88, // シ
				523.25, // ド[27]
				554.36, // ド#
				587.32, // レ
				622.25, // レ#
				659.25, // ミ
				698.45, // ファ
				739.98, // ファ#
				783.99, // ソ
				830.6, // ソ#
				880, // ラ
				932.32, // ラ#
				987.76, // シ
				1046.5, // ド
				1108.73, // ド#
				1174.65, // レ
				1244.5, // レ#
				1318.51, // ミ
				1396.91, // ファ
				1479.97, // ファ#
				1567.98, // ソ
				1661.21, // ソ#
				1760, // ラ
				1864.65, // ラ#
				1975.53, // シ
				2093 // ド
		};
		String text = ((Button)button).getText().toString();
		int i = Integer.parseInt(text);
		int level = (2 + i) / frs2.length;
		int j = frs2[(2 + i) % frs2.length];
		m_fr = frs[12 * (2 + level) + j];
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// point the slider to thwe GUI widget
		fSlider = (SeekBar) findViewById(R.id.seekBar1);

		// create a listener for the slider bar;
		OnSeekBarChangeListener listener = new OnSeekBarChangeListener() {
			public void onStopTrackingTouch(SeekBar seekBar) {
			}

			public void onStartTrackingTouch(SeekBar seekBar) {
			}

			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				if (fromUser){
					double sliderval = progress / (double) seekBar.getMax();
					m_fr = 440 + 440 * sliderval;
				}
			}
		};

		// set the listener on the slider
		fSlider.setOnSeekBarChangeListener(listener);

		// start a new thread to synthesise audio
		t = new Thread() {
			public void run() {
				// set process priority
				setPriority(Thread.MAX_PRIORITY);
				// set the buffer size
				int buffsize = AudioTrack.getMinBufferSize(sr,
						AudioFormat.CHANNEL_OUT_MONO,
						AudioFormat.ENCODING_PCM_16BIT);
				// create an audiotrack object
				AudioTrack audioTrack = new AudioTrack(
						AudioManager.STREAM_MUSIC, sr,
						AudioFormat.CHANNEL_OUT_MONO,
						AudioFormat.ENCODING_PCM_16BIT, buffsize,
						AudioTrack.MODE_STREAM);

				short samples[] = new short[buffsize];
				int amp = 10000;
				double twopi = 8. * Math.atan(1.);
				
				double ph = 0.0;

				// start audio
				audioTrack.play();

				// synthesis loop
				while (isRunning) {
					
					for (int i = 0; i < buffsize; i++) {
						samples[i] = (short) (amp * Math.sin(ph));
						ph += twopi * m_fr / sr;
					}
					audioTrack.write(samples, 0, buffsize);
				}
				audioTrack.stop();
				audioTrack.release();
			}
		};
		t.start();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void onDestroy() {
		super.onDestroy();
		isRunning = false;
		try {
			t.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		t = null;
	}
}
