package com.rohitraj.gfgscraperapi.controller;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

/**
 * Controller to handle requests to the root URL and redirect to the Swagger UI.
 */
@RestController
@Hidden // This annotation hides this endpoint from the OpenAPI documentation
public class RootController {
    @GetMapping("/")
    public RedirectView redirectToSwaggerUi() {
        return new RedirectView("/swagger-ui/index.html");
    }
}