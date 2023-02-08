package ru.opencvdemo;


import org.opencv.core.Mat;
import org.opencv.objdetect.CascadeClassifier;
import ru.opencvdemo.opencv.ObjectsDetector;
import ru.opencvdemo.window.MainWindow;
import ru.opencvdemo.window.Render;

import java.awt.image.BufferedImage;

public class Application
{
    public static void main(String[] args) throws Exception
    {
        MainWindow mainWindow = new MainWindow();   // главное окно
        mainWindow.show();  // показываем окно

        BufferedImage image = mainWindow.getImage();    // главное изображение
        Render render = new Render(image, mainWindow.getFrame());   // рендер

        ObjectsDetector faceDetector = new ObjectsDetector();     // обнаружение лиц
        faceDetector.setVideoCaptureIndex(0);   // задаем номер камеры = 0
        CascadeClassifier classifier = ObjectsDetector.faceClassifier();    // используем классификатор распознавания лиц

        Mat matrix = faceDetector.captureFrame();   // делаем снимок с камеры
        BufferedImage img = faceDetector.detectObjects(matrix, classifier);      // выполняем поиск объектов

        render.draw(img);  // рисуем

        // рисуем постоянно видеопоток
        while(mainWindow.isVisible())
        {
            faceDetector.captureFrame(matrix);   // делаем снимок с камеры
            faceDetector.detectObjects(matrix, classifier, img);      // выполняем поиск объектов
            render.draw(img);  // рисуем
        }
    }
}
