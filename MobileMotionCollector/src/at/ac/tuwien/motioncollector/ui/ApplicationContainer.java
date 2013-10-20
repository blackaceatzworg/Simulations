package at.ac.tuwien.motioncollector.ui;

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
import at.ac.tuwien.motioncollector.model.*;
import at.ac.tuwien.motioncollector.osc.DeviceDataListener;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ApplicationContainer {

	private static ApplicationContainer instance;
	
	
	private JFrame frame;

	private List<Device> devices;

	
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
		//this.listener = new DeviceDataListener();
		//this.listener.registerHandler(new DeviceDataConsoleWriter());
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		frame = new JFrame();
		
		frame.setTitle(Settings.getValue(SettingsKeys.ApplicationName.key())+" "+ Settings.getValue(SettingsKeys.Version.key() ));
		
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel();
		frame.getContentPane().add(panel, BorderLayout.SOUTH);
		
		JButton btnStart = new JButton("Start");
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ApplicationContainer.this.listener.startListening();
			}
		});
		panel.add(btnStart);
	}
	
	public DeviceDataListener getListener() {
		return listener;
	}
	
	
	
	

}
