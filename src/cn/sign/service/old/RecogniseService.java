/**
 * Project             :FaceRecognise project
 * Comments            :人脸识别服务类，是人脸识别的基础
 * Version             :1.0
 * Modification history: number | time |   why  |  who
 * 1 | 2013-4-20 | 创建 | jxm 
 */
package cn.sign.service.old;

import static com.googlecode.javacv.cpp.opencv_core.*;
import static com.googlecode.javacv.cpp.opencv_highgui.*;
import static com.googlecode.javacv.cpp.opencv_imgproc.*;
import static com.googlecode.javacv.cpp.opencv_contrib.*;

import cn.sign.face.PreprocessFace;
import cn.sign.face.Recognition;
import cn.sign.model.old.UserBean;

import com.googlecode.javacv.CanvasFrame;
import com.googlecode.javacv.cpp.opencv_objdetect.*;

import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 
 * @author acer
 *
 */
public class RecogniseService extends Thread {
	private final double CHANGE_IN_IMAGE_FOR_COLLECTION = 0.6;// 图像改变：收集人脸时，视为新人脸的阈值
	private final double CHANGE_IN_SECONDS_FOR_COLLECTION = 3.0;// 时间改变：收集下一张人脸的时间间隔
	private final int FACE_WIDTH = 70; // 人脸宽度
	// private final int FACE_HEIGHT = 90;
	private final int DESIRE_IMAGE_WIDTH = 320; // 图像期望宽度
	private final boolean LRS = true;
	private final int BORDER = 8; // 变宽
	private int FACENUM = 6; // 人脸采集的最大数量
	private int recogniseTime = 0; // 人脸识别次数

	private CanvasFrame canvasFrame; // 画布

	private static Recognition recognition;
	private PreprocessFace preprocessFace;

	private CascadeClassifier faceCascade;
	private CascadeClassifier eyeCascade1;
	private CascadeClassifier eyeCascade2;
	private CvCapture capture = null;// 捕获器（摄像头）

	private int faceCounter = 0; // 用于记录当前采集了几张人脸
	private Vector<CvMat> preprocessedFaces = new Vector<CvMat>(); // 存储当前采集的所有人脸，采集完时共6张
	public static boolean gotFace = false;//是否成功采集到人脸 
	boolean gotFaceAndEyes = false; // 是否检测到人脸和眼睛
	private boolean flag = true; // 标记是否需要进行识别服务
	private boolean recogniseFlag = false; // recogniseFlag标记人脸数据库data.xml是否加载成功

	private MODES m_mode = MODES.MODE_DETECTION;// 见265行if (m != null) {}
												// 在无任何任务缺启动线程run时，默认情况下m_mode就是检测模式

	/**
	 * 
	 * @author acer
	 *         RecogniseService的模式：人脸检测（未完成）、人脸采集、人脸识别、人脸采集等待、人脸采集倒计时、人脸收集结束
	 *
	 */
	public enum MODES {
		MODE_DETECTION, MODE_COLLECT_FACES, MODE_RECOGNITION, MODE_WAIT, MODE_COUNTDOWN, MODE_ENTRY_OVER, MODE_MANUAL_COLLECT_FACES
	};

	private BlockingQueue result; // 标记任务执行结果的情况，true代表人脸采集成功、人脸置信度

	private static RecogniseService recogniseService;

	private int cameraNumber = 0;// 表示获哪个摄像头设备，一般默认使用0号（即本机自带摄像头）
	private int width = 640;
	private int height = 480;
	private String windowName = "人脸识别";
	private volatile boolean run = true;
	private int countDownNum = 3;
	private CvMat displayedFrame; // 摄像头实时采集的画面
	private Timer timer; // 用于倒计时的计时器
	private BlockingQueue<MODES> modeQueue = new LinkedBlockingQueue<MODES>();// 保存当前队执行过的所有服务模式，使用offer()方法添加任务。poll导出任务

