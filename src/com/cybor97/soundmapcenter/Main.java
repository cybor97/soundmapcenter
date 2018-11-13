package com.cybor97.soundmapcenter;

import javax.sound.sampled.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.nio.ByteBuffer;

public class Main {
    private static AudioFormat format = new AudioFormat(8000, 16, 1, true, false);
    private static SourceDataLine sourceDataLine;

    public static void main(String[] args) throws IOException, LineUnavailableException {

        sourceDataLine = (SourceDataLine) AudioSystem.getLine(new DataLine.Info(SourceDataLine.class, format));
        sourceDataLine.open(format);
        sourceDataLine.start();
        ((FloatControl) sourceDataLine.getControl(FloatControl.Type.MASTER_GAIN)).setValue(6f);

        GraphWindow window = new GraphWindow();

        //        ServerSocket listener = new ServerSocket(12001);
//        while (!Thread.interrupted()) {
//            Socket client = listener.accept();
//            playFromSocket(client);
//        }

        playFromSocket(new Socket("192.168.0.101", 12001));

        sourceDataLine.drain();
        sourceDataLine.close();

    }

    private static void playFromSocket(Socket socket) throws IOException {
        byte[] buffer = new byte[4];

        while (!Thread.interrupted() && !socket.isClosed()) {
            InputStream inputStream = socket.getInputStream();
            if (inputStream.read(buffer, 0, buffer.length) != 0) {
                if (buffer.length == 4) {
                    buffer = new byte[ByteBuffer.wrap(buffer).getInt()];
                } else {
//                        System.out.print(buffer[0]);
                    sourceDataLine.write(buffer, 0, buffer.length);
//                        window.drawSoundFrame(aggregate(buffer, 3));
                }
            }
        }
    }

    private static byte[] aggregate(byte[] buffer, int targetLength) throws IllegalArgumentException {
        byte[] resultBytes = new byte[targetLength];
        if (buffer.length > targetLength) {
            int step = buffer.length / targetLength;
            for (int i = 0; i < targetLength; i++) {
                int actualPartLength = 0;
                int localSum = 0;
                for (int j = i; j < i + step && j < buffer.length; j++) {
                    actualPartLength++;
                    localSum += buffer[j];
                }
                resultBytes[i] = (byte) (localSum / actualPartLength);
            }
            return resultBytes;
        } else throw new IllegalArgumentException("GO FUCK YOURSELF WITH THAT KIND OF ARGS!!!");
    }
}
