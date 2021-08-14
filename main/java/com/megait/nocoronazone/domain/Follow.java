package com.megait.nocoronazone.domain;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@ToString
public class Follow {
    @Id @GeneratedValue
    private Long no;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private User who;

    @OneToMany
    private List<User> whom;

    @Builder
    public Follow(){
        whom = new ArrayList<>();
    }


}
