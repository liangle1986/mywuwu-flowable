package com.mywuwu.service.impl;

import com.mywuwu.service.BpmnModelService;
import org.springframework.stereotype.Service;

/**
 * @program mywuwu-flowable
 * @description: 创建工作流实体文件实现类
 * @author: lianglele
 * @create: 2019/06/02 11:26
 */
@Service
public class BpmnModelServiceImpl implements BpmnModelService {
//    @Autowired
//    private WorkflowMapper workflowMapper;
//
//    @Autowired
//    private WorkflowConditionMapper workflowConditionMapper;
//
//    @Autowired
//    private WorkflowStepMapper workflowStepMapper;
//
//    @Override
//    public BpmnModel bpmnAssembly(Integer workflowId) {
//        try {
//            BpmnModel bpmnModel = new BpmnModel();
//            //获取工作流对象
//            WorkflowInfo workflow = workflowMapper.getByID(workflowId);
//            Process process = new Process();
//            //指定key
//            process.setId("process" + workflowId);
//            process.setName(workflow.getFlowname());
//            //获取活动下所有步骤
//            Map<String, Object> map = new HashMap<>();
//            map.put("flowid", workflowId);
//            List<WorkflowStepInfo> steps = workflowStepMapper.gets(map);
//            Map<Object, WorkflowStepInfo> workflowstep = new HashMap<>();
//            //添加节点到bpmnmodel（排序type asc）
//            for (WorkflowStepInfo w : steps) {
//                if (w.getType() != WorkflowStepTypeEnum.START.toValue()) {
//                    //将步骤存入集合
//                    workflowstep.put(w.getId(), w);
//                }
//                if (w.getType() == WorkflowStepTypeEnum.START.toValue()) {
//                    //开始步骤存入集合
//                    workflowstep.put("start", w);
//                    process.addFlowElement(createStartEvent(w));
//                } else if (w.getType() == WorkflowStepTypeEnum.FINISH.toValue()) {
//                    process.addFlowElement(createEndEvent(w));
//                } else if (w.getType() == WorkflowStepTypeEnum.COUNTERSIGN.toValue()) {
//                    continue;
//                } else {
//                    process.addFlowElement(createUserTask("_" + w.getId(), w.getName(), "", w.getAuditname()));
//                }
//            }
//            //获取开始节点(type=1)
//            WorkflowStepInfo workflowStepInfo = workflowstep.get("start");
//            Assert.notNull(workflowStepInfo, "开始步骤为空");
//            //存入当前开始节点到list
//            List<WorkflowStepInfo> list = new ArrayList<>();
//            list.add(workflowStepInfo);
//            //定义集合记录存入的节点
//            List<WorkflowStepInfo> jilu = new ArrayList<>();
//            long l = System.currentTimeMillis();
//            for (int i = 0; i < steps.size(); i++) {
//                if (steps.get(i).getType() == WorkflowStepTypeEnum.FINISH.toValue()) {
//                    break;
//                }
//                //根据集合中当前节点，获取下一节点，并连线
//                int size = list.size();
//                for (int x = 0; x < size; x++) {
//                    WorkflowStepInfo wf = list.get(0);
//                    String nextstepid = wf.getNextstepid();
//                    if (nextstepid == null) {
//                        list.remove(0);
//                        continue;
//                    }
//                    String[] split = nextstepid.split(",");
//                    for (int j = 0; j < split.length; j++) {
//                        //获取下一节点
//                        WorkflowStepInfo step = workflowstep.get(Integer.parseInt(split[j]));
//                        WorkflowConditionInfo byID = null;
//                        //判断节点是否是会签节点
//                        if (step.getType() == WorkflowStepTypeEnum.COUNTERSIGN.toValue()) {
//                            //如果是会签根据当前节点信息生成
//                            process.addFlowElement(createUserTask1("_" + step.getId(), "huiqian", "", ""));
//                            if (step.getConditionid() != null) {
//                                //获取节点条件，添加
//                                String conditionid = step.getConditionid();
//                                JSONObject jsonObject = JSON.parseObject(conditionid);
//                                Integer integer = (Integer) jsonObject.get(wf.getId());
//                                if (integer == null) {
//                                    process.addFlowElement(createSequenceFlow("_" + wf.getId(), "_" + step.getId(), "_" + wf.getId() + "_" + step.getId() + "ffsdf", "", ""));
//                                } else {
//                                    byID = workflowConditionMapper.getByID(integer);
//                                    //连线
//                                    process.addFlowElement(createSequenceFlow("_" + wf.getId(), "_" + step.getId(), "_" + wf.getId() + "_" + step.getId() + "ffsdf", "", byID.getConditionexpression()));
//                                }
//                            } else {
//                                //无条件连线
//                                process.addFlowElement(createSequenceFlow("_" + wf.getId(), "_" + step.getId(), "_" + wf.getId() + "_" + step.getId() + "ffsdf", "", ""));
//                            }
//                        } else {
//                            //判断节点条件
//                            int i1 = new Random().nextInt(100);
//                            if (step.getConditionid() != null) {
//                                //获取节点条件，添加
//                                String conditionid = step.getConditionid();
//                                JSONObject jsonObject = JSON.parseObject(conditionid);
//                                Integer integer = (Integer) jsonObject.get(wf.getId());
//                                if (integer == null) {
//                                    process.addFlowElement(createSequenceFlow("_" + wf.getId(), "_" + step.getId(), "_" + wf.getId() + "_" + step.getId() + i1, "", ""));
//                                } else {
//                                    byID = workflowConditionMapper.getByID(integer);
//                                    process.addFlowElement(createSequenceFlow("_" + wf.getId(), "_" + step.getId(), "_" + wf.getId() + "_" + step.getId() + i1, "", byID.getConditionexpression()));
//                                }
//                            } else {
//                                //无条件连线
//                                process.addFlowElement(createSequenceFlow("_" + wf.getId(), "_" + step.getId(), "_" + wf.getId() + "_" + step.getId() + i1, "", ""));
//                            }
//                        }
//                        //重复节点，过滤掉
//                        if (!jilu.contains(step)) {
//                            list.add(step);
//                        }
//                        jilu.add(step);
//                    }
//                    list.remove(wf);
//                }
//            }
//            long l1 = System.currentTimeMillis();
//            System.out.println("用时时间是" + (l1 - l));
//            bpmnModel.addProcess(process);
//            return bpmnModel;
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new RuntimeException("流程模型添加失败");
//        }
//    }
}
