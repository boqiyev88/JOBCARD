package uz.uat.backend.dto;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Tasks {

    private String id;
    private String description;
    private int pref;
    private int insp;

}
