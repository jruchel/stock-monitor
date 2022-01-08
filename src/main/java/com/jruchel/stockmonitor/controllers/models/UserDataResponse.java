package com.jruchel.stockmonitor.controllers.models;

import com.jruchel.stockmonitor.models.entities.MonitoredStock;
import com.jruchel.stockmonitor.models.entities.Role;
import com.jruchel.stockmonitor.validation.UUID;
import lombok.*;

import javax.validation.constraints.Email;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDataResponse {

    @UUID
    private String id;
    private String username;
    private String password;
    @Email
    private String email;
    private List<MonitoredStockResponse> monitoredStocks;
    private Set<Role> roles;

}
