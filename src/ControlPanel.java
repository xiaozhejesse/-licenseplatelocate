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
			loadImage.setText("ͼƬ����");
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
			directLocation.setText("һ���Զ�λ");
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
			toGray.setText("�Ҷ�ת��");
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
			equilibration.setText("ͼ�����");
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
			border.setText("ǿ����Ե");
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
			binarization.setText("��ֵ��");
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
			location.setText("���ƶ�λ");
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
			noteText.setText("����ʶ�𷽰���\n"+"����1.��ȡ����\n"+"����2.�Ҷ�ת��\n"
					+"����3.ͼ�����\n"+"����4.ǿ����Ե\n"+"����5.���ƶ�λ\n"+"����6.��ֵ��\n"+"ͬʱҲ���Խ���������һ�������\n"+"\n�������ڱ�Ե��ʱ����" +
							"������������˹���ӣ���һ��΢������,������ͼ��Ϊf(i,j),�������ͼ��Ϊg(i,j),������˹4���������������£�\n"+
							"float[] elements = { 0.0f, -1.0f, 0.0f, -1.0f, 4.f, -1.0f, 0.0f, -1.0f, 0.0f }\n"+
							"���������ҳ���ʱͨ�����ĸ�������ķ�����λ,���Ͻǵĵ���x����������һ�ξ���ĻҶ�ֵ�ܺ�����һ���ĻҶ��ܺ����ֵ;" +
							"��Y��������һ�ξ���ĻҶ�ֵ�ܺ�����һ���ĻҶ��ܺ����ֵ��" +
							"�����Ƶķ��������ҵ����Ͻǵĵ㡢���½ǵĵ�,�����½ǵĵ㡣Ȼ��ͨ�����ĸ�����ȷ������,��Ҫ�õ����ĸ��ۼ�����");
			noteText.setBounds(185, 60, 218, 427);
		}
		return noteText;
	}

}
