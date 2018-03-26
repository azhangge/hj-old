/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.huajie.exam.agent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.quartz.CronScheduleBuilder;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

import com.hjedu.examination.dao.IExamContestDAO;
import com.hjedu.examination.entity.contest.ContestCase;
import com.hjedu.examination.entity.contest.ContestCaseStub;
import com.hjedu.examination.entity.contest.ContestSessionCase;
import com.hjedu.examination.entity.contest.ExamContestSession;
import com.hjedu.examination.entity.random2.ExamPaperRandom2;
import com.hjedu.examination.service.impl.ContestSessionService;
import com.hjedu.examination.service.impl.IContestCaseRandom2Service;
import com.huajie.util.SpringHelper;

/**
 *
 * @author huajie.com
 */
//本类为竞赛的具体调度操作，竞赛的动态显示、隐藏、奖励全部由本类具体调用
public class ContestAgent implements Job {
    
    private static Scheduler scheduler = null;
    private static List<ExamContestSession> contests = Collections.synchronizedList(new ArrayList());
    //每一个竞赛在当期都有一个试题存根，以保证当期所有用户的试题是一样的
    private static Map<String, ContestCaseStub> stubs = Collections.synchronizedMap(new HashMap());
    private static final IContestCaseRandom2Service contestCaseService = SpringHelper.getSpringBean("ContestCaseRandom2Service");
    private static final ContestSessionService sessionService = SpringHelper.getSpringBean("ContestSessionService");
    
    public static Map<String, ContestCaseStub> getStubs() {
        return stubs;
    }
    
    public static void setStubs(Map<String, ContestCaseStub> stubs) {
        ContestAgent.stubs = stubs;
    }
    
    public static List<ExamContestSession> getContests() {
        return contests;
    }
    
    public static void setContests(List<ExamContestSession> contests) {
        ContestAgent.contests = contests;
    }

