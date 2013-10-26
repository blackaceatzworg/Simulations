package at.ac.tuwien.motioncollector.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import at.ac.tuwien.motioncollector.model.TimelineData;
import at.ac.tuwien.motioncollector.model.ui.UITimeline;

public class TimelinePanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private UITimeline timeline;
	JPanel drawingPanel;

	private Color color;
	private long timespan;

	public TimelinePanel(Color color, long timespan) {
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

		timeline = new UITimeline(color, drawingPanel.getHeight(),
				drawingPanel.getWidth(), timespan);

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

	public void addData(TimelineData data) {
		timeline.appendData(data);

		this.drawingPanel.repaint();

	}

}
