package uz.uat.backend.model.enums;

public enum Status {
    NEW,           ///  yaratilganda             1
    PENDING,      ///  tasdiqlashga kelganda     2
    IN_PROCESS,   ///  ish bajarilyapti          3
    CONFIRMED,    ///  tasdiqlandi               4
    COMPLETED,   ///   to'liq tugatildi          5
    REJECTED,    ///   rad etildi                6

}
