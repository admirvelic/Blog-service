package com.vella.blog.model;

import com.vella.security.user.User;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table
public class Blog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    private String title;

    private String text;

    private LocalDate dataCreatedAt;

    private LocalTime timeCreatedAt;

    private LocalDate dateEditedAt;

    private LocalTime timeEditedAt;

}
