import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.JPanel;

public class ResultShowPanel extends Frame {
	
	private static final long serialVersionUID = 1L;
	public JPanel histogramPanel = null;
	public JPanel showImagePanel = null;
	public ImageProcessing ip = null;
	public Image showImage = null;
	public Image showSubImage =null;
	
	public ResultShowPanel() {
		super();
		initialize();
	}
	private JPanel getShowImagePanel() {
		if (showImagePanel == null) {
			showImagePanel = new JPanel() {
				private static final long serialVersionUID = 1L;
				public void paint(Graphics arg0) {
					super.paint(arg0);
					if (showImage != null) {
						arg0.drawImage(showImage, 0, 0, null, null);
					}
				}
				
			};			
			showImagePanel.setBounds(25, 65, 353, 240);
			showImagePanel.setLayout(null);
			showImagePanel.setPreferredSize(new java.awt.Dimension(320,240));

		}
		return showImagePanel;
	}
	private JPanel getHistogramPanel() {
		if (histogramPanel == null) {
			histogramPanel = new JPanel() {
				private static final long serialVersionUID = 1L;
				public void paint(Graphics arg0) {
					super.paint(arg0);
					arg0.setColor(Color.BLACK);
					int startx = (getWidth() - 256) / 2;
					int starty = (getHeight() - 100) / 2 + 100;
					arg0.drawString("0", startx - 15, starty + 15);
					arg0.drawLine(startx - 1, 50, startx - 1, getHeight() - 50);
					for (int i = 50; i <= 256; i += 50) {
						arg0.drawString("" + i, startx + i - 15, starty + 15);
					}
					arg0.drawLine(5, starty + 1, getWidth() - 5, starty + 1);
					for (int i = 20; i <= 100; i += 20) {
						arg0.drawString("" + Math.pow(10, i / 20)/100000.0, startx - 45, starty - i + 15);
					}
					if (ip.getHistogramArray() != null) {
						int[] histogram = ip.getHistogramArray();
						arg0.setColor(Color.black);
						for (int i = 0; i < 256; i++) {
							if (histogram[i] <= 10) {
								arg0.drawLine(startx + i, starty, startx + i, starty - histogram[i] * 2);
							}
							else if (histogram[i] > 10 && histogram[i] <= 100) {
								arg0.drawLine(startx + i, starty, startx + i, starty - 20 - histogram[i] / 5);
							}
							else if (histogram[i] > 100 && histogram[i] <= 1000) {
								arg0.drawLine(startx + i, starty, startx + i, starty - 40 - histogram[i] / 50);
							}
							else if (histogram[i] > 1000 && histogram[i] <= 10000) {
								arg0.drawLine(startx + i, starty, startx + i, starty - 60 - histogram[i] / 500);
							}
							else if (histogram[i] > 10000 && histogram[i] <= 100000) {
								arg0.drawLine(startx + i, starty, startx + i, starty - 80 - histogram[i] / 5000);
							}
							else {
								arg0.drawLine(startx + i, starty, startx + i, starty - 100);
							}
						}
					}
				}
				
			};
			histogramPanel.setBounds(25, 325, 353, 158);
			histogramPanel.setPreferredSize(new java.awt.Dimension(350,240));
		}
		return histogramPanel;
	}
	private void initialize() {
		ip = new ImageProcessing();
		setLayout(null);
		this.setName("MainFrame");
		this.setTitle("ShowPanel");
		this.add(getShowImagePanel());
		this.add(getHistogramPanel());
		this.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent e) {
				System.out.println("windowClosing()"); 
				System.exit(0);
			}
		});
 	}
	
}
