package com.huajie.util;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import com.hjedu.platform.entity.ManualModel;

/**
 *
 * @author Administrator
 */
public class XmlManualUtils {

    /**
     * 读取一个帮助文档的XML文件，并返回帮助文档对象
     *
     * @param path 存放帮助文档的目录
     * @param id 文档的ID
     * @return 返回帮助文档对象
     */
    public static ManualModel parseManualfromXML(String path, String id) {
        ManualModel mm = new ManualModel();
        mm.setId(id);
        try {
            path = path + File.separator + id+ ".manual";
            File file = new File(path);
            //Document doc = DocumentHelper.parseText(str);
            SAXReader saxReader = new SAXReader();
            saxReader.setEncoding("utf-8");
            Document doc = saxReader.read(file);

            String dtitle = "/manual/title";
            String dcontent = "/manual/content";
            String dtime = "/manual/gen_time";
            String dord = "/manual/ord";

            String title = doc.selectSingleNode(dtitle).getText();
            String content = doc.selectSingleNode(dcontent).getText();
            String time = doc.selectSingleNode(dtime).getText();
            String ord = doc.selectSingleNode(dord).getText();

            mm.setTitle(title);
            mm.setContent(content);
            if (time != null) {
                try {
                    Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(time);
                    mm.setInputdate(date);
                } catch (Exception e) {
                }
            }
            if (ord != null) {
                try {
                    int o = Integer.parseInt(ord);
                    mm.setOrd(o);
                } catch (Exception e) {
                }
            }

            System.out.println("");

        } catch (Exception e) {
            e.printStackTrace();

        }
        return mm;
    }

    /**
     * 将一个帮助文档写入XML
     *
     * @param path 存放帮助文档的目录
     * @param mm 帮助文档对象
     */
    public static void writeManualToXML(String path, ManualModel mm) {
        try {
            path = path + File.separator + mm.getId() + ".manual";

            String id = "/manual/id";
            String title = "/manual/title";
            String content = "/manual/content";
            String time = "/manual/gen_time";
            String ord = "/manual/ord";

            Document doc = DocumentHelper.createDocument();
            Element eid = DocumentHelper.makeElement(doc, id);
            Element etitle = DocumentHelper.makeElement(doc, title);
            Element econtent = DocumentHelper.makeElement(doc, content);
            Element etime = DocumentHelper.makeElement(doc, time);
            Element eord = DocumentHelper.makeElement(doc, ord);

            eid.setText(mm.getId());
            etitle.setText(mm.getTitle());
            econtent.setText(mm.getContent());
            String t = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(mm.getInputdate());
            etime.setText(t);
            eord.setText(String.valueOf(mm.getOrd()));

            //System.out.println(doc.asXML());
            writeXML(path, doc);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void writeManualDirectoryToXML(String path, List<ManualModel> mms) {
        try {
            path = path + File.separator + "directory.manual";
            String outputDate = "/directory/output_time";
            String root = "/directory/manuals";
            String manual = "manual";
            String id = "id";
            String title = "title";
            String time = "gen_time";
            String ord = "ord";

            Document doc = DocumentHelper.createDocument();
            Element eout = DocumentHelper.makeElement(doc, outputDate);
            eout.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            Element eroot = DocumentHelper.makeElement(doc, root);

            //System.out.println(doc.asXML());
            for (ManualModel mm : mms) {
                Element emanual = DocumentHelper.createElement(manual);
                System.out.println(eroot);
                eroot.add(emanual);
                Element eid = DocumentHelper.createElement(id);
                Element etitle = DocumentHelper.createElement(title);
                Element etime = DocumentHelper.createElement(time);
                Element eord = DocumentHelper.createElement(ord);
                emanual.add(eid);
                emanual.add(etitle);
                emanual.add(etime);
                emanual.add(eord);
                eid.setText(mm.getId());
                etitle.setText(mm.getTitle());
                String t = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(mm.getInputdate());
                etime.setText(t);
                eord.setText(String.valueOf(mm.getOrd()));

            }
            //System.out.println(doc.asXML());
            writeXML(path, doc);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 将全部帮助文档写出
     *
     * @param path 存放帮助文档的目录
     * @param mms 帮助文档列表
     */
    public static void writeAllManualToXML(String path, List<ManualModel> mms) {
        try {
            File file = new File(path);
            if (!file.exists()) {
                file.mkdir();
            }
            for (ManualModel mm : mms) {
                writeManualToXML(path, mm);
            }
            writeManualDirectoryToXML(path, mms);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 将一个Document对象写入XML文件
     *
     * @param path 保存对象的整个文件路径，包括目录与文件名
     * @param doc Document对象
     */
    public static void writeXML(String path, Document doc) {
        try {
            OutputFormat out = OutputFormat.createPrettyPrint();
            out.setEncoding("utf-8");
            XMLWriter writer = new XMLWriter(new FileOutputStream(path), out);
            writer.write(doc);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 将帮助文档的目录加载成为列表
     *
     * @param path 帮助文档目录所在的文件夹目录
     * @return 返回列表，对象中无content字段
     */
    public static List<ManualModel> parseDirectoryfromXML(String path) {
        List<ManualModel> mms = new ArrayList();
        try {
            path = path + File.separator + "directory.manual";
            SAXReader saxReader = new SAXReader();
            saxReader.setEncoding("utf-8");
            Document doc = saxReader.read(path);

            List<Element> nodes = doc.selectNodes("/directory/manuals/manual");
            System.out.println(nodes.size());
            for (Element el : nodes) {
                ManualModel mm = new ManualModel();
                String id = "id";
                String title = "title";
                String time = "gen_time";
                String ord = "ord";

                try {
                    id = el.selectSingleNode("id").getText();
                    mm.setId(id);
                    title = el.selectSingleNode("title").getText();
                    mm.setTitle(title);
                    time = el.selectSingleNode("gen_time").getText();
                    ord = el.selectSingleNode("ord").getText();
                    if (time != null) {
                        try {
                            Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(time);
                            mm.setInputdate(date);
                        } catch (Exception e) {
                        }
                    }
                    if (ord != null) {
                        try {
                            int o = Integer.parseInt(ord);
                            mm.setOrd(o);
                        } catch (Exception e) {
                        }
                    }
                    mms.add(mm);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //System.out.println("One manual is added..............................................................");
            }

        } catch (Exception e) {
            e.printStackTrace();

        }
        return mms;
    }

    public static void main(String args[]) {
        String path = "C:\\manuals\\";
        File f = new File(path);
        //IManualDAO mDAO = SpringHelper.getSpringBean("ManualDAO");
        //List<ManualModel> mms = mDAO.findAllManual();
        //XmlManualUtils.writeAllManualToXML(path, mms);
        //writeManualDirectoryToXML(path, mms);
        List<ManualModel> mms = XmlManualUtils.parseDirectoryfromXML(path);
        for (ManualModel mm : mms) {
            System.out.println(mm.getTitle());
        }

    }

}
