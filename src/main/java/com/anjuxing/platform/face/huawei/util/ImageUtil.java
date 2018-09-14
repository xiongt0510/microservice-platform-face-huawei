package com.anjuxing.platform.face.huawei.util;

import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Objects;

/**
 * @author xiongt
 * @Description
 */
public class ImageUtil {

    private ImageUtil(){}

    /**
     * 将图片类型转换为Base64
     * @param image
     * @return
     */
    public static String ImageToBase64(File image){

        if (Objects.isNull(image)){
            throw new NullPointerException("文件 image 是空");
        }

        try {
            if (Objects.isNull(ImageIO.read(image))){
                throw new NullPointerException("传入的文件不是图片格式");
            }
        } catch (IOException e) {
            throw new RuntimeException("读取文件失败\n" + e.getMessage());
        }

            try {
                FileInputStream inputFile = new FileInputStream(image);
                byte[] buffer = new byte[(int) image.length()];
                inputFile.read(buffer);
                inputFile.close();
                return new BASE64Encoder().encode(buffer);
            } catch (Exception e) {
                throw new RuntimeException("文件无效\n" + e.getMessage());
            }

    }
}
