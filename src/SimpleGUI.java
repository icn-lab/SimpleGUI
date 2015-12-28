

// Copyright (c) 2015 Intelligent Communication Network (Ito-Nose) Laboratory
// Tohoku University.
// 
// All rights reserved.
// 
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions are met:
// * Redistributions of source code must retain the above copyright notice, 
// this list of conditions and the following disclaimer.
// * Redistributions in binary form must reproduce the above copyright notice, 
// this list of conditions and the following disclaimer in the documentation 
// and/or other materials provided with the distribution.
// * Neither the name of the "Intelligent Communication Network Laboratory, Tohoku University" nor the names of its contributors 
// may be used to endorse or promote products derived from this software 
// without specific prior written permission.
// 
// THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
// ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
// WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
// DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
// DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
// (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
// LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
// ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
// (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
// SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

import org.Gyutan.*;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.*;
import java.awt.event.*;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Hashtable;
import java.util.Properties;


public class SimpleGUI {
	final String progname = "SimpleGUI";
	final String version = "version: 20151228";
	
	Properties properties;
	String propertyName = progname+".properties";
	
	JFrame frame;
	JPanel panel;
	
	JTextArea  textArea;
	JSlider    speedSlider;
	
	JButton   doSynthesizeButton;

	JMenuBar  menuBar;
	JMenu     fileMenu;
	JMenuItem menuItemSave;
	JMenu     configMenu;
	JMenu     helpMenu;
	
	Gyutan gyutan;
	String htsVoice = null;
	String senHome  = null;
	
	int minSpeed = 1;
	int maxSpeed = 40;
	int curSpeed = 10;
	double speed = 0.0;
	
	public void initialize(){
		// initialize OpenJTalk;
		gyutan = new Gyutan();
		// initialize GUI;
		initializeGUI();
				
		setSpeed(curSpeed);
		// load properties file
		properties = loadProperties(propertyName);
		
		// set settings
		htsVoice = getProperty("htsVoice");
		if(htsVoice != null)
			gyutan.initializeEngine(htsVoice);

		senHome  = getProperty("senHome");
		if(senHome != null)
			gyutan.initializeSen(senHome);
		
		setSynthesizeButtonState();
	}
	
