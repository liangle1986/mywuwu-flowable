package com.mywuwu.handler;

import org.flowable.engine.delegate.TaskListener;
import org.flowable.task.service.delegate.DelegateTask;

/**
 * @program mywuwu-flowable
 * @description: 老板审批
 * @author: lianglele
 * @create: 2019/06/01 23:03
 */
public class BossTaskHandler implements TaskListener {

    @Override

    public void notify(DelegateTask delegateTask) {

        delegateTask.setAssignee("老板");

    }

}