package com.felipegeroldi.front_gestao_vagas.modules.candidate.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.felipegeroldi.front_gestao_vagas.modules.candidate.dto.TokenDTO;
import com.felipegeroldi.front_gestao_vagas.modules.candidate.service.CandidateService;
import com.felipegeroldi.front_gestao_vagas.modules.candidate.service.ProfileCandidateService;

import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequestMapping("/candidate")
public class CandidateController {

    private CandidateService candidateService;
    private ProfileCandidateService profileCandidateService;

    

    public CandidateController(CandidateService candidateService, ProfileCandidateService profileCandidateService) {
        this.candidateService = candidateService;
        this.profileCandidateService = profileCandidateService;
    }

    @GetMapping("/login")
    public String login() {
        return "/candidate/login";
    }
    
    @PostMapping("/signIn")
    public String signIn(HttpSession session, RedirectAttributes redirectAttributes, String username, String password) {
        try {
            TokenDTO token = candidateService.login(username, password);
            List<SimpleGrantedAuthority> grants = token.getRoles()
                .stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()))
                .toList();

            var auth = new UsernamePasswordAuthenticationToken(null, null, grants);
            auth.setDetails(token.getAccessToken());

            SecurityContextHolder.getContext().setAuthentication(auth);
            session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());
            session.setAttribute("token", token);

            return "redirect:/candidate/profile";
        } catch (HttpClientErrorException e) {
            redirectAttributes.addFlashAttribute("error_message", "Usuário/Senha incorretos");
            return "redirect:/candidate/login";
        }
    }
    
    @PreAuthorize("hasRole('CANDIDATE')")
    @GetMapping("/profile")
    public String profile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        profileCandidateService.execute(authentication.getDetails().toString());

        return "/candidate/profile";
    }
}
