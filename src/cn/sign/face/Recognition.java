/**
 * Project             :FaceRecognise project
 * Comments            :����ʶ��
 * Version             :1.0
 * Modification history: number | time |   why  |  who
 * 1 | 2013-3-20 | ���� | jxm 
 * 2 | 2013-4-26 |�Ż����룬����ע���� |jxm 
 */
package cn.sign.face;

import static com.googlecode.javacv.cpp.opencv_contrib.*;
import static com.googlecode.javacv.cpp.opencv_core.*;

import java.io.File;

public class Recognition implements Cloneable {
	// ����ʶ����
	private FaceRecognizer recognizer = null;

	// �ж�Ϊİ���˵���ֵ
	private float unknowThreshold = 0.6f;//���ƶȴ���0.6Ϊİ���ˣ������ع�Ч��Խ����ֵ������Եļ�С���Ӵ�Ҫ��
	private double similarity;

	public Recognition(FaceRecognizer recognizer,float threshold) {
		this.recognizer = recognizer;
		this.unknowThreshold = threshold;
	}

	public void train(MatVector preprocessedFaces, int[] faceLabels) {
		if (recognizer == null) {
			System.out.println("ERROR: The FaceRecognizer algorithm  is not available in your version of OpenCV. Please update to OpenCV v2.4.1 or newer.\n");
			return;
		}

		recognizer.train(preprocessedFaces, faceLabels);
		File file = new File("dat/data.xml");
		if(file.exists()){
			file.delete();
		}
		save("dat/data.xml");

	}
	public void trainWithoutSave(MatVector preprocessedFaces, int[] faceLabels) {
		if (recognizer == null) {
			System.out.println("ERROR: The FaceRecognizer algorithm  is not available in your version of OpenCV. Please update to OpenCV v2.4.1 or newer.\n");
			return;
		}

		recognizer.train(preprocessedFaces, faceLabels);
	}

	public void update(MatVector preprocessedFaces, int[] faceLabels) {
		train(preprocessedFaces, faceLabels);
	}

	public void save(String fileName) {
		if (fileName.endsWith(".xml")) {
			recognizer.save(fileName);
		} else {
			System.out.println("ERROR TO SAVE");
		}
	}

	public void load(String fileName) {
		if (fileName.endsWith(".xml")) {
			recognizer.load(fileName);
		} else {
			System.out.println("ERROR TO LOAD");
		}
	}

	public int predict(CvMat preprocessedFace) {
		int identity = -1;
		identity = recognizer.predict(preprocessedFace);
		return identity;
	}

	public CvMat getImageFrom1DFloatMat(CvMat matrixRow, int height) {

		CvMat rectangularMat = new CvMat();

		CvMat dst = new CvMat();
		cvNormalize(rectangularMat, dst, 0, 255, NORM_MINMAX, null);

		return dst;
	}

	public CvMat reconstructFace(CvMat preprocessedFace) {
		try {

			CvMat eigenvectors = recognizer.getMat("eigenvectors");  //�˴�������
			CvMat averageFaceRow = recognizer.getMat("mean");
			int faceHeight = preprocessedFace.rows();
			CvMat temp = new CvMat();
			cvReshape(preprocessedFace, temp, 1, 1);
			
			CvMat projection = subspaceProject(eigenvectors, averageFaceRow,temp);
					//�ӿռ���Ŀ������ʸ����ƽ�������У���ʱ��
					

			CvMat reconstructionRow = subspaceReconstruct(eigenvectors,
					averageFaceRow, projection);

			CvMat reconstructionMat = new CvMat();
			cvReshape(reconstructionRow, reconstructionMat, 1, faceHeight);

			CvMat reconstructedFace = cvCreateMat(reconstructionMat.rows(),
					reconstructionMat.cols(), CV_8U);
			cvConvert(reconstructionMat, reconstructedFace);

			return reconstructedFace;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public double getSimilarity(CvMat A, CvMat B) {
		if (A.rows() > 0 && A.rows() == B.rows() && A.cols() > 0
				&& A.cols() == B.cols()) {

			double errorL2 = cvNorm(A, B, CV_L2);

			double similarity = errorL2 / (double) (A.rows() * A.cols());
			return similarity;
		} else {
			return 100000000.0;
		}
	}

	public int recognise(CvMat preprocessedFace) {

		CvMat reconstructedFace = new CvMat();

		reconstructedFace = reconstructFace(preprocessedFace);

		similarity = getSimilarity(preprocessedFace, reconstructedFace);
		System.out.println("recognise��֤ʶ��ɿ���(�ع� vs Ԥ����ͼ��<0.6)��"+similarity);

		int identity = -1;
		if (similarity < unknowThreshold) {
			identity = recognizer.predict(preprocessedFace);//ʶ�𣬲�����������ţ�����Ӧ��ѧ�ţ�
		}
		return identity; //�����������
	}
	
	public Recognition clone() throws CloneNotSupportedException {
		return (Recognition) super.clone();
	}

	public float getUnknowThreshold() {
		return unknowThreshold;
	}

	public void setUnknowThreshold(float unknowThreshold) {
		this.unknowThreshold = unknowThreshold;
	}

	public synchronized FaceRecognizer getRecognizer() {
		return recognizer;
	}

	public synchronized void setRecognizer(FaceRecognizer recognizer) {
		this.recognizer = recognizer;
	}

	public synchronized double getSimilarity() {
		return similarity;
	}
}
