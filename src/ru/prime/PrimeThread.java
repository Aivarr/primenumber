package ru.prime;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Semaphore;

public class PrimeThread extends Thread {

    private final int MAX_NUMBER = 1_000_000;

    private static Set<String> allFiles = new HashSet<>();

    private String filePath;
    private CommonResource commonResource;
    private Semaphore semaphore;

    public PrimeThread(String filePath, CommonResource commonResource, Semaphore semaphore) {
        this.filePath = filePath;
        this.semaphore = semaphore;
        this.commonResource = commonResource;
        allFiles.add(commonResource.getFilePath());
        allFiles.add(filePath);
    }

    @Override
    public void run() {

        clearAllFiles();

        int primeNumber = 2;

        try {

            semaphore.acquire();

            if (commonResource.readResultFile() == 0) {
                commonResource.writeResultFile(primeNumber);
                writeLocalFile(primeNumber);
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        semaphore.release();

        while (getNextPrimeNumber(primeNumber) < MAX_NUMBER) {
            try {

                semaphore.acquire();

                primeNumber = getNextPrimeNumber(commonResource.readResultFile());
                commonResource.writeResultFile(primeNumber);
                writeLocalFile(primeNumber);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            semaphore.release();
        }

        System.out.println("Completed");
    }

    public void writeLocalFile(int num) {

        File file = new File(filePath);

        try (FileWriter writer = new FileWriter(file.getAbsoluteFile(),true);
             BufferedWriter buffer = new BufferedWriter(writer)) {

            buffer.write(num + " ");

            /*System.out.println(num + " " + filePath);*/

        } catch(IOException e) {
            throw new RuntimeException(e);
        }
    }

    private int getNextPrimeNumber(int primeNumber) {

        do {
            primeNumber++;
        } while (!BigInteger.valueOf(primeNumber).isProbablePrime((int) Math.log(primeNumber)));

        return primeNumber;
    }

    public void clearAllFiles() {
        for (String filePath : allFiles) {

            File file = new File(filePath);

            try {

                if (!file.exists()) {
                    file.createNewFile();
                }

                FileWriter writer = new FileWriter(file.getAbsoluteFile(), false);
                BufferedWriter buffer = new BufferedWriter(writer);

                buffer.write("");

                buffer.close();
                writer.close();

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
