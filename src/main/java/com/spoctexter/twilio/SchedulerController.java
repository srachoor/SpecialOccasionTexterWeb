package com.spoctexter.twilio;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.ScheduledAnnotationBeanPostProcessor;
import org.springframework.scheduling.config.ScheduledTask;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;

@RestController
@RequestMapping("/scheduler")
public class SchedulerController {
    private static final String SCHEDULED_TASKS = "scheduledTasks";

    private ScheduledAnnotationBeanPostProcessor postProcessor;

    @Autowired
    public SchedulerController(ScheduledAnnotationBeanPostProcessor postProcessor){
        this.postProcessor = postProcessor;
    }

    @GetMapping(value = "/stop")
    public String stopSchedule(SmsScheduler smsScheduler) {
        postProcessor.postProcessBeforeDestruction(smsScheduler, SCHEDULED_TASKS);
        return "OK";
    }

    @GetMapping(value = "/start")
    public String startSchedule(SmsScheduler smsScheduler) {
        postProcessor.postProcessAfterInitialization(smsScheduler, SCHEDULED_TASKS);
        return "OK";
    }

    @GetMapping(value = "/list")
    public String listSchedules() throws JsonProcessingException {
        Set<ScheduledTask> setTasks = postProcessor.getScheduledTasks();
        if (!setTasks.isEmpty()) {
            return setTasks.toString();
        } else {
            return "Currently no scheduler tasks are running";
        }
    }
}
