package org.jeecg.modules.im.base.util.im;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * 群组头像生成工具
 */
public final class GroupAvatarUtil {
	public static String[] getXy(int size){
		String[] s = new String[size];
		int _x = 0;
		int _y = 0;
		if(size ==1){
			_x = _y = 6;
			s[0] = "6,6";
		}
		if(size == 2){
			_x =_y = 4;
			s[0] = "4,"+(132/2-60/2);
			s[1] = 60+2*_x+","+ (132/2-60/2);
		}
		if(size == 3){
			_x=_y =4;
			s[0] = (132/2-60/2)+","+_y;
			s[1] = _x+","+(60+2*_y);
			s[2] = (60+2*_y)+","+(60+2*_y);
		}
		if(size ==4){
			_x=_y =4;
			s[0] = _x+","+_y;
			s[1] = (_x*2 + 60)+","+_y;
			s[2] = _x+","+(60+2*_y);
			s[3] = (60+2*_y)+","+(60+2*_y);
		}
		if(size == 5){
			_x = _y = 3;
			s[0] = (132-40*2-_x)/2+","+(132-40*2-_y)/2;
			s[1] = ((132-40*2-_x)/2+40+_x)+","+(132-40*2-_y)/2;
			s[2] = _x+","+((132-40*2-_x)/2+40+_y);
			s[3] = (_x*2+40)+","+((132-40*2-_x)/2+40+_y);
			s[4] = (_x*3+40*2)+","+((132-40*2-_x)/2+40+_y);
		}
		if(size == 6){
			_x = _y = 3;
			s[0] = _x+","+((132-40*2-_x)/2);
			s[1] = (_x*2+40)+","+((132-40*2-_x)/2);
			s[2] = (_x*3+40*2)+","+((132-40*2-_x)/2);
			s[3] = _x+","+((132-40*2-_x)/2+40+_y);
			s[4] = (_x*2+40)+","+((132-40*2-_x)/2+40+_y);
			s[5] = (_x*3+40*2)+","+((132-40*2-_x)/2+40+_y);
		}
		if(size == 7){
			_x=_y =3;
			s[0] = (132-40)/2+","+_y;
			s[1] = _x+","+(_y*2+40);
			s[2] = (_x*2+40)+","+(_y*2+40);
			s[3] = (_x*3+40*2)+","+(_y*2+40);
			s[4] = _x+","+(_y*3+40*2);
			s[5] = (_x*2+40)+","+(_y*3+40*2);
			s[6] = (_x*3+40*2)+","+(_y*3+40*2);
		}
		if(size == 8){
			_x=_y =3;
			s[0] = (132-80-_x)/2+","+_y;
			s[1] = ((132-80-_x)/2+_x+40)+","+_y;
			s[2] = _x+","+(_y*2+40);
			s[3] = (_x*2+40)+","+(_y*2+40);
			s[4] = (_x*3+40*2)+","+(_y*2+40);
			s[5] = _x+","+(_y*3+40*2);
			s[6] = (_x*2+40)+","+(_y*3+40*2);
			s[7] = (_x*3+40*2)+","+(_y*3+40*2); 
		}
		if(size == 9){
			_x=_y = 3;
			s[0]=_x+","+_y;
			s[1] = _x*2+40+","+_y;
			s[2] = _x*3+40*2 +","+_y;
			s[3] = _x+","+(_y*2+40);
			s[4] = (_x*2+40)+","+(_y*2+40);
			s[5] = (_x*3+40*2)+","+(_y*2+40);
			s[6] = _x+","+(_y*3+40*2);
			s[7] = (_x*2+40)+","+(_y*3+40*2);
			s[8] = (_x*3+40*2)+","+(_y*3+40*2); 
		}
		return s;
	}
	
	public static int getWidth(int size){
		int width = 0;
		if(size == 1){
			width = 120;
		}
		if(size>1 && size<=4){
			width = 60;
		}
		if(size>=5){
			width = 40;
		}
		return width;
	}
	
