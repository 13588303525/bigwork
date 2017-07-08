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

	private String fileContentType;//�ļ���Mime������xxxContentType
	private String fileFileName; //�ļ�����ʽ�����ǣ�xxxFileName
	private File file;//�ļ�����Ϊxxx
	
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


	//�ϴ���Ƭ
	public String upload() throws Exception {
		//String savePath = ServletActionContext.getServletContext().getRealPath("/upload/"+this.fileFileName);
		
		//��ʽ1
		String savePath = "E:/�ļ������/�����ϣ��ŵ翪�Σ�/��׿/eclipse/eclipse/workspace/bigworkUI/upload/"+this.fileFileName;
		try{
			System.out.println(savePath);
			System.out.println(fileFileName);
			input = new FileInputStream(file);//���ļ���
			output = new FileOutputStream(savePath);//д�ļ���
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

	// RecogniseService����
	private RecogniseService recogniseService;
	// ���RecogniseService������
	private BlockingQueue<Integer> recogniseResult = new LinkedBlockingDeque<Integer>();
	
	public int recognizeFace() throws InterruptedException{
		RecogniseService.getInstance(recogniseResult).recognitionMode();
		// ȡ�����
		int r = recogniseResult.take(); // ��ȡ���Ƴ���recogniseResult���е�ͷ��
		if (r >= 0) {// IDΪ0������Ա
			System.out.println("ǩ���ɹ�!");
		}
		
		return -1;
	}
	
	
	
	
}