	public void initializeGUI(){
		frame = new JFrame();
		frame.setTitle(progname);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		panel = new JPanel();
		panel.setLayout(new BorderLayout());
		
		textArea     = new JTextArea(10,20);
		textArea.setLineWrap(true);
		textArea.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 32));
		JScrollPane scrollPane = new JScrollPane(textArea);
		
		JPanel subPanel = new JPanel();
		subPanel.setLayout(new BorderLayout());
	
		speedSlider = new JSlider(SwingConstants.HORIZONTAL, minSpeed, maxSpeed, curSpeed);
		speedSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent event){
				setSpeed(speedSlider.getValue());
			}
		});
		
		Hashtable<Integer, Component> sliderLabel = new Hashtable<Integer, Component>();
		sliderLabel.put(minSpeed, new JLabel("Slow"));
		sliderLabel.put(maxSpeed, new JLabel("Fast"));
		speedSlider.setLabelTable(sliderLabel);
		speedSlider.setPaintLabels(true);
		
		doSynthesizeButton = new JButton("Synthesize");
		doSynthesizeButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event){
				doSynthesize(speed);
			}
		});
		
		subPanel.add("Center", speedSlider);
		subPanel.add("South", doSynthesizeButton);

		menuBar    = new JMenuBar();
		fileMenu   = new JMenu("File");
		configMenu = new JMenu("Settings");
		helpMenu   = new JMenu("Help");
		
		menuItemSave = new JMenuItem("Save WAV");
		menuItemSave.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event){
				saveWAV();
			}
		});
		
		JMenuItem menuItemExit = new JMenuItem("exit");
		menuItemExit.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event){
				doExit();
			}
		});
		
		fileMenu.add(menuItemSave);
		fileMenu.add(menuItemExit);
		menuItemSave.setEnabled(false);
		
		JMenuItem menuItemConfigSen      = new JMenuItem("Sen Home");
		menuItemConfigSen.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event){
				setSenHome();
			}
		});
		
		JMenuItem menuItemConfigHTSVoice = new JMenuItem("htsvoice");
		menuItemConfigHTSVoice.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event){
				setHTSVoice();
			}
		});
		
		configMenu.add(menuItemConfigSen);
		configMenu.add(menuItemConfigHTSVoice);
	
		
		JMenuItem menuItemAbout = new JMenuItem("About");
		menuItemAbout.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event){
				JOptionPane.showMessageDialog(frame, String.format("%s\n%s", progname, version));
			}
		});
		
		helpMenu.add(menuItemAbout);

		menuBar.add(fileMenu);
		menuBar.add(configMenu);
		menuBar.add(helpMenu);
		
		panel.add("North", menuBar);
		panel.add("Center", scrollPane);
		panel.add("South", subPanel);
		//panel.add("South", doSynthesizeButton);

		frame.add(panel);
		frame.pack();
		frame.setVisible(true);
	}
	
	public Properties loadProperties(String filename){
		Properties prop = new Properties();
		try{
			prop.load(new FileInputStream(filename));
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return prop;
	}
	
	public void storeProperties(String filename){
		try{
			properties.store(new FileOutputStream(filename), null);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void setProperty(String key, String value){
		properties.setProperty(key, value);
	}
	
	public String getProperty(String key){
		if(properties.containsKey(key))
			return properties.getProperty(key);
		else
			return null;
	}
	
	public void doSynthesize(){
		if(gyutan.availableSen() && gyutan.availableEngine()){
			String text = textArea.getText();
			if(text != null && text.length() > 0){
				gyutan.set_audio_buff_size(10000);
				gyutan.synthesis(text);
				menuItemSave.setEnabled(true);
			}
		}
	}
	
	public void doSynthesize(double speed){
		if(gyutan.availableSen() && gyutan.availableEngine()){
			String text = textArea.getText();
			if(text != null && text.length() > 0){
				gyutan.set_audio_buff_size(10000);
				gyutan.set_speed(speed);
				gyutan.synthesis(text, null, null);
				menuItemSave.setEnabled(true);
			}
		}
	}
	
	public void setSenHome(){
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

		while(true){
			int selected = fileChooser.showOpenDialog(frame);
			if(selected == JFileChooser.APPROVE_OPTION){
				senHome = fileChooser.getSelectedFile().toString();
				System.err.printf("file:%s\n", senHome);
				gyutan.initializeSen(senHome);
				if(gyutan.availableSen()){
					setProperty("senHome", senHome);
					break;
				}
			}
			else
				break;
		}
		
		setSynthesizeButtonState();
	}
	
	public void setHTSVoice(){
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		
		// add .htsvoice extension filter
		FileFilter filter = new FileNameExtensionFilter("htsvoice file", "htsvoice");
		fileChooser.addChoosableFileFilter(filter);
		
		while(true){
			int selected = fileChooser.showOpenDialog(frame);
			if(selected == JFileChooser.APPROVE_OPTION){
				htsVoice = fileChooser.getSelectedFile().toString();
				gyutan.initializeEngine(htsVoice);
				if(gyutan.availableEngine()){
					setProperty("htsVoice", htsVoice);
					break;
				}
			}
			else
				break;
		}
		
		setSynthesizeButtonState();
	}
	
	public void setSpeed(int value){
		curSpeed = value;
		speed = curSpeed / 10.0;
	}
	
	public void saveWAV(){
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		int selected = fileChooser.showSaveDialog(frame);
		try{
			if(selected == JFileChooser.APPROVE_OPTION)
				gyutan.save_riff(new FileOutputStream(fileChooser.getSelectedFile()));
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void setSynthesizeButtonState(){
		if(gyutan.availableSen() && gyutan.availableEngine()){
			storeProperties(propertyName);
			doSynthesizeButton.setEnabled(true);
		}
		else{
			doSynthesizeButton.setEnabled(false);
		}
	}
	
	public void doExit(){
		System.exit(0);
	}
	
	public static void main(String[] args){
		SimpleGUI simpleGUI = new SimpleGUI();
		simpleGUI.initialize();
	}
}
