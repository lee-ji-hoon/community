package com.community.profile.form;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class WithdrawForm {
    @Length(min = 8, max = 50)
    private String checkPassword;
}
