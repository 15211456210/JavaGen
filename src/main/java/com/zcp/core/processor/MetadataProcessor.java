package com.zcp.core.processor;

import com.zcp.core.engine.Context;
import com.zcp.core.engine.metas.MetaData;
import lombok.Data;

/**
 * @author ZCP
 * @title: MetadataProcessor
 * @projectName JavaGen
 * @description: 元数据处理器
 * @date 2023/1/22 17:53
 */
@Data
public abstract class MetadataProcessor implements IProcessor {

    public MetadataProcessor(Context context) {
        this.context = context;
    }

    /**
     * 上下文
     */
    private Context context;

    /**
     * 下一个处理器
     */
    private MetadataProcessor nextProcessor;

    public MetadataProcessor(Context context, MetadataProcessor nextProcessor) {
        this.context = context;
        this.nextProcessor = nextProcessor;
    }

    public MetadataProcessor(MetadataProcessor nextProcessor) {
        this.nextProcessor = nextProcessor;
    }

    @Override
    public void process(MetaData metaData) {
        if (nextProcessor != null){
            nextProcessor.process(metaData);
        }
    }
}
