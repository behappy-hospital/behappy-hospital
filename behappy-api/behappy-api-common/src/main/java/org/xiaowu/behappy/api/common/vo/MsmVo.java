package org.xiaowu.behappy.api.common.vo;



import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Map;

@Data
@Schema(description = "短信实体")
public class MsmVo {
    @Schema(description = "phone")
    private String phone;
    @Schema(description = "短信模板code")
    private String templateCode;
    @Schema(description = "短信模板参数")
    private Map<String,Object> param;
}
