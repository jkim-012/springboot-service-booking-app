package com.example.bookingsystem.member.controller;

import com.example.bookingsystem.member.dto.NewMemberDto;
import com.example.bookingsystem.member.service.MemberService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/member")
@RequiredArgsConstructor
@RestController
public class MemberController {

  private final MemberService memberService;

  // API endpoint for registering
  @PostMapping()
  public ResponseEntity<?> register(@RequestBody @Valid NewMemberDto newMemberDto){

    memberService.register(newMemberDto);
    return ResponseEntity.ok("Registration successfully completed.");
  }

}
