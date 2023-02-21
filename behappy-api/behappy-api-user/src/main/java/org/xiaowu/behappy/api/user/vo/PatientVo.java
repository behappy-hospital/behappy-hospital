package org.xiaowu.behappy.api.user.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * Patient
 * </p>
 */
@Data
@Schema(description = "Patient")
public class PatientVo {

	private static final long serialVersionUID = 1L;

	@Schema(description = "用户id")
	private Long userId;

	@Schema(description = "姓名")
	private String name;

	@Schema(description = "证件类型")
	private String certificatesType;

	@Schema(description = "证件编号")
	private String certificatesNo;

	@Schema(description = "性别")
	private Integer sex;

	@Schema(description = "出生年月")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date birthdate;

	@Schema(description = "手机")
	private String phone;

	@Schema(description = "是否结婚")
	private Integer isMarry;

	@Schema(description = "省code")
	private String provinceCode;

	@Schema(description = "市code")
	private String cityCode;

	@Schema(description = "区code")
	private String districtCode;

	@Schema(description = "详情地址")
	private String address;

	@Schema(description = "联系人姓名")
	private String contactsName;

	@Schema(description = "联系人证件类型")
	private String contactsCertificatesType;

	@Schema(description = "联系人证件号")
	private String contactsCertificatesNo;

	@Schema(description = "联系人手机")
	private String contactsPhone;

	@Schema(description = "是否有医保")
	private Integer isInsure;

	@Schema(description = "就诊卡")
	private String cardNo;

	@Schema(description = "状态（0：默认 1：已认证）")
	private String status;

	@Schema(description = "id")
	private Long id;

	@Schema(description = "创建时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;

	@Schema(description = "更新时间")
	private Date updateTime;

	@Schema(description = "逻辑删除(1:已删除，0:未删除)")
	private Integer isDeleted;

	@Schema(description = "其他参数")
	private Map<String,Object> param = new HashMap<>();
}

