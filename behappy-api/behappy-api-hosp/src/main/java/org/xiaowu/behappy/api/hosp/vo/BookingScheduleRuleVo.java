package org.xiaowu.behappy.api.hosp.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * <p>
 * RegisterRule
 * </p>
 */
@Data
@Schema(description = "可预约排班规则数据")
public class BookingScheduleRuleVo {

	@Schema(description = "可预约日期")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date workDate;

	@Schema(description = "可预约日期")
	@JsonFormat(pattern = "MM月dd日")
	private Date workDateMd; //方便页面使用

	@Schema(description = "周几")
	private String dayOfWeek;

	@Schema(description = "就诊医生人数")
	private Integer docCount;

	@Schema(description = "科室可预约数")
	private Integer reservedNumber;

	@Schema(description = "科室剩余预约数")
	private Integer availableNumber;

	@Schema(description = "状态 0：正常 1：即将放号 -1：当天已停止挂号")
	private Integer status;
}

