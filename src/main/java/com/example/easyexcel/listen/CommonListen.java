package com.example.easyexcel.listen;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.example.easyexcel.service.EasyExcelService;
import com.example.easyexcel.service.impl.EasyExcelServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private static final int BATCH_COUNT = 100;

    /**
     * 模板对应的实体类
     */
    private Class<T> tClass;

    /**
     * 调用的方法名
     */
    private String methodName;

    List<Object> list = new ArrayList<>();

    private EasyExcelService easyExcelService;

    /**
     * 如果使用了spring来管理，请使用此有参构造方法。
     * 每次创建Listener的时候需要把spring管理的类传进来
     * @param tClass
     * @param methodName
     */
    public CommonListen(Class<T> tClass, String methodName) {
        this.tClass = tClass;
        this.methodName = methodName;
        this.easyExcelService = new EasyExcelServiceImpl();
    }

    /**
     * 每一条excel的数据解析都会调用此方法
     * @param t
     * @param analysisContext
     */
    @Override
    public void invoke(T t, AnalysisContext analysisContext) {
        logger.debug("");
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}
