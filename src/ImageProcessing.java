import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.awt.image.Kernel;
import javax.imageio.ImageIO;
import java.awt.image.ConvolveOp;


public class ImageProcessing {

	private Image processingImage = null;
	private BufferedImage currentImage = null;
	private BufferedImage originalImage = null;
	private int width;
	private int height;
	private int[][][] pixels = null;
	private int[] histogramArray = null;

	
	
	public void readImageFromFile(File file) {
		try {
			originalImage = ImageIO.read(new FileInputStream(file));
			currentImage = ImageIO.read(new FileInputStream(file));
			processingImage = currentImage;
			initialize();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public Image getProcessingImage() {
		return processingImage;
	}	
	public int[] getHistogramArray() {
		return histogramArray;
	}
	private void initialize() {
		width = currentImage.getWidth();
		height = currentImage.getHeight();
		pixels = new int[width][height][4];
		histogramArray = new int[256];
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				int pixel = currentImage.getRGB(i, j);
				pixels[i][j][0] = getAlpha(pixel);
				pixels[i][j][1] = getRed(pixel);
				pixels[i][j][2] = getGreen(pixel);
				pixels[i][j][3] = getBlue(pixel);
			}
		}
	}
	public void toGrayMethd() {
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				int grayValue = (int) (pixels[i][j][1] * 0.30 + pixels[i][j][2] * 0.59 + pixels[i][j][3] * 0.11 + 0.5);
				pixels[i][j][1] = grayValue;
				pixels[i][j][2] = grayValue;
				pixels[i][j][3] = grayValue;
				histogramArray[grayValue]++;
			}
		}
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				currentImage.setRGB(i, j, getGRB(pixels[i][j][0], pixels[i][j][1], pixels[i][j][2], pixels[i][j][3]));
			}
		}
	}
	public void equilibrationMethd() {
		int totalPixels = width * height;
		double[] densityArray = new double[256];
		double[] distributionArray = new double[256];
		for (int i = 0; i < 256; i++) {
			densityArray[i] = histogramArray[i] * 1.0 / totalPixels;
			distributionArray[i] = 0;
		}
		for (int i = 0; i < 256; i++) {
			for (int j = 0; j <= i; j++) {
				distributionArray[i] += densityArray[j];
			}
		}
		for (int i = 0; i < 256; i++) {
			distributionArray[i] = distributionArray[i] * 255;
		}
		for (int i = 0; i < 256; i++) {
			histogramArray[i] = 0;
		}
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				int grayValue = (int) (distributionArray[pixels[i][j][2]] + 0.5);
				pixels[i][j][1] = grayValue;
				pixels[i][j][2] = grayValue;
				pixels[i][j][3] = grayValue;
				histogramArray[grayValue]++;
			}
		}
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				currentImage.setRGB(i, j, getGRB(pixels[i][j][0], pixels[i][j][1], pixels[i][j][2], pixels[i][j][3]));
			}
		}
	}
	public BufferedImage borderMethd()
	{
		BufferedImage outImage = new BufferedImage(currentImage
				.getWidth(), currentImage.getHeight(), currentImage
				.getType());
		float[] elements = { 0.0f, -1.0f, 0.0f, -1.0f, 4.f, -1.0f, 0.0f, -1.0f,
				0.0f };
		Kernel kernel = new Kernel(3, 3, elements);// 设置卷积矩阵
		ConvolveOp cop = new ConvolveOp(kernel,// 设置卷积操作
				ConvolveOp.EDGE_ZERO_FILL, null);
		cop.filter(currentImage, outImage);
		currentImage = outImage;
		for (int i = 0; i < 256; i++) {
			histogramArray[i] = 0;
		}
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				int pixel = currentImage.getRGB(i, j);
				pixels[i][j][0] = getAlpha(pixel);
				pixels[i][j][1] = getRed(pixel);
				pixels[i][j][2] = getGreen(pixel);
				pixels[i][j][3] = getBlue(pixel);
				histogramArray[pixels[i][j][1]]++;
			}
		}
		return outImage;
	}
	public BufferedImage binarizationMethd() {
	
		int wfix = 140, hfix = 50;
		int sum=0;
		for (int i = 0; i < wfix; i++) {
			for (int j = 0; j < hfix; j++) {
				int pixel = currentImage.getRGB(i, j);
				pixels[i][j][0] = getAlpha(pixel);
				pixels[i][j][1] = getRed(pixel);
				pixels[i][j][2] = getGreen(pixel);
				pixels[i][j][3] = getBlue(pixel);
				sum+=pixels[i][j][2];
			}
		}
		int totalPixels = wfix*hfix;
		double[] densityArray = new double[256];
		for (int i = 0; i < 256; i++) {
			densityArray[i] = histogramArray[i] * 1.0 / totalPixels;
		}
		
		int bestT = 0;
		double bestDeviation = 0;
		for (int i = 0; i < 256; i++) {
			double w0 = 0;
			double w1 = 0;
			for (int j = 0; j <= i; j++) {
				w0 += densityArray[j];
			}
			for (int j = i + 1; j < 256; j++) {
				w1 += densityArray[j];
			}
			double u0 = 0;
			double u1 = 0;
			for (int j = 0; j <= i; j++) {
				u0 += j * densityArray[j];
			}
			for (int j = i + 1; j < 256; j++) {
				u1 += j * densityArray[j];
			}
			u0 = u0 / w0;
			u1 = u1 / w1;
			if (w0 * w1 * (u0 - u1) * (u0 - u1) > bestDeviation) {
				bestT = i;
				bestDeviation = w0 * w1 * (u0 - u1) * (u0 - u1);
			}
		}
		for (int i = 0; i < 256; i++) {
			histogramArray[i] = 0;
		}
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				if (pixels[i][j][2] >= bestT) {
					pixels[i][j][1] = 255;
					pixels[i][j][2] = 255;
					pixels[i][j][3] = 255;
					histogramArray[255]++;
				}
				else {
					pixels[i][j][1] = 0;
					pixels[i][j][2] = 0;
					pixels[i][j][3] = 0;
					histogramArray[0]++;
				}
			}
		}
		for (int i = 0; i < wfix; i++) {
			for (int j = 0; j < hfix; j++) {
				currentImage.setRGB(i, j, getGRB(pixels[i][j][0], pixels[i][j][1], pixels[i][j][2], pixels[i][j][3]));
			}
		}
		return currentImage;
	}
	private int getAlpha(int pixel) {
		return (pixel>>24)&0xff;
	}
	private int getRed(int pixel) {
		return (pixel>>16)&0xff;
	}
	private int getGreen(int pixel) {
		return (pixel>>8)&0xff;
	}
	private int getBlue(int pixel) {
		return pixel&0xff;
	}
	private int getGRB(int alpha, int red, int green, int blue) {
		return (alpha<<24 | red<<16 | green<<8 | blue);
	}
	public  BufferedImage imagLocationMethd(){
		
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				int pixel = currentImage.getRGB(i, j);
				pixels[i][j][0] = getAlpha(pixel);
				pixels[i][j][1] = getRed(pixel);
				pixels[i][j][2] = getGreen(pixel);
				pixels[i][j][3] = getBlue(pixel);
			}
		}
		
		long xwalk = 0;
		// x轴累加器
		long ywalk_left = 0;
		// y左边轴累加器
		long ywalk_right = 0;
		// y左边轴累加器

		int acclefttop = 0;// 左上角点累加器
		int accrighttop = 0;// 右上角点累加器
		int accleftbellow = 0;// 左下角上角点累加器
		int accrightbellow = 0;// 右下角点累加器
		int[] fixlefttop = new int[2000];// 左上角点数组
		int[] fixrighttop = new int[2000];// 右上角点数组
		int[] fixleftbellow = new int[2000];// 左下角点数组
		int[] fixrightbellow = new int[2000];// 右下角点数组
		
		for(int i=20;i<this.height-25;i++)
		{
			for(int j=1;j<this.width-70;j++)
			{
				for(int k=0;k<70;k++)
				{
					xwalk+=this.pixels[j+k][i][2];
				}
				//xwalk累加
				for(int m=0;m<20;m++)
				{
					ywalk_left+=this.pixels[j][i+m][2];
				}
				//y左边累加
				if(xwalk>3000&&ywalk_left>1300)
				{
					if(acclefttop+2<2000)
					{
						fixlefttop[acclefttop]=i*width+j;
						acclefttop++;
					}
				}
				//求满足的左上角的点
				for(int m=0;m<20;m++)
				{
					ywalk_right+=this.pixels[j+70][i+m][2];
				}
				//y右边累加
				if(xwalk>3000&&ywalk_right>1300)
				{
					if(accrighttop+2<2000)
					{
						fixrighttop[accrighttop]=i*width+j+70;
						accrighttop++;
					}
				}
				xwalk=0;
				ywalk_left=0;
				ywalk_right=0;
				
			}
		}
		xwalk=0;
		ywalk_left=0;
		ywalk_right=0;
		for(int i=50;i<this.height-1;i++)
		{
			for(int j=1;j<this.width-70-1;j++)
			{
				for(int k=0;k<70;k++)
				{
					xwalk+=this.pixels[j+k][i][2];
				}
				for(int m=0;m<20;m++)
				{
					ywalk_left+=this.pixels[j][i-m][2];					
				}
				if(xwalk>3000&&ywalk_left>1300)
				{
					if(accleftbellow+2<2000)
					{
						fixleftbellow[accleftbellow]=i*width+j;
						accleftbellow++;
					}
				}
				for(int m=0;m<20;m++)
				{
					ywalk_right+=this.pixels[j+70][i-m][2];
				}
				if(xwalk>3000&&ywalk_right>1300)
				{
					if(accrightbellow+2<2000)
					{
						fixrightbellow[accrightbellow]=i*width+j+700;
						accrightbellow++;
					}
						
				}
				xwalk=0;
				ywalk_left=0;
				ywalk_right=0;
			}
		}
		
		int x = 0, y = 0; // 初始化定位坐标
		double rate = 0;
		// 设置长宽比变量
		int wt, ht;// 设置长宽变量
		boolean fin = false, righttop = false, leftbellow = false;
		// 设置状态开关
		int temp = 0;// 右下角点数组长度
		for (; fixrightbellow[temp] != 0; temp++)
			;
		// 求右下角点数组长度
		for (int i = 0; fixlefttop[i] != 0; i++) {
			for (int j = temp - 1; j >= 0; j--) {

				ht = (fixrightbellow[j] - fixlefttop[i])
						/ width;// 求矩形高
				wt = fixrightbellow[j] - fixlefttop[i] - ht
						* width;// 求矩形宽
				rate = (double) wt / (double) ht;// 求长宽比
				if (wt > 70 && ht > 10 && rate < 3.5
						&& rate > 2.5 && wt < 150 && ht < 60) {// 长宽比以及长宽条件
					for (int m = 0; fixrighttop[m] != 0; m++) {
						if (fixlefttop[i] == fixrighttop[m]
								- wt)
							righttop = true;
					}
					// 找到右上角点
					for (int m = 0; fixleftbellow[m] != 0; m++) {
						if (fixrightbellow[i] == fixleftbellow[m]
								+ wt)
							leftbellow = true;
					}
					// 找到左下角点
					if (righttop == true || leftbellow == true) {
						// 找到一个即可，因为太严格，会因为图象质量找不到符合的
						fin = true;

						y = fixlefttop[i] / width;
						x = fixlefttop[i] - y * width;
						break;
					}
				}
				if (fin == true)
					break;
			}
		}
		System.out.println("x" + x + "   y" + y);
		
		if (x == 0 && y == 0) {// 如果没有找到
			int widthed = this.width;
			long max = 0;
			long maxi = 0;
			for (int a = 0; a < this.width*this.height - widthed * 29
					- 85; a++) {
				long value = 0;
				if (a - (a / widthed) * widthed > widthed - 90)
					continue;// 左边接近右边界
				/*----------------通过两次循环求得矩形框中灰度值最大得矩形--------------*/
				int xx=0,yy=0,xx0=0,yy0=0;
				for (int i = 0; i < 4; i++) {
					for (int j = 0; j < 85; j++) {
						xx=(int)(a + i * widthed + j)/widthed;
						yy=(int)(a + i * widthed + j)-xx*widthed;
						xx0=(int)(a + i * widthed + j + 25 * widthed - 1)/widthed;
						yy0=(int)(a + i * widthed + j + 25 * widthed - 1)-xx0*widthed;
						value += this.pixels[yy][xx][2];
						value += this.pixels[yy0][xx0][2];
					}
				}
				for (int m = 0; m < 22; m++) {
					for (int n = 0; n < 4; n++) {
						xx=(int)(a + 4 * widthed + m* widthed + n)/widthed;
						yy=(int)(a + 4 * widthed + m* widthed + n)-xx*widthed;
						xx0=(int)(a + 4 * widthed + m
								* widthed + n + 76)/widthed;
						yy0=(int)(a + 4 * widthed + m
								* widthed + n + 76)-xx0*widthed;
						value += this.pixels[yy][xx][2];
						value += this.pixels[yy0][xx0][2];
					}

				}
				if (value >= max) {
					max = value;
					maxi = a;
				}

			}
			x = (int) (maxi / widthed);
			y = (int) (maxi - widthed * x);
			System.out.println("x" + x + "   y" + y);

		}
		int wfix = 140, hfix = 50;// 设定初时截图边界
		int cutx = 0, cuty = 0;
		if (x + wfix - 15 + 1 > this.width)
			wfix = this.width - x + 40 - 1;// 宽度越界检测
		if (y + hfix - 15 + 1 > this.height)
			hfix = this.height - y + 15 - 1;// 高度越界检测
		if (x - 40 > 0)
			cutx = x - 40;// 防止截图越界左边
		if (y - 15 > 0)
			cuty = y - 15;// 防止截图越界上边

		currentImage = originalImage.getSubimage(cutx+5, cuty-5,
				wfix, hfix);
		return currentImage;

		
	}
	
	

}
