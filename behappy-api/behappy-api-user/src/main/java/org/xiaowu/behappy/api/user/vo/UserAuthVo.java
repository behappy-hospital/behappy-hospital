package org.xiaowu.behappy.api.user.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description="会员认证对象")
public class UserAuthVo {

    @ApiModelProperty(value = "用户姓名")
    private String name;

    @ApiModelProperty(value = "证件类型")
    private String certificatesType;

    @ApiModelProperty(value = "证件编号")
    private String certificatesNo;

    @ApiModelProperty(value = "证件路径")
    private String certificatesUrl;

}
