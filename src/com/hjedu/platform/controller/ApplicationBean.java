package com.hjedu.platform.controller;

import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import com.hjedu.platform.dao.ICustomerServiceDAO;
import com.hjedu.platform.dao.IPositionDAO;
import com.hjedu.platform.dao.ISystemConfigDAO;
import com.hjedu.platform.dao.ISystemInfoDAO;
import com.hjedu.platform.entity.BbsPosition;
import com.hjedu.platform.entity.CustomerService;
import com.hjedu.platform.entity.SystemConfig;
import com.hjedu.platform.entity.SystemInfo;
import com.huajie.app.util.UrlUtil;
import com.huajie.util.CookieUtils;
import com.huajie.util.JsfHelper;
import com.huajie.util.SpringHelper;

@ManagedBean
@RequestScoped
public class ApplicationBean implements Serializable {

    SystemInfo info;
    String fillBegain = SpringHelper.getSpringBean("fillBegain");
    String fillEnd = SpringHelper.getSpringBean("fillEnd");
    String path;
    String filePath = "";
    CustomerService customerService;
    ICustomerServiceDAO customerServiceDAO = SpringHelper.getSpringBean("CustomerServiceDAO");
    SystemConfig systemConfig;
    ISystemConfigDAO systemConfigDAO = SpringHelper.getSpringBean("SystemConfigDAO");
    ISystemInfoDAO infoDAO = SpringHelper.getSpringBean("SystemInfoDAO");
    IPositionDAO positionDAO = SpringHelper.getSpringBean("PositionDAO");

    String relativePath = SpringHelper.getSpringBean("relativeDir").toString();
    String departmentStr = SpringHelper.getSpringBean("department_str").toString();
    String cidStr = SpringHelper.getSpringBean("cid_str").toString();

    Integer maxFraction = SpringHelper.getSpringBean("max_fraction");

    String custom1 = SpringHelper.getSpringBean("user_custom1");
    String custom2 = SpringHelper.getSpringBean("user_custom2");
    String custom3 = SpringHelper.getSpringBean("user_custom3");
    String custom4 = SpringHelper.getSpringBean("user_custom4");
    String custom5 = SpringHelper.getSpringBean("user_custom5");

    List<BbsPosition> positions;
    
    String mp4VirtualUrl;
    String mp3VirtualUrl;
    String imageVirtualUrl;
    String docVirtualUrl;
    String docxVirtualUrl;
    String pptVirtualUrl;
    String pptxVirtualUrl;
    String pdfVirtualUrl;
    String htmlVirtualUrl;
    
    private String businessId;

    public String getDocVirtualUrl() {
		return docVirtualUrl;
	}

	public void setDocVirtualUrl(String docVirtualUrl) {
		this.docVirtualUrl = docVirtualUrl;
	}

	public String getDocxVirtualUrl() {
		return docxVirtualUrl;
	}

	public void setDocxVirtualUrl(String docxVirtualUrl) {
		this.docxVirtualUrl = docxVirtualUrl;
	}

	public String getPptVirtualUrl() {
		return pptVirtualUrl;
	}

	public void setPptVirtualUrl(String pptVirtualUrl) {
		this.pptVirtualUrl = pptVirtualUrl;
	}

	public String getPptxVirtualUrl() {
		return pptxVirtualUrl;
	}

	public void setPptxVirtualUrl(String pptxVirtualUrl) {
		this.pptxVirtualUrl = pptxVirtualUrl;
	}

	public String getPdfVirtualUrl() {
		return pdfVirtualUrl;
	}

	public void setPdfVirtualUrl(String pdfVirtualUrl) {
		this.pdfVirtualUrl = pdfVirtualUrl;
	}

	public String getHtmlVirtualUrl() {
		return htmlVirtualUrl;
	}

	public void setHtmlVirtualUrl(String htmlVirtualUrl) {
		this.htmlVirtualUrl = htmlVirtualUrl;
	}
    
	public String getMp3VirtualUrl() {
		return mp3VirtualUrl;
	}

	public void setMp3VirtualUrl(String mp3VirtualUrl) {
		this.mp3VirtualUrl = mp3VirtualUrl;
	}

	public String getMp4VirtualUrl() {
		return mp4VirtualUrl;
	}

	public void setMp4VirtualUrl(String mp4VirtualUrl) {
		this.mp4VirtualUrl = mp4VirtualUrl;
	}

	public String getImageVirtualUrl() {
		return imageVirtualUrl;
	}

	public void setImageVirtualUrl(String imageVirtualUrl) {
		this.imageVirtualUrl = imageVirtualUrl;
	}

