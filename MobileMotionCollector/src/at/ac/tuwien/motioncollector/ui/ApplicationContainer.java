package at.ac.tuwien.motioncollector.ui;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.InvalidPropertiesFormatException;
import java.util.List;
import java.util.Properties;

import javax.swing.JFrame;

import at.ac.tuwien.motioncollector.Settings;
import at.ac.tuwien.motioncollector.SettingsKeys;
import at.ac.tuwien.motioncollector.handler.DeviceDataConsoleWriter;
import at.ac.tuwien.motioncollector.handler.UIDeviceDataHandler;
import at.ac.tuwien.motioncollector.model.*;
import at.ac.tuwien.motioncollector.osc.DeviceDataListener;

import javax.swing.JPanel;

import java.awt.BorderLayout;

import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.border.EmptyBorder;

public class ApplicationContainer {

	private static ApplicationContainer instance;
	
	
	private JFrame frame;
	
	private DevicesPanel devicesPanel;
	private TimelinePanel timelinePanel;
	
	private DeviceDataListener listener;
	
	/**
	 * Launch the application.
	 */
	
	public static ApplicationContainer getInstance() {
		return instance;
	}
	
	public static void main(String[] args) {

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					instance = new ApplicationContainer();
					instance.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	private ApplicationContainer() {
		initialize();
		System.out.println("Working Directory = " +
	              System.getProperty("user.dir"));
		
	}

	/**
	 * Initialize the contents of the frame.
	 */
	JButton btnStop;
	JButton btnStart;
	
	private void initialize() {
		
		frame = new JFrame();
		
		frame.setTitle(Settings.getValue(SettingsKeys.ApplicationName.key())+" "+ Settings.getValue(SettingsKeys.Version.key() ));
		
		frame.setBounds(100, 100, 604, 368);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) { 
				if(listener!=null){
					ApplicationContainer.this.listener.close();
				}
				
				super.windowClosing(e);
			}
		});
		
		this.devicesPanel = new DevicesPanel();
		frame.getContentPane().add(devicesPanel,BorderLayout.WEST);
		devicesPanel.setPreferredSize(new Dimension(200, 100));
		
		this.timelinePanel = new TimelinePanel();
		timelinePanel.setBorder(new EmptyBorder(10, 0, 10, 10));
		frame.getContentPane().add(timelinePanel,BorderLayout.CENTER);
		
		
		
		
		JPanel panel = new JPanel();
		frame.getContentPane().add(panel, BorderLayout.SOUTH);
		
		btnStop = new JButton("Stop");
		btnStart = new JButton("Start");
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ApplicationContainer.this.createListener();
				btnStart.setEnabled(false);
				btnStop.setEnabled(true);
			}
		});
		panel.add(btnStart);
		
		
		btnStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ApplicationContainer.this.closeListener();
				btnStart.setEnabled(true);
				btnStop.setEnabled(false);
			}
		});
		panel.add(btnStop);
		
		btnStop.setEnabled(false);
	}
	private void createListener(){
		listener = new DeviceDataListener();
		listener.registerHandler(new DeviceDataConsoleWriter());
		listener.registerHandler(new UIDeviceDataHandler());
		new Thread(listener).start();
		listener.startListening();
	}
	
	private void closeListener(){
		this.listener.stopListening();
		this.listener.close();
		this.listener = null;
	}
	
	
	public DeviceDataListener getListener() {
		return listener;
	}
	public DevicesPanel getDevicesPanel() {
		return devicesPanel;
	}
	
	
	

}
