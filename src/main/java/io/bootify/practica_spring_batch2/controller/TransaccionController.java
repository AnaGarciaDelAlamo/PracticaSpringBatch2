package io.bootify.practica_spring_batch2.controller;

import io.bootify.practica_spring_batch2.model.TransaccionDTO;
import io.bootify.practica_spring_batch2.service.TransaccionService;
import io.bootify.practica_spring_batch2.util.WebUtils;
import jakarta.validation.Valid;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/transaccions")
public class TransaccionController {

    private final TransaccionService transaccionService;

    /*@Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job job;*/

    public TransaccionController(final TransaccionService transaccionService) {
        this.transaccionService = transaccionService;
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("transaccions", transaccionService.findAll());
        return "transaccion/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("transaccion") final TransaccionDTO transaccionDTO) {
        return "transaccion/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("transaccion") @Valid final TransaccionDTO transaccionDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "transaccion/add";
        }
        transaccionService.create(transaccionDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("transaccion.create.success"));
        return "redirect:/transaccions";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final Long id, final Model model) {
        model.addAttribute("transaccion", transaccionService.get(id));
        return "transaccion/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final Long id,
            @ModelAttribute("transaccion") @Valid final TransaccionDTO transaccionDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "transaccion/edit";
        }
        transaccionService.update(id, transaccionDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("transaccion.update.success"));
        return "redirect:/transaccions";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable(name = "id") final Long id,
            final RedirectAttributes redirectAttributes) {
        transaccionService.delete(id);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("transaccion.delete.success"));
        return "redirect:/transaccions";
    }

    /*@PostMapping("/importTransacciones")
    public void importCsvToDBPracticaSpringBatch(){
        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("startAt", System.currentTimeMillis()).toJobParameters();
        try {
            jobLauncher.run(job, jobParameters);
        } catch (JobExecutionAlreadyRunningException | JobParametersInvalidException |
                 JobInstanceAlreadyCompleteException | JobRestartException e) {
            throw new RuntimeException(e);
        }

    }*/

}
