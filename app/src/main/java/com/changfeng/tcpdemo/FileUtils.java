package com.changfeng.tcpdemo;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.channels.FileChannel;

public class FileUtils {
    public static final String TAG = "FileUtils";

    public static String getFileName(String url) {
        int index = url.lastIndexOf("/");
        if (index == -1 || index == url.length() - 1) {
            return url;
        } else {
            return url.substring(index + 1);
        }
    }

    public static String ReadFile(String filename, String encode, boolean trim) {
        String res = "";
        File file = new File(filename);
        if (!file.exists())
            return "";
        BufferedReader reader = null;

        try {
            InputStreamReader isr = new InputStreamReader(new FileInputStream(
                    filename), encode);
            // BufferedReader read = new BufferedReader(isr);
            reader = new BufferedReader(isr);
            String tempString = null;
            int line = 1;

            while ((tempString = reader.readLine()) != null) {
                // 显示行号
                // System.out.println("line " + line + ": " + tempString);
                if (trim == true)
                    res = res + tempString.trim();
                else
                    res = res + tempString;
                line++;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return res;
    }

    public static String getStringFromFile(String filename) {
        FileInputStream fileInputStream = null;
        BufferedReader bufferedReader = null;
        try {
            fileInputStream = new FileInputStream(new File(filename));
            bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream, "utf-8"));

            StringBuilder result = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                result.append(line);
            }
            bufferedReader.close();
            return result.toString();
        } catch (FileNotFoundException e) {
            Log.e(TAG, "getStringFromFile() " + e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, "getStringFromFile() " + e.getMessage());
        } finally {
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            } catch (IOException expected) {
                Log.e(TAG, "getStringFromFile() " + expected.getMessage());
            }
        }
        return "";

    }


    public static void copyFilePath(File sourceFile, File targetFile) throws IOException {
        BufferedInputStream inBuff = null;
        BufferedOutputStream outBuff = null;
        try {
            inBuff = new BufferedInputStream(new FileInputStream(sourceFile));

            outBuff = new BufferedOutputStream(new FileOutputStream(targetFile));

            byte[] b = new byte[1024 * 5];
            int len;
            while ((len = inBuff.read(b)) != -1) {
                outBuff.write(b, 0, len);
            }
            outBuff.flush();
        } finally {
            if (inBuff != null)
                inBuff.close();
            if (outBuff != null)
                outBuff.close();
        }
    }


    public static void copyFolder(String oldPath, String newPath) {

        try {
            (new File(newPath)).mkdirs();
            File a = new File(oldPath);
            String[] file = a.list();
            File temp = null;
            for (int i = 0; i < file.length; i++) {
                if (oldPath.endsWith(File.separator)) {
                    temp = new File(oldPath + file[i]);
                } else {
                    temp = new File(oldPath + File.separator + file[i]);
                }

                if (temp.isFile()) {
                    FileInputStream input = new FileInputStream(temp);
                    FileOutputStream output = new FileOutputStream(newPath + "/" + (temp.getName()).toString());
                    byte[] b = new byte[1024 * 5];
                    int len;
                    while ((len = input.read(b)) != -1) {
                        output.write(b, 0, len);
                    }
                    output.flush();
                    output.close();
                    input.close();
                }
                if (temp.isDirectory()) {
                    copyFolder(oldPath + "/" + file[i], newPath + "/" + file[i]);
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "copyFolder() copy dir error", e);
        }

    }

    public static boolean exist(String path) {
        return new File(path).exists();
    }

    public static boolean fileIsVideo(String fn) {
        String suff = fn.substring(fn.lastIndexOf('.') + 1).toLowerCase();
        if (" .avi:.dat:.divx:.xvid:.wmv:.ts:.url:.mms:.tp:.vob:.mpg:.mpeg:.mov:.mp4:.mkv:.m2ts:.mts:.dts:.m2v:.m4v:.asf:.dtv"
                .indexOf(suff) > 0)
            return true;
        else
            return false;
    }

    public static boolean fileIsAudio(String fn) {
        String suff = fn.substring(fn.lastIndexOf('.') + 1).toLowerCase();
        if (" .mp3:.wma".indexOf(suff) > 0)
            return true;
        else
            return false;
    }

    public static boolean fileIsText(String fn) {
        String suff = fn.substring(fn.lastIndexOf('.') + 1).toLowerCase();
        if (" .txt".indexOf(suff) > 0)
            return true;
        else
            return false;
    }

    public static boolean fileIsGif(String fn) {
        String suff = fn.substring(fn.lastIndexOf('.') + 1).toLowerCase();
        if (" .gif ".indexOf(suff) > 0)
            return true;
        else
            return false;
    }

    public static boolean fileIsJpeg(String fn) {
        String suff = fn.substring(fn.lastIndexOf('.') + 1).toLowerCase();
        if (" .jpeg .jpg".indexOf(suff) > 0)
            return true;
        else
            return false;
    }

    public static boolean fileIsImage(String fn) {
        String suff = fn.substring(fn.lastIndexOf('.') + 1).toLowerCase();
        if (" .jpeg .bmp .jpg .png .gif".indexOf(suff) > 0)
            return true;
        else
            return false;
    }

    public static long getFileSizes(String f) {// 取得文件大小
        long s = 0;
        try {
            File file = new File(f);
            if (file.exists()) {
                FileInputStream fis = new FileInputStream(file);
                s = fis.available();
                fis.close();
            } else {
                return 0;
            }
        } catch (Exception e) {

        }
        return s;
    }

    public static String joinPath(String... args) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String arg : args) {
            stringBuilder.append(File.separator).append(arg.replaceAll("^/+", ""));
        }
        return stringBuilder.toString();
    }

    public static boolean isDirectory(String dir, String fileName) {
        return (new File(joinPath(dir, fileName)).isDirectory());
    }

    public static void writeFile(String filename, String text) throws Exception {
        File f = new File(filename);
        if (!f.exists())
            f.createNewFile();
        else {
            f.delete();
            f.createNewFile();
        }
        BufferedWriter output = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(filename), "utf-8"));

        // new BufferedWriter(new FileWriter(filename,
        // false),"utf-8");
        output.write(text);

        output.flush();
        output.close();
    }

    public static boolean renameFile(String src, String dst) {
        File srcFile = new File(src);
        File dstFile = new File(dst);
        if (dstFile.exists()) {
            dstFile.delete();
        }
        return srcFile.renameTo(dstFile);
    }


    public static void copy(String oldPath, String newPath) {
        try {
            int byteread = 0;
            File oldfile = new File(oldPath);
            if (oldfile.exists()) {
                InputStream inStream = new FileInputStream(oldPath);
                FileOutputStream fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1444];
                //int length;
                while ((byteread = inStream.read(buffer)) != -1) {
                    fs.write(buffer, 0, byteread);
                }

                inStream.close();
                fs.close();
                inStream.close();
            }
        } catch (Exception e) {
            System.out.println("error 1 ");
            e.printStackTrace();
        }
    }

    public static String removeSuffix(String fileName) {
        int index = fileName.lastIndexOf(".");

        if (index == -1) {
            return fileName;
        } else {
            return fileName.substring(0, index);
        }
    }

    public static void copyFileUsingFileChannels(File source, File dest)
            throws IOException {
        FileChannel inputChannel = null;
        FileChannel outputChannel = null;
        try {
            inputChannel = new FileInputStream(source).getChannel();
            outputChannel = new FileOutputStream(dest).getChannel();
            outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
        } finally {
            inputChannel.close();
            outputChannel.close();
        }
    }

    public static void defileFile(File file) {
        if (file.isFile()) {
            file.delete();
            return;
        }

        if (file.isDirectory()) {
            File[] childFiles = file.listFiles();
            if (childFiles == null || childFiles.length == 0) {
                file.delete();
                return;
            }

            for (File f : childFiles) {
                defileFile(f);
            }
            file.delete();
        }

    }

    public static long getFolderSize(File file) throws Exception {
        long size = 0;
        File[] fileList = file.listFiles();
        for (File f : fileList) {
            if (f.isDirectory()) {
                size += getFolderSize(f);
            } else {
                size += f.length();
            }
        }
        return size;
    }
}
