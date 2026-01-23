package com.pechenka.app.service;

import com.pechenka.app.entity.Employee;
import com.pechenka.app.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // username - это табельный номер
        Employee employee = employeeRepository.findByPersonnelNumber(username)
                .orElseThrow(() -> new UsernameNotFoundException("Сотрудник с табельным номером " + username + " не найден"));

        // извлекаем фамилию из полного имени
        String surname = extractSurname(employee.getFullName());

        // преобразуем роль в формат, понятный Spring Security
        String role = convertRoleToSecurityFormat(employee.getRole());

        return User.builder()
                .username(employee.getPersonnelNumber())
                .password(surname) // Фамилия как пароль
                .authorities(new SimpleGrantedAuthority("ROLE_" + role))
                .build();
    }

    private String extractSurname(String fullName) {
        if (fullName == null || fullName.trim().isEmpty()) {
            return "";
        }
        String[] parts = fullName.trim().split("\\s+");
        return parts.length > 0 ? parts[0] : fullName;
    }

    private String convertRoleToSecurityFormat(String role) {
        if (role == null) return "USER";

        return switch (role) {
            case "Администратор" -> "ADMIN";
            case "Комплектовщик" -> "PICKER";
            case "Оператор" -> "OPERATOR";
            case "Аналитик" -> "ANALYST";
            default -> "USER";
        };
    }
}