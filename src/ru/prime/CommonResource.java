package ru.prime;

import java.io.*;

class CommonResource {

    private File resultFile;

    public CommonResource(String resultPath){
        resultFile = new File(resultPath);

        if (!resultFile.exists()) {
            try {
                resultFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void writeResultFile(int num) {

        try (FileWriter writer = new FileWriter(resultFile.getAbsoluteFile(),true);
            BufferedWriter buffer = new BufferedWriter(writer)) {

            buffer.write(" " + num);

        } catch(IOException e) {
            throw new RuntimeException(e);
        }
    }

    public int readResultFile() {

        StringBuilder sb = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new FileReader(resultFile.getAbsoluteFile()))) {

            String s;
            while ((s = reader.readLine()) != null) {
                sb.append(s);
                sb.append("\n");
            }

        } catch(IOException e) {
            throw new RuntimeException(e);
        }

        String result = sb.toString();
        if (!result.equals("")) {
            result = result.substring(result.lastIndexOf(' '), result.length());
            return Integer.parseInt(result.trim());
        } else {
            return 0;
        }
    }

    public String getFilePath(){
        return resultFile.getAbsolutePath();
    }
}