	/**
	 * 参数为null时，以该参数创建一个RecogniseService对象，初始化时将该参数传给result。
	 * 如果参数不为null时，直接将该参数传给result
	 * 
	 * @param recogniseResult
	 * @return
	 */
	public static RecogniseService getInstance(BlockingQueue recogniseResult) {
		if (recogniseService == null) {
			recogniseService = new RecogniseService(recogniseResult);// 创建用户时报错
			recogniseService.start();// 启动识别服务线程，执行run方法
		} else {
			recogniseService.setRecogniseResult(recogniseResult);
		}
		return recogniseService;
	}

	/**
	 * 获得当前识别服务recogniseService的实例对象
	 * 
	 * @return
	 */
	public static RecogniseService getInstance() {
		return recogniseService;
	}

	// 构造器
	private RecogniseService(BlockingQueue recogniseResult) {
		this.result = recogniseResult;

		Properties pp = new Properties();
		InputStream fis = null;
		float threshold = 0.7f;
		try {
			fis = new FileInputStream("dat/oracle.properties");
			pp.load(fis); // 加载dat文件夹下的oracle.properties数据文件，读取相关数据
			String thresholdStr = pp.getProperty("threshold"); // threshold=0.7
			threshold = Float.parseFloat(thresholdStr);
			String camIdStr = pp.getProperty("camId"); // camId=0
			cameraNumber = Integer.parseInt(camIdStr);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				fis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		this.recognition = new Recognition(createFisherFaceRecognizer(), threshold);

		this.preprocessFace = new PreprocessFace();

		initDetectors();
		initWebcam(cameraNumber, width, height);
		File file = new File("dat/data.xml");
		if (file.exists()) {
			this.recognition.load("dat/data.xml");
			recogniseFlag = true;
		} else {
			System.out.println("data.xml不存在!");
		}
	}

	public void initDetectors() {

		try {
			faceCascade = new CascadeClassifier();
			faceCascade.load("dat/haarcascade_frontalface_alt.xml");
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			eyeCascade1 = new CascadeClassifier();
			eyeCascade1.load("dat/haarcascade_eye.xml");
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			eyeCascade2 = new CascadeClassifier();
			eyeCascade2.load("dat/haarcascade_eye_tree_eyeglasses.xml");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public CvCapture initWebcam(int cameraNumber, int width, int height) {
		try {
			capture = cvCreateCameraCapture(cameraNumber);
			cvSetCaptureProperty(capture, CV_CAP_PROP_FRAME_WIDTH, width);
			cvSetCaptureProperty(capture, CV_CAP_PROP_FRAME_HEIGHT, height);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return capture;
	}

	public void initRecogniseWindow() {
		this.canvasFrame = new CanvasFrame(windowName);// 创建标题为windowName的画布框（窗口）
		this.canvasFrame.setSize(width, height);
		this.canvasFrame.setIconImage(Toolkit.getDefaultToolkit().getImage("image/logo.jpg"));
		this.canvasFrame.setLocation(Toolkit.getDefaultToolkit().getScreenSize().width - 740,
				Toolkit.getDefaultToolkit().getScreenSize().height / 2 - 240);
		this.canvasFrame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) { // 窗口关闭时，将识别服务设置为等待模式
				if (RecogniseService.getInstance() != null
						&& RecogniseService.getInstance().getState() != State.WAITING) {
					recogniseService.waitMode();
				}
			}
		});
	}

	@Override
	public void run() {
		initRecogniseWindow();
		CvMat old_prepreprocessedFace = null;
		double old_time = 0;
		while (flag) { // flag标记是否需要进行识别服务操作
			CvMat cameraFrame;
			cameraFrame = (CvMat) cvQueryFrame(capture).asCvMat();// 通过捕获器capture获得当前帧的图像
			if (cameraFrame.empty()) {
				System.out.println("ERROR: Couldn't grab the next camera frame.\n");
				return;
			}

			displayedFrame = cvCreateMat(cameraFrame.rows(), cameraFrame.cols(), cameraFrame.type());
			cvCopy(cameraFrame, displayedFrame);

			CvRect faceRect = new CvRect();
			CvPoint leftEye = new CvPoint(), rightEye = new CvPoint();

			// 将对实时采集的画面进行人脸检测，并转换为人脸的灰度图像
			CvMat preprocessedFace = preprocessFace.getPreprocessedFace(displayedFrame, FACE_WIDTH, DESIRE_IMAGE_WIDTH,
					faceCascade, eyeCascade1, eyeCascade2, LRS, faceRect, leftEye, rightEye);

			gotFaceAndEyes = false;
			if (preprocessedFace != null)
				gotFaceAndEyes = true;

			// 检测到人脸，在人脸区域画出边界
			if (faceRect.width() > 0) {
				cvRectangleR(displayedFrame, faceRect, CV_RGB(255, 255, 0), 2, CV_AA, 0);

				// 检测出人眼，以人眼中心为圆点画圆圈
				CvScalar eyeColor = CV_RGB(0, 255, 255);
				if (leftEye.x() >= 0) { // Check if the eye was detected
					cvCircle(displayedFrame, cvPoint(faceRect.x() + leftEye.x(), faceRect.y() + leftEye.y()), 6,
							eyeColor, 1, CV_AA, 0);
				}
				if (rightEye.x() >= 0) { // Check if the eye was detected
					cvCircle(displayedFrame, cvPoint(faceRect.x() + rightEye.x(), faceRect.y() + rightEye.y()), 6,
							eyeColor, 1, CV_AA, 0);
				}
			}

			MODES m = modeQueue.poll(); // 得到当前的识别服务队列的最近一个服务模式
			if (m != null) {
				m_mode = m;
			}

			if (m_mode == MODES.MODE_DETECTION) { // 人脸检测，实际上什么都不做，所有模式执行后都会返回这个模式

			} else if (m_mode == MODES.MODE_MANUAL_COLLECT_FACES) { // 手动人脸采集

				if (faceCounter < FACENUM) {
					if (gotFaceAndEyes) { // 是否检测到人脸和眼睛
						if (preprocessedFaces.size() < FACENUM) { // 判断当前采集人脸的数量，最多为6张
							preprocessedFaces.add(preprocessedFace);
						}
						faceCounter += 1;
						gotFace = true;
						// 用形象可视的形式通知人脸采集成功（很短的时间内，在图像的一个很大的白色矩形上显示）
						CvMat displayedFaceRegion = new CvMat();
						cvGetSubArr(displayedFrame, displayedFaceRegion, faceRect);
						cvAddS(displayedFaceRegion, CV_RGB(90, 90, 90), displayedFaceRegion, null);
					}else{
						gotFace = false;
					}
					System.out.println("成功采集" + faceCounter + "张人脸");
					detectMode();
				} else {// 人脸采集达到6张，faceCounter清零，并且显示“OK”
					try {
						result.put(true);
					} catch (Exception e) {
						e.printStackTrace();
					}
					faceCounter = 0;
					entryPhotoOver();
				}
			} else if (m_mode == MODES.MODE_COLLECT_FACES) { // 人脸采集

				if (faceCounter < FACENUM) {
					if (gotFaceAndEyes) { // 是否检测到人脸和眼睛

						double imageDiff = 10000000000.0; // 该值为相似度的极大值(越大越不相似)
						if (old_prepreprocessedFace != null) { // 得到前后两张人脸的相似度，根据阈值判断是否收集改人脸
							imageDiff = recognition.getSimilarity(preprocessedFace, old_prepreprocessedFace);
						}

						double current_time = (double) cvGetTickCount();// 得到前后两张人脸采集的时间间隔
						double timeDiff_seconds = (current_time - old_time) / cvGetTickFrequency();

						if ((imageDiff > CHANGE_IN_IMAGE_FOR_COLLECTION)// 判断新人脸和时间间隔
								&& (timeDiff_seconds > CHANGE_IN_SECONDS_FOR_COLLECTION)) {

							System.out.println("getSimilarity人脸采集时的前后图像对比>0.6：" + imageDiff);

							if (preprocessedFaces.size() < FACENUM) { // 判断当前采集人脸的数量，最多为6张
								preprocessedFaces.add(preprocessedFace);
							}

							faceCounter += 1;

							// 用形象可视的形式通知人脸采集成功（很短的时间内，在图像的一个很大的白色矩形上显示）
							CvMat displayedFaceRegion = new CvMat();
							cvGetSubArr(displayedFrame, displayedFaceRegion, faceRect);
							cvAddS(displayedFaceRegion, CV_RGB(90, 90, 90), displayedFaceRegion, null);

							if (old_prepreprocessedFace == null) {
								old_prepreprocessedFace = cvCreateMat(preprocessedFace.rows(), preprocessedFace.cols(),
										preprocessedFace.type());
							}
							cvCopy(preprocessedFace, old_prepreprocessedFace);// 将当前采集的照片保存下来，作为“旧”照片，同样用于后续采集的对比

							old_time = current_time;// 当前时间也保存为“旧”时间，同上
						}
					}
				} else {// 人脸采集达到6张，faceCounter清零，并且显示“OK”
					try {
						result.put(true);
					} catch (Exception e) {
						e.printStackTrace();
					}
					faceCounter = 0;
					// detectMode();
					entryPhotoOver();
				}
			} else if (m_mode == MODES.MODE_COUNTDOWN) { // 倒计时模式
				// 在图片的中心点画倒计时的数字
				CvPoint point = new CvPoint(displayedFrame.cols() / 2, displayedFrame.rows() / 2);
				drawString(displayedFrame, String.valueOf(getCountDownNum()), point, CV_RGB(0, 85, 230), 4, 3, 3);

			} else if (getM_mode() == MODES.MODE_RECOGNITION) { // 人脸识别模式
				if (recogniseFlag) {
					if (gotFaceAndEyes) {
						int r = recognition.recognise(preprocessedFace);// 根据检测到的人脸图像，调用识别方法，返回人物的编码ID
						double similarity = recognition.getSimilarity();// 获得相似度
						if (recognition.getUnknowThreshold() <= similarity
								&& similarity <= recognition.getUnknowThreshold() + 0.1f && recogniseTime < 3) {
							recogniseTime += 1;
						} else {// 如果三次检测都为陌生人。直接将保存置信度，供其它函数调用？
							recogniseTime = 0;
							try {
								result.put(r);
							} catch (Exception e) {
								e.printStackTrace();
							}
							detectMode();
						}

					} // 如果没有检测到人脸则什么都不干，
				} else {
					try {
						result.put(-2);
					} catch (Exception e) {
						e.printStackTrace();
					}
					detectMode();
				}

			} else if (m_mode == MODES.MODE_ENTRY_OVER) { // 人脸采集完成
				// 在图片中心点画“OK”
				CvPoint point = new CvPoint(displayedFrame.cols() / 2, displayedFrame.rows() / 2);
				drawString(displayedFrame, "OK", point, CV_RGB(0, 85, 230), 4, 3, 3);

			} else if (getM_mode() == MODES.MODE_WAIT) { // 等待
				canvasFrame.dispose(); // 重新布置窗口
				WaitSyn(); // 等待（同步方法）
			}

			int cx = (displayedFrame.cols() - FACE_WIDTH) / 2;
			if (preprocessedFace != null) {
				CvMat srcBGR = cvCreateMat(preprocessedFace.rows(), preprocessedFace.cols(), CV_8UC3);
				cvCvtColor(preprocessedFace, srcBGR, CV_GRAY2BGR); // 人脸灰度图像转换为彩色图像

				CvRect dstRC = cvRect(cx, BORDER, FACE_WIDTH, FACE_WIDTH);

				CvMat dstROI = cvCreateMat(srcBGR.rows(), srcBGR.cols(), srcBGR.type());
				cvGetSubArr(displayedFrame, dstROI, dstRC);

				cvCopy(srcBGR, dstROI);
			}

			canvasFrame.showImage(displayedFrame.asIplImage());// 在窗口上实时显示摄像头拍摄的图片

			int keypress = cvWaitKey(25);
			if (keypress == 27) {
				break;
			}

			if (displayedFrame != null) {
				cvReleaseMat(displayedFrame);
			}
		}
	}

	public void releaseCapture() {
		// 最后必须得释放capture，否则程序无法关闭
		if (capture != null) {
			cvReleaseCapture(capture);
		}
	}

	// 用于绘画倒计时和“OK”
	public CvRect drawString(CvMat img, String text, CvPoint coord, CvScalar color, float fontScale, int thickness,
			int fontFace) {

		int[] baseline = new int[] { 0 };
		CvFont font = new CvFont(fontFace, fontScale, thickness);
		CvSize textSize = new CvSize();
		cvGetTextSize(text, font, textSize, baseline);
		baseline[0] += thickness;

		if (coord.y() >= 0) {
			coord.y(coord.y() + textSize.height() / 4);
		} else {
			coord.y(coord.y() + img.rows() - baseline[0] + 1);
		}

		if (coord.x() > 0) {
			coord.x(coord.x() - textSize.width() / 2 + 1);
		}

		CvRect boundingRect = cvRect(coord.x(), coord.y() - textSize.height(), textSize.width(),
				baseline[0] + textSize.height());
		cvPutText(img, text, coord, font, color);

		return boundingRect;
	}

	// 采集训练图像时，在窗口显示倒计时:手动采集模式
	public void entryPhotoManual() {
		countDownMOde();
		timer = new Timer();
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				// countDownNum -= 1;
				setCountDownNum(getCountDownNum() - 1);

				if (countDownNum <= 0) {
					timer.cancel();
					// countDownNum = 3;
					setCountDownNum(3);
					// m_mode = MODES.MODE_COLLECT_FACES;
					// setM_mode(MODES.MODE_COLLECT_FACES);
					detectMode();
				}

			}
		}, 1000, 1000);

	}

	// 采集训练图像时，在窗口显示倒计时
	public void entryPhoto() {
		countDownMOde();
		timer = new Timer();
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				// countDownNum -= 1;
				setCountDownNum(getCountDownNum() - 1);

				if (countDownNum <= 0) {
					timer.cancel();
					// countDownNum = 3;
					setCountDownNum(3);
					// m_mode = MODES.MODE_COLLECT_FACES;
					// setM_mode(MODES.MODE_COLLECT_FACES);
					collectMode();
				}

			}
		}, 1000, 1000);

	}

	// 倒计时后，显示完成的时间线程，显示“OK”
	public void entryPhotoOver() {

		enrtyOverMode();
		timer = new Timer();
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				timer.cancel();
				detectMode();
			}
		}, 1000, 1000);

	}

	public synchronized void WaitSyn() {
		try {

			this.wait();

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	// 重建图像采集的窗口：（在本类中没有用到）
	public void Resume() {

		synchronized (this) {
			initRecogniseWindow();
			detectMode();
			this.notify();
		}
	}
	
	
	public void train() {
		new TrainThread(recognition, new UserDBService()).start();
	}

	public void saveUser(UserBean user) {
		SaveThread save = new SaveThread(preprocessedFaces, user, new UserDBService());
		save.start();
		try {
			save.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void updateUser(UserBean user) {
		UpdateThread update = new UpdateThread(preprocessedFaces, user, new UserDBService());
		update.start();
		try {
			update.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/** SaveUser、train、updateUser的线程类 **/
	// SaveUser()方法的线程类
	class SaveThread extends Thread {
		private Vector<CvMat> faces;
		private UserBean user;
		private UserDBService db;

		public SaveThread(Vector<CvMat> faces, UserBean user, UserDBService db) {
			this.faces = faces;
			this.user = user;
			this.db = db;
		}

		@Override
		public void run() {
			db.insertUser(user);//
			if (faces.size() > 0 && "是".equals(user.getHavePhoto())) {
				String picsPath = user.getPicsPath();
				String id = user.getId();
				for (int i = 0; i < faces.size(); i++) {
					cvSaveImage(picsPath + id + "_" + i + ".bmp", faces.get(i));
				}
				faces.clear();
			}

		}

	}

	// train()方法的线程类
	class TrainThread extends Thread {
		private Recognition recognition;
		private UserDBService db;

		public TrainThread(Recognition recognition, UserDBService db) {
			this.recognition = recognition;
			this.db = db;
		}

		@Override
		public void run() {
			ArrayList<UserBean> users = db.getAllHavePhotoUsers();///

			if (users.size() > 1) {
				MatVector Faces = new MatVector(users.size() * FACENUM);
				int[] lables = new int[users.size() * FACENUM];
				System.out.println("更新人脸时train的用户：");
				for (int i = 0; i < users.size(); i++) {
					UserBean user = users.get(i);
					
					System.out.println(user.getId());//
					
					String id = user.getId();
					String picsPath = user.getPicsPath();
					for (int j = 0; j < FACENUM; j++) {
						CvMat m = cvLoadImageM("pics/" + id + "_" + j + ".bmp", CV_8U);
						Faces.put(i * FACENUM + j, m);
						lables[i * FACENUM + j] = Integer.valueOf(id);
					}
				}
				recognition.train(Faces, lables);

				try {
					result.put(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {

				try {
					result.put(false);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}

	}

	// updateUser()方法的线程类：更新人脸数据库
	class UpdateThread extends Thread {
		private Vector<CvMat> faces;
		private UserBean user;
		private UserDBService db;

		public UpdateThread(Vector<CvMat> faces, UserBean user, UserDBService db) {
			this.faces = faces;
			this.user = user;
			this.db = db;
		}

		@Override
		public void run() {
			db.updateUser(user);

			if (faces.size() > 0 && "是".equals(user.getHavePhoto())) {
				String picsPath = user.getPicsPath();
				String id = user.getId();
				for (int i = 0; i < faces.size(); i++) {
					cvSaveImage(picsPath + id + "_" + i + ".bmp", faces.get(i));
				}
				faces.clear();
			}

		}

	}

	/** 设置识别服务的模式 **/
	// 人脸检测
	public void detectMode() {
		modeQueue.offer(MODES.MODE_DETECTION);
	}

	// 手动人脸采集
	public void manualCollectMode() {
		modeQueue.offer(MODES.MODE_MANUAL_COLLECT_FACES);
	}

	// 人脸采集
	public void collectMode() {
		modeQueue.offer(MODES.MODE_COLLECT_FACES);
	}

	// 人脸识别
	public void recognitionMode() {
		modeQueue.offer(MODES.MODE_RECOGNITION);// 将指定元素插入到此队列的尾部（将人脸识别模式插入modeQueue）
	}

	// 服务等待
	public void waitMode() {
		modeQueue.offer(MODES.MODE_WAIT);
	}

	// 倒计时
	public void countDownMOde() {
		modeQueue.offer(MODES.MODE_COUNTDOWN);
	}

	// 结束
	public void enrtyOverMode() {
		modeQueue.offer(MODES.MODE_ENTRY_OVER);
	}

	/** 以下是对成员变量的get/set操作 **/
	public MODES getM_mode() {
		return m_mode;
	}

	public void setM_mode(MODES m_mode) {
		this.m_mode = m_mode;
	}

	public BlockingQueue<Integer> getRecogniseResult() {
		return result;
	}

	public void setRecogniseResult(BlockingQueue<Integer> recogniseResult) {
		this.result = recogniseResult;
	}

	public int getCameraNumber() {
		return cameraNumber;
	}

	public void setCameraNumber(int cameraNumber) {
		this.cameraNumber = cameraNumber;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public String getWindowName() {
		return windowName;
	}

	public void setWindowName(String windowName) {
		this.windowName = windowName;
	}

	public boolean isRun() {
		return run;
	}

	public void setRun(boolean run) {
		this.run = run;
	}

	public synchronized int getCountDownNum() {
		return countDownNum;
	}

	public synchronized void setCountDownNum(int countDownNum) {
		this.countDownNum = countDownNum;
	}

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	public boolean isRecogniseFlag() {
		return recogniseFlag;
	}

	public static Recognition getRecognition() {
		return recognition;
	}
}
