package at.ac.tuwien.motioncollector.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import timeline.ComputedTimeline;
import timeline.CorrelationTimline;
import timeline.JumpRecognitionTimeline;
import timeline.TimelineData;
import at.ac.tuwien.motioncollector.model.DeviceData;

public class ComputationPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private JumpRecognitionTimeline timeline;
	JPanel drawingPanel;

	private Color color;
	private long timespan;

	public ComputationPanel(Color color , long timespan) {
		super();
		this.color = color;
		this.timespan = timespan;

		initialize();
	}

	private void initialize() {
		setBorder(new EmptyBorder(5, 5, 0, 5));
		setLayout(new BorderLayout(0, 0));

		drawingPanel = new JPanel() {

			private static final long serialVersionUID = 1L;

			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);

				timeline.paint(g);

			}
		};
		drawingPanel.setBackground(Color.WHITE);
		add(drawingPanel, BorderLayout.CENTER);
		drawingPanel.setLayout(null);
 
		
		this.timeline =  new JumpRecognitionTimeline();
		this.timeline.setTimespan(timespan);
			
		this.addComponentListener(new ComponentListener() {

			@Override
			public void componentShown(ComponentEvent e) {
			}

			@Override
			public void componentResized(ComponentEvent e) {
				timeline.setHeight(drawingPanel.getHeight());
				timeline.setWidth(drawingPanel.getWidth());
			}

			@Override
			public void componentMoved(ComponentEvent e) {
			}

			@Override
			public void componentHidden(ComponentEvent e) {
			}
		});
	}

	public void setActive(boolean active) {
		this.timeline.setActive(active);
	}

	public void isActive() {
		this.timeline.isActive();
	}



	public void addData(DeviceData data) {
		timeline.appendData(data);
		
		this.drawingPanel.repaint();
		
	}
	


}
