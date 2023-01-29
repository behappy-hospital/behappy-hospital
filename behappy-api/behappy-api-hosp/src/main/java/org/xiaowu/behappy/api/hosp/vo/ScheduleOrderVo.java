package org.xiaowu.behappy.api.hosp.vo;



import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Schema(description = "Schedule")
public class ScheduleOrderVo {

	@Schema(description = "医院编号")
	private String hoscode;

	@Schema(description = "医院名称")
	private String hosname;

	@Schema(description = "科室编号")
	private String depcode;

	@Schema(description = "科室名称")
	private String depname;

	@Schema(description = "排班编号（医院自己的排班主键）")
	private String hosScheduleId;

	@Schema(description = "医生职称")
	private String title;

	@Schema(description = "安排日期")
	private Date reserveDate;

	@Schema(description = "剩余预约数")
	private Integer availableNumber;

	@Schema(description = "安排时间（0：上午 1：下午）")
	private Integer reserveTime;

	@Schema(description = "医事服务费")
	private BigDecimal amount;

	@Schema(description = "退号时间")
	private Date quitTime;

	@Schema(description = "挂号开始时间")
	private Date startTime;

	@Schema(description = "挂号结束时间")
	private Date endTime;

	@Schema(description = "当天停止挂号时间")
	private Date stopTime;
}

