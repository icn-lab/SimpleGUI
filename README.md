# SimpleGUI
Copyright (c) 2015 Intelligent Communication Network (Ito-Nose) Laboratory
                   Tohoku University.

All rights reserved.

SimpleGUI is a Text-to-Speech software with simple GUI.
This software is a sample code to use Gyutan.
Thanks to HTS working group, speech synthesis can be used easier. 

## How to use
See bin/SimpleGUI.sh (OS X, linux) or bin/SimpleGUI.bat (Windows)
To run, you need some libraries.
 * Sasakama(Sasakama.jar)
 * HTS voice (htsvoice)
 * Gyutan(Gyutan.jar)
 * Japanese morphological analyzer(sen.jar, junit.jar, commons-logging.jar)
 * dictionary(sen/{dic, conf})

To set your environment, edit bin/SimpleGUI.sh.

    % sh SimpleGUI.sh

When program is run, some error message like below is displayed.

    java.io.FileNotFoundException: SimpleGUI.properties (そのようなファイルやディレクトリはありません)
    			       at java.io.FileInputStream.open(Native Method)
    			       at java.io.FileInputStream.<init>(FileInputStream.java:146)
    			       at java.io.FileInputStream.<init>(FileInputStream.java:101)
    			       at SimpleGUI.loadProperties(SimpleGUI.java:120)
    			       at SimpleGUI.initialize(SimpleGUI.java:35)
    			       at SimpleGUI.main(SimpleGUI.java:229)

This messeages means that properties file is missing.
Please ignore this message.

When SimpleGUI is run, if properties file is found, settings are read from properties file.

First, you must set directory path of Sen dictionary and file path of htsvoice.
You can set these paths on settings tab.
When you set these paths correctly, Synthesize button is enabled.
If you set paths correctly, path setting is saved to properties file named "SimpleGUI.properties".

You input text and push Synthesize button to synthesize text.
If you want to save synthesized speech to file, you select File->Save WAV.
You can save synthesized speech to WAV format file.
