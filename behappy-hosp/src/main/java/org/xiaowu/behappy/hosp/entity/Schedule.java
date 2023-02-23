package org.xiaowu.behappy.hosp.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel(description = "Schedule")
@Document("Schedule")
public class Schedule extends BaseMongoEntity {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "医院编号")
	@Indexed //普通索引
	private String hoscode;

	@ApiModelProperty(value = "科室编号")
	@Indexed //普通索引
	private String depcode;

	@ApiModelProperty(value = "职称")
	private String title;

	@ApiModelProperty(value = "医生名称")
	private String docname;

	@ApiModelProperty(value = "擅长技能")
	private String skill;

	@ApiModelProperty(value = "排班日期")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date workDate;

	@ApiModelProperty(value = "排班时间（0：上午 1：下午）")
	private Integer workTime;

	@ApiModelProperty(value = "可预约数")
	private Integer reservedNumber;

	@ApiModelProperty(value = "剩余预约数")
	private Integer availableNumber;

	@ApiModelProperty(value = "挂号费")
	private BigDecimal amount;

	@ApiModelProperty(value = "排班状态（-1：停诊 0：停约 1：可约）")
	private Integer status;

	@ApiModelProperty(value = "排班编号（医院自己的排班主键）")
	@Indexed //普通索引
	private String hosScheduleId;

}

