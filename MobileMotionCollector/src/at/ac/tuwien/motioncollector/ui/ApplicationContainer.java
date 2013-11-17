package at.ac.tuwien.motioncollector.ui;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream.GetField;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.InvalidPropertiesFormatException;
import java.util.List;
import java.util.Properties;

import javax.swing.JFrame;

import at.ac.tuwien.motioncollector.Settings;
import at.ac.tuwien.motioncollector.SettingsKeys;
import at.ac.tuwien.motioncollector.handler.DeviceDataConsoleWriter;
import at.ac.tuwien.motioncollector.handler.DeviceDataWindowHandler;
import at.ac.tuwien.motioncollector.handler.UIDeviceDataHandler;
import at.ac.tuwien.motioncollector.model.*;
import at.ac.tuwien.motioncollector.osc.AddressBroadcastSender;
import at.ac.tuwien.motioncollector.osc.DeviceDataListener;

import javax.swing.JPanel;

import java.awt.BorderLayout;

import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.border.EmptyBorder;

import java.awt.Color;

public class ApplicationContainer {

	private static ApplicationContainer instance;

	private JFrame frame;

	private DevicesPanel devicesPanel;
	
	private DeviceDataListener listener;
	private AddressBroadcastSender broadcastSender;

	private DeviceDataWindowHandler deviceDataWindowHandler;
	
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

		try {
			this.broadcastSender = AddressBroadcastSender.startInstance(
					NetworkInterface.getByName("en1"),
					InetAddress.getByName("192.168.0.255"),
					Settings.getInt(SettingsKeys.OSCBroadcastPort.key()));
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * Initialize the contents of the frame.
	 */
	JButton btnStop;
	JButton btnStart;
	private JButton btnOpenDevice;

	private UIDeviceDataHandler uiDeviceDataHandler;

	private void initialize() {

		frame = new JFrame();

		frame.setTitle(Settings.getValue(SettingsKeys.ApplicationName.key())
				+ " " + Settings.getValue(SettingsKeys.Version.key()));

		frame.setBounds(100, 100, 604, 368);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				if (listener != null) {
					try {
						ApplicationContainer.this.listener.close();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				if (broadcastSender != null) {
					broadcastSender.stop();
				}

				super.windowClosing(e);
			}
		});

		this.devicesPanel = new DevicesPanel();
		frame.getContentPane().add(devicesPanel, BorderLayout.WEST);
		devicesPanel.setPreferredSize(new Dimension(200, 100));

		JPanel panel = new JPanel();
		frame.getContentPane().add(panel, BorderLayout.SOUTH);

		btnStop = new JButton("Stop");
		btnStart = new JButton("Start");
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					ApplicationContainer.this.createListener();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				btnStart.setEnabled(false);
				btnStop.setEnabled(true);
			}
		});
		
		btnOpenDevice = new JButton("Open Device");
		btnOpenDevice.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Device device = devicesPanel.getSelectedDevice();
				DeviceDataWindow window = new DeviceDataWindow(device);
				
				window.setVisible(true);
				
				deviceDataWindowHandler.registerWindow(window);
				
			}
		});
		panel.add(btnOpenDevice);
		panel.add(btnStart);

		btnStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					ApplicationContainer.this.closeListener();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				btnStart.setEnabled(true);
				btnStop.setEnabled(false);
			}
		});
		panel.add(btnStop);

		btnStop.setEnabled(false);
	}

	private void createListener() throws IOException {
		listener = new DeviceDataListener();
		
		this.uiDeviceDataHandler = new UIDeviceDataHandler();
		this.deviceDataWindowHandler = new DeviceDataWindowHandler();
		
		listener.registerHandler(this.uiDeviceDataHandler);
		listener.registerHandler(this.deviceDataWindowHandler);
//		this.deviceDataWindowHandler = new DeviceDataWindowHandler();
//		listener.registerHandler(this.deviceDataWindowHandler);
//		
//		
		listener.startListening();
	}

	private void closeListener() throws IOException {
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
	
	public DeviceDataWindowHandler getDeviceDataWindowHandler() {
		return deviceDataWindowHandler;
	}
}
