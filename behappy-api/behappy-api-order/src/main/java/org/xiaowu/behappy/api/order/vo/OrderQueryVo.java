package org.xiaowu.behappy.api.order.vo;



import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Order")
public class OrderQueryVo {


	@Schema(description = "会员id")
	private Long userId;

	@Schema(description = "订单交易号")
	private String outTradeNo;

	@Schema(description = "就诊人id")
	private Long patientId;

	@Schema(description = "就诊人")
	private String patientName;

	@Schema(description = "医院名称")
	private String keyword;

	@Schema(description = "订单状态")
	private String orderStatus;

	@Schema(description = "安排日期")
	private String reserveDate;

	@Schema(description = "创建时间")
	private String createTimeBegin;
	private String createTimeEnd;

}

