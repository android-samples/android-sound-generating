package com.example.mysoundsample;

import android.os.Bundle;
import android.R.integer;
import android.app.Activity;
import android.util.Log;
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
	//Thread t;
	
	//double m_fr = 440.f;
	private SoundThreadManager m_manager;
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		if(m_manager != null){
			m_manager.allStop();
		}
	}



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
		Log.d("myapp", "allStop");
		m_manager.allStop();
		int i = Integer.parseInt(text);
		{
			Log.d("myapp", "pushSound0");
			int level = (2 + i) / frs2.length;
			int j = frs2[(2 + i) % frs2.length];
			double[] frs3 = new double[]{
					frs[12 * (2 + level) + j],
					frs[12 * (2 + level) + j + 4],
					frs[12 * (2 + level) + j + 7]};
			//frs3 = new double[]{frs[12 * (2 + level) + j]};
			m_manager.pushSound(frs3, 0, 500);
		}
		/*
		{
			Log.d("myapp", "pushSound1");
			i += 4;
			int level = (2 + i) / frs2.length;
			int j = frs2[(2 + i) % frs2.length];
			double fr = frs[12 * (2 + level) + j];
			m_manager.pushSound(fr, 1, 1000);
		}
		{
			Log.d("myapp", "pushSound0");
			i += 3;
			int level = (2 + i) / frs2.length;
			int j = frs2[(2 + i) % frs2.length];
			double fr = frs[12 * (2 + level) + j];
			m_manager.pushSound(fr, 2, 1000);
		}
		Log.d("myapp", "sleeping");
		*/
		/*
		try{
			//Thread.sleep(1000);
		}
		catch(InterruptedException ex){
		}
		*/
		//Log.d("myapp", "allPlay");
		//m_manager.allPlay();
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// シンセ用スレッド
		m_manager = new SoundThreadManager();
	}

	public void onDestroy() {
		super.onDestroy();
		// スレッド停止を命令する
		m_manager.allStop();
		m_manager = null;
	}
}



/*
	SeekBar fSlider;
	double sliderval;
fSlider = (SeekBar) findViewById(R.id.seekBar1);

		// set the listener on the slider
		fSlider.setOnSeekBarChangeListener(listener);
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
*/

