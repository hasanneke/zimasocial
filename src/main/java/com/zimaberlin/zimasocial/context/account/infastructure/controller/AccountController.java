package com.zimaberlin.zimasocial.context.account.infastructure.controller;

import com.zimaberlin.zimasocial.context.account.service.AccountService;
import com.zimaberlin.zimasocial.context.social.api.author.AuthorView;
import com.zimaberlin.zimasocial.context.account.value.DeleteReason;
import com.zimaberlin.zimasocial.context.account.value.DisableReason;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "api/v1/account")
@RequiredArgsConstructor
public class AccountController {
    final AccountService accountService;
    @PatchMapping("/me/make-account-public")
    public ResponseEntity<AuthorView> makeAccountPublic() {
        accountService.makeAccountPublic();
        return ResponseEntity.ok().build();
    }
    @PatchMapping("/me/make-account-private")
    public ResponseEntity<AuthorView> makeAccountPrivate() {
        accountService.makeAccountPrivate();
        return ResponseEntity.ok().build();
    }
    @GetMapping("/disable-account")
    public ResponseEntity<Void> disableAccount(@RequestParam(name = "reason") DisableReason reason) {
        accountService.disableAccount(reason);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/delete-account")
    public ResponseEntity<Void> deleteAccount(@RequestParam(name = "reason") DeleteReason reason) {
        accountService.deleteAccount(reason);
        return ResponseEntity.ok().build();
    }
}
