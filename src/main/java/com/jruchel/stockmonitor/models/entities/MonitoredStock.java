package com.jruchel.stockmonitor.models.entities;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MonitoredStock {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private String id;
    private String ticker;
    private double notifyBelow;
    private double notifyAbove;
    private double notifyEveryPercent;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_stock")
    private User user;

    @PreRemove
    public void preRemove() {
        user.getMonitoredStocks().remove(this);
        this.user = null;
    }

}
