package org.xiaowu.behappy.manager.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.xiaowu.behappy.common.mybatis.base.BaseEntity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * OrderInfo
 * </p>
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "OrderInfo")
@TableName("order_info")
public class OrderInfo extends BaseEntity {

	private static final long serialVersionUID = 1L;

	@Schema(description = "排班id")
	@TableField("schedule_id")
	private Long scheduleId;

	@Schema(description = "就诊人id")
	@TableField("patient_id")
	private Long patientId;

	@Schema(description = "预约号序")
	@TableField("number")
	private Integer number;

	@Schema(description = "建议取号时间")
	@TableField("fetch_time")
	private String fetchTime;

	@Schema(description = "取号地点")
	@TableField("fetch_address")
	private String fetchAddress;

	@Schema(description = "医事服务费")
	@TableField("amount")
	private BigDecimal amount;

	@Schema(description = "支付时间")
	@TableField("pay_time")
	private Date payTime;

	@Schema(description = "退号时间")
	@TableField("quit_time")
	private Date quitTime;

	@Schema(description = "订单状态")
	@TableField("order_status")
	private Integer orderStatus;

}

