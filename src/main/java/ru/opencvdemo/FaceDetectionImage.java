package ru.opencvdemo;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;

import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.videoio.VideoCapture;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;

public class FaceDetectionImage
{
    private final String inputFileName = "C:\\Users\\ASUS\\Downloads\\facedetection_input.jpg";
    private final String outputFileName = "C:\\Users\\ASUS\\Downloads\\cam_image_output.jpg";
    private final String cascadeClassifierXMLFileName = "src/main/resources/face.xml";

    private final int screenWidth = 1200;
    private final int screenHeight = 800;

    volatile static boolean isFrameReadyToDraw = true;

    public void showWindow() throws IOException
    {
        // считываем исходное изображение
        BufferedImage image = convertMatrixToBufferedImage(captureFrame());

        // создаем окно
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        // создаем блок отображения картинок
        ImageIcon imageIcon = new ImageIcon(image);
        JLabel imageLabel = new JLabel(imageIcon);

        // layout - чтобы элементы не расползались
        BorderLayout borderLayout = new BorderLayout();
        Container mainWindow = frame.getContentPane();      // получаем область контента
        mainWindow.setLayout(borderLayout);                 // задаем layout
        mainWindow.add(imageLabel, BorderLayout.CENTER);    // задаем расположение по центру

        // отображаем окно
        frame.setSize(image.getWidth(), image.getHeight());
        frame.setVisible(true);

        // получаем обработанное изображение
        BufferedImage img = start();

        // рисуем
        Graphics2D graphics = image.createGraphics();
        graphics.drawImage(img, 0, 0, img.getWidth(), img.getHeight(), null);

        while (frame.isVisible())
        {
            if(isFrameReadyToDraw)
            {
                isFrameReadyToDraw = false;
                SwingUtilities.invokeLater(() -> {
                    frame.repaint();
                    isFrameReadyToDraw = true;
                });
            }

        }

    }

    public BufferedImage start() throws IOException
    {
//        // загружаем библиотеку OpenCV core
//        System.loadLibrary( Core.NATIVE_LIBRARY_NAME );

        // считываем изображение из файла и сохраняем его в матрицу
//        Mat imageMatrix = Imgcodecs.imread(inputFileName);
        Mat imageMatrix = captureFrame();   // получаем изображение с камеры

        // инициализируем CascadeClassifier
        CascadeClassifier classifier = new CascadeClassifier(cascadeClassifierXMLFileName);

        // выполняем обнаружение объектов
        MatOfRect faceDetections = new MatOfRect();     // матрица прямоугольников
        classifier.detectMultiScale(imageMatrix, faceDetections);   // выполняем обнаружение

        // рисуем прямоугольники
        drawDetectedRects(imageMatrix, faceDetections.toArray());

        // создаем изображение из матрицы
        BufferedImage image = convertMatrixToBufferedImage(imageMatrix);

        // сохраняем изображение в файл
        Imgcodecs.imwrite(outputFileName, imageMatrix);

        System.out.println("Image Processed");

        return image;
    }

    public BufferedImage convertMatrixToBufferedImage(Mat imageMatrix)      // создаем изображение из матрицы
    {
        BufferedImage image = new BufferedImage(imageMatrix.width(), imageMatrix.height(), BufferedImage.TYPE_3BYTE_BGR);
        byte[] data = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        imageMatrix.get(0, 0, data);

        return  image;
    }

    public Mat captureFrame()
    {
        // создаем камеру, номер камеры - 0 (camera:: 0)
        VideoCapture capture = new VideoCapture(0);

        // пробуем сделать кадр, тем самым инициализировав камеру
        Mat matrix = new Mat();
        capture.read(matrix);

        // смотрим, чтобы камера работала
        if(!capture.isOpened())
        {
            System.out.println("camera not detected");
        }
        else
        {
            System.out.println("Camera detected ");
        }
        return matrix;
    }

    public void drawDetectedRects(Mat image, Rect[] rects)
    {
        // рисуем прямоугольники
        for (Rect rect : rects) {
            Imgproc.rectangle(
                    image,                                               // изображение, на котором рисуем
                    new Point(rect.x, rect.y),                           // слева снизу
                    new Point(rect.x + rect.width, rect.y + rect.height), // сверху справа
                    new Scalar(0, 255, 0),                                      // цвет в формате BGR
                    3
            );
        }
    }

    public static void main (String[] args) throws IOException
    {
        // загружаем библиотеку OpenCV core
        System.loadLibrary( Core.NATIVE_LIBRARY_NAME );

        // запускаем процесс
        FaceDetectionImage faceDetectionImage = new FaceDetectionImage();
        faceDetectionImage.showWindow();
    }
}
