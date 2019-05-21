package com.crk.elasticsearch;

import com.crk.elasticsearch.domain.ResultBean;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.client.Requests;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.*;

/**
 * @Auther: Administrator
 * @Date: 2018/8/21 07
 * @Description:
 */
@Api(value = "Index", tags = "索引")
@RestController
@RequestMapping("index")
public class IndexController {

    private final String INDEX = "index";
    private final String TYPE = "type";
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private TransportClient transportClient;


    @ApiOperation(value = "结构化创建索引")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "index", value = "索引名", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "type", value = "类型", required = true, dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "fields", value = "结构化索引字段名,不定参数，传入的时候参数名为索引字段名，值为对应的数据类型")
    })
    @RequestMapping(value = "/create" , method = RequestMethod.POST)
    public ResultBean createIndex(@RequestParam Map<String,String> param){
        ResultBean resultBean = new ResultBean();
        String index = null;
        String type = null;
        List<String> fieldList = new ArrayList<>();
        logger.info("接收的创建索引的参数：" + param);
        Set<Map.Entry<String, String>> set = param.entrySet();
        for (Map.Entry<String, String> entry: set) {
            String key = entry.getKey();
            if(key.trim().equals(INDEX)){
                index = entry.getValue();
            }else if(key.trim().equals(TYPE)){
                type = entry.getValue();
            }else{
                fieldList.add(key);
            }
        }
        if(StringUtils.isBlank(index) || StringUtils.isBlank(type)){
            resultBean.setSuccess(false);
            resultBean.setMsg("参数错误！");
            return resultBean;
        }
        try {
            XContentBuilder settings = XContentFactory.jsonBuilder()
                    .startObject()
                        .field("number_of_shards",6)
                        .field("number_of_replicas",1)
                        .startObject("analysis").startObject("analyzer").startObject("ik")
                            .field("tokenizer","ik_max_word")
                        .endObject().endObject().endObject()
                    .endObject();
            XContentBuilder mapping = XContentFactory.jsonBuilder();
            mapping.startObject().field("dynamic","strict").startObject("properties");
            for (int i = 0,j = fieldList.size(); i < j; i++) {
                String field = fieldList.get(i);
                String fieldType = param.get(field);
                mapping.startObject(field).field("type",fieldType);
                if(fieldType.trim().equals("date")){
                    mapping.field("format","yyyy-MM-dd HH:mm:ss || yyyy-MM-dd ");
                }
                mapping.endObject();
            }
            mapping.endObject().endObject();
            CreateIndexRequest createIndexRequest = Requests.createIndexRequest(index).settings(settings).mapping(type,mapping);
            CreateIndexResponse response = transportClient.admin().indices().create(createIndexRequest).get();
            logger.info("建立索引映射成功：" + response.isAcknowledged());
            resultBean.setSuccess(true);
            resultBean.setMsg("创建索引成功！");
        } catch (Exception e) {
            resultBean.setSuccess(false);
            resultBean.setMsg("创建索引失败！");
            logger.error("创建索引失败！要创建的索引为{}，文档类型为{}，异常为：",index,type,e.getMessage(),e);
        }
        return resultBean;
    }

    @ApiOperation(value = "删除索引")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "index", value = "索引名", required = true, dataType = "String", paramType = "query"),
    })
    @RequestMapping(value = "/delete" , method = RequestMethod.POST)
    public ResultBean deleteIndex(String index){
        ResultBean resultBean = new ResultBean();
        if (StringUtils.isBlank(index)) {
            resultBean.setMsg("参数错误，索引为空");
            resultBean.setSuccess(false);
            return resultBean;
        }
        try {
            DeleteIndexRequest deleteIndexRequest = Requests.deleteIndexRequest(index);
            DeleteIndexResponse response = transportClient.admin().indices().delete(deleteIndexRequest).get();
            logger.info("删除索引结果:{}",response.isAcknowledged());
            resultBean.setSuccess(response.isAcknowledged());
            resultBean.setMsg(response.isAcknowledged() ? "删除索引成功！" : "删除索引失败！");
        } catch (Exception e) {
            resultBean.setSuccess(false);
            resultBean.setMsg("创建索引失败！");
            logger.error("删除索引失败！要删除的索引为{}，异常为：",index,e.getMessage(),e);
        }
        return resultBean;
    }
    
}