package com.example.easyexcel.listen;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.read.metadata.holder.ReadRowHolder;
import com.example.easyexcel.entity.excel.CommonExcel;
import com.example.easyexcel.module.ImportResult;
import com.example.easyexcel.module.ImportTips;
import com.example.easyexcel.service.EasyExcelService;
import com.example.easyexcel.util.SpringContextUtil;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @Author hezhan
 * @Date 2020/6/11 11:04
 * 通用excel监听类，不能被spring管理
 */
@Getter
@Setter
public class CommonListener<T extends CommonExcel> extends AnalysisEventListener<T> {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 解析excel表格数据，分批存储到数据库，这里定义每批为几条数据，
     * 每批存储到数据库后，都会清理list，方便内存回收
     */
    private static final int BATCH_COUNT = 1000;

    /**
     * excel实体类，用来校验上传的excel是否是正确的模板excel
     */
    private T entity;

    /**
     * 校验是否是正确的模板
     */
    private boolean templateFlag = true;

    /**
     * 上传后返回的数据
     */
    private ImportResult importResult = new ImportResult();

    /**
     * 成功的总条数
     */
    private AtomicInteger successTotal = new AtomicInteger();

    /**
     * 失败的总条数
     */
    private AtomicInteger failTotal = new AtomicInteger();

    /**
     * 总条数
     */
    private AtomicInteger total = new AtomicInteger();

    /**
     * 存放错误提示信息
     */
    private List<ImportTips> tips = new ArrayList<>();

    /**
     * 临时存放错误提示，之后相同的序号要组装起来到tips里
     */
    private List<ImportTips> tempTips = new ArrayList<>();

    /**
     * 调用的方法名
     */
    private String methodName;

    /**
     * 要保存的数据
     */
    List<T> list = new ArrayList<>();

    /**
     * 读取excel表的数据后需要调用的业务对象，例如通过这对象将数据存到数据库中
     */
    private final EasyExcelService easyExcelService = SpringContextUtil.getBean(EasyExcelService.class);

    /**
     * 传入导入后要保存到数据库的方法名以及excel实体类对象
     *
     * @param methodName 保存到数据库要调用的方法名
     * @param t          导入的实体类对象
     */
    public CommonListener(String methodName, T t) {
        this.methodName = methodName;
        entity = t;
    }

    /**
     * 校验导入的excel表头，是否是指定模板
     *
     * @param headMap 导入的excel表头map
     * @param context 上下文阅读器
     */
    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        // 获取数据实体的字段列表
        Field[] fields = entity.getClass().getFields();
        for (Field field : fields) {
            // 获取实体类字段上的ExcelProperty注解，如果有此注解，则是模板的表头字段
            ExcelProperty excelProperty = field.getAnnotation(ExcelProperty.class);
            if (excelProperty != null){
                // 根据实体类注解的下标从导入的表头中获取对应的值
                String headName = headMap.get(excelProperty.index());
                if (StringUtils.isBlank(headName) || !excelProperty.value()[0].equals(headName)){
                    // 如果表头为空，或者名称对应不上实体类中标明的值，则不是正确的模板
                    templateFlag = false;
                    importResult.setErrorMessage("请使用正确的导入模板");
                    break;
                }
            }
        }
    }

    /**
     * 每一条excel的数据解析都会调用此方法
     *
     * @param t 当前从excel中读取到的数据
     * @param context 上下文阅读器
     */
    @Override
    public void invoke(T t, AnalysisContext context) {
        if (templateFlag){
            logger.debug("开始处理数据");
            // 获取行号
            ReadRowHolder readRowHolder = context.readRowHolder();
            Integer rowIndex = readRowHolder.getRowIndex();
            t.setNum(String.valueOf(rowIndex));
            list.add(t);
            total.incrementAndGet();// 导入的总数+1
            // 达到设定的一批处理数据上限后，就存一次数据库，防止OOM
            if (list.size() >= BATCH_COUNT) {
                checkData();
                saveData();
                // 保存到数据库后将这一批数据清空
                list.clear();
            }
        }
    }

    /**
     * 在读取完excel后会调用此方法
     * @param context 上下文阅读器
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        if (templateFlag){
            checkData();
            // 这里也要保存数据，确保最后遗留的数据也存储到数据库
            saveData();
            // 最后组装下最终的提示数据
            // 根据序号升序排列
            if (!CollectionUtils.isEmpty(tips)) {
                tips.sort((t1, t2) -> {
                    Integer num1 = Integer.valueOf(t1.getNum());
                    Integer num2 = Integer.parseInt(t2.getNum());
                    return num1.compareTo(num2);
                });
            }
            importResult.setTips(tips);
            importResult.setTotal(String.valueOf(total.get()));
            importResult.setSuccessTotal(String.valueOf(successTotal.get()));
            importResult.setFailTotal(String.valueOf(failTotal.get()));
            logger.debug("所有数据解析保存完毕");
        }
    }

    /**
     * 校验数据
     */
    private void checkData() {
        /*
        这里是校验数据的逻辑，现在先模拟第一条失败
         */
        List<String> errorNumList = new ArrayList<>();
        errorNumList.add("1");
        buildTips(errorNumList, "错误原因");

        // 最后相同的序号组装在一起
        if (!CollectionUtils.isEmpty(tempTips)) {
            Map<String, List<ImportTips>> groupMap = tempTips.stream().collect(Collectors.groupingBy(ImportTips::getNum, LinkedHashMap::new, Collectors.toList()));
            for (Map.Entry<String, List<ImportTips>> entry : groupMap.entrySet()) {
                ImportTips tip = new ImportTips();
                tip.setNum(entry.getKey());
                List<ImportTips> messages = entry.getValue();
                StringBuilder s = new StringBuilder();
                messages.forEach(m -> {
                    s.append(m.getMessage());
                });
                s.deleteCharAt(s.length() - 1);
                tip.setMessage(s.toString());
                tips.add(tip);
            }
            // 校验后，要去掉校验失败的数据，将剩下的数据导入
            if (!CollectionUtils.isEmpty(tips)) {
                List<String> nums = tips.stream().map(ImportTips::getNum).collect(Collectors.toList());
                list.removeIf(user -> nums.contains(user.getNum()));
                failTotal.addAndGet(nums.size());// 失败的条数加起来
            }
        }
    }

    /**
     * 构建临时的校验错误提示，后续会组装成完成的错误提示
     *
     * @param removeList 要生成的错误提示的行号集合
     * @param message    错误原因
     */
    private void buildTips(List<String> removeList, String message) {
        if (CollectionUtils.isEmpty(removeList)) {
            return;
        }
        List<String> accountTips = removeList.stream().distinct().collect(Collectors.toList());
        accountTips.forEach(r -> {
            ImportTips tip = new ImportTips();
            tip.setNum(r);
            tip.setMessage(message);
            tempTips.add(tip);
        });
    }

    /**
     * 保存数据
     */
    private void saveData() {
        try {
            Method save = easyExcelService.getClass().getMethod(methodName, List.class, int.class);
            save.invoke(easyExcelService, list, BATCH_COUNT);
            successTotal.addAndGet(list.size());// 加上成功的条数
        } catch (Exception e) {
            logger.error("解析excel表数据，调用" + methodName + "方法保存数据到数据库时报错--->", e);            // 如果失败则加上失败的条数，还要返回失败原因
            list.forEach(l -> {
                ImportTips tip = new ImportTips();
                tip.setNum(l.getNum());
                tip.setMessage("保存到数据库时发生错误;");
            });
            failTotal.addAndGet(list.size());// 失败的条数加起来
        }

    }
}