	public String getPath() {
        return this.path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public List<BbsPosition> getPositions() {
        this.loadPositions();
        return positions;
    }

    public void setPositions(List<BbsPosition> positions) {
        this.positions = positions;
    }

    public Integer getMaxFraction() {
        return maxFraction;
    }

    public void setMaxFraction(Integer maxFraction) {
        this.maxFraction = maxFraction;
    }

    public String getCidStr() {
        return cidStr;
    }

    public void setCidStr(String cidStr) {
        this.cidStr = cidStr;
    }

    public String getRelativePath() {
        return relativePath;
    }

    public void setRelativePath(String relativePath) {
        this.relativePath = relativePath;
    }

    public String getFillBegain() {
        return fillBegain;
    }

    public void setFillBegain(String fillBegain) {
        this.fillBegain = fillBegain;
    }

    public String getDepartmentStr() {
        return departmentStr;
    }

    public void setDepartmentStr(String departmentStr) {
        this.departmentStr = departmentStr;
    }

    public String getCustom1() {
        return custom1;
    }

    public void setCustom1(String custom1) {
        this.custom1 = custom1;
    }

    public String getCustom2() {
        return custom2;
    }

    public void setCustom2(String custom2) {
        this.custom2 = custom2;
    }

    public String getCustom3() {
        return custom3;
    }

    public void setCustom3(String custom3) {
        this.custom3 = custom3;
    }

    public String getCustom4() {
        return custom4;
    }

    public void setCustom4(String custom4) {
        this.custom4 = custom4;
    }

    public String getCustom5() {
        return custom5;
    }

    public void setCustom5(String custom5) {
        this.custom5 = custom5;
    }

    public String getFillEnd() {
        return fillEnd;
    }

    public void setFillEnd(String fillEnd) {
        this.fillEnd = fillEnd;
    }

    public SystemConfig getSystemConfig() {
        return systemConfig;
    }

    public void setSystemConfig(SystemConfig systemConfig) {
        this.systemConfig = systemConfig;
    }

    public CustomerService getCustomerService() {
        return customerService;
    }

    public void setCustomerService(CustomerService customerService) {
        this.customerService = customerService;
    }

    public SystemInfo getInfo() {
        return info;
    }

    public void setInfo(SystemInfo info) {
        this.info = info;
    }

    @PostConstruct
    public void init() {
    	this.businessId = CookieUtils.getBusinessId(JsfHelper.getRequest());
        path = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
        HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        this.mp3VirtualUrl = UrlUtil.getMp3VirtualUrlByRequest(req);
        this.mp4VirtualUrl = UrlUtil.getMp4VirtualUrlByRequest(req);
        this.imageVirtualUrl = UrlUtil.getImageVirtualUrlByRequest(req);
        this.docVirtualUrl = UrlUtil.getDocVirtualUrlByRequest(req);
        this.docxVirtualUrl = UrlUtil.getDocxVirtualUrlByRequest(req);
        this.pptVirtualUrl = UrlUtil.getPptVirtualUrlByRequest(req);
        this.pptxVirtualUrl = UrlUtil.getPptxVirtualUrlByRequest(req);
        this.pdfVirtualUrl = UrlUtil.getPdfVirtualUrlByRequest(req);
        this.htmlVirtualUrl = UrlUtil.getHtmlVirtualUrlByRequest(req);
        this.systemConfig = systemConfigDAO.getSystemConfigByBusinessId(this.businessId);
//        this.customerService = this.customerServiceDAO.getCustomerService();
        this.loadFilePath();
        this.loadInfo();
    }

    private void loadInfo() {
        this.info = this.infoDAO.findSystemInfoByBusinessId(this.businessId);

    }

    private void loadPositions() {
        this.positions = this.positionDAO.findAllBbsPosition();

    }

    public void loadFilePath() {
        String dir = "";
        boolean b = systemConfigDAO.getSystemConfig().getIfRelative();
        if (b) {
            String tp = systemConfigDAO.getSystemConfig().getFilePath();
            if (!tp.endsWith("/")) {
                tp = tp + "/";
            }

            dir = FacesContext.getCurrentInstance().getExternalContext().getRealPath(tp);
        } else {
            dir = systemConfigDAO.getSystemConfig().getFilePath();
        }
        if (!dir.endsWith("\\")) {
            dir = dir + "\\";
        }
        this.filePath = dir;
    }

    /**
     * 此方法用于将以秒为单位的时长变换为文本格式，用于在前端页面显示时间长度
     *
     * @param len 时间长度，以秒为单位
     * @return 返回 XX小时XX分XX秒 格式的文本
     */
    public String wrapperTimeLen(long len) {
        String str = "";
        int hour = 0;
        int min = 0;
        int sec = 0;
        hour = (int) len / 3600;
        min = (int) (len % 3600) / 60;
        sec = (int) len % 60;
        if (hour != 0) {
            str += hour + "小时";
        }
        if (min != 0) {
            str += min + "分";
        }
        str += sec + "秒";
        return str;
    }

}
