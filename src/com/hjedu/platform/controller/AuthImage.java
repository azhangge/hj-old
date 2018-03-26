package com.hjedu.platform.controller;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import javax.imageio.stream.ImageOutputStream;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet(urlPatterns = {"/servlet/AuthImage"})
public class AuthImage extends HttpServlet {
    private static final int len = 5;
    private static final int IMAGE_WIDTH = 100;
    private static final int IMAGE_HEIGHT = 22;
    private static final int FONT_HEIGHT = 22;
    private static final String SVG_SOURCE1 = "<!DOCTYPE svg PUBLIC \"-//W3C//DTD SVG 1.0//EN\" \"http://www.w3.org/TR/2001/REC-SVG-20010904/DTD/svg10.dtd\"><svg width=\"100\" height=\"22\" xmlns=\"http://www.w3.org/2000/svg\"><text x=\"0\" y=\"22\" font-family=\"Arial\" font-size=\"22\" fill=\"black\">";
    private static final String SVG_SOURCE2 = "</text></svg>";
    private boolean svgMode = false;
    private PrintWriter out = null;

    protected void service(HttpServletRequest arg0, HttpServletResponse arg1)
            throws ServletException, IOException {
        String vcode = sn2vcode();
        arg0.getSession().setAttribute("rand", vcode);

        if (this.svgMode) {
            outSVG(vcode, arg1);
        } else {
            outJPEG(vcode, arg1);
        }
    }

    private void outSVG(String vcode, HttpServletResponse resp)
            throws IOException {
        resp.setContentType("image/svg+xml");
        resp.getOutputStream().print("<!DOCTYPE svg PUBLIC \"-//W3C//DTD SVG 1.0//EN\" \"http://www.w3.org/TR/2001/REC-SVG-20010904/DTD/svg10.dtd\"><svg width=\"100\" height=\"22\" xmlns=\"http://www.w3.org/2000/svg\"><text x=\"0\" y=\"22\" font-family=\"Arial\" font-size=\"22\" fill=\"black\">" + vcode + "</text></svg>");
    }

    private void outJPEG(String vcode, HttpServletResponse resp) throws IOException {
        resp.setContentType("image/jpeg");
        BufferedImage image = new BufferedImage(100, 22,
                1);
        Random random = new Random();
        Graphics g = image.getGraphics();

        g.setColor(Color.WHITE);
        g.fillRect(0, 0, 100, 22);
        g.setFont(new Font("Times New Roman", 3, 22));
        g.setColor(getRandColor(160, 200));

        for (int i = 0; i < 155; ++i) {
            int x = random.nextInt(100);
            int y = random.nextInt(22);
            int xl = random.nextInt(12);
            int yl = random.nextInt(12);

            g.drawLine(x, y, x + xl, y + yl);
        }

        for (int i = 1; i <= 5; ++i) {
            String rand = vcode.substring(i - 1, i);
            g.setColor(
                    new Color(20 + random.nextInt(110), 20
                    + random.nextInt(110), 20 + random.nextInt(110)));
            g.drawString(rand, 20 * (i - 1) + 0, 16);
        }

        ImageWriter writer = (ImageWriter) ImageIO.getImageWritersByFormatName("jpeg").next();

        JPEGImageWriteParam params = new JPEGImageWriteParam(null);

        ImageOutputStream ios = ImageIO.createImageOutputStream(resp.getOutputStream());

        writer.setOutput(ios);

        writer.write(null, new IIOImage(image, null, null), params);

        writer.dispose();

        ios.close();
    }

    static String sn2vcode() {
        String sRand = "";

        Random random = new Random();

        for (int i = 0; i < 5; ++i) {
            String rand = String.valueOf(random.nextInt(10));

            sRand = sRand + rand;
        }

        return sRand;
    }

    public void init()
            throws ServletException {
        try {
            GraphicsEnvironment.getLocalGraphicsEnvironment();
        } catch (Throwable e) {
            this.svgMode = true;
        }
    }

    public Color getRandColor(int fc, int bc) {
        Random random = new Random();

        if (fc > 255) {
            fc = 255;
        }
        if (bc > 255) {
            bc = 255;
        }
        int r = fc + random.nextInt(bc - fc);
        int g = fc + random.nextInt(bc - fc);
        int b = fc + random.nextInt(bc - fc);
        return new Color(r, g, b);
    }
}