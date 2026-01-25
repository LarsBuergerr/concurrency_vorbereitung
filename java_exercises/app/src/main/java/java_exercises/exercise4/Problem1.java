package java_exercises.exercise4;   

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Problem1 {
    


    public static void main(String args[]) throws MalformedURLException, IOException {

        URL url1 = URI.create("http://212.183.159.230/100MB.zip").toURL();
        URL url2 = URI.create("http://212.183.159.230/100MB.zip").toURL();

        Downloader d1 = new Downloader(url1, "out1");
        Downloader d2 = new Downloader(url2, "out2");

        ProgressListener l1 = new ProgressListener("l1", d2);
        ProgressListener l2 = new ProgressListener("l2", d1);

        d1.addListener(l1);
        d2.addListener(l2);


        ExecutorService executor = Executors.newFixedThreadPool(2);

    
        executor.execute(() -> {try {
            d1.run();
        } catch(Exception e) {
            e.printStackTrace();
        }});
        executor.execute(() -> {
            try {
                d2.run();
            } catch(Exception e) {
                e.printStackTrace();
        }});
    }
}


class Downloader {
    private final InputStream in;
    private final OutputStream out;
    private final List<ProgressListener> listeners;


    public Downloader(URL url, String outputFilename) throws IOException {
        in = url.openConnection().getInputStream();
        out = new FileOutputStream(outputFilename);
        listeners = new ArrayList<>();
    }

    public synchronized void addListener(ProgressListener listener) {
        listeners.add(listener);
    }

    private synchronized void updateProgress(int total) {
        for (ProgressListener listener : listeners) {
            listener.onProgress(total);
        }
    }

    public synchronized void performAction(String callerName) {
        try { Thread.sleep(10); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
    }

    public void run() throws IOException {
        int n = 0, total = n;
        byte buffer[] = new byte[1024];
        while ((n = in.read(buffer)) != -1) {
            out.write(buffer, 0, n);
            total += n;
            updateProgress(total);
        }
        out.flush();
    }
}


class ProgressListener {

    String id;
    Downloader otherDownloader;

    public ProgressListener(String id, Downloader otherDownloader) {
        this.id = id;
        this.otherDownloader = otherDownloader;
    }

    void onProgress(int n) {
        System.out.printf("Listener %s is performing action in other Downloader", this.id);
        this.otherDownloader.performAction(this.id);
    }
}