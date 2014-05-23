/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.hadoop.examples.render.tools;
// ant includes

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;


/**
 * From AntSoundPlayer
 */
public class SoundPlayer implements LineListener {
  private static final Logger log = LoggerFactory.getLogger(SoundPlayer.class);

  private final File file;
  private final int loops;
  private final Long duration;

  /**
   * @param file the location of the audio file to be played 
   * @param loops the number of times the file should be played 
   * @param duration the number of milliseconds the file should be
   *        played
   */
  public SoundPlayer(File file, int loops, Long duration) {
    this.file = file;
    this.loops = loops;
    this.duration = duration;
  }

  /**
   * Plays the file for duration milliseconds or loops.
   */
  public  boolean play() throws IOException {

    Clip audioClip = null;

    try (AudioInputStream audioInputStream =
             AudioSystem.getAudioInputStream(file)) {

      if (audioInputStream == null) {
        log.warn("Can't get data from file {}", file);
        return false;
      }

      AudioFormat format = audioInputStream.getFormat();
      DataLine.Info info = new DataLine.Info(Clip.class, format,
          AudioSystem.NOT_SPECIFIED);

      audioClip = (Clip) AudioSystem.getLine(info);
      audioClip.addLineListener(this);
      audioClip.open(audioInputStream);

      if (duration != null) {
        playClip(audioClip, duration);
      } else {
        playClip(audioClip, loops);
      }

    } catch (LineUnavailableException e) {
      log.info("The sound device is currently unavailable: {}", e, e);
      return false;

    } catch (UnsupportedAudioFileException uafe) {
      log.info("Audio format is not yet supported: "
               + uafe, uafe);
      return false;

    } finally {
      if (audioClip != null) {
        audioClip.drain();
        audioClip.close();
      }
    }
    return true;
  }

  private void playClip(Clip clip, int loops) {

    clip.loop(loops);
    do {
      try {
        long timeLeft =
            (clip.getMicrosecondLength() - clip.getMicrosecondPosition())
            / 1000;
        if (timeLeft > 0) {
          Thread.sleep(timeLeft);
        }
      } catch (InterruptedException e) {
        break;
      }
    } while (clip.isRunning());

    if (clip.isRunning()) {
      clip.stop();
    }
  }

  private void playClip(Clip clip, long duration) {
    clip.loop(Clip.LOOP_CONTINUOUSLY);
    try {
      Thread.sleep(duration);
    } catch (InterruptedException e) {
      // Ignore Exception
    }
    clip.stop();
  }

  /**
   * This is implemented to listen for any line events and closes the
   * clip if required.
   * @param event the line event to follow
   */
  public void update(LineEvent event) {
    if (event.getType().equals(LineEvent.Type.STOP)) {
      Line line = event.getLine();
      line.close();
    }
  }


}

