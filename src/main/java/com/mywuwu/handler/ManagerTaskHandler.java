package com.mywuwu.handler;

import org.flowable.engine.delegate.TaskListener;
import org.flowable.task.service.delegate.DelegateTask;

/**
 * @program mywuwu-flowable
 * @description: 审批监听
 * @author: lianglele
 * @create: 2019/06/01 23:01
 */
public class ManagerTaskHandler implements TaskListener {

    @Override

    public void notify(DelegateTask delegateTask) {

        delegateTask.setAssignee("经理");

    }

}