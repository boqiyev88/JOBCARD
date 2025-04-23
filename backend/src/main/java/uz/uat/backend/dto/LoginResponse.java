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
    private int newTaskCount;
    private int inProcessTaskCount;
    private int closedTaskCount;
    private int messageCount;

}
