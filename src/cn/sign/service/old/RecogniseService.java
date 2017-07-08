/**
 * Project             :FaceRecognise project
 * Comments            :����ʶ������࣬������ʶ��Ļ���
 * Version             :1.0
 * Modification history: number | time |   why  |  who
 * 1 | 2013-4-20 | ���� | jxm 
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
	private final double CHANGE_IN_IMAGE_FOR_COLLECTION = 0.6;// ͼ��ı䣺�ռ�����ʱ����Ϊ����������ֵ
	private final double CHANGE_IN_SECONDS_FOR_COLLECTION = 3.0;// ʱ��ı䣺�ռ���һ��������ʱ����
	private final int FACE_WIDTH = 70; // �������
	// private final int FACE_HEIGHT = 90;
	private final int DESIRE_IMAGE_WIDTH = 320; // ͼ���������
	private final boolean LRS = true;
	private final int BORDER = 8; // ���
	private int FACENUM = 6; // �����ɼ����������
	private int recogniseTime = 0; // ����ʶ�����

	private CanvasFrame canvasFrame; // ����

	private static Recognition recognition;
	private PreprocessFace preprocessFace;

	private CascadeClassifier faceCascade;
	private CascadeClassifier eyeCascade1;
	private CascadeClassifier eyeCascade2;
	private CvCapture capture = null;// ������������ͷ��

	private int faceCounter = 0; // ���ڼ�¼��ǰ�ɼ��˼�������
	private Vector<CvMat> preprocessedFaces = new Vector<CvMat>(); // �洢��ǰ�ɼ��������������ɼ���ʱ��6��
	public static boolean gotFace = false;//�Ƿ�ɹ��ɼ������� 
	boolean gotFaceAndEyes = false; // �Ƿ��⵽�������۾�
	private boolean flag = true; // ����Ƿ���Ҫ����ʶ�����
	private boolean recogniseFlag = false; // recogniseFlag����������ݿ�data.xml�Ƿ���سɹ�

	private MODES m_mode = MODES.MODE_DETECTION;// ��265��if (m != null) {}
												// �����κ�����ȱ�����߳�runʱ��Ĭ�������m_mode���Ǽ��ģʽ

	/**
	 * 
	 * @author acer
	 *         RecogniseService��ģʽ��������⣨δ��ɣ��������ɼ�������ʶ�������ɼ��ȴ��������ɼ�����ʱ�������ռ�����
	 *
	 */
	public enum MODES {
		MODE_DETECTION, MODE_COLLECT_FACES, MODE_RECOGNITION, MODE_WAIT, MODE_COUNTDOWN, MODE_ENTRY_OVER, MODE_MANUAL_COLLECT_FACES
	};

	private BlockingQueue result; // �������ִ�н���������true���������ɼ��ɹ����������Ŷ�

	private static RecogniseService recogniseService;

	private int cameraNumber = 0;// ��ʾ���ĸ�����ͷ�豸��һ��Ĭ��ʹ��0�ţ��������Դ�����ͷ��
	private int width = 640;
	private int height = 480;
	private String windowName = "����ʶ��";
	private volatile boolean run = true;
	private int countDownNum = 3;
	private CvMat displayedFrame; // ����ͷʵʱ�ɼ��Ļ���
	private Timer timer; // ���ڵ���ʱ�ļ�ʱ��
	private BlockingQueue<MODES> modeQueue = new LinkedBlockingQueue<MODES>();// ���浱ǰ��ִ�й������з���ģʽ��ʹ��offer()�����������poll��������

	/**
	 * ����Ϊnullʱ���Ըò�������һ��RecogniseService���󣬳�ʼ��ʱ���ò�������result��
	 * ���������Ϊnullʱ��ֱ�ӽ��ò�������result
	 * 
	 * @param recogniseResult
	 * @return
	 */
	public static RecogniseService getInstance(BlockingQueue recogniseResult) {
		if (recogniseService == null) {
			recogniseService = new RecogniseService(recogniseResult);// �����û�ʱ����
			recogniseService.start();// ����ʶ������̣߳�ִ��run����
		} else {
			recogniseService.setRecogniseResult(recogniseResult);
		}
		return recogniseService;
	}

	/**
	 * ��õ�ǰʶ�����recogniseService��ʵ������
	 * 
	 * @return
	 */
	public static RecogniseService getInstance() {
		return recogniseService;
	}

	// ������
	private RecogniseService(BlockingQueue recogniseResult) {
		this.result = recogniseResult;

		Properties pp = new Properties();
		InputStream fis = null;
		float threshold = 0.7f;
		try {
			fis = new FileInputStream("dat/oracle.properties");
			pp.load(fis); // ����dat�ļ����µ�oracle.properties�����ļ�����ȡ�������
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
			System.out.println("data.xml������!");
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
		this.canvasFrame = new CanvasFrame(windowName);// ��������ΪwindowName�Ļ����򣨴��ڣ�
		this.canvasFrame.setSize(width, height);
		this.canvasFrame.setIconImage(Toolkit.getDefaultToolkit().getImage("image/logo.jpg"));
		this.canvasFrame.setLocation(Toolkit.getDefaultToolkit().getScreenSize().width - 740,
				Toolkit.getDefaultToolkit().getScreenSize().height / 2 - 240);
		this.canvasFrame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) { // ���ڹر�ʱ����ʶ���������Ϊ�ȴ�ģʽ
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
		while (flag) { // flag����Ƿ���Ҫ����ʶ��������
			CvMat cameraFrame;
			cameraFrame = (CvMat) cvQueryFrame(capture).asCvMat();// ͨ��������capture��õ�ǰ֡��ͼ��
			if (cameraFrame.empty()) {
				System.out.println("ERROR: Couldn't grab the next camera frame.\n");
				return;
			}

			displayedFrame = cvCreateMat(cameraFrame.rows(), cameraFrame.cols(), cameraFrame.type());
			cvCopy(cameraFrame, displayedFrame);

			CvRect faceRect = new CvRect();
			CvPoint leftEye = new CvPoint(), rightEye = new CvPoint();

			// ����ʵʱ�ɼ��Ļ������������⣬��ת��Ϊ�����ĻҶ�ͼ��
			CvMat preprocessedFace = preprocessFace.getPreprocessedFace(displayedFrame, FACE_WIDTH, DESIRE_IMAGE_WIDTH,
					faceCascade, eyeCascade1, eyeCascade2, LRS, faceRect, leftEye, rightEye);

			gotFaceAndEyes = false;
			if (preprocessedFace != null)
				gotFaceAndEyes = true;

			// ��⵽���������������򻭳��߽�
			if (faceRect.width() > 0) {
				cvRectangleR(displayedFrame, faceRect, CV_RGB(255, 255, 0), 2, CV_AA, 0);

				// �������ۣ�����������ΪԲ�㻭ԲȦ
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

			MODES m = modeQueue.poll(); // �õ���ǰ��ʶ�������е����һ������ģʽ
			if (m != null) {
				m_mode = m;
			}

			if (m_mode == MODES.MODE_DETECTION) { // ������⣬ʵ����ʲô������������ģʽִ�к󶼻᷵�����ģʽ

			} else if (m_mode == MODES.MODE_MANUAL_COLLECT_FACES) { // �ֶ������ɼ�

				if (faceCounter < FACENUM) {
					if (gotFaceAndEyes) { // �Ƿ��⵽�������۾�
						if (preprocessedFaces.size() < FACENUM) { // �жϵ�ǰ�ɼ����������������Ϊ6��
							preprocessedFaces.add(preprocessedFace);
						}
						faceCounter += 1;
						gotFace = true;
						// ��������ӵ���ʽ֪ͨ�����ɼ��ɹ����̵ܶ�ʱ���ڣ���ͼ���һ���ܴ�İ�ɫ��������ʾ��
						CvMat displayedFaceRegion = new CvMat();
						cvGetSubArr(displayedFrame, displayedFaceRegion, faceRect);
						cvAddS(displayedFaceRegion, CV_RGB(90, 90, 90), displayedFaceRegion, null);
					}else{
						gotFace = false;
					}
					System.out.println("�ɹ��ɼ�" + faceCounter + "������");
					detectMode();
				} else {// �����ɼ��ﵽ6�ţ�faceCounter���㣬������ʾ��OK��
					try {
						result.put(true);
					} catch (Exception e) {
						e.printStackTrace();
					}
					faceCounter = 0;
					entryPhotoOver();
				}
			} else if (m_mode == MODES.MODE_COLLECT_FACES) { // �����ɼ�

				if (faceCounter < FACENUM) {
					if (gotFaceAndEyes) { // �Ƿ��⵽�������۾�

						double imageDiff = 10000000000.0; // ��ֵΪ���ƶȵļ���ֵ(Խ��Խ������)
						if (old_prepreprocessedFace != null) { // �õ�ǰ���������������ƶȣ�������ֵ�ж��Ƿ��ռ�������
							imageDiff = recognition.getSimilarity(preprocessedFace, old_prepreprocessedFace);
						}

						double current_time = (double) cvGetTickCount();// �õ�ǰ�����������ɼ���ʱ����
						double timeDiff_seconds = (current_time - old_time) / cvGetTickFrequency();

						if ((imageDiff > CHANGE_IN_IMAGE_FOR_COLLECTION)// �ж���������ʱ����
								&& (timeDiff_seconds > CHANGE_IN_SECONDS_FOR_COLLECTION)) {

							System.out.println("getSimilarity�����ɼ�ʱ��ǰ��ͼ��Ա�>0.6��" + imageDiff);

							if (preprocessedFaces.size() < FACENUM) { // �жϵ�ǰ�ɼ����������������Ϊ6��
								preprocessedFaces.add(preprocessedFace);
							}

							faceCounter += 1;

							// ��������ӵ���ʽ֪ͨ�����ɼ��ɹ����̵ܶ�ʱ���ڣ���ͼ���һ���ܴ�İ�ɫ��������ʾ��
							CvMat displayedFaceRegion = new CvMat();
							cvGetSubArr(displayedFrame, displayedFaceRegion, faceRect);
							cvAddS(displayedFaceRegion, CV_RGB(90, 90, 90), displayedFaceRegion, null);

							if (old_prepreprocessedFace == null) {
								old_prepreprocessedFace = cvCreateMat(preprocessedFace.rows(), preprocessedFace.cols(),
										preprocessedFace.type());
							}
							cvCopy(preprocessedFace, old_prepreprocessedFace);// ����ǰ�ɼ�����Ƭ������������Ϊ���ɡ���Ƭ��ͬ�����ں����ɼ��ĶԱ�

							old_time = current_time;// ��ǰʱ��Ҳ����Ϊ���ɡ�ʱ�䣬ͬ��
						}
					}
				} else {// �����ɼ��ﵽ6�ţ�faceCounter���㣬������ʾ��OK��
					try {
						result.put(true);
					} catch (Exception e) {
						e.printStackTrace();
					}
					faceCounter = 0;
					// detectMode();
					entryPhotoOver();
				}
			} else if (m_mode == MODES.MODE_COUNTDOWN) { // ����ʱģʽ
				// ��ͼƬ�����ĵ㻭����ʱ������
				CvPoint point = new CvPoint(displayedFrame.cols() / 2, displayedFrame.rows() / 2);
				drawString(displayedFrame, String.valueOf(getCountDownNum()), point, CV_RGB(0, 85, 230), 4, 3, 3);

			} else if (getM_mode() == MODES.MODE_RECOGNITION) { // ����ʶ��ģʽ
				if (recogniseFlag) {
					if (gotFaceAndEyes) {
						int r = recognition.recognise(preprocessedFace);// ���ݼ�⵽������ͼ�񣬵���ʶ�𷽷�����������ı���ID
						double similarity = recognition.getSimilarity();// ������ƶ�
						if (recognition.getUnknowThreshold() <= similarity
								&& similarity <= recognition.getUnknowThreshold() + 0.1f && recogniseTime < 3) {
							recogniseTime += 1;
						} else {// ������μ�ⶼΪİ���ˡ�ֱ�ӽ��������Ŷȣ��������������ã�
							recogniseTime = 0;
							try {
								result.put(r);
							} catch (Exception e) {
								e.printStackTrace();
							}
							detectMode();
						}

					} // ���û�м�⵽������ʲô�����ɣ�
				} else {
					try {
						result.put(-2);
					} catch (Exception e) {
						e.printStackTrace();
					}
					detectMode();
				}

			} else if (m_mode == MODES.MODE_ENTRY_OVER) { // �����ɼ����
				// ��ͼƬ���ĵ㻭��OK��
				CvPoint point = new CvPoint(displayedFrame.cols() / 2, displayedFrame.rows() / 2);
				drawString(displayedFrame, "OK", point, CV_RGB(0, 85, 230), 4, 3, 3);

			} else if (getM_mode() == MODES.MODE_WAIT) { // �ȴ�
				canvasFrame.dispose(); // ���²��ô���
				WaitSyn(); // �ȴ���ͬ��������
			}

			int cx = (displayedFrame.cols() - FACE_WIDTH) / 2;
			if (preprocessedFace != null) {
				CvMat srcBGR = cvCreateMat(preprocessedFace.rows(), preprocessedFace.cols(), CV_8UC3);
				cvCvtColor(preprocessedFace, srcBGR, CV_GRAY2BGR); // �����Ҷ�ͼ��ת��Ϊ��ɫͼ��

				CvRect dstRC = cvRect(cx, BORDER, FACE_WIDTH, FACE_WIDTH);

				CvMat dstROI = cvCreateMat(srcBGR.rows(), srcBGR.cols(), srcBGR.type());
				cvGetSubArr(displayedFrame, dstROI, dstRC);

				cvCopy(srcBGR, dstROI);
			}

			canvasFrame.showImage(displayedFrame.asIplImage());// �ڴ�����ʵʱ��ʾ����ͷ�����ͼƬ

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
		// ��������ͷ�capture����������޷��ر�
		if (capture != null) {
			cvReleaseCapture(capture);
		}
	}

	// ���ڻ滭����ʱ�͡�OK��
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

	// �ɼ�ѵ��ͼ��ʱ���ڴ�����ʾ����ʱ:�ֶ��ɼ�ģʽ
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

	// �ɼ�ѵ��ͼ��ʱ���ڴ�����ʾ����ʱ
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

	// ����ʱ����ʾ��ɵ�ʱ���̣߳���ʾ��OK��
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

	// �ؽ�ͼ��ɼ��Ĵ��ڣ����ڱ�����û���õ���
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

	/** SaveUser��train��updateUser���߳��� **/
	// SaveUser()�������߳���
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
			if (faces.size() > 0 && "��".equals(user.getHavePhoto())) {
				String picsPath = user.getPicsPath();
				String id = user.getId();
				for (int i = 0; i < faces.size(); i++) {
					cvSaveImage(picsPath + id + "_" + i + ".bmp", faces.get(i));
				}
				faces.clear();
			}

		}

	}

	// train()�������߳���
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
				System.out.println("��������ʱtrain���û���");
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

	// updateUser()�������߳��ࣺ�����������ݿ�
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

			if (faces.size() > 0 && "��".equals(user.getHavePhoto())) {
				String picsPath = user.getPicsPath();
				String id = user.getId();
				for (int i = 0; i < faces.size(); i++) {
					cvSaveImage(picsPath + id + "_" + i + ".bmp", faces.get(i));
				}
				faces.clear();
			}

		}

	}

	/** ����ʶ������ģʽ **/
	// �������
	public void detectMode() {
		modeQueue.offer(MODES.MODE_DETECTION);
	}

	// �ֶ������ɼ�
	public void manualCollectMode() {
		modeQueue.offer(MODES.MODE_MANUAL_COLLECT_FACES);
	}

	// �����ɼ�
	public void collectMode() {
		modeQueue.offer(MODES.MODE_COLLECT_FACES);
	}

	// ����ʶ��
	public void recognitionMode() {
		modeQueue.offer(MODES.MODE_RECOGNITION);// ��ָ��Ԫ�ز��뵽�˶��е�β����������ʶ��ģʽ����modeQueue��
	}

	// ����ȴ�
	public void waitMode() {
		modeQueue.offer(MODES.MODE_WAIT);
	}

	// ����ʱ
	public void countDownMOde() {
		modeQueue.offer(MODES.MODE_COUNTDOWN);
	}

	// ����
	public void enrtyOverMode() {
		modeQueue.offer(MODES.MODE_ENTRY_OVER);
	}

	/** �����ǶԳ�Ա������get/set���� **/
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
