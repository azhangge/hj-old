package com.huajie.exam.agent;

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

import com.huajie.util.ExternalUserUtil;

/**
 *
 * @author huajie.com
 */
public class ExternalUserAgent implements Job {

    private static Scheduler scheduler = null;

    public static void buildSchedule() {
        try {
            scheduler = new StdSchedulerFactory().getScheduler();
            scheduler.clear();

            JobDetail jd = JobBuilder.newJob(ExternalUserAgent.class).withIdentity("ExternalUserAgent", Scheduler.DEFAULT_GROUP).build();
            Trigger trigger = TriggerBuilder.newTrigger().withIdentity("externalTrigger11", Scheduler.DEFAULT_GROUP).withSchedule(
                    CronScheduleBuilder.cronSchedule("0 0 4 * * ?")).build();//每天4点执行一次
            scheduler.start();
            scheduler.scheduleJob(jd, trigger);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void destroySchedule() {

        try {
            scheduler.shutdown();
            scheduler = null;
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void execute(JobExecutionContext arg0) throws JobExecutionException {
        try {
            // TODO Auto-generated method stub
            boolean ok = ExternalUserUtil.downloadExternalFile();
            if (ok) {
                ExternalUserUtil.readExternalUsers();
            }
            ExternalUserUtil.deleteObsoletedExamCase();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String args[]) {

        ExternalUserAgent.buildSchedule();
    }

}
