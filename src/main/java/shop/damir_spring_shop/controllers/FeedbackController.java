package shop.damir_spring_shop.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import shop.damir_spring_shop.models.Feedback;
import shop.damir_spring_shop.repositories.FeedbackRepository;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping(path = "/feedbacks")
public class FeedbackController {
    // /feedbacks/approve/{id}
    // /feedbacks/delete/{id}
    private final FeedbackRepository feedbackRepository;

    @PostMapping("/approve/{id}")
    public String approveFeedback(@PathVariable("id") Long id) {
        Feedback feedback = feedbackRepository.findById(id).orElse(null);
        if (feedback != null) {
            feedback.setReleaseStatus(true);
            feedbackRepository.save(feedback);
        }

        return "redirect:/products/{id}/feedbacks";
    }

    @PostMapping("/delete/{id}")
    public String deleteFeedback(@PathVariable("id") Long id) {
        feedbackRepository.findById(id).ifPresent(feedbackRepository::delete);

        return "redirect:/products/{id}/feedbacks";
    }
}
