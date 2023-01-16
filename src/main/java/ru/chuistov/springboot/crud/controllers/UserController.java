package ru.chuistov.springboot.crud.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.chuistov.springboot.crud.entities.User;
import ru.chuistov.springboot.crud.security.UserDetailsImpl;
import ru.chuistov.springboot.crud.services.RoleService;
import ru.chuistov.springboot.crud.services.UserService;

import java.security.Principal;

@Controller
@RequestMapping("/")
public class UserController {

    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public UserController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("/admin")
    public String showAdminPage(Model model) {
        model.addAttribute("authorizedUser", getAuthorizedUser());
        model.addAttribute("users", userService.findAll());
        model.addAttribute("user", new User());
        model.addAttribute("roles", roleService.findAll());
        return "admin";
    }

    @GetMapping("/user")
    public String showUserPage(Model model) {
        model.addAttribute("authorizedUser", getAuthorizedUser());
        return "user";
    }

    @GetMapping("/admin/new")
    public String startCreateUser(@ModelAttribute("user") User user) {
        return "admin/new";
    }

    @PostMapping("/admin")
    public String finishCreateUser(@ModelAttribute("user") User user) {
        userService.save(user);
        return "redirect:/admin";
    }

    @GetMapping("/admin/{id}/edit")
    public String startUpdateUser(@PathVariable("id") long id, Model model) {
        User user = userService.findById(id);
        model.addAttribute("user", user);
        model.addAttribute("allRoles", roleService.findAll());
        return "admin/edit";
    }

    @PatchMapping("/admin/{id}")
    public String finishUpdateUser(@ModelAttribute("user") User user,
                                   @PathVariable("id") long id) {
        userService.update(user);
        return "redirect:/admin";
    }

    @DeleteMapping("/admin/{id}")
    public String deleteUser(@PathVariable("id") long id) {
        userService.deleteById(id);
        return "redirect:/admin";
    }

    private User getAuthorizedUser() {
        return ((UserDetailsImpl)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
    }
}