    //构建定时任务，具体执行在execute中
    public static void buildSchedule() {
        initContestList();//第一次初始化，通常发生在系统启动后
        try {
            scheduler = new StdSchedulerFactory().getScheduler();
            scheduler.clear();
            
            JobDetail jd = JobBuilder.newJob(ContestAgent.class).withIdentity("ContestAgent", Scheduler.DEFAULT_GROUP).build();
            Trigger trigger = TriggerBuilder.newTrigger().withIdentity("contestTrigger11", Scheduler.DEFAULT_GROUP).withSchedule(
                    CronScheduleBuilder.cronSchedule("5/5 * * * * ?")).build();// 5秒后每5秒检查一次
            scheduler.start();
            scheduler.scheduleJob(jd, trigger);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void destroySchedule() {
        
        try {
        	if(scheduler!=null){
        		scheduler.shutdown();
        		scheduler = null;
        	}
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }

    //执行定时任务
    public void execute(JobExecutionContext arg0) throws JobExecutionException {
        try {
            // TODO Auto-generated method stub
            this.synContest();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void initContestList() {
        IExamContestDAO contestDAO = SpringHelper.getSpringBean("ExamContestDAO");
        List<ExamContestSession> csts = contestDAO.findAllShowedExamContest();
        contests.clear();
        contests.addAll(csts);
    }
    
    public static void refreshContestList(ExamContestSession ecs) {
        for (ExamContestSession ecst : contests) {
            if (ecst.getId().equals(ecs.getId())) {
                contests.remove(ecst);
                contests.add(ecs);
                System.out.println("A contest refreshed.");
                break;
            }
        }
    }

    //同步移除竞赛
    public static void removeContestList(String id) {
        for (ExamContestSession ecst : contests) {
            if (ecst.getId().equals(id)) {
                contests.remove(ecst);
                break;
            }
        }
    }

    //加入内存的新竞赛在5秒内会被处理（）
    public static void addContest(ExamContestSession ecs) {
        contests.add(ecs);
    }

    //check all of the contests.
    //竞赛同步的具体操作，由execute调用
    public void synContest() {
        for (ExamContestSession ecs : contests) {
            //System.out.println("Contest:" + ecs.getName() + ",begain:" + ecs.getBegainTime() + ",end time:" + ecs.getEndTime());
            ecs.setIfToday(computeIfToday(ecs));//检查是否是今天的考试
            boolean result = this.computeIfAvailable(ecs);//检查现在是否可用
            if (result) {
                if (!ecs.isAvailable()) {//主要用于之前是非可用状态，但当前时间进入了可用状态的主机
                    String str = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                    ecs.setSessionStr(str);
                    ExamPaperRandom2 p = ecs.getRandom2Paper();
                    if (p != null) {
                        bulidStub(p);//构建一个竞赛的试题存根
                    }
                    ecs.setAvailable(true);
                    sessionService.begainContestSession(ecs);
                }
            } else {//主要用于设置当前不可用的竞赛
                ecs.setAvailable(false);
                sessionService.endAllContestSessionByContest(ecs);
            }
        }
    }
    
    public static void refreshStub(ExamPaperRandom2 paper) {
        bulidStub(paper);
    }
    
    private static void bulidStub(ExamPaperRandom2 paper) {
        ContestCaseStub stub = contestCaseService.buildExamCaseStub(paper);
        //System.out.println("stub:" + stub.getChoices().size() + ":" + stub.getMchoices().size());
        stubs.remove(paper.getId());
        stubs.put(paper.getId(), stub);
    }

    //从试题存根构建一个新的考试实例，并装载此考试实例对应的竞赛会话实例
    public static ContestCase buildContestCase(ContestCase cc) {
        ContestCaseStub stub = stubs.get(cc.getExamination().getRandom2Id());
        if (stub == null) {
            stub = contestCaseService.buildExamCaseStub(cc.getExamination().getRandom2Paper());
        }
        contestCaseService.buildExamCaseFromStub(cc, stub);
        loadContestSession(cc);
        return cc;
    }
    
    //此方法用于为考试实例加载对应的竞赛会话实例及其对应的sessionId、sessionStr
    public static void loadContestSession(ContestCase cc) {
        if (cc != null) {
            ExamContestSession contest = cc.getExamination();
            if (contest != null) {
                String cid = contest.getId();
                for (ExamContestSession c : contests) {//从当前内存中的所有竞赛中查找
                    if (cid.equals(c.getId())) {
                        cc.setSessionStr(c.getSessionStr());
                        ContestSessionCase csc = c.getSessionCase();
                        if (csc != null) {
                            cc.setSessionId(csc.getId());
                        }
                        break;
                    }
                }
            }
        }
    }

    //check if the time is in the avilable time section.
    public boolean computeIfAvailable(ExamContestSession ecs) {
        //System.out.println("checking contest.");

        boolean inDay = false;
        int day = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        if (day == Calendar.SUNDAY) {
            inDay = ecs.isSun();
        } else if (day == Calendar.MONDAY) {
            inDay = ecs.isMon();
        } else if (day == Calendar.TUESDAY) {
            inDay = ecs.isTues();
        } else if (day == Calendar.WEDNESDAY) {
            inDay = ecs.isWed();
        } else if (day == Calendar.THURSDAY) {
            inDay = ecs.isThur();
        } else if (day == Calendar.FRIDAY) {
            inDay = ecs.isFri();
        } else if (day == Calendar.SATURDAY) {
            inDay = ecs.isSat();
        }

        //System.out.println(day + ":" + inDay);
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c1.setTime(ecs.getBegainTime());
        c2.setTime(ecs.getEndTime());
        
        boolean inHour = false;
        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        int hour1 = c1.get(Calendar.HOUR_OF_DAY);
        int hour2 = c2.get(Calendar.HOUR_OF_DAY);
        //System.out.println(hour + ":" + hour1 + ":" + hour2);
        if (hour > hour1 && hour < hour2) {
            inHour = true;
        } else if (hour == hour1 && hour < hour2) {
            int m = Calendar.getInstance().get(Calendar.MINUTE);
            int m1 = c1.get(Calendar.MINUTE);
            if (m >= m1) {
                inHour = true;
            }
        } else if (hour1 < hour && hour == hour2) {
            int m = Calendar.getInstance().get(Calendar.MINUTE);
            int m2 = c2.get(Calendar.MINUTE);
            if (m <= m2) {
                inHour = true;
            }
        } else if (hour1 < hour && hour == hour2) {
            int m = Calendar.getInstance().get(Calendar.MINUTE);
            int m1 = c1.get(Calendar.MINUTE);
            int m2 = c2.get(Calendar.MINUTE);
            if (m <= m2 && m >= m1) {
                inHour = true;
            }
        }

        //System.out.println( "In hour:" + inHour);
        //System.out.println("check result:" + (inDay && inHour));
        return inDay && inHour;
        
    }
    
    public boolean computeIfToday(ExamContestSession ecs) {
        //System.out.println("checking contest.");

        boolean inDay = false;
        int day = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        if (day == Calendar.SUNDAY) {
            if (ecs.isSun()) {
                inDay = true;
            }
        } else if (day == Calendar.MONDAY) {
            if (ecs.isMon()) {
                inDay = true;
            }
        } else if (day == Calendar.TUESDAY) {
            if (ecs.isTues()) {
                inDay = true;
            }
        } else if (day == Calendar.WEDNESDAY) {
            if (ecs.isWed()) {
                inDay = true;
            }
        } else if (day == Calendar.THURSDAY) {
            if (ecs.isThur()) {
                inDay = true;
            }
        } else if (day == Calendar.FRIDAY) {
            if (ecs.isFri()) {
                inDay = true;
            }
        } else if (day == Calendar.SATURDAY) {
            if (ecs.isSat()) {
                inDay = true;
            }
        }
        return inDay;
    }
    
    public static void main(String args[]) {
        
        ContestAgent.buildSchedule();
    }
    
}
