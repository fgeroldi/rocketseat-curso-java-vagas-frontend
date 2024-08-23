package com.felipegeroldi.front_gestao_vagas.modules.candidate.controller;

import java.util.List;
import java.util.UUID;

import org.apache.logging.log4j.util.Strings;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.felipegeroldi.front_gestao_vagas.modules.candidate.dto.CreateCandidateDTO;
import com.felipegeroldi.front_gestao_vagas.modules.candidate.dto.JobDTO;
import com.felipegeroldi.front_gestao_vagas.modules.candidate.dto.ProfileUserDTO;
import com.felipegeroldi.front_gestao_vagas.modules.candidate.dto.TokenDTO;
import com.felipegeroldi.front_gestao_vagas.modules.candidate.service.ApplyJobService;
import com.felipegeroldi.front_gestao_vagas.modules.candidate.service.CandidateService;
import com.felipegeroldi.front_gestao_vagas.modules.candidate.service.FindJobService;
import com.felipegeroldi.front_gestao_vagas.modules.candidate.service.ProfileCandidateService;

import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequestMapping("/candidate")
public class CandidateController {

    private CandidateService candidateService;
    private ProfileCandidateService profileCandidateService;
    private FindJobService findJobService;
    private ApplyJobService applyJobService;

    

    public CandidateController(CandidateService candidateService,
            ProfileCandidateService profileCandidateService,
            FindJobService findJobService,
            ApplyJobService applyJobService
        ) {
        this.candidateService = candidateService;
        this.profileCandidateService = profileCandidateService;
        this.findJobService = findJobService;
        this.applyJobService = applyJobService;
    }

    @GetMapping("/login")
    public String login() {
        return "/candidate/login";
    }

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("candidate", new CreateCandidateDTO());

        return "candidate/create";
    }

    @PostMapping("/create")
    public String save(Model model, CreateCandidateDTO candidate) {
        model.addAttribute("candidate", candidate);
        return "candidate/create";
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
    public String profile(Model model) {
        try {
            ProfileUserDTO user = profileCandidateService.execute(getToken());
            model.addAttribute("user", user);
    
            return "/candidate/profile";
        } catch (HttpClientErrorException e) {
            return "redirect:/candidate/login";
        }
        
    }

    @PreAuthorize("hasRole('CANDIDATE')")
    @PostMapping("/jobs/apply")
    public String applyJob(@RequestParam("jobId") UUID jobId) {
        try {
            applyJobService.execute(getToken(), jobId);
            return "redirect: /candidate/jobs";
        } catch (HttpClientErrorException e) {
            return "redirect:/candidate/login";
        }
    }

    @PreAuthorize("hasRole('CANDIDATE')")
    @GetMapping("/jobs")
    public String jobs(Model model, String filter) {
        try {
            if (Strings.isEmpty(filter)) {
                List<JobDTO> jobs = findJobService.execute(getToken(), filter);
                model.addAttribute("jobs", jobs);
            }

            return "/candidate/jobs";
        } catch (HttpClientErrorException e) {
            return "redirect:/candidate/login";
        }
    }

    private String getToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getDetails().toString();
    }
}
