package com.example.mysoundsample;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

class SoundThreadManager{
	SoundTimeThread[] m_threads = new SoundTimeThread[3];
	
	public SoundThreadManager(){
	}
	
	public void allStop(){
		for(int i = 0; i < m_threads.length; i++){
			if(m_threads[i] != null){
				m_threads[i].triggerStop();
				try {
					m_threads[i].join();
				} catch (InterruptedException e) {
					//throw e;
				}
			}
		}
	}
	
	public void pushSound(double[] frs, int channel, long millisec){
		m_threads[channel] = new SoundTimeThread(frs, millisec);
	}
	
}


class SoundTimeThread extends Thread{
	int sr = 44100; // 44.1Kbps
	int m_amp = 10000;
	
	int m_buffsize;
	double twopi = 8. * Math.atan(1.);
	AudioTrack m_audioTrack;
	double[] m_phs = new double[]{0};
	double[] m_frs = new double[]{440};
	long m_millisec;
	long m_stopTime;

	/*
	 * Usage: SoundTimeThread t = new SoundTimeThread(440, 1000);　t.prepare(); t.start(); t.play();
	 */
	public SoundTimeThread(double[] frs, long millisec){
		m_frs = frs.clone();
		m_millisec = millisec;
		m_phs = new double[m_frs.length];
		m_stopTime = System.currentTimeMillis() + millisec;
		m_buffsize = AudioTrack.getMinBufferSize(sr,
				AudioFormat.CHANNEL_OUT_MONO,
				AudioFormat.ENCODING_PCM_16BIT); // 11288
		m_audioTrack = new AudioTrack(
				AudioManager.STREAM_MUSIC, sr,
				AudioFormat.CHANNEL_OUT_MONO,
				AudioFormat.ENCODING_PCM_16BIT, m_buffsize,
				AudioTrack.MODE_STREAM);
		m_audioTrack.play(); // すぐに鳴らす
		start();
	}
	
	public void triggerStop(){
		m_stopTime = 0;
	}
	
	// 最初に必要なバッファを用意する
	short[] samples;
	public void prepare(){
		if(samples == null)samples = new short[m_buffsize];
		for (int i = 0; i < m_buffsize; i++) {
			samples[i] = 0;
		}
		for(int j = 0; j < m_phs.length; j++){
			double ph = m_phs[j];
			double fr = m_frs[j];
			double amp = m_amp / m_phs.length;
			for (int i = 0; i < m_buffsize; i++) {
				samples[i] += (short) (amp * Math.sin(ph)); // サイン波
				ph += twopi * fr / sr;
			}
			m_phs[j] = ph;
		}
		m_audioTrack.write(samples, 0, m_buffsize);
	}

	@Override
	public void run() {
		m_stopTime = System.currentTimeMillis() + m_millisec;
		while(System.currentTimeMillis() < m_stopTime){
			prepare();
			// 0.2秒休む
			/*
			try{
				Thread.sleep((long)(0.2 * 1000));
			}
			catch(InterruptedException ex){
				break;
			}
			*/
		}
		m_audioTrack.stop();
		m_audioTrack.release();
	}
}

/*
class SoundThread extends Thread{
	boolean m_isRunning = true;
	int sr = 44100; // 44.1Kbps
	int amp = 10000;
	int buffsize;
	double twopi = 8. * Math.atan(1.);
	AudioTrack m_audioTrack;
	double m_ph = 0;
	double m_fr = 440;
	long m_startTime;
	long m_stopTime;
	boolean m_stopping = false;
	boolean m_stopBuffer = true;
	//short[] samples;
	
	public void bufferStop(){
		m_stopBuffer = true;
	}
	public void bufferStart(){
		m_stopBuffer = false;
	}
	
	public void soundStop(){
		m_startTime = m_stopTime = 0;
		m_stopping = true;
		m_audioTrack.stop();
	}
	public void soundPlay(){
		m_stopping = false;
		m_audioTrack.play();
	}
	
	public void pushSound(double fr, long millisec){
		m_fr = fr;
		m_ph = 0;
		m_stopTime = System.currentTimeMillis() + millisec;
		//if(samples == null)samples = new short[buffsize];
		/*
		double ph = 0.0;
		for (int i = 0; i < buffsize; i++) {
			samples[i] = (short) (amp * Math.sin(ph)); // サイン波
			ph += twopi * fr / sr;
		}
		m_audioTrack.write(samples, 0, buffsize);
		/
	}
	short[] samples;
	public void soundBuffer(){
		if(samples == null)samples = new short[buffsize];
		for (int i = 0; i < buffsize; i++) {
			samples[i] = (short) (amp * Math.sin(m_ph)); // サイン波
			m_ph += twopi * m_fr / sr;
		}
		m_audioTrack.write(samples, 0, buffsize);
	}
	public void soundBufferClear(){
	}

	
	public void triggerStop(){
		m_isRunning = false;
	}
	
	@Override
	public void run() {
		// set process priority
		//setPriority(Thread.MAX_PRIORITY);
		// set the buffer size
		buffsize = AudioTrack.getMinBufferSize(sr,
				AudioFormat.CHANNEL_OUT_MONO,
				AudioFormat.ENCODING_PCM_16BIT); // 11288
		
		// create an audiotrack object
		m_audioTrack = new AudioTrack(
				AudioManager.STREAM_MUSIC, sr,
				AudioFormat.CHANNEL_OUT_MONO,
				AudioFormat.ENCODING_PCM_16BIT, buffsize,
				AudioTrack.MODE_STREAM);

		// start audio
		m_audioTrack.play();

		// synthesis loop
		while (m_isRunning) {
			long t = System.currentTimeMillis();
			if(!m_stopBuffer && t < m_stopTime){
				//short samples[] = new short[buffsize];
				soundBuffer();
				// 0.1秒休む
				try{
					Thread.sleep((long)(0.1 * 1000));
				}
				catch(InterruptedException ex){
					break;
				}
			}
			else{
				m_audioTrack.stop();
				soundBufferClear();
				// 0.2秒休む
				try{
					Thread.sleep((long)(0.2 * 1000));
				}
				catch(InterruptedException ex){
					break;
				}
			}
		}
		
		// 後処理
		m_audioTrack.stop();
		m_audioTrack.release();
	}
}
*/
