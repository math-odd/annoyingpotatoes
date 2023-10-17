package engine;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.FloatControl.Type;

public class SoundManager {
    private static HashMap<String, Clip> clips = new HashMap<>();
    private static ArrayList<Clip> bgms = new ArrayList<>();
    private static float masterVolume = screen.SettingScreen.getSoundVolume();
    private static final float minimum = -80;
    private static final float maximum = 6;
    private static final float one = Math.abs((minimum-maximum)/100);
    private static float master = (float)(minimum + one*(50*Math.log10(masterVolume)));

    public static void playSound(String soundFilePath, String clipName, boolean isLoop, boolean isBgm) {
        Clip clip = clips.get(clipName);
        if (clip != null && clip.isActive()) {
            return;
        }
        new Thread(new Runnable() {
            public void run() {
                try {
                    File soundFile = new File(soundFilePath);
                    AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
                    Clip clip = AudioSystem.getClip();
                    clip.open(audioIn);
                    FloatControl floatControl = (FloatControl)clip.getControl(Type.MASTER_GAIN);
                    floatControl.setValue(master);
                    if (isLoop) {
                        clip.loop(-1);
                    } else {
                        clip.start();
                    }
                    clips.put(clipName, clip);
                    if(isBgm) bgms.add(clip);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static void playSound(String soundFilePath, String clipName, boolean isLoop, boolean isBgm, float fadeInSpeed) {
        Clip clip = clips.get(clipName);
        if (clip != null && clip.isActive()) {
            return;
        }
        new Thread(new Runnable() {
            public void run() {
                try {
                    File soundFile = new File(soundFilePath);
                    AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
                    Clip clip = AudioSystem.getClip();
                    clip.open(audioIn);
                    FloatControl floatControl = (FloatControl) clip.getControl(Type.MASTER_GAIN);
                    floatControl.setValue(minimum);
                    if (isLoop) {
                        clip.loop(-1);
                    } else {
                        clip.start();
                    }
                    clips.put(clipName, clip);
                    if(isBgm) bgms.add(clip);
                    fadeIn(clipName, fadeInSpeed);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static void stopSound(String clipName) {
        Clip clip = clips.get(clipName);
        if (clip != null && clip.isActive()) {
            clip.stop();
            bgms.remove(clip);
        }
    }

    public static void stopSound(String clipName, float fadeoutSpeed) {
        Clip clip = clips.get(clipName);
        if (clip != null && clip.isActive()) {
            new Thread(new Runnable() {
                public void run() {
                    float volume = ((FloatControl) clip.getControl(Type.MASTER_GAIN)).getValue();
                    FloatControl floatControl = (FloatControl) clips.get(clipName).getControl(Type.MASTER_GAIN);
                    while (volume > minimum) {
                        floatControl.setValue(volume);
                        volume -= (0.4 * fadeoutSpeed);
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    clip.stop();
                    bgms.remove(clip);
                }
            }).start();
        }
    }

    private static void fadeIn(String clipName, float fadeInSpeed) {
        new Thread(new Runnable() {
            Clip clip = clips.get(clipName);

            public void run() {
                float volume = ((FloatControl) clip.getControl(Type.MASTER_GAIN)).getValue();
                FloatControl floatControl = (FloatControl) clips.get(clipName).getControl(Type.MASTER_GAIN);
                floatControl.setValue(-80);
                while (volume < master) {
                    floatControl.setValue(volume);
                    volume += (0.4 * fadeInSpeed);
                    if(volume>0) volume = 0;
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }).start();
    }

    public static void setMasterVolume(float volume) {
        masterVolume = volume;
        for (Clip clip : clips.values()) {
            if (clip != null && clip.isActive()) {
                FloatControl floatControl = (FloatControl) clip.getControl(Type.MASTER_GAIN);
                master = (float)(minimum + one*(50*Math.log10(volume)));
                floatControl.setValue(master);
                System.out.println(volume+" = "+ master);
            }
        }
    }

    public static void bgmSetting(boolean bgm){
        if(bgm){
            for(Clip clip : bgms){
                FloatControl floatControl = (FloatControl)clip.getControl(Type.MASTER_GAIN);
                floatControl.setValue(master);
            }
        }
        else{
            for(Clip clip : bgms){
                FloatControl floatControl = (FloatControl)clip.getControl(Type.MASTER_GAIN);
                floatControl.setValue((float)(minimum + one*(50*Math.log10(0))));
            }
        }
    }

    public static void setVolume(String clipName, double percent){
        Clip clip = clips.get(clipName);
        FloatControl floatcontrol = (FloatControl)clip.getControl(Type.MASTER_GAIN);
        float volume = floatcontrol.getValue();
        floatcontrol.setValue((float)(volume*percent));
    }
}