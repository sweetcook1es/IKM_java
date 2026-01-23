package com.pechenka.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    // обработка входа с отображением ошибок/сообщений
    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error,
                        @RequestParam(value = "logout", required = false) String logout,
                        Model model) {

        if (error != null) {
            model.addAttribute("error", "Неверный табельный номер или фамилия!");
        }

        if (logout != null) {
            model.addAttribute("message", "Вы успешно вышли из системы.");
        }

        return "auth/login";
    }

    // перенаправление на главную
    @GetMapping("/")
    public String home() {
        return "redirect:/admin/dashboard";
    }
}