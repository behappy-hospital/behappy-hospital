package org.xiaowu.behappy.api.user.vo;



import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description="会员认证对象")
public class UserAuthVo {

    @Schema(description = "用户姓名")
    private String name;

    @Schema(description = "证件类型")
    private String certificatesType;

    @Schema(description = "证件编号")
    private String certificatesNo;

    @Schema(description = "证件路径")
    private String certificatesUrl;

}