	public static void download(String urlString, String filename,String savePath) throws Exception {  
        // 构造URL  
        URL url = new URL(urlString);  
        // 打开连接  
        URLConnection con = url.openConnection();  
        //设置请求超时为5s  
        con.setConnectTimeout(5*1000);  
        // 输入流  
        InputStream is = con.getInputStream();  
      
        // 1K的数据缓冲  
        byte[] bs = new byte[1024];  
        // 读取到的数据长度  
        int len;  
        // 输出的文件流  
       File sf=new File(savePath);  
       if(!sf.exists()){  
           sf.mkdirs();  
       }  
       OutputStream os = new FileOutputStream(sf.getPath()+File.separator+filename);  
        // 开始读取  
        while ((len = is.read(bs)) != -1) {  
          os.write(bs, 0, len);  
        }  
        // 完毕，关闭所有链接  
        os.close();  
        is.close();  
    }   
	
	public static String zoom(String sourcePath,String targetPath,int width,int height) throws IOException{ 
        File imageFile = new File(sourcePath); 
        if(!imageFile.exists()){ 
            throw new IOException("Not found the images:"+sourcePath); 
        } 
        if(targetPath==null || targetPath.isEmpty()) targetPath = sourcePath; 
        String format = sourcePath.substring(sourcePath.lastIndexOf(".")+1,sourcePath.length()); 
        BufferedImage image = ImageIO.read(imageFile); 
        image = zoom(image,width,height); 
        ImageIO.write(image, format, new File(targetPath)); 
        return targetPath; 
    } 
	
	private static BufferedImage zoom(BufferedImage sourceImage , int width , int height){ 
        BufferedImage zoomImage = new BufferedImage(width, height, sourceImage.getType()); 
        Image image = sourceImage.getScaledInstance(width, height, Image.SCALE_SMOOTH); 
        Graphics gc = zoomImage.getGraphics(); 
        gc.setColor(Color.WHITE); 
        gc.drawImage( image , 0, 0, null); 
        return zoomImage; 
    }
	
	public static void createImage(List<String> files,String outPath) throws Exception{
		String[] imageSize = getXy(files.size());
		int width = getWidth(files.size());
		BufferedImage ImageNew = new BufferedImage(132,132,BufferedImage.TYPE_INT_RGB);
//设置背景为白色
		for(int m=0;m<132;m++){
			for(int n=0;n<132;n++){
				ImageNew.setRGB(m, n, 0xFFFFFF);
			}
		}
		for(int i=0;i<imageSize.length;i++){
			String size = imageSize[i];
			String[] sizeArr = size.split(",");
			int x = Integer.valueOf(sizeArr[0]);
			int y = Integer.valueOf(sizeArr[1]);
			String f = zoom(files.get(i),"E:\\test",width,width);
			File fileOne = new File(f);
		       BufferedImage ImageOne = ImageIO.read(fileOne);
 
		       //从图片中读取RGB
		       int[] ImageArrayOne = new int[width*width];
		       ImageArrayOne = ImageOne.getRGB(0,0,width,width,ImageArrayOne,0,width);
			ImageNew.setRGB(x,y,width,width,ImageArrayOne,0,width);//设置左半部分的RGB
		}
		File outFile = new File(outPath);
	    ImageIO.write(ImageNew, "png", outFile);//写图片
	}
	
	public static void main(String[] args) throws Exception{
    	List<String> imgList=new ArrayList<String>();
    	imgList.add("E:/qq/1.png");
    	imgList.add("E:/qq/2.png");
    	imgList.add("E:/qq/3.png");
    	imgList.add("E:/qq/4.png");
    	imgList.add("E:/qq/5.png");
    	imgList.add("E:/qq/6.png");
    	imgList.add("E:/qq/7.png");
    	imgList.add("E:/qq/8.png");
    	imgList.add("E:/qq/9.png");
		createImage(imgList,"E:/qq/g.png");
		System.out.println("生成完成");
	}
 
}