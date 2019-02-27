import java.awt.Color;
import java.awt.Frame;
import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;
import javax.swing.border.MatteBorder;
import javax.swing.JButton;
import javax.swing.JEditorPane;

public class ControlPanel extends Frame {
	
	private JEditorPane noteText;
	private static final long serialVersionUID = 1L;
	public ResultShowPanel cmainFrameshow =null;
	private ImageProcessing ip = null;
	private JButton loadImage = null;
	private JButton directLocation = null;
	private JButton toGray = null;
	private JButton equilibration = null;
	private JButton border = null;
	private JButton binarization = null;
	private JButton location = null;
	
	private JButton getLoadImage() {
		if (loadImage == null) {
			loadImage = new JButton();
			loadImage.setBounds(15, 105, 147, 23);
			loadImage.setText("图片加载");
			loadImage.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					System.out.println("Load Image Button is pressed."); 
					JFileChooser fc = new JFileChooser();
					if (fc.showOpenDialog(loadImage) == JFileChooser.APPROVE_OPTION) {
						ip.readImageFromFile(fc.getSelectedFile());

						if(cmainFrameshow==null)
						{
							cmainFrameshow=new ResultShowPanel();
							cmainFrameshow.pack();
							cmainFrameshow.setSize(400, 510);
							cmainFrameshow.setLocation(420,0);
							cmainFrameshow.setVisible(true);
						}
						cmainFrameshow.ip=ip;
						cmainFrameshow.showImage=ip.getProcessingImage();
						cmainFrameshow.showImagePanel.updateUI();
						

					}
				}
			});
		}
		return loadImage;
	}
	private JButton getDirectLocation() {
		if (directLocation == null) {
			directLocation = new JButton();
			directLocation.setBounds(15, 380, 147, 23);
			directLocation.setText("一次性定位");
			directLocation.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					System.out.println("Processing Button is pressed. "); 
					ip.toGrayMethd();
					ip.equilibrationMethd();
					cmainFrameshow.showImage=ip.borderMethd();
					cmainFrameshow.showImage=ip.imagLocationMethd();
					cmainFrameshow.showImage=ip.binarizationMethd();
					cmainFrameshow.showImagePanel.updateUI();
					cmainFrameshow.histogramPanel.updateUI();
				}
			});
		}
		return directLocation;
	}
	private JButton getToGray() {
		if (toGray == null) {
			toGray = new JButton();
			toGray.setBounds(15, 150, 147, 23);
			toGray.setText("灰度转化");
			toGray.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					System.out.println("Gray Scale Button is pressed. "); 
					ip.toGrayMethd();
					cmainFrameshow.showImagePanel.updateUI();
					cmainFrameshow.histogramPanel.updateUI();
					//showImage=ip.turnGray();

				}
			});
		}
		return toGray;
	}
	private JButton getEquilibration() {
		if (equilibration == null) {
			equilibration = new JButton();
			equilibration.setBounds(15, 195, 147, 23);
			equilibration.setText("图象均衡");
			equilibration.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					System.out.println("Equilibration Button is pressed. "); 
					ip.equilibrationMethd();
					cmainFrameshow.showImagePanel.updateUI();
					cmainFrameshow.histogramPanel.updateUI();
				}
			});
		}
		return equilibration;
	}
	private JButton getPeripherization() {
		if (border == null) {
			border = new JButton();
			border.setBounds(15, 240, 147, 23);
			border.setText("强化边缘");
			border.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					System.out.println("Peripherization Button is pressed. "); 
					cmainFrameshow.showImage=ip.borderMethd();
					cmainFrameshow.showImagePanel.updateUI();
					cmainFrameshow.histogramPanel.updateUI();
					//ip.peripherization();

				}
			});
		}
		return border;
	}
	private JButton getBinarization() {
		if (binarization == null) {
			binarization = new JButton();
			binarization.setBounds(15, 330, 147, 23);
			binarization.setText("二值化");
			binarization.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					System.out.println("Binarization Button is pressed. "); 
					cmainFrameshow.showImage=ip.binarizationMethd();
					cmainFrameshow.showImagePanel.updateUI();
					cmainFrameshow.histogramPanel.updateUI();
					//showImage=ip.border();
				}
			});
		}
		return binarization;
	}
	private JButton getJLocation() {
		if (location == null) {
			location = new JButton();
			location.setBounds(15, 285, 147, 23);
			location.setText("车牌定位");
			location.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					System.out.println("Location Button is pressed. "); 
					cmainFrameshow.showImage=ip.imagLocationMethd();
					cmainFrameshow.showImagePanel.updateUI();
					cmainFrameshow.histogramPanel.updateUI();
				}
			});
		}
		return location;
	}
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI1();
			}
		});
	}
	public ControlPanel() {
		super();
		initialize();
		add(getNoteTextArea());
	}
	private void initialize() {
		ip = new ImageProcessing();
		setLayout(null);
		this.setName("MainFrame");
		this.setTitle("ControlPanel");
		this.add(getEquilibration());
		this.add(getPeripherization());
		this.add(getBinarization());
		this.add(getLoadImage());
		this.add(getToGray());
		this.add(getDirectLocation());
		this.add(getJLocation());
		this.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent e) {
				System.out.println("windowClosing()"); 
				System.exit(0);
			}
		});
    	
	}
	private static void createAndShowGUI1() {

		ControlPanel mainFrame = new ControlPanel();
		mainFrame.pack();
		mainFrame.setSize(420, 510);
		mainFrame.setLocation(0, 0);
		mainFrame.setVisible(true);

	}
	protected JEditorPane getNoteTextArea() {
		if (noteText == null) {
			noteText = (JEditorPane) new JEditorPane();
			noteText.setBorder(new MatteBorder(2, 0, 2, 0, Color.black));
			noteText.setBackground(Color.WHITE);
			noteText.setText("车牌识别方案：\n"+"步骤1.读取数据\n"+"步骤2.灰度转化\n"
					+"步骤3.图象均衡\n"+"步骤4.强化边缘\n"+"步骤5.车牌定位\n"+"步骤6.二值化\n"+"同时也可以将上述步骤一次性完成\n"+"\n本方案在边缘化时采用" +
							"采用了拉普拉斯算子，是一种微分算子,是输入图像为f(i,j),算子输出图像为g(i,j),拉普拉斯4算子邻域算子如下：\n"+
							"float[] elements = { 0.0f, -1.0f, 0.0f, -1.0f, 4.f, -1.0f, 0.0f, -1.0f, 0.0f }\n"+
							"本方案在找车牌时通过找四个特征点的方法定位,左上角的点往x轴正方向走一段距离的灰度值总和满足一定的灰度总和最低值;" +
							"往Y轴向下走一段距离的灰度值总和满足一定的灰度总和最低值。" +
							"用相似的方法可以找到右上角的点、左下角的点,和右下角的点。然后通过这四个点来确定矩形,主要用到了四个累加器。");
			noteText.setBounds(185, 60, 218, 427);
		}
		return noteText;
	}

}
