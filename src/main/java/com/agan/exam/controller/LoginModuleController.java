package com.agan.exam.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginModuleController {

  /**
   * Login string.
   *
   * @return the string
   */
  @GetMapping({"/", "/login"})
  public String login() {
    return "/auth/login";
  }
}
