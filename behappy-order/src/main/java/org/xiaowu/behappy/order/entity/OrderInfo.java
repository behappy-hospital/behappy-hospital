package org.xiaowu.behappy.order.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.xiaowu.behappy.common.mybatis.base.BaseEntity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * Order
 * </p>
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "Order")
@TableName("order_info")
public class OrderInfo extends BaseEntity {

	private static final long serialVersionUID = 1L;

	@Schema(description = "userId")
	@TableField("user_id")
	private Long userId;

	@Schema(description = "订单交易号")
	@TableField("out_trade_no")
	private String outTradeNo;

	@Schema(description = "医院编号")
	@TableField("hoscode")
	private String hoscode;

	@Schema(description = "医院名称")
	@TableField("hosname")
	private String hosname;

	@Schema(description = "科室编号")
	@TableField("depcode")
	private String depcode;

	@Schema(description = "科室名称")
	@TableField("depname")
	private String depname;

	@Schema(description = "排班id")
	@TableField("schedule_id")
	private String scheduleId;

	@Schema(description = "医生职称")
	@TableField("title")
	private String title;

	@Schema(description = "安排日期")
	@JsonFormat(pattern = "yyyy-MM-dd")
	@TableField("reserve_date")
	private Date reserveDate;

	@Schema(description = "安排时间（0：上午 1：下午）")
	@TableField("reserve_time")
	private Integer reserveTime;

	@Schema(description = "就诊人id")
	@TableField("patient_id")
	private Long patientId;

	@Schema(description = "就诊人名称")
	@TableField("patient_name")
	private String patientName;

	@Schema(description = "就诊人手机")
	@TableField("patient_phone")
	private String patientPhone;

	@Schema(description = "预约记录唯一标识（医院预约记录主键）")
	@TableField("hos_record_id")
	private String hosRecordId;

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

	@Schema(description = "退号时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
	@TableField("quit_time")
	private Date quitTime;

	@Schema(description = "订单状态")
	@TableField("order_status")
	private Integer orderStatus;

}

