package com.mywuwu.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.engine.*;
import org.flowable.engine.runtime.Execution;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.image.ProcessDiagramGenerator;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * @program mywuwu-flowable
 * @description: 工作流控制器
 * @author: lianglele
 * @create: 2019/06/01 23:04
 */
@RestController
@CrossOrigin
@Api(tags = "ExpenseController", description = "ExpenseController | 工作流项目")
@RequestMapping(value = "expense")
public class ExpenseController {

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired

    private RepositoryService repositoryService;

    @Autowired
    private ProcessEngine processEngine;

    @Autowired
    private IdentityService identityService;

    /**
     * 生成当前流程图
     *
     * @param httpServletResponse
     * @param processId
     */
    @ApiOperation(value = "生成当前流程图", notes = "生成当前流程图")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "httpServletResponse", value = "回调对象", required = true, dataType = "HttpServletResponse"),
            @ApiImplicitParam(name = "processId", value = "流程标示", required = true, dataType = "String")
    })
    @GetMapping("processDiagram")

    public void genProcessDiagram(HttpServletResponse httpServletResponse, String processId) {

        try {

            ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processId).singleResult();

            if (StringUtils.isEmpty(processInstance)) {

                return;

            }

            Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();

            String instanceId = task.getProcessInstanceId();

            List<Execution> executionList = runtimeService.createExecutionQuery().processInstanceId(instanceId).list();

            List<String> activityIdList = new ArrayList<>();

            List<String> flowList = new ArrayList<>();

            for (Execution execution : executionList) {

                List<String> idList = runtimeService.getActiveActivityIds(execution.getId());

                activityIdList.addAll(idList);

            }

            BpmnModel bpmnModel = repositoryService.getBpmnModel(processInstance.getProcessDefinitionId());

            ProcessEngineConfiguration processEngineConfiguration = processEngine.getProcessEngineConfiguration();

            ProcessDiagramGenerator processDiagramGenerator = processEngineConfiguration.getProcessDiagramGenerator();

            InputStream inputStream = processDiagramGenerator.generateDiagram(bpmnModel, "png", activityIdList, flowList,

                    processEngineConfiguration.getActivityFontName(),

                    processEngineConfiguration.getLabelFontName(),

                    processEngineConfiguration.getAnnotationFontName(),

                    processEngineConfiguration.getClassLoader(),

                    1.0,true);

            OutputStream out = null;

            byte[] buf = new byte[1024];

            int legth = 0;

            try {

                out = httpServletResponse.getOutputStream();

                while ((legth = inputStream.read(buf)) != -1) {

                    out.write(buf, 0, legth);

                }

            } finally {

                if (inputStream != null) {

                    inputStream.close();

                }

                if (out != null) {

                    out.close();

                }

            }

        } catch (Exception e) {

            System.out.println(e.getMessage());

        }

    }

    /**
     * 审核-拒绝
     *
     * @param taskId
     * @return
     */

    @ApiOperation(value = "审核-拒绝", notes = "审核-拒绝")
    @ApiImplicitParam(name = "taskId", value = "任务标示", required = true, dataType = "String")
    @PostMapping(value = "reject")
    @ResponseBody
    public String reject(String taskId) {

        HashMap<String, Object> map = new HashMap<>();

        map.put("outcome", "驳回");

        taskService.complete(taskId, map);


        return "reject";

    }

    /**
     * 审核-通过
     *
     * @param taskId
     * @return
     */
    @ApiOperation(value = "审核-通过", notes = "审核-通过")
    @ApiImplicitParam(name = "taskId", value = "任务标示", required = true, dataType = "String")
    @PostMapping(value = "apply")

    @ResponseBody

    public String apply(String taskId) {

        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();

        if (StringUtils.isEmpty(task)) {

            throw new RuntimeException("流程不存在");

        }

        HashMap<String, Object> map = new HashMap<>();

        map.put("outcome", "通过");

        taskService.complete(taskId, map);

        return "processed ok";

    }

    /**
     * 获取审批列表
     *
     * @param userId
     * @return
     */
    @ApiOperation(value = "获取审批列表", notes = "获取审批列表")
    @ApiImplicitParam(name = "userId", value = "用户标示", required = true, dataType = "String")

    @GetMapping(value = "list")

    @ResponseBody

    public Object list(String userId) {

        String temp = "";

        List<Task> taskList = taskService.createTaskQuery().taskAssignee(userId).orderByTaskCreateTime().desc().list();

        for (Task task : taskList) {

            temp += task.toString();

            System.out.println(task.toString());

        }

        return temp;

    }

    /**
     * 开始报销流程
     *
     * @param userId      用户ID
     * @param money       报销金额
     * @param description 描述
     * @return
     */
    @ApiOperation(value = "开始报销流程", notes = "开始报销流程")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户标示", required = true, dataType = "String"),
            @ApiImplicitParam(name = "money", value = "金额", required = true, dataType = "String"),
            @ApiImplicitParam(name = "description", value = "描述", required = true, dataType = "String")
    })
    @PostMapping(value = "add")

    @ResponseBody

    public String addExpense(String userId, Integer money, String description) {

        HashMap<String, Object> map = new HashMap<>();

        map.put("taskUser", userId);

        map.put("money", money);

        map.put("description", description);

        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("Expense", map);

        return "提交成功，流程ID为" + processInstance.getId();

    }

}