package com.example.easyexcel.listen;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.example.easyexcel.exception.CustomException;
import com.example.easyexcel.service.EasyExcelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author hezhan
 * @Date 2020/6/11 11:04
 * 通用excel监听类，不能被spring管理
 */
public class CommonListen<T> extends AnalysisEventListener<T> {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 解析excel表格数据，分批存储到数据库，这里定义每批为几条数据，
     * 每批存储到数据库后，都会清理list，方便内存回收
     */
    private static final int BATCH_COUNT = 1000;

    /**
     * 调用的方法名
     */
    private String methodName;

    List<Object> list = new ArrayList<>();

    /**
     * 读取excel表的数据后需要调用的业务对象，例如通过这对象将数据存到数据库中
     */
    private EasyExcelService easyExcelService;

    /**
     * 如果使用了spring来管理，请使用此有参构造方法。
     * 每次创建Listener的时候需要把spring管理的类传进来
     * @param easyExcelService spring管理的类
     * @param methodName 要调用的方法名
     */
    public CommonListen(EasyExcelService easyExcelService, String methodName) {
        this.methodName = methodName;
        this.easyExcelService = easyExcelService;
    }

    /**
     * 每一条excel的数据解析都会调用此方法
     * @param t
     * @param analysisContext
     */
    @Override
    public void invoke(T t, AnalysisContext analysisContext) {
        logger.debug("开始处理数据");
        list.add(t);
        // 达到设定的一批处理数据上限后，就存一次数据库，防止OOM
        if (list.size() >= BATCH_COUNT){
            saveData();
            // 保存到数据库后将这一批数据清空
            list.clear();
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        // 这里也要保存数据，确保最后遗留的数据也存储到数据库
        saveData();
        logger.debug("所有数据解析保存完毕");
    }

    private void saveData(){
        try {
            Method save = easyExcelService.getClass().getMethod(methodName, List.class, int.class);
            save.invoke(easyExcelService, list, BATCH_COUNT);
        } catch (Exception e) {
            throw new CustomException("解析excel表数据，调用" + methodName + "方法保存数据到数据库时报错，原因为：" + e.getCause());
        }

    }
}
