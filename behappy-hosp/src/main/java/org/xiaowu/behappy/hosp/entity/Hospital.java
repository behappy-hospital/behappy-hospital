package org.xiaowu.behappy.hosp.entity;

import com.alibaba.fastjson2.JSONObject;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.xiaowu.behappy.common.mongo.base.BaseMongoEntity;

/**
 * <p>
 * Hospital
 * </p>
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "Hospital")
@Document("Hospital")
public class Hospital extends BaseMongoEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "医院编号")
    @Indexed(unique = true) //唯一索引
    private String hoscode;

    @Schema(description = "医院名称")
    @Indexed //普通索引
    private String hosname;

    @Schema(description = "医院类型")
    private String hostype;

    @Schema(description = "省code")
    private String provinceCode;

    @Schema(description = "市code")
    private String cityCode;

    @Schema(description = "区code")
    private String districtCode;

    @Schema(description = "详情地址")
    private String address;

    @Schema(description = "医院logo")
    private String logoData;

    @Schema(description = "医院简介")
    private String intro;

    @Schema(description = "坐车路线")
    private String route;

    @Schema(description = "状态 0：未上线 1：已上线")
    private Integer status;

    //预约规则
    @Schema(description = "预约规则")
    private BookingRule bookingRule;

    /**
     * Map转换为Hospital对象时，预约规则bookingRule为一个对象属性，rule为一个数组属性，因此在转换时我们要重新对应的set方法，不然转换不会成功
     * @param bookingRule
     */
    public void setBookingRule(String bookingRule) {
        this.bookingRule = JSONObject.parseObject(bookingRule, BookingRule.class);
    }

}

