package com.example.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CustomMemberDetails implements UserDetails {

    private Long id;
    private String email;
    private String password;
    private LocalDateTime registerAt;
    private LocalDateTime modifiedAt;

    public static CustomMemberDetails fromEntity(MemberEntity member){
        return new CustomMemberDetails(
                member.getId(),
                member.getEmail(),
                member.getPassword(),
                member.getCreatedDateTIme(),
                member.getModifiedDateTime()
        );
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public String getPassword(){
        return this.password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}