package org.xiaowu.behappy.manager.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * Schedule
 * </p>
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "Schedule")
@TableName("schedule")
public class Schedule extends BaseNoAutoEntity {

	private static final long serialVersionUID = 1L;

	@Schema(description = "医院编号")
	@TableField("hoscode")
	private String hoscode;

	@Schema(description = "科室编号")
	@TableField("depcode")
	private String depcode;

	@Schema(description = "职称")
	@TableField("title")
	private String title;

	@Schema(description = "医生名称")
	@TableField("docname")
	private String docname;

	@Schema(description = "擅长技能")
	@TableField("skill")
	private String skill;

	@Schema(description = "安排日期")
	@TableField("work_date")
	private String workDate;

	@Schema(description = "安排时间（0：上午 1：下午）")
	@TableField("work_time")
	private Integer workTime;

	@Schema(description = "可预约数")
	@TableField("reserved_number")
	private Integer reservedNumber;

	@Schema(description = "剩余预约数")
	@TableField("available_number")
	private Integer availableNumber;

	@Schema(description = "挂号费")
	@TableField("amount")
	private String amount;

	@Schema(description = "排班状态（-1：停诊 0：停约 1：可约）")
	@TableField("status")
	private Integer status;
}

