package uz.uat.backend.dto;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ResultService {
    private ResultCode resultCode;
    private String resultMessage;
    private Object job;
    private Object work;
    private Object serviceWithTask;
}
