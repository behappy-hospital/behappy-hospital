package org.xiaowu.behappy.hosp.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.xiaowu.behappy.common.mongo.base.BaseMongoEntity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * Schedule
 * </p>
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "Schedule")
@Document("Schedule")
public class Schedule extends BaseMongoEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "医院编号")
    @Indexed //普通索引
    private String hoscode;

    @Schema(description = "科室编号")
    @Indexed //普通索引
    private String depcode;

    @Schema(description = "职称")
    private String title;

    @Schema(description = "医生名称")
    private String docname;

    @Schema(description = "擅长技能")
    private String skill;

    @Schema(description = "排班日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date workDate;

    @Schema(description = "排班时间（0：上午 1：下午）")
    private Integer workTime;

    @Schema(description = "可预约数")
    private Integer reservedNumber;

    @Schema(description = "剩余预约数")
    private Integer availableNumber;

    @Schema(description = "挂号费")
    private BigDecimal amount;

    @Schema(description = "排班状态（-1：停诊 0：停约 1：可约）")
    private Integer status;

    @Schema(description = "排班编号（医院自己的排班主键）")
    @Indexed //普通索引
    private String hosScheduleId;

}

