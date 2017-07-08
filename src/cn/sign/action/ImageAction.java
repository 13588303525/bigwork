package cn.sign.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

import org.apache.commons.io.IOUtils;
import org.apache.struts2.ServletActionContext;

import org.springframework.stereotype.Controller;

import cn.sign.service.old.RecogniseService;

@Controller("imageAction")
public class ImageAction {
	private InputStream input;
	private OutputStream output;

	private String fileContentType;//文件的Mime必须是xxxContentType
	private String fileFileName; //文件名格式必须是：xxxFileName
	private File file;//文件对象为xxx
	
	public String getFileContentType() {return fileContentType;}
	public void setFileContentType(String fileContentType) {this.fileContentType = fileContentType;}
	public String getFileFileName() {return fileFileName;}
	public void setFileFileName(String fileFileName) {this.fileFileName = fileFileName;}
	public File getFile() {return file;}
	public void setFile(File file) {this.file = file;}
	
	//getter && setter
	public InputStream getInput() {return input;}
	public void setInput(InputStream input) {this.input = input;}
	public OutputStream getOutput() {return output;}
	public void setOutput(OutputStream output) {this.output = output;}


	//上传照片
	public String upload() throws Exception {
		//String savePath = ServletActionContext.getServletContext().getRealPath("/upload/"+this.fileFileName);
		
		//方式1
		String savePath = "E:/文件保存库/大三上（信电开课）/安卓/eclipse/eclipse/workspace/bigworkUI/upload/"+this.fileFileName;
		try{
			System.out.println(savePath);
			System.out.println(fileFileName);
			input = new FileInputStream(file);//读文件流
			output = new FileOutputStream(savePath);//写文件流
			IOUtils.copy(input, output);
			output.flush();
			input.close();
			output.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return "uploadSuccess";
	}
	
	
	public String read() {  
		try {
			input = new FileInputStream(new File("D:\\meinv.jpg"));  
		} catch (FileNotFoundException e) {  
			e.printStackTrace();
		}
		return "readSuccess";  
	}

	// RecogniseService服务
	private RecogniseService recogniseService;
	// 存放RecogniseService服务结果
	private BlockingQueue<Integer> recogniseResult = new LinkedBlockingDeque<Integer>();
	
	public int recognizeFace() throws InterruptedException{
		RecogniseService.getInstance(recogniseResult).recognitionMode();
		// 取出结果
		int r = recogniseResult.take(); // 获取并移除此recogniseResult队列的头部
		if (r >= 0) {// ID为0，管理员
			System.out.println("签到成功!");
		}
		
		return -1;
	}
	
	
	
	
}
