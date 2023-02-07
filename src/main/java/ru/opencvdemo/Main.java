package ru.opencvdemo;

import org.bytedeco.ffmpeg.global.avcodec;
//import org.bytedeco.javacpp.avcodec;
//import org.bytedeco.javacpp.opencv_core;
//import org.bytedeco.javacpp.opencv_objdetect;
import org.bytedeco.javacv.*;
import org.bytedeco.opencv.global.opencv_objdetect;

//import static org.bytedeco.javacpp.opencv_core.*;
//import static org.bytedeco.javacpp.opencv_imgcodecs.cvLoadImage;
//import static org.bytedeco.javacpp.opencv_imgproc.rectangle;

//import hypermedia.video.OpenCV;

public class Main
{
    private static final String fileName = "video.avi";
//    private static final opencv_objdetect.CvHaarClassifierCascade classifierFace =
//            new opencv_objdetect.CvHaarClassifierCascade(cvLoad("src/main/resources/face.xml"));

    public static void main(String[] args) throws FrameGrabber.Exception, FrameRecorder.Exception
    {
        // подключение к камере
        System.out.println("Подключение к камере...");

        OpenCVFrameGrabber grabber = new OpenCVFrameGrabber(0);     // адаптер камеры - 0 = номер камеры в системе
        System.out.println("Подключение выполнено");

        grabber.start();    // запуск подключения
        Frame frame = grabber.grab();   // получаем кадр

        System.out.println("Запись началась");

        // запись в файл
        FFmpegFrameRecorder recorder = new FFmpegFrameRecorder(fileName, frame.imageWidth, frame.imageHeight, 0);   // рекордер для записи в файл
        recorder.setFrameRate(30);  // задаем частоту 30 кадров в секунду
        recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264);   // ставим кодек H264
        recorder.setFormat("avi");  // формат записи
        double quality = 0.8;     // качество записи
        recorder.setVideoBitrate((int) (quality * 1024 * 1024));
        recorder.start();   // запускаем запись

//        // для поиска объектов
//        OpenCVFrameConverter.ToIplImage converter = new OpenCVFrameConverter.ToIplImage();  // конвертер кадров в изображения
//        opencv_core.IplImage iplImage = null;

        // вывод изображения
        CanvasFrame canvasFrame = new CanvasFrame("Изображение с камеры");      // окно изображения
        canvasFrame.setCanvasSize(frame.imageWidth, frame.imageHeight);     // задаем размеры окна, равные кадру
        while (canvasFrame.isVisible() && frame != null)     // считываем кадры
        {
            frame = grabber.grab();     // получаем кадр

//            iplImage = converter.convert(frame);    // конвертируем кадр в изображение
//
//            findObject(iplImage);   // выполняем поиск объектов на изображении
//
//            frame = converter.convert(iplImage);    // конвертируем изображение в кадр
            canvasFrame.showImage(frame);   // отображаем кадр на экран
            recorder.record(frame);     // записываем кадр
        }
        canvasFrame.dispose();  // закрываем окно (очищаем ресурсы)

        recorder.stop();    // завершаем запись
        recorder.close();   // закрываем рекордер
    }

    /** Функция поиска объектов на изображении
     * @param currentFrame текущий кадр
     */
//    public static void findObject(opencv_core.IplImage currentFrame)
//    {
//        opencv_core.CvMemStorage storage = opencv_core.CvMemStorage.create();   // создаем временное хранилище
//        opencv_core.CvSeq faces = opencv_objdetect.cvHaarDetectObjects(currentFrame, classifierFace, storage, 1.5, 4, opencv_objdetect.CV_HAAR_MAGIC_VAL);
//        int total = faces.total();      // сколько лиц было найдено
//        if(total > 0)   // если лица найдены
//        {
//            for (int i = 0; i < total; i++)     // перебираем все лица
//            {
//                opencv_core.CvRect rect = new opencv_core.CvRect(cvGetSeqElem(faces, i));   // получаем размер и координаты прямоугольника лица
//
//                int x = rect.x();
//                int y = rect.y();
//                int width = rect.width();
//                int height = rect.height();
//
//                rectangle(cvarrToMat(currentFrame), new Rect(x, y, width, height), new Scalar(0, 255, 0, 0), 2, 0, 0);  // рисуем прямоугольник на кадре
//            }
//        }
//    }
}
