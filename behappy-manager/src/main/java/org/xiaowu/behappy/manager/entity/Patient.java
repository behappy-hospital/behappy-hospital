package org.xiaowu.behappy.manager.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.xiaowu.behappy.common.mybatis.base.BaseEntity;

/**
 * <p>
 * Patient
 * </p>
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "Patient")
public class Patient extends BaseEntity {

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
	private String birthdate;

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

}

