package com.health.utils;

import javax.imageio.stream.FileImageOutputStream;
import java.io.File;

public class ImageStoreUtils {

    public static void upload(byte[] bytes,String filename){
        if(bytes.length<3||filename.equals("")) return;//判断输入的byte是否为空
        String path="F:/healthimg/"+filename;
        try{
            FileImageOutputStream imageOutput = new FileImageOutputStream(new File(path));//打开输入流
            imageOutput.write(bytes, 0, bytes.length);//将byte写入硬盘
            imageOutput.close();

        } catch(Exception ex) {

            ex.printStackTrace();
        }
    }
    public static void delete(String filename){

        String path="F:/healthimg/"+filename;
        boolean delete = new File(path).delete();


    }


    }

