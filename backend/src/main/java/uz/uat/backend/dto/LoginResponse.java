package uz.uat.backend.dto;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LoginResponse {

    private ResultCode resultCode;
    private String resultNote;
    private String name;
    private String roleName;
    private Long newTaskCount;
    private Long inProcessTaskCount;
    private Long closedTaskCount;
    private Long messageCount;
    private String token;

}
