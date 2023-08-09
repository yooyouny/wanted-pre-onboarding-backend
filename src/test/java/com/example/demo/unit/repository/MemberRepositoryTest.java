package com.example.demo.unit.repository;

import com.example.demo.controller.dto.request.PostCreateRequest;
import com.example.demo.entity.MemberEntity;
import com.example.demo.entity.PostEntity;
import com.example.demo.repository.MemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.swing.text.html.Option;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    void saveNewMember(){
        //given
        MemberEntity request = MemberEntity.builder()
                .email("email@")
                .password("abcdefghijklmnop")
                .build();

        //when
        MemberEntity savedMember = memberRepository.save(request);

        //then
        Assertions.assertEquals(savedMember.getEmail(), "email@");
        Assertions.assertEquals(savedMember.getPassword(), "abcdefghijklmnop");

    }
    @Test
    void findByEmail(){
        //given
        String email = "email@";
        String pwd = "12345678";
        MemberEntity requestMember = MemberEntity.builder()
                .email(email)
                .password(pwd)
                .build();
        memberRepository.save(requestMember);

        //when
        Optional<MemberEntity> foundMember = memberRepository.findByEmail(email);

        //then
        Assertions.assertTrue(foundMember.isPresent());
        MemberEntity savedMember = foundMember.get();
        Assertions.assertEquals(email, savedMember.getEmail());
        Assertions.assertEquals("12345678", savedMember.getPassword());
    }

}
