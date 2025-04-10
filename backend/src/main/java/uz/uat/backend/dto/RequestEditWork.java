package uz.uat.backend.dto;

import lombok.Builder;

@Builder
public record RequestEditWork(
        String workid,
        String service_id,
        String threshold,
        String repeat_int,
        String zone,
        String mpr,
        String access,
        String airplane_app,
        String description,
        String access_note,
        String task_description,
        boolean dit,
        boolean avionic,
        boolean mechanic,
        boolean cab_mechanic,
        boolean sheet_metal,
        boolean ndt
) {
}
