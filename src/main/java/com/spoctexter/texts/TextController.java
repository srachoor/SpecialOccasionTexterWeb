package com.spoctexter.texts;

import com.spoctexter.userAccount.UserAccountService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "api/v1/spoc/texts")
public class TextController {

    private final TextService textService;
    private final UserAccountService userAccountService;

    public TextController(TextService textService, UserAccountService userAccountService) {
        this.textService = textService;
        this.userAccountService = userAccountService;
    }

    @RequestMapping(path="{username}")
    @PreAuthorize(value = "authentication.principal.equals(#username)")
    public List<Text> getTextsByUser(@NotNull @PathVariable(name="username") String username) {
        System.out.println(username);
        UUID accountId = userAccountService.getUserAccountByUserName(username).getId();
        System.out.println(accountId);
        return textService.getTextsByUser(accountId);
    }
}
