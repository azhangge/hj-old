package com.huajie.servlet;

import java.io.File;
import java.io.FileOutputStream;
import javax.servlet.http.HttpServletRequest;

public class ReImage
{
  static String cp = "resources/sys/images";

  public static boolean savaImage(Long id, byte[] image, HttpServletRequest request) {
    String imageName = request.getRealPath(cp) + "\\" + id.toString() + ".jpg";
    try {
      File f = new File(imageName);
      FileOutputStream fos = new FileOutputStream(f);
      fos.write(image);
      fos.close();
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }

  public static boolean deleteImage(Long id, HttpServletRequest request) {
    String imageName = request.getRealPath(cp) + "\\" + id.toString() + ".jpg";
    try {
      File f = new File(imageName);
      f.delete();
    } catch (Exception e) {
      e.printStackTrace();

      return false;
    }
    return true;
  }
}