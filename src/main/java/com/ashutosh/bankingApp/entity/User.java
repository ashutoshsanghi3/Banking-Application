package com.ashutosh.bankingApp.entity;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NonNull
    private String firstName;
    @NonNull
    private String lastName;
    @Nullable
    private String otherName;
    @NonNull
    private String gender;
    @NonNull
    private String address;
    @NonNull
    private String stateOfOrigin;
    private String accountNumber;
    private BigDecimal accountBalance;
    @NonNull
    private String email;
    @NonNull
    private String phoneNumber;
    @Nullable
    private String alternativePhoneNumber;
    private String status;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime modifiedAt;

}